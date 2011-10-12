/*******************************************************************************
 * Copyright (c) 2011 JBoss by Red Hat.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.uri;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.ProjectOverlayVirtualComponent;


/**
 * {@link OverlayURIResolver} for {@link ProjectOverlayVirtualComponent}s.
 */
@SuppressWarnings("restriction")
public class ProjectOverlayURIResolver extends AbstractOverlayURIResolver<ProjectOverlayVirtualComponent> {

	private static final String CLASSIFIER = "classifier";

	private static final String TYPE = "type";

	private static final String WAR = "war";
  
  @Override
  protected String getType() {
    return "prj";
  }

  @Override
  protected Class<ProjectOverlayVirtualComponent> getOverlayType() {
    return ProjectOverlayVirtualComponent.class;
  }

  @Override
  protected OverlayURI doResolve(ProjectOverlayVirtualComponent component) {
    String name = component.getName();
    Map<String, String> parameters = new HashMap<String, String>();
    if(component.getClassifier() != null) {
      parameters.put(CLASSIFIER, component.getClassifier());
    }
    if (!component.getPackagingType().equals(WAR)) {
    	parameters.put(TYPE, component.getPackagingType());
    }
    return newOverlayURI(Collections.singletonList(name), parameters);
  }

  @Override
  protected ProjectOverlayVirtualComponent doResolve(IProject project, IPath runtimePath, OverlayURI uri) {
    String name = uri.getSegments().get(0);
    String classifier = uri.getParameters().get(CLASSIFIER);
    String packagingType = uri.getParameters().get(TYPE);
    if (packagingType == null) {
    	packagingType = WAR;
    }
    IProject overlayProject = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
    return new ProjectOverlayVirtualComponent(overlayProject, packagingType, classifier);
  }
}
