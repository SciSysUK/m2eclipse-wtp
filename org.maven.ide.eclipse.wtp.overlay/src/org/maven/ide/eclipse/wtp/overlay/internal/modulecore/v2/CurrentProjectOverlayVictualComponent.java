package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v2;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.resources.VirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.componentcore.resources.IVirtualReference;

@SuppressWarnings("restriction")
public class CurrentProjectOverlayVictualComponent extends VirtualComponent {

	private static final IVirtualReference[] NO_REFERENCES = new IVirtualReference[0];

	public CurrentProjectOverlayVictualComponent(IProject project) {
		super(project, IVirtualComponent.ROOT);
	}

	public IVirtualFolder getRootFolder() {
		IVirtualComponent component = ComponentCore.createComponent(getProject());
		if (component == null) {
			return null;
		}
		return component.getRootFolder();
	}
	
	@Override
	public IVirtualReference[] getReferences(Map<String, Object> paramMap){
		return NO_REFERENCES;
	}
}
