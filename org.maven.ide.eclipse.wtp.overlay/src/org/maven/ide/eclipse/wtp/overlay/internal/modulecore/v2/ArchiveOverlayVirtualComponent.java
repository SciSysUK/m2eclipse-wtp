package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v2;

import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.maven.ide.eclipse.wtp.overlay.modulecore.IOverlayVirtualComponent;

/**
 * A {@link IOverlayVirtualComponent} that deals with references to other external {@link IFile archives}. This
 * component can be used to support overlays that refer to artifacts that are not contained within the workspace.
 * 
 * @see OverlayFilter
 */
public abstract class ArchiveOverlayVirtualComponent implements IOverlayVirtualComponent {

	private OverlayFilter overlayFilter;

	public IVirtualFolder getRootFolder() {
		return overlayFilter.apply(getUnfilteredRootFolder());
	}
	
	private IVirtualFolder getUnfilteredRootFolder() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setInclusions(Set<String> inclusions) {
		overlayFilter.setInclusions(inclusions);
	}

	public void setExclusions(Set<String> exclusions) {
		overlayFilter.setExclusions(exclusions);
	}

	public Set<String> getInclusions() {
		return overlayFilter.getInclusions();
	}

	public Set<String> getExclusions() {
		return overlayFilter.getExclusions();
	}
}
