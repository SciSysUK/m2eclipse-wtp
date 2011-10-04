
package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.resource;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.jar.JarFile;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.componentcore.resources.IVirtualResource;

/**
 * Adapts content from a {@link JarFile} into a {@link IVirtualFolder}.
 */
class JarVirtualFolder extends PartialReadOnlyVirtualFolder {

  private IProject project;

  private JarFile jarFile;

  private String name;

  private IPath runtimePath;

  private Map<String, IVirtualResource> members = new LinkedHashMap<String, IVirtualResource>();

  protected JarVirtualFolder(IProject project, JarFile jarFile, String name, IPath runtimePath) {
    this.project = project;
    this.jarFile = jarFile;
    //FIXME when to close jar file
    this.name = name;
    this.runtimePath = runtimePath;
  }

  public JarVirtualFolder getOrAddFolder(String name) {
    IVirtualResource folder = members.get(name);
    if(folder == null) {
      folder = new JarVirtualFolder(project, jarFile, name, runtimePath.append(name));
      members.put(name, folder);
    }
    return (JarVirtualFolder) folder;
  }

  protected void addMember(IVirtualResource member) {
    members.put(member.getName(), member);
  }

  protected JarFile getJarFile() {
    return jarFile;
  }

  public IVirtualResource[] members() throws CoreException {
    Collection<IVirtualResource> memberValues = members.values();
    return memberValues.toArray(new IVirtualResource[memberValues.size()]);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public IPath getRuntimePath() {
    return runtimePath;
  }

  public IProject getProject() {
    return project;
  }
}
