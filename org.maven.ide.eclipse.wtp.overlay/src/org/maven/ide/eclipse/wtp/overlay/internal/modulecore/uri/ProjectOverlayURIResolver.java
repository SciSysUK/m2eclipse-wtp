
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

  private static final String OVERLAY_TYPE = "type";

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
    if(component.getOverlayType() != null) {
      parameters.put(OVERLAY_TYPE, component.getOverlayType());
    }
    return newOverlayURI(Collections.singletonList(name), null);
  }

  @Override
  protected ProjectOverlayVirtualComponent doResolve(IProject project, IPath runtimePath, OverlayURI uri) {
    String name = uri.getSegments().get(0);
    String overlayType = uri.getParameters().get(OVERLAY_TYPE);
    IProject overlayProject = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
    return new ProjectOverlayVirtualComponent(overlayProject, overlayType);
  }
}
