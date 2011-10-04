
package org.maven.ide.eclipse.wtp.overlay.internal.modulecore;

import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.componentcore.resources.IVirtualReference;
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.filter.AntPathFilter;
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.filter.FilteredVirtualFolder;
import org.maven.ide.eclipse.wtp.overlay.modulecore.IOverlayVirtualComponent;


/**
 * Helper class that can be delegated to when implementing {@link IOverlayVirtualComponent}s. This helper class is
 * required as the various {@link IOverlayVirtualComponent}s implementations need to extend different subclasses.
 */
class OverlayVirtualComponentHelper {

  private Set<String> inclusions;

  private Set<String> exclusions;

  /**
   * Return the specified root folder with filters from this overlay applied.
   * 
   * @param unfilteredRootFolder the unfiltered root folder.
   * @return a filtered folder
   */
  public IVirtualFolder getFilteredRootFolder(IVirtualFolder unfilteredRootFolder) {
    AntPathFilter pathFilter = new AntPathFilter(inclusions, exclusions);
    return new FilteredVirtualFolder(unfilteredRootFolder, pathFilter);
  }

  /**
   * Return the specified unfiltered references with filters from the overlay applied.
   * 
   * @param unfilteredReferences the unfiltered references
   * @return references with filteres applied
   */
  public Set<IVirtualReference> getFilteredReferences(Set<IVirtualReference> unfilteredReferences) {
    Set<IVirtualReference> filteredReferences = new LinkedHashSet<IVirtualReference>();
    AntPathFilter pathFilter = new AntPathFilter(inclusions, exclusions);
    for(IVirtualReference virtualReference : unfilteredReferences) {
      IPath path = virtualReference.getRuntimePath().append(virtualReference.getArchiveName());
      if(!pathFilter.isFiltered(path)) {
        filteredReferences.add(virtualReference);
      }
    }
    return filteredReferences;
  }

  /**
   * @see IOverlayVirtualComponent#getInclusions()
   */
  public Set<String> getInclusions() {
    return inclusions;
  }

  /**
   * @see IOverlayVirtualComponent#setInclusions(Set)
   */
  public void setInclusions(Set<String> inclusions) {
    this.inclusions = inclusions;
  }

  /**
   * @see IOverlayVirtualComponent#getExclusions()
   */
  public Set<String> getExclusions() {
    return exclusions;
  }

  /**
   * @see IOverlayVirtualComponent#setExclusions(Set)
   */
  public void setExclusions(Set<String> exclusions) {
    this.exclusions = exclusions;
  }

  @Override
  public boolean equals(Object obj) {
    //FIXME tidy up
    if(this == obj) {
      return true;
    }
    if(obj == null) {
      return false;
    }
    if(getClass() != obj.getClass()) {
      return false;
    }
    OverlayVirtualComponentHelper other = (OverlayVirtualComponentHelper) obj;
    if(exclusions == null) {
      if(other.exclusions != null) {
        return false;
      }
    } else if(!exclusions.equals(other.exclusions)) {
      return false;
    }
    if(inclusions == null) {
      if(other.inclusions != null) {
        return false;
      }
    } else if(!inclusions.equals(other.inclusions)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((exclusions == null) ? 0 : exclusions.hashCode());
    result = prime * result + ((inclusions == null) ? 0 : inclusions.hashCode());
    return result;
  }
}
