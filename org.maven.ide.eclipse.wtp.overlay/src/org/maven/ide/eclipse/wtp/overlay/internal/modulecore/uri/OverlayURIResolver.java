/*******************************************************************************
 * Copyright (c) 2011 JBoss by Red Hat.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.uri;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.maven.ide.eclipse.wtp.overlay.modulecore.IOverlayVirtualComponent;


/**
 * Strategy interface that can be used to resolve to and from {@link OverlayURI}s.
 * 
 * @see OverlayURI
 */
public interface OverlayURIResolver {

  /**
   * Determines if the resolver supports the given URI
   * 
   * @param uri the URI
   * @return <tt>true</tt> if the resolver supports the URI
   */
  boolean canResolve(OverlayURI uri);

  /**
   * Resolve the specified URI into an {@link IOverlayVirtualComponent}.
   * 
   * @param project the war project
   * @param runtimePath the runtime path of the component
   * @param uri the URI to resolve
   * @return a resolved {@link IOverlayVirtualComponent}
   */
  IOverlayVirtualComponent resolve(IProject project, IPath runtimePath, OverlayURI uri);

  /**
   * Determines if the resolver supports the given component
   * 
   * @param component the component
   * @return <tt>true</tt> if the resolver supports the component
   */
  boolean canResolve(IOverlayVirtualComponent component);

  /**
   * Resolve the specified component into a {@link OverlayURI}.
   * 
   * @param component the component to resolve
   * @return a URI
   */
  OverlayURI resolve(IOverlayVirtualComponent component);

}
