
package org.maven.ide.eclipse.wtp.overlay.internal.modulecore;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.resources.VirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.componentcore.resources.IVirtualReference;
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.filter.ExludeJavaPathFilter;
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.filter.FilteredVirtualFolder;
import org.maven.ide.eclipse.wtp.overlay.modulecore.IOverlayVirtualComponent;


/**
 * A {@link IOverlayVirtualComponent} that deals with references to the current "Self" project. When the current project
 * and an overlay contain a resource with the same name it is possible that one resource can overwrite another. This
 * overlay ensures that the overlay order as specified in the maven-war-plugin configuration is respected by WTP.
 * <p>
 * NOTE: Self overlays do not support filtering of resources.
 * 
 * @see ProjectOverlayVirtualComponent
 * @see SelfOverlayVirtualComponent
 */
@SuppressWarnings("restriction")
public class SelfOverlayVirtualComponent extends VirtualComponent implements IOverlayVirtualComponent {

  private static final IVirtualReference[] NO_REFERENCES = new IVirtualReference[0];

  /**
   * Create a new {@link SelfOverlayVirtualComponent} instance.
   * 
   * @param project the current project
   */
  public SelfOverlayVirtualComponent(IProject project) {
    super(project, IVirtualComponent.ROOT);
  }

  public IVirtualFolder getRootFolder() {
    IVirtualComponent component = ComponentCore.createComponent(getProject());
    if(component == null) {
      return null;
    }
    return new FilteredVirtualFolder(component.getRootFolder(), new ExludeJavaPathFilter());
  }

  @Override
  public IVirtualReference[] getReferences(Map<String, Object> paramMap) {
    return NO_REFERENCES;
  }

  public void setInclusions(Set<String> inclusionPatterns) {
    //We currently do not support filter configuration of self overlays
  }

  public void setExclusions(Set<String> exclusPatterns) {
    //We currently do not support filter configuration of self overlays
  }

  public Set<String> getExclusions() {
    return Collections.emptySet();
  }

  public Set<String> getInclusions() {
    return Collections.emptySet();
  }
}
