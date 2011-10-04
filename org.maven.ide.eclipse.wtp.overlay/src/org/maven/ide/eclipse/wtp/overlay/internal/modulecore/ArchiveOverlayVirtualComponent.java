
package org.maven.ide.eclipse.wtp.overlay.internal.modulecore;

import java.io.File;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.common.componentcore.internal.resources.VirtualArchiveComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.resource.JarFileVirtualFolder;
import org.maven.ide.eclipse.wtp.overlay.modulecore.IOverlayVirtualComponent;


/**
 * A {@link IOverlayVirtualComponent} that deals with references to other external {@link IFile archives}. This
 * component can be used to support overlays that refer to artifacts that are not contained within the workspace.
 * 
 * @see OverlayVirtualComponentHelper
 */
@SuppressWarnings("restriction")
public class ArchiveOverlayVirtualComponent extends VirtualArchiveComponent implements IOverlayVirtualComponent {

  private OverlayVirtualComponentHelper helper = new OverlayVirtualComponentHelper();

  private IPath unpackDirPath;

  public ArchiveOverlayVirtualComponent(IProject project, String archiveLocation, IPath runtimePath, IPath unpackDirPath) {
    super(project, archiveLocation, runtimePath);
    this.unpackDirPath = unpackDirPath;
  }

  public IVirtualFolder getRootFolder() {
    return helper.getFilteredRootFolder(getUnfilteredRootFolder());
  }

  private IVirtualFolder getUnfilteredRootFolder() {
    File archive = getArchive();
    try {
      return new JarFileVirtualFolder(getProject(), archive, unpackDirPath);
    } catch(Exception e) {
      e.printStackTrace();
      throw new RuntimeException(e.getLocalizedMessage(), e);
    }
  }

  private File getArchive() {
    File archive = (File) getAdapter(File.class);
    //FIXME check that the file exists and is readable?
    return archive;
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

  public IPath getUnpackDirPath() {
    return unpackDirPath;
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj)
      return true;
    if(!super.equals(obj))
      return false;
    if(getClass() != obj.getClass())
      return false;
    ArchiveOverlayVirtualComponent other = (ArchiveOverlayVirtualComponent) obj;
    if(!super.equals(obj)) {
      return false;
    }
    if(!helper.equals(other.helper)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + helper.hashCode();
    return result;
  }

}
