package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v2;

import java.util.Set;

import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;

public class OverlayFilter {

	private Set<String> inclusions;
	
	private Set<String> exclusions;

	public void setInclusions(Set<String> inclusions) {
		this.inclusions = inclusions;
	}

	public void setExclusions(Set<String> exclusions) {
		this.exclusions = exclusions;
	}

	public Set<String> getInclusions() {
		return inclusions;
	}

	public Set<String> getExclusions() {
		return exclusions;
	}

	public IVirtualFolder apply(IVirtualFolder virtualFolder) {
		AntPathFilter pathFilter = new AntPathFilter(inclusions, exclusions);
		return new FilteredVirtualFolder(virtualFolder, pathFilter);
		
	}
}
