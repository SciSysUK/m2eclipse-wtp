package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v2;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.common.componentcore.resources.IVirtualFile;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.componentcore.resources.IVirtualResource;

/**
 * Base class for {@link IVirtualFolder} implementations that present a read-only view of the virtual resources. Many of
 * the standard {@link IVirtualFolder} methods are not supported by this class.
 */
public abstract class PartialReadOnlyVirtualFolder extends PartialReadOnlyVirtualResource implements IVirtualFolder {

	public IVirtualResource[] members(int memberFlags) throws CoreException {
		throw new UnsupportedOperationException();
	}

	public void create(int updateFlags, IProgressMonitor aMonitor) throws CoreException {
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

	public IContainer getUnderlyingFolder() {
		throw new UnsupportedOperationException();
	}

	public IContainer[] getUnderlyingFolders() {
		throw new UnsupportedOperationException();
	}

}
