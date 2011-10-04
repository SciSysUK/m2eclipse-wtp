package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v2.converter;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v2.ArchiveOverlayVirtualComponent;
import org.maven.ide.eclipse.wtp.overlay.modulecore.IOverlayVirtualComponent;
import org.maven.ide.eclipse.wtp.overlay.modulecore.OverlayComponentCore;

public class ArchiveOveralyVirtualComponentURIConverter extends OverlayVirtualComponentURIConverter {

	@Override
	protected Class<? extends IOverlayVirtualComponent> getSupportedClass() {
		return ArchiveOverlayVirtualComponent.class;
	}

	@Override
	protected String getReferenceType() {
		return "var";
	}

	@Override
	public OverlayComponentURIData createOverlayComponentURIData(IOverlayVirtualComponent component) {
		ArchiveOverlayVirtualComponent archiveComponent = (ArchiveOverlayVirtualComponent) component;
		String postfix = null; // TODO get archive location
		Map<String, String> params = new HashMap<String, String>();
		params.put("unpackFolder", archiveComponent.getUnpackDirPath().toPortableString());
		return new OverlayComponentURIData(getReferenceType(), postfix, params) ;
	}

	@Override
	public IOverlayVirtualComponent createOverlayVirtualComponent(IVirtualComponent context, OverlayComponentURIData uri, IPath runtimePath) {
		IProject project = context.getProject();
		IPath unpackPath = project.getFolder(uri.getParams().get("unpackFolder")).getProjectRelativePath();
		return OverlayComponentCore.createOverlayArchiveComponent(project, uri.getPostfix(), unpackPath, runtimePath);
	}

}
