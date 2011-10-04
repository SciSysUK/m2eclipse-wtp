package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v2.converter;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.maven.ide.eclipse.wtp.overlay.modulecore.IOverlayVirtualComponent;

/**
 * Holds data relating to {@link IOverlayVirtualComponent overlays} that is to be used in conversion to and from {@link URI}s.
 * 
 * @author Alex Clarke
 */
public class OverlayComponentURIData {
	
	private String referenceType;
	
	private String postfix;

	private Map<String, String> params;
	
	public OverlayComponentURIData(String referenceType, String postfix, Map<String, String> params) {
		super();
		this.referenceType = referenceType;
		this.postfix = postfix;
		this.params = params;
	}

	public String getReferenceType() {
		return referenceType;
	}

	public String getPostfix() {
		return postfix;
	}

	public Map<String, String> getParams() {
		return params;
	}

}
