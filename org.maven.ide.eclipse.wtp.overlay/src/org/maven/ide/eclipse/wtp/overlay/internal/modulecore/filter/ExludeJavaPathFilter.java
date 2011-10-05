
package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.filter;

import org.eclipse.core.runtime.IPath;

/**
 * Simple {@link IPathFilter} that can be used to excluded <tt>.java</tt> files.
 */
public class ExludeJavaPathFilter implements IPathFilter {

  public boolean isFiltered(IPath path) {
    String pathString = path.toString();
    return pathString.startsWith("/WEB-INF/classes") && pathString.endsWith(".java");
  }
  
}
