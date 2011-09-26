package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v2;

import org.eclipse.core.runtime.IPath;

public interface IPathFilter {

	public boolean isFiltered(IPath path);

}
