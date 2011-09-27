package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v2;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.componentcore.resources.IVirtualReference;

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
	
	public Set<IVirtualReference> apply(Set<IVirtualReference> virtualReferences) {
		Set<IVirtualReference> filteredReferences = new LinkedHashSet<IVirtualReference>();
		AntPathFilter pathFilter = new AntPathFilter(inclusions, exclusions);
		for (IVirtualReference virtualReference : virtualReferences) {
			IPath path = virtualReference.getRuntimePath().append(virtualReference.getArchiveName());
			if (!pathFilter.isFiltered(path)) {
				filteredReferences.add(virtualReference);
			}
		}
		return filteredReferences;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((exclusions == null) ? 0 : exclusions.hashCode());
		result = prime * result + ((inclusions == null) ? 0 : inclusions.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OverlayFilter other = (OverlayFilter) obj;
		if (exclusions == null) {
			if (other.exclusions != null)
				return false;
		} else if (!exclusions.equals(other.exclusions))
			return false;
		if (inclusions == null) {
			if (other.inclusions != null)
				return false;
		} else if (!inclusions.equals(other.inclusions))
			return false;
		return true;
	}
	
	

}
