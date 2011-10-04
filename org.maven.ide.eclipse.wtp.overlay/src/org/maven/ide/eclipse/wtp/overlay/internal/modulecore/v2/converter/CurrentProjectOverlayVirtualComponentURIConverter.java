package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v2.converter;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v2.CurrentProjectOverlayVictualComponent;
import org.maven.ide.eclipse.wtp.overlay.modulecore.IOverlayVirtualComponent;
import org.maven.ide.eclipse.wtp.overlay.modulecore.OverlayComponentCore;

public class CurrentProjectOverlayVirtualComponentURIConverter extends OverlayVirtualComponentURIConverter {

	@Override
	protected Class<? extends IOverlayVirtualComponent> getSupportedClass() {
		return CurrentProjectOverlayVictualComponent.class;
	}

	@Override
	protected String getReferenceType() {
		return "slf";
	}

	@Override
	public OverlayComponentURIData createOverlayComponentURIData(IOverlayVirtualComponent component) {
		Map<String, String> params = new HashMap<String, String>();
		return new OverlayComponentURIData(getReferenceType(), null, params);
	}

	@Override
	public IOverlayVirtualComponent createOverlayVirtualComponent(IVirtualComponent context, OverlayComponentURIData uri, IPath runtimePath) {
		return OverlayComponentCore.createSelfOverlayComponent(context.getProject());
	}

}
