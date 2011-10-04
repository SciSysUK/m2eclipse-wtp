
package org.maven.ide.eclipse.wtp.overlay.internal.modulecore;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jst.common.internal.modulecore.AddClasspathFoldersParticipant;
import org.eclipse.jst.common.internal.modulecore.AddClasspathLibReferencesParticipant;
import org.eclipse.jst.common.internal.modulecore.AddMappedOutputFoldersParticipant;
import org.eclipse.jst.common.internal.modulecore.IgnoreJavaInSourceFolderParticipant;
import org.eclipse.jst.common.internal.modulecore.SingleRootExportParticipant;
import org.eclipse.jst.j2ee.internal.common.exportmodel.JEEHeirarchyExportParticipant;
import org.eclipse.jst.j2ee.internal.common.exportmodel.JavaEESingleRootCallback;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.flat.FlatVirtualComponent;
import org.eclipse.wst.common.componentcore.internal.flat.FlatVirtualComponent.FlatComponentTaskModel;
import org.eclipse.wst.common.componentcore.internal.flat.IFlatResource;
import org.eclipse.wst.common.componentcore.internal.flat.IFlattenParticipant;
import org.eclipse.wst.common.componentcore.internal.resources.VirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.componentcore.resources.IVirtualReference;
import org.eclipse.wst.common.componentcore.resources.IVirtualResource;
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.resource.FlatVirtualComponentVirtualFolder;
import org.maven.ide.eclipse.wtp.overlay.modulecore.IOverlayVirtualComponent;


/**
 * A {@link IOverlayVirtualComponent} that deals with references to other workspace {@link IProject projects}.
 * <p>
 * This implementation will use a {@link FlatVirtualComponent} to extract {@link IFlatResource}s before adapting them
 * back into {@link IVirtualResource}s.
 * 
 * @see OverlayVirtualComponentHelper
 * @see SelfOverlayVirtualComponent
 */
@SuppressWarnings("restriction")
public class ProjectOverlayVirtualComponent extends VirtualComponent implements IOverlayVirtualComponent {

  private IProject overlayProject;

  private OverlayVirtualComponentHelper overlayFilter = new OverlayVirtualComponentHelper();

  private String overlayType;

  /**
   * Create anew {@link ProjectOverlayVirtualComponent} instance.
   * 
   * @param overlayProject the project being overlayed
   * @param overlayType the overlay type
   */
  public ProjectOverlayVirtualComponent(IProject overlayProject, String overlayType) {
    super(overlayProject, ROOT);
    this.overlayProject = overlayProject;
    this.overlayType = overlayType;
  }

  public IVirtualFolder getRootFolder() {
    //FIXME cache the result
    return overlayFilter.getFilteredRootFolder(getUnfilteredRootFolder());
  }

  private FlatVirtualComponentVirtualFolder getUnfilteredRootFolder() {
    IVirtualComponent overlayComponent = ComponentCore.createComponent(overlayProject);
    if(overlayComponent == null) {
      return null;
    }
    FlatVirtualComponent flatVirtualComponent = new FlatVirtualComponent(overlayComponent, getFlatComponentDataModel());
    return new FlatVirtualComponentVirtualFolder(flatVirtualComponent);
  }

  private FlatComponentTaskModel getFlatComponentDataModel() {
    FlatComponentTaskModel dataModel = new FlatComponentTaskModel();
    dataModel.put(FlatVirtualComponent.PARTICIPANT_LIST, getFlattenParticipants());
    return dataModel;
  }

  private List<IFlattenParticipant> getFlattenParticipants() {
    return Arrays.<IFlattenParticipant> asList(new SingleRootExportParticipant(new JavaEESingleRootCallback()),
        new JEEHeirarchyExportParticipant(), new AddClasspathLibReferencesParticipant(),
        new AddClasspathFoldersParticipant(), new AddMappedOutputFoldersParticipant(),
        new IgnoreJavaInSourceFolderParticipant());
  }

  @Override
  public IVirtualReference[] getReferences(Map<String, Object> options) {
    try {
      Set<IVirtualReference> references = getUnfilteredRootFolder().getReferences();
      Set<IVirtualReference> filteredReferences = overlayFilter.getFilteredReferences(references);
      return filteredReferences.toArray(new IVirtualReference[filteredReferences.size()]);
    } catch(CoreException e) {
      throw new RuntimeException(e);
    }
  }

  public String getOverlayType() {
    return overlayType;
  }

  public void setInclusions(Set<String> inclusions) {
    overlayFilter.setInclusions(inclusions);
  }

  public void setExclusions(Set<String> exclusions) {
    overlayFilter.setExclusions(exclusions);
  }

  public Set<String> getInclusions() {
    return overlayFilter.getInclusions();
  }

  public Set<String> getExclusions() {
    return overlayFilter.getExclusions();
  }
  
  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(!super.equals(obj))
      return false;
    if(getClass() != obj.getClass())
      return false;
    ProjectOverlayVirtualComponent other = (ProjectOverlayVirtualComponent) obj;
    if(!super.equals(obj)) {
      return false;
    }
    if(!overlayFilter.equals(other.overlayFilter)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + overlayFilter.hashCode();
    return result;
  }
}
