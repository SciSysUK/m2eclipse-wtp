/*******************************************************************************
 * Copyright (c) 2008 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.maven.ide.eclipse.wtp.overlay.modulecore;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.ComponentcorePackage;
import org.eclipse.wst.common.componentcore.internal.DependencyType;
import org.eclipse.wst.common.componentcore.internal.ReferencedComponent;
import org.eclipse.wst.common.componentcore.resolvers.IReferenceResolver;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualReference;
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v2.converter.ArchiveOveralyVirtualComponentURIConverter;
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v2.converter.CurrentProjectOverlayVirtualComponentURIConverter;
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v2.converter.OverlayComponentURIData;
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v2.converter.OverlayVirtualComponentURIConverter;
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v2.converter.ProjectOverlayVirtualComponentURIConverter;

/**
 * Overlay Reference Resolver
 * 
 * @author Fred Bricon
 */
@SuppressWarnings("restriction")
public class OverlayReferenceResolver implements IReferenceResolver {

	private static final String URI_SEPERATOR = "&";
	
	private static final String PROTOCOL = "module:";
	private static final String TYPE = "overlay";
	private static final String PREFIX = PROTOCOL + "/" + TYPE;

	private List<OverlayVirtualComponentURIConverter> overalyVirtualComponentURIConverters = Arrays.asList(
			new ArchiveOveralyVirtualComponentURIConverter(), new ProjectOverlayVirtualComponentURIConverter(),
			new CurrentProjectOverlayVirtualComponentURIConverter());
	

	public boolean canResolve(IVirtualComponent context, ReferencedComponent referencedComponent) {
		URI uri = referencedComponent.getHandle();
		if (isOverlayModule(uri) && context instanceof IOverlayVirtualComponent) {
			for (OverlayVirtualComponentURIConverter converter : overalyVirtualComponentURIConverters) {
				if (converter.isSupported(uri.segment(1))) {
					return true;
				}
			}
		}
		return false;
	}

	public IVirtualReference resolve(IVirtualComponent context, ReferencedComponent referencedComponent) {
		if (canResolve(context, referencedComponent)) {
			URI uri = referencedComponent.getHandle();
			for (OverlayVirtualComponentURIConverter converter : overalyVirtualComponentURIConverters) {
				if (converter.isSupported(uri.segment(1))) {
					String postfix = uri.segment(2); // TODO - this looks like it won't work for Archives
					OverlayComponentURIData uriData = new OverlayComponentURIData(uri.segment(1), postfix, getParameters(uri.query()));
					IVirtualComponent overlayComponent = converter.createOverlayVirtualComponent(context, uriData, referencedComponent.getRuntimePath());
					IVirtualReference ref = ComponentCore.createReference(context, overlayComponent);
					ref.setArchiveName(referencedComponent.getArchiveName());
					ref.setRuntimePath(referencedComponent.getRuntimePath());
					ref.setDependencyType(referencedComponent.getDependencyType().getValue());
					return ref;
				}
			}
		}
		throw new RuntimeException("Unable to resolve context: "+ context +" referencedCompontent: "+ referencedComponent);
	}

	public boolean canResolve(IVirtualReference reference) {
		if (reference != null && reference.getReferencedComponent() instanceof IOverlayVirtualComponent) {
			for (OverlayVirtualComponentURIConverter converter : overalyVirtualComponentURIConverters) {
				if (converter.isSupported((IOverlayVirtualComponent) reference.getReferencedComponent())) {
					return true;
				}
			}
		}
		return false;
	}

	public ReferencedComponent resolve(IVirtualReference reference) {
		if (canResolve(reference)) {
			ReferencedComponent rc = ComponentcorePackage.eINSTANCE.getComponentcoreFactory().createReferencedComponent();
			rc.setArchiveName(reference.getArchiveName());
			rc.setRuntimePath(reference.getRuntimePath());
			rc.setDependencyType(DependencyType.CONSUMES_LITERAL);
			rc.setHandle(createURI(reference));
			return rc;
		}
		throw new RuntimeException("Unable to resolve reference: "+ reference);
	}

	
	private URI createURI(IVirtualReference reference) {
		IOverlayVirtualComponent overlayComponent = (IOverlayVirtualComponent) reference.getReferencedComponent();
		for (OverlayVirtualComponentURIConverter converter : overalyVirtualComponentURIConverters) {
			if (converter.isSupported(overlayComponent)) {
				OverlayComponentURIData uriData = converter.createOverlayComponentURIData(overlayComponent);
				return buildURI(uriData);
			}
		}
		throw new RuntimeException("No converters were able to handle overlay component");
	}

	private URI buildURI(OverlayComponentURIData uriData) {
		String queryString = buildQueryString(uriData.getParams());
		return URI.createURI(PREFIX).appendSegment(uriData.getReferenceType()).appendSegment(uriData.getPostfix())
				.appendQuery(queryString);
	}

	private String buildQueryString(Map<String, String> params) {
		StringBuilder builder = new StringBuilder();
		for (String key : params.keySet()) {
			builder.append(key).append("=").append(params.get(key));
			builder.append(URI_SEPERATOR);
		}
		return builder.toString();
	}

	private boolean isOverlayModule(URI uri) {
		return uri.segmentCount() > 2 && uri.segment(0).equals(TYPE);
	}
	
	public Map<String, String> getParameters(String queryString) {
		if (queryString == null || queryString.length() == 0) {
			return Collections.emptyMap();
		}
		Map<String, String> parameters = new HashMap<String, String>();
		String[] entries = queryString.split(URI_SEPERATOR);
		for (String entry : entries) {
			if ("".equals(entry)) {
				continue;
			}
			String[] keyValue = entry.split("=");
			if (keyValue.length == 2) {
				parameters.put(keyValue[0], keyValue[1]);
			}
		}
		return parameters;
	}

}
