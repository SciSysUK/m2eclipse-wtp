
package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.flat.FlatVirtualComponent;
import org.eclipse.wst.common.componentcore.internal.flat.IFlatFile;
import org.eclipse.wst.common.componentcore.internal.flat.IFlatFolder;
import org.eclipse.wst.common.componentcore.internal.flat.IFlatResource;
import org.eclipse.wst.common.componentcore.internal.resources.VirtualArchiveComponent;
import org.eclipse.wst.common.componentcore.internal.resources.VirtualFile;
import org.eclipse.wst.common.componentcore.internal.resources.VirtualFolder;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFile;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.componentcore.resources.IVirtualReference;
import org.eclipse.wst.common.componentcore.resources.IVirtualResource;


/**
 * Adapts {@link FlatVirtualComponent}s to {@link IVirtualFolder}s.
 */
@SuppressWarnings("restriction")
public class FlatVirtualComponentVirtualFolder extends PartialReadOnlyVirtualFolder {

  private FlatVirtualComponent flatVirtualComponent;

  public FlatVirtualComponentVirtualFolder(FlatVirtualComponent flatVirtualComponent) {
    if(flatVirtualComponent == null) {
      throw new IllegalArgumentException("flatVirtualComponent must not be null");
    }
    this.flatVirtualComponent = flatVirtualComponent;
  }

  public IPath getRuntimePath() {
    return IVirtualComponent.ROOT;
  }

  public IProject getProject() {
    return flatVirtualComponent.getComponent().getProject();
  }

  public IVirtualResource[] members() throws CoreException {
    return asVirtualResources(flatVirtualComponent.fetchResources());
  }

  private IVirtualResource[] asVirtualResources(IFlatResource[] flatResources) {
    List<IVirtualResource> virtualResources = new ArrayList<IVirtualResource>();

    for(IFlatResource resource : flatResources) {
      IVirtualResource virtualResource = asVirtualResource(resource);
      if(virtualResource != null) {
        virtualResources.add(virtualResource);
      }
    }
    return virtualResources.toArray(new IVirtualResource[virtualResources.size()]);
  }

  private IVirtualResource asVirtualResource(IFlatResource resource) {
    if(resource instanceof IFlatFile) {
      return asVirtualFile((IFlatFile) resource);
    }
    if(resource instanceof IFlatFolder) {
      return asVirtualFolder((IFlatFolder) resource);
    }
    throw new IllegalStateException("Unknown IFlatResource type " + resource.getClass());
  }

  private IVirtualFile asVirtualFile(IFlatFile flatFile) {
    if(!isInWorkspace(flatFile)) {
      return null;
    }
    IFile workspaceFile = (IFile) flatFile.getAdapter(IFile.class);
    IPath filePath = getResourcePath(flatFile);
    return new VirtualFile(getProject(), filePath, workspaceFile);
  }

  private IVirtualFolder asVirtualFolder(final IFlatFolder flatFolder) {
    IPath folderPath = getResourcePath(flatFolder);
    return new VirtualFolder(getProject(), folderPath) {
      public IVirtualResource[] members() throws CoreException {
        return asVirtualResources(flatFolder.members());
      };
    };
  }

  /**
   * Returns {@link IVirtualReference}s from the underlying {@link FlatVirtualComponent} by searching for any
   * {@link IFlatFile}s that are not in the workspace.
   * 
   * @return references to flat files not contained in the workspace
   * @throws CoreException
   */
  public Set<IVirtualReference> getReferences() throws CoreException {
    Set<IVirtualReference> virtualReferences = new LinkedHashSet<IVirtualReference>();
    collectVirtualReferences(virtualReferences, flatVirtualComponent.fetchResources());
    return Collections.unmodifiableSet(virtualReferences);
  }

  private void collectVirtualReferences(Set<IVirtualReference> virtualReferences, IFlatResource[] flatResources) {
    for(IFlatResource flatResource : flatResources) {
      if(flatResource instanceof IFlatFolder) {
        collectVirtualReferences(virtualReferences, ((IFlatFolder) flatResource).members());
      } else if(flatResource instanceof IFlatFile) {
        IFlatFile flatFile = (IFlatFile) flatResource;
        if(!isInWorkspace(flatFile)) {
          IVirtualReference virtualReference = createReference(flatFile);
          if(virtualReference != null) {
            virtualReferences.add(virtualReference);
          }
        }
      }
    }
  }

  private IVirtualReference createReference(IFlatFile flatFile) {
    File file = (File) flatFile.getAdapter(File.class);
    if(file != null && file.exists()) {
      String archiveLocation = VirtualArchiveComponent.LIBARCHIVETYPE + Path.SEPARATOR + file.getAbsolutePath();
      IPath runtimePath = flatFile.getModuleRelativePath();
      VirtualArchiveComponent referencedComponent = new VirtualArchiveComponent(getProject(), archiveLocation,
          runtimePath);

      IVirtualReference virtualReference = ComponentCore.createReference(flatVirtualComponent.getComponent(),
          referencedComponent);
      virtualReference.setArchiveName(file.getName());
      virtualReference.setRuntimePath(runtimePath);
      return virtualReference;
    }
    return null;
  }

  private IPath getResourcePath(IFlatResource flatResource) {
    return flatResource.getModuleRelativePath().append(flatResource.getName());
  }

  private boolean isInWorkspace(IFlatFile flatFile) {
    return flatFile.getAdapter(IFile.class) != null;
  }
}
