package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v2.converter;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v2.ProjectOverlayVirtualComponent;
import org.maven.ide.eclipse.wtp.overlay.modulecore.IOverlayVirtualComponent;
import org.maven.ide.eclipse.wtp.overlay.modulecore.OverlayComponentCore;

public class ProjectOverlayVirtualComponentURIConverter extends OverlayVirtualComponentURIConverter {

	@Override
	protected Class<? extends IOverlayVirtualComponent> getSupportedClass() {
		return ProjectOverlayVirtualComponent.class;
	}

	@Override
	protected String getReferenceType() {
		return "prj";
	}

	@Override
	public OverlayComponentURIData createOverlayComponentURIData(IOverlayVirtualComponent component) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("contentType", null); // TODO get webconetent / classes
		return new OverlayComponentURIData(getReferenceType(), component.getProject().getName(), params);
	}

	@Override
	public IOverlayVirtualComponent createOverlayVirtualComponent(IVirtualComponent context, OverlayComponentURIData uri, IPath runtimePath) {
		return OverlayComponentCore.createOverlayComponent(context.getProject(), uri.getPostfix());
	}

}
