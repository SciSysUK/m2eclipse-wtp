package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v2;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualContainer;
import org.eclipse.wst.common.componentcore.resources.IVirtualFile;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.componentcore.resources.IVirtualResource;

/**
 * Base class for {@link IVirtualFolder} implementations that present a read-only view of the virtual resources. Many of
 * the standard {@link IVirtualFolder} methods are not supported by this class.
 */
public abstract class PartialReadOnlyVirtualFolder implements IVirtualFolder {

	public IVirtualResource[] members(int memberFlags) throws CoreException {
		throw new UnsupportedOperationException();
	}

	public IPath getRuntimePath() {
		return IVirtualComponent.ROOT;
	}

	public void create(int updateFlags, IProgressMonitor aMonitor) throws CoreException {
		throw new UnsupportedOperationException();
	}

	public boolean exists() {
		throw new UnsupportedOperationException();
	}

	public boolean exists(IPath path) {
		throw new UnsupportedOperationException();
	}

	public IVirtualResource findMember(String name) {
		return findMember(new Path(name), 0);
	}

	public IVirtualResource findMember(String name, int searchFlags) {
		return findMember(new Path(name), searchFlags);
	}

	public IVirtualResource findMember(IPath path) {
		return findMember(path, 0);
	}

	public IVirtualResource findMember(IPath path, int searchFlags) {
		throw new UnsupportedOperationException();
	}

	public IVirtualFile getFile(IPath path) {
		throw new UnsupportedOperationException();
	}

	public IVirtualFile getFile(String name) {
		throw new UnsupportedOperationException();
	}

	public IVirtualFolder getFolder(IPath path) {
		throw new UnsupportedOperationException();
	}

	public IVirtualFolder getFolder(String name) {
		throw new UnsupportedOperationException();
	}

	public IVirtualResource[] getResources(String aResourceType) {
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
		throw new UnsupportedOperationException();
	}

	public IResource[] getUnderlyingResources() {
		throw new UnsupportedOperationException();
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

	public IContainer getUnderlyingFolder() {
		throw new UnsupportedOperationException();
	}

	public IContainer[] getUnderlyingFolders() {
		throw new UnsupportedOperationException();
	}

}
