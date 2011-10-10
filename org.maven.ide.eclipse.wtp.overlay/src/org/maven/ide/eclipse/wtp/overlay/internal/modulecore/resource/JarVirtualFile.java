
package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.resource;

import java.io.File;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.common.componentcore.resources.IVirtualFile;


/**
 * Adapts content from a {@link JarFile} into a {@link IVirtualFile}.
 */
class JarVirtualFile extends PartialReadOnlyVirtualFile {

  private JarVirtualFolder owner;

  private JarEntry entry;

  private IPath runtimePath;

  /**
   * Create a new {@link JarVirtualFile}.
   * 
   * @param jarFileVirtualFolder
   * @param jarEntry
   * @param name
   * @param runtimePath
   */
  public JarVirtualFile(JarVirtualFolder owner, JarEntry jarEntry, IPath runtimePath) {
    this.owner = owner;
    this.entry = jarEntry;
    this.runtimePath = runtimePath;
  }

  @Override
  public IProject getProject() {
    return owner.getProject();
  }

  @Override
  public IPath getRuntimePath() {
    return runtimePath;
  }

  @Override
  public String getName() {
    return runtimePath.lastSegment();
  }
  
  @Override
  @SuppressWarnings("rawtypes")
  public Object getAdapter(Class adapter) {
    if(File.class.equals(adapter)) {
      return unpack().getFullPath().toFile();
    } else if(IFile.class.equals(adapter)) {
      return unpack();
    }
    return null;
  }

  private IFile unpack() {
    return owner.unpackJarEntry(entry);
  }  
}
