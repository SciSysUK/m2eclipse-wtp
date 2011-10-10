/*******************************************************************************
 * Copyright (c) 2010 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.maven.ide.eclipse.wtp.overlay.modulecore;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.ArchiveOverlayVirtualComponent;
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.ProjectOverlayVirtualComponent;
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.SelfOverlayVirtualComponent;

/**
 * Overlay Component Core
 * 
 * @author Fred Bricon
 */
public class OverlayComponentCore {
	
	public static IOverlayVirtualComponent createOverlayComponent(IProject aProject, String packagingType, String classifier) {
		return new ProjectOverlayVirtualComponent(aProject, packagingType, classifier);
	}

	//TODO check and prevent circular references
	public static IOverlayVirtualComponent createSelfOverlayComponent(IProject aProject) {
		return new SelfOverlayVirtualComponent(aProject);
	}

	public static IOverlayVirtualComponent createOverlayArchiveComponent(IProject aComponentProject, String archiveLocation, IPath unpackDirPath, IPath aRuntimePath) {
		return new ArchiveOverlayVirtualComponent(aComponentProject, archiveLocation, aRuntimePath, unpackDirPath);
	}
	
}
