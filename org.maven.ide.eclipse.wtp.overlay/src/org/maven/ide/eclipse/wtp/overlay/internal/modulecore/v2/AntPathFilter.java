package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v2;

import java.util.Set;

import org.eclipse.core.runtime.IPath;

public class AntPathFilter implements IPathFilter {
	
	private AntPathMatcher matcher;
	
	private Set<String> inclusions;
	
	private Set<String> exclusions;
	
	public AntPathFilter(Set<String> inclusions, Set<String> exclusions) {
		matcher = new AntPathMatcher();
		this.inclusions = inclusions;
		this.exclusions = exclusions;
	}

	public boolean isFiltered(IPath path) {
		return isFiltered(path.toString());
	}
	
	private boolean isFiltered(String path) {
		if (path.startsWith("/")) {
			path = path.substring(1);
		}
		boolean included = isIncluded(path);
		boolean excluded = isExcluded(path);
		return !included || excluded;
	}

	private boolean isIncluded(String path) {
		if (inclusions.isEmpty())
		{ 
			return true;
		}
		for (String inclusion : inclusions) {
			if (matcher.match(inclusion, path)) {
				return true;
			}
		}
		return false;
	}

	private boolean isExcluded(String path) {
		for (String exclusion : exclusions) {
			if (matcher.match(exclusion, path)) {
				return true;
			}
		}
		return false;
	}
	
}
