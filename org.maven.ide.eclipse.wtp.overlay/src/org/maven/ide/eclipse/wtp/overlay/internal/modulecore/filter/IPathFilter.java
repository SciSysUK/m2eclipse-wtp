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
