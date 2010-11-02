/*******************************************************************************
 * Copyright (c) 2008 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.maven.ide.eclipse.wtp.modulecore;

import org.apache.maven.artifact.Artifact;
import org.eclipse.core.resources.IProject;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;

/**
 * MavenComponentCore
 *
 * @author Fred Bricon
 */
public class MavenComponentCore {

  public static IVirtualComponent createOverlayComponent(IProject aProject) {
    return new WarOverlayVirtualComponent();
  }
  
  public static IVirtualComponent createOverlayArchiveComponent(IProject aProject, Artifact artifact) {
    return new WarOverlayVirtualArchiveComponent();
  }
}
