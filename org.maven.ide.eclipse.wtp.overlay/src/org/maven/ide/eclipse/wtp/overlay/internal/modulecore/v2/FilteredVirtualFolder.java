package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v2;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualContainer;
import org.eclipse.wst.common.componentcore.resources.IVirtualFile;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.componentcore.resources.IVirtualResource;

public class FilteredVirtualFolder implements IVirtualFolder {

	private IVirtualFolder source;
	
	private IPathFilter pathFilter;

	public FilteredVirtualFolder(IVirtualFolder source, IPathFilter pathFilter) {
		this.source = source;
		this.pathFilter = pathFilter;
	}

	public IContainer getUnderlyingFolder() {
		return source.getUnderlyingFolder();
	}

	public IContainer[] getUnderlyingFolders() {
		return source.getUnderlyingFolders();
	}

	public void create(int updateFlags, IProgressMonitor aMonitor) throws CoreException {
		source.create(updateFlags, aMonitor);
	}

	public boolean exists(IPath path) {
		return source.exists(path) && !isFiltered(path);
	}

	public IVirtualResource findMember(String name) {
		return nullIfFiltered(source.findMember(name));
	}

	public IVirtualResource findMember(String name, int searchFlags) {
		return nullIfFiltered(source.findMember(name, searchFlags));
	}

	public IVirtualResource findMember(IPath path) {
		return nullIfFiltered(source.findMember(path));
	}

	public IVirtualResource findMember(IPath path, int searchFlags) {
		return nullIfFiltered(source.findMember(path, searchFlags));
	}

	public IVirtualFile getFile(IPath path) {
		return source.getFile(path);
	}

	public IVirtualFolder getFolder(IPath path) {
		return source.getFolder(path);
	}

	public IVirtualFile getFile(String name) {
		return source.getFile(name);
	}

	public IVirtualFolder getFolder(String name) {
		return source.getFolder(name);
	}

	public IVirtualResource[] members() throws CoreException {
		return applyFilter(source.members());
	}

	public IVirtualResource[] members(int memberFlags) throws CoreException {
		return applyFilter(source.members(memberFlags));
	}

	public IVirtualResource[] getResources(String aResourceType) {
		return applyFilter(source.getResources(aResourceType));
	}

	public void createLink(IPath aProjectRelativeLocation, int updateFlags, IProgressMonitor monitor)
			throws CoreException {
		source.createLink(aProjectRelativeLocation, updateFlags, monitor);
	}

	public void removeLink(IPath aProjectRelativeLocation, int updateFlags, IProgressMonitor monitor)
			throws CoreException {
		source.removeLink(aProjectRelativeLocation, updateFlags, monitor);
	}

	public void delete(int updateFlags, IProgressMonitor monitor) throws CoreException {
		source.delete(updateFlags, monitor);
	}

	@Override
	public int hashCode() {
		return source.hashCode();
	}

	public boolean equals(Object other) {
		return source.equals(other);
	}

	public boolean exists() {
		return source.exists();
	}

	public String getFileExtension() {
		return source.getFileExtension();
	}

	public IPath getWorkspaceRelativePath() {
		return source.getWorkspaceRelativePath();
	}

	public IPath getProjectRelativePath() {
		return source.getProjectRelativePath();
	}

	public IPath getRuntimePath() {
		return source.getRuntimePath();
	}

	public String getName() {
		return source.getName();
	}

	public IVirtualComponent getComponent() {
		return source.getComponent();
	}

	public IVirtualContainer getParent() {
		return source.getParent();
	}

	public IProject getProject() {
		return source.getProject();
	}

	public int getType() {
		return source.getType();
	}

	public IResource getUnderlyingResource() {
		return source.getUnderlyingResource();
	}

	public IResource[] getUnderlyingResources() {
		return source.getUnderlyingResources();
	}

	public boolean isAccessible() {
		return source.isAccessible();
	}

	public String getResourceType() {
		return source.getResourceType();
	}

	public void setResourceType(String aResourceType) {
		source.setResourceType(aResourceType);
	}

	public boolean contains(ISchedulingRule rule) {
		return source.contains(rule);
	}

	public boolean isConflicting(ISchedulingRule rule) {
		return source.isConflicting(rule);
	}

	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		return source.getAdapter(adapter);
	}

	private IVirtualResource[] applyFilter(IVirtualResource[] members) {
		List<IVirtualResource> filteredMembers = new ArrayList<IVirtualResource>();
		for (IVirtualResource member : members) {
			if (member instanceof IVirtualFolder) {
				filteredMembers.add(new FilteredVirtualFolder((IVirtualFolder) member, pathFilter));
			} else {
				if (!isFiltered(member)) {
					filteredMembers.add(member);
				}
			}
		}
		return filteredMembers.toArray(new IVirtualResource[filteredMembers.size()]);
	}

	private IVirtualResource nullIfFiltered(IVirtualResource resource) {
		return (isFiltered(resource) ? null : resource);
	}

	private boolean isFiltered(IVirtualResource virtualResource) {
		return isFiltered(virtualResource.getRuntimePath());
	}

	private boolean isFiltered(IPath path) {
		return pathFilter.isFiltered(path);
	}
}
