
package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.resource;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualContainer;
import org.eclipse.wst.common.componentcore.resources.IVirtualResource;


/**
 * Base class for {@link IVirtualResource} implementations that present a read-only view of the virtual resources. Many
 * of the standard {@link IVirtualResource} methods are not supported by this class.
 */
public abstract class PartialReadOnlyVirtualResource implements IVirtualResource {

  public abstract IPath getRuntimePath();

  public abstract IProject getProject();

  public boolean exists() {
    throw new UnsupportedOperationException();
  }

  public void createLink(IPath aProjectRelativeLocation, int updateFlags, IProgressMonitor monitor)
      throws CoreException {
    throw new UnsupportedOperationException();
  }

  public void removeLink(IPath aProjectRelativeLocation, int updateFlags, IProgressMonitor monitor)
      throws CoreException {
    throw new UnsupportedOperationException();
  }

  public void delete(int updateFlags, IProgressMonitor monitor) throws CoreException {
    throw new UnsupportedOperationException();
  }

  public String getFileExtension() {
    throw new UnsupportedOperationException();
  }

  public IPath getWorkspaceRelativePath() {
    throw new UnsupportedOperationException();
  }

  public IPath getProjectRelativePath() {
    throw new UnsupportedOperationException();
  }

  public String getName() {
    throw new UnsupportedOperationException();
  }

  public IVirtualComponent getComponent() {
    throw new UnsupportedOperationException();
  }

  public IVirtualContainer getParent() {
    throw new UnsupportedOperationException();
  }

  public int getType() {
    throw new UnsupportedOperationException();
  }

  public IResource getUnderlyingResource() {
    return null;
  }

  public IResource[] getUnderlyingResources() {
    return new IResource[] {};
  }

  public boolean isAccessible() {
    throw new UnsupportedOperationException();
  }

  public String getResourceType() {
    throw new UnsupportedOperationException();
  }

  public void setResourceType(String aResourceType) {
    throw new UnsupportedOperationException();
  }

  public boolean contains(ISchedulingRule rule) {
    throw new UnsupportedOperationException();
  }

  public boolean isConflicting(ISchedulingRule rule) {
    throw new UnsupportedOperationException();
  }

  @SuppressWarnings("rawtypes")
  public Object getAdapter(Class adapter) {
    throw new UnsupportedOperationException();
  }
}
