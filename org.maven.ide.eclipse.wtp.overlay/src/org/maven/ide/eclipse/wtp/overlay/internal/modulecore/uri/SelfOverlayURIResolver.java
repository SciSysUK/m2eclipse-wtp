
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
