
package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.filter;

import org.eclipse.core.runtime.IPath;

/**
 * Strategy interface that can be used to filter {@link IPath paths}.
 */
public interface IPathFilter {

  /**
   * Determine if the specified path is filtered.
   * @param path the path
   * @return <tt>true</tt> if the path is filtered
   */
  public boolean isFiltered(IPath path);

}
