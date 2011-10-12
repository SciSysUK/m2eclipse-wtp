/*******************************************************************************
 * Copyright (c) 2011 JBoss by Red Hat.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

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
