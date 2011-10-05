/*******************************************************************************
 * Copyright (c) 2008 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.maven.ide.eclipse.wtp;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.war.Overlay;
import org.apache.maven.project.MavenProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jst.j2ee.web.project.facet.WebFacetUtils;
import org.eclipse.m2e.core.lifecyclemapping.model.IPluginExecutionMetadata;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.MavenProjectChangedEvent;
import org.eclipse.m2e.core.project.configurator.AbstractBuildParticipant;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;
import org.eclipse.m2e.jdt.IClasspathDescriptor;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualReference;
import org.eclipse.wst.common.project.facet.core.IFacetedProject;
import org.eclipse.wst.common.project.facet.core.ProjectFacetsManager;
import org.maven.ide.eclipse.wtp.internal.StringUtils;
import org.maven.ide.eclipse.wtp.overlay.modulecore.IOverlayVirtualComponent;
import org.maven.ide.eclipse.wtp.overlay.modulecore.OverlayComponentCore;


/**
 * Project configurator that supports WAR overlay projects.
 * 
 * @author Fred Bricon
 */
public class OverlayConfigurator extends WTPProjectConfigurator {

  @Override
  public void configureClasspath(IMavenProjectFacade facade, IClasspathDescriptor classpath, IProgressMonitor monitor)
      throws CoreException {
  }

  @Override
  public AbstractBuildParticipant getBuildParticipant(IMavenProjectFacade projectFacade, MojoExecution execution,
      IPluginExecutionMetadata executionMetadata) {
    return null;
  }

  @Override
  public void configure(ProjectConfigurationRequest request, IProgressMonitor monitor) throws CoreException {
  }

  @Override
  @SuppressWarnings("restriction")
  public void mavenProjectChanged(MavenProjectChangedEvent event, IProgressMonitor monitor) throws CoreException {
    IMavenProjectFacade facade = event.getMavenProject();
    if(facade == null) {
      return;
    }

    IProject project = facade.getProject();
    if(project.getResourceAttributes().isReadOnly()) {
      return;
    }

    IFacetedProject facetedProject = ProjectFacetsManager.create(project, true, monitor);
    if(!facetedProject.hasProjectFacet(WebFacetUtils.WEB_FACET)) {
      return;
    }

    MavenProject mavenProject = facade.getMavenProject(monitor);
    try {
      markerManager.deleteMarkers(facade.getPom(), MavenWtpConstants.WTP_MARKER_OVERLAY_ERROR);
      configureComponentReferences(project, mavenProject, monitor);
    } catch(Exception ex) {
      markerManager.addErrorMarkers(facade.getPom(), MavenWtpConstants.WTP_MARKER_OVERLAY_ERROR, ex);
    }
  }

  /**
   * Configure the {@link IVirtualReference references} of the {@link IVirtualComponent} to support overlays.
   * 
   * @param project the current WTP project
   * @param mavenProject the maven project
   * @param monitor the current monitor
   * @throws CoreException
   * @throws MojoExecutionException
   */
  private void configureComponentReferences(IProject project, MavenProject mavenProject, IProgressMonitor monitor)
      throws CoreException, MojoExecutionException {

    IVirtualComponent virtualComponent = ComponentCore.createComponent(project);
    if(virtualComponent == null) {
      return;
    }

    MavenSessionHelper helper = new MavenSessionHelper(mavenProject);
    try {
      helper.ensureDependenciesAreResolved("maven-war-plugin", "war:war");

      List<Overlay> overlays = getOverlaysToConvertToReferences(project, mavenProject);
      Set<IVirtualReference> references = new LinkedHashSet<IVirtualReference>();
      for(Overlay overlay : overlays) {
        IOverlayVirtualComponent overlayComponent = getOverlayVirtualComponent(project, mavenProject, overlay);
        if(overlayComponent != null) {
          IVirtualReference reference = ComponentCore.createReference(virtualComponent, overlayComponent);
          reference.setRuntimePath(getTargetPath(overlay));
          references.add(reference);
        }
      }

      IVirtualReference[] overlayReferences = references.toArray(new IVirtualReference[references.size()]);
      if(referencesHaveChanged(virtualComponent, overlayReferences)) {
        updateReferences(virtualComponent, overlayReferences);
      }
    } finally {
      helper.dispose();
    }

  }

  /**
   * Returns a list of {@link Overlay}s that should be converted to references. Overlays are returned in the correct
   * order for WTP.
   * 
   * @param project the current WTP project
   * @param mavenProject the maven project
   * @return a list of overylays
   * @throws CoreException
   * @throws MojoExecutionException
   */
  private List<Overlay> getOverlaysToConvertToReferences(IProject project, MavenProject mavenProject)
      throws CoreException, MojoExecutionException {
    WarPluginConfiguration warPlugin = new WarPluginConfiguration(mavenProject, project);
    List<Overlay> overlays = warPlugin.getOverlays();

    // Remove the current project overlay if it is the only item as it will be handled by the WAR component.
    // If the current project is in list a CurrentProjectOverlayVictualComponent will be created 
    // to ensure the specified overlay order is respected
    if(overlays.size() == 1 && overlays.get(0).isCurrentProject()) {
      overlays.remove(0);
    }

    //Component order must be inverted to follow maven's overlay order behaviour 
    //as in WTP, last components supersede the previous ones
    Collections.reverse(overlays);

    return overlays;
  }

