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
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.SelfOverlayVirtualComponent;

/**
 * {@link OverlayURIResolver} for {@link SelfOverlayVirtualComponent}s.
 */
public class SelfOverlayURIResolver extends AbstractOverlayURIResolver<SelfOverlayVirtualComponent> {

  @Override
  protected String getType() {
    return "slf";
  }

  @Override
  protected Class<SelfOverlayVirtualComponent> getOverlayType() {
    return SelfOverlayVirtualComponent.class;
  }
  
  @Override
  public OverlayURI doResolve(SelfOverlayVirtualComponent component) {
    return newOverlayURI(null, null);
  }

  @Override
  protected SelfOverlayVirtualComponent doResolve(IProject project, IPath runtimePath, OverlayURI uri) {
    return new SelfOverlayVirtualComponent(project);
  }
}
