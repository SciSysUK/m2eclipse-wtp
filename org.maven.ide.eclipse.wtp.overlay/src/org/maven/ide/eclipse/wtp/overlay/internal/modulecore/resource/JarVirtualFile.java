
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

  private JarEntry entry;

  private String name;

  private IPath runtimePath;

  private JarFileVirtualFolder jarFileVirtualFolder;

  /**
   * Create a new {@link JarVirtualFile}.
   * 
   * @param jarFileVirtualFolder
   * @param jarEntry
   * @param name
   * @param runtimePath
   */
  public JarVirtualFile(JarFileVirtualFolder jarFileVirtualFolder, JarEntry jarEntry, String name, IPath runtimePath) {
    this.jarFileVirtualFolder = jarFileVirtualFolder;
    this.entry = jarEntry;
    this.name = name;
    this.runtimePath = runtimePath;
  }

  @Override
  public IProject getProject() {
    return jarFileVirtualFolder.getProject();
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
    return jarFileVirtualFolder.unpackJarEntry(entry);
  }

  @Override
  public IPath getRuntimePath() {
    return runtimePath;
  }

  @Override
  public String getName() {
    return name;
  }
}