  /**
   * Returns a {@link IOverlayVirtualComponent} for the given {@link Overlay}.
   * 
   * @param project the current WTP project
   * @param mavenProject the maven project
   * @param overlay the overlay to convert
   * @return a {@link IOverlayVirtualComponent} for the overlay
   */
  private IOverlayVirtualComponent getOverlayVirtualComponent(IProject project, MavenProject mavenProject,
      Overlay overlay) {

    if(overlay.shouldSkip()) {
      return null;
    }

    IOverlayVirtualComponent overlayComponent = createOverlayComponent(project, mavenProject, overlay);
    overlayComponent.setInclusions(new LinkedHashSet<String>(Arrays.asList(overlay.getIncludes())));
    overlayComponent.setExclusions(new LinkedHashSet<String>(Arrays.asList(overlay.getExcludes())));
    return overlayComponent;
  }

  /**
   * Create a new overlay component for the given {@link Overlay}. This returned component will not have include/exclude
   * filters set.
   * 
   * @param project the current WTP project
   * @param mavenProject the maven project
   * @param overlay the overlay
   * @return a new overlay component
   */
  private IOverlayVirtualComponent createOverlayComponent(IProject project, MavenProject mavenProject, Overlay overlay) {
    IMavenProjectFacade workspaceDependency = getWorkspaceDependency(overlay);

    if(workspaceDependency == null) {
      return createOverlayArchiveComponent(project, mavenProject, overlay);
    }

    IProject overlayProject = workspaceDependency.getProject();
    if(overlayProject != null && overlayProject.equals(project)) {
      return OverlayComponentCore.createSelfOverlayComponent(overlayProject);
    }
    return OverlayComponentCore.createOverlayComponent(overlayProject, overlay.getType());
  }

  /**
   * Factory method to create a new {@link IOverlayVirtualComponent} for the specified overlay.
   * 
   * @param project the current WTP project
   * @param mavenProject the maven project
   * @param overlay the overlay used to create the virtual component
   * @return a new {@link IOverlayVirtualComponent}
   */
  private IOverlayVirtualComponent createOverlayArchiveComponent(IProject project, MavenProject mavenProject,
      Overlay overlay) {
    IPath m2eWtpFolder = ProjectUtils.getM2eclipseWtpFolder(mavenProject, project);
    IPath unpackDirPath = new Path(m2eWtpFolder.toOSString() + "/overlays");
    String archiveLocation = ArtifactHelper.getM2REPOVarPath(overlay.getArtifact());
    return OverlayComponentCore.createOverlayArchiveComponent(project, archiveLocation, unpackDirPath,
        getTargetPath(overlay));
  }

  /**
   * Returns the {@link IMavenProjectFacade workspace} dependency for the given {@link Overlay} or <tt>null</tt> if the
   * overlay is not in the workspace.
   * 
   * @param overlay the overlay
   * @return a {@link IMavenProjectFacade} or <tt>null</tt>
   */
  private IMavenProjectFacade getWorkspaceDependency(Overlay overlay) {
    Artifact artifact = overlay.getArtifact();
    IMavenProjectFacade workspaceDependency = projectManager.getMavenProject(artifact.getGroupId(),
        artifact.getArtifactId(), artifact.getVersion());
    return workspaceDependency;
  }

  /**
   * Returns <tt>true</tt> if the overlay references specified differ from the current references set on the component.
   * 
   * @param virtualComponent the virtual component
   * @param overlayReferences the reference to compare
   * @return <tt>true</tt> if the references have changed
   */
  private boolean referencesHaveChanged(IVirtualComponent virtualComponent, IVirtualReference[] overlayReferences) {
    IVirtualReference[] existingOverlayReferences = WTPProjectsUtil.extractHardReferences(virtualComponent, true);
    return (WTPProjectsUtil.hasChanged2(existingOverlayReferences, overlayReferences));
  }

  /**
   * Update the specified {@link IVirtualComponent} to use a specific set of overlay references.
   * 
   * @param virtualComponent the component to update
   * @param overlayReferences the new set of overlay references
   */
  private void updateReferences(IVirtualComponent virtualComponent, IVirtualReference[] overlayReferences) {
    IVirtualReference[] existingNonOverlayReferences = WTPProjectsUtil.extractHardReferences(virtualComponent, false);
    IVirtualReference[] updatedReferences = new IVirtualReference[existingNonOverlayReferences.length
        + overlayReferences.length];
    System.arraycopy(existingNonOverlayReferences, 0, updatedReferences, 0, existingNonOverlayReferences.length);
    System.arraycopy(overlayReferences, 0, updatedReferences, existingNonOverlayReferences.length,
        overlayReferences.length);
    virtualComponent.setReferences(updatedReferences);
  }

  /**
   * Returns the target path that should be used for the given {@link Overlay}.
   * 
   * @param overlay the overlay
   * @return the target path
   */
  private IPath getTargetPath(Overlay overlay) {
    String targetPath = overlay.getTargetPath();
    if(StringUtils.nullOrEmpty(targetPath)) {
      targetPath = "/";
    }
    return new Path(targetPath);
  }
}
