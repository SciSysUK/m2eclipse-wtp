package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v2;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.resources.VirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.componentcore.resources.IVirtualReference;
import org.maven.ide.eclipse.wtp.overlay.modulecore.IOverlayVirtualComponent;

@SuppressWarnings("restriction")
public class CurrentProjectOverlayVictualComponent extends VirtualComponent implements IOverlayVirtualComponent {

	private static final IVirtualReference[] NO_REFERENCES = new IVirtualReference[0];
	
	private static final String REFERENCE_TYPE = "slf";
	
	public CurrentProjectOverlayVictualComponent(IProject project) {
		super(project, IVirtualComponent.ROOT);
	}
	
	public String getReferenceType() {
		return REFERENCE_TYPE;
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

	public void setInclusions(Set<String> inclusionPatterns) {
		//TODO we currently do not support filtering of self overlays
	}

	public void setExclusions(Set<String> exclusPatterns) {
		//TODO we currently do not support filtering of self overlays
	}

	public Set<String> getExclusions() {
		return Collections.emptySet();
	}

	public Set<String> getInclusions() {
		return Collections.emptySet();
	}
}
