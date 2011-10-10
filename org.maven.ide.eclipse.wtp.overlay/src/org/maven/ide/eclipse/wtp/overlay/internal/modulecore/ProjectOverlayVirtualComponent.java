
package org.maven.ide.eclipse.wtp.overlay.internal.modulecore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jst.common.internal.modulecore.AddClasspathFoldersParticipant;
import org.eclipse.jst.common.internal.modulecore.AddClasspathLibReferencesParticipant;
import org.eclipse.jst.common.internal.modulecore.AddMappedOutputFoldersParticipant;
import org.eclipse.jst.common.internal.modulecore.IgnoreJavaInSourceFolderParticipant;
import org.eclipse.jst.j2ee.internal.common.exportmodel.JEEHeirarchyExportParticipant;
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

  private OverlayVirtualComponentHelper helper;

  private String classifier;

  private String packagingType;

  /**
   * Create anew {@link ProjectOverlayVirtualComponent} instance.
   * 
   * @param overlayProject the project being overlayed
   * @param classifier the overlay classifier
   * @param packagingType the packaging type
   */
  public ProjectOverlayVirtualComponent(IProject overlayProject, String packagingType, String classifier) {
    super(overlayProject, ROOT);
    this.overlayProject = overlayProject;
    this.packagingType = packagingType;
    this.classifier = classifier;
    helper = new OverlayVirtualComponentHelper();
  }

  public IVirtualFolder getRootFolder() {
    //FIXME cache the result
    return helper.getFilteredRootFolder(getUnfilteredRootFolder());
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
    List<IFlattenParticipant> participants = new ArrayList<IFlattenParticipant>();
    participants.add(new JEEHeirarchyExportParticipant());
    participants.add(new AddClasspathLibReferencesParticipant());
    participants.add(new AddClasspathFoldersParticipant());
    participants.add(new AddMappedOutputFoldersParticipant());
    participants.add(new IgnoreJavaInSourceFolderParticipant());
    if ("war-overlay".equals(packagingType)) {
      participants.add(new WarOverlayIgnoreParticipant(classifier));
    }
    return participants;
  }

  @Override
  public IVirtualReference[] getReferences(Map<String, Object> options) {
    try {
      Set<IVirtualReference> references = getUnfilteredRootFolder().getReferences();
      Set<IVirtualReference> filteredReferences = helper.getFilteredReferences(references);
      return filteredReferences.toArray(new IVirtualReference[filteredReferences.size()]);
    } catch(CoreException e) {
      throw new RuntimeException(e);
    }
  }

  public String getClassifier() {
    return classifier;
  }

  public String getPackagingType() {
    return packagingType;
  }

  public void setInclusions(Set<String> inclusions) {
    helper.setInclusions(inclusions);
  }

  public void setExclusions(Set<String> exclusions) {
    helper.setExclusions(exclusions);
  }

  public Set<String> getInclusions() {
    return helper.getInclusions();
  }

  public Set<String> getExclusions() {
    return helper.getExclusions();
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
    if(!helper.equals(other.helper)) {
      return false;
    }
    // FIXME Add Packaging type
    if(classifier != other.classifier && (classifier != null && !classifier.equals(other.classifier))) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + helper.hashCode();
    if(classifier != null) {
      result = prime * result + classifier.hashCode();
    }
    return result;
  }

}
