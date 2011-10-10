
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
class JarVirtualNestedFolder extends PartialReadOnlyVirtualFolder {

  private JarVirtualFolder owner;

  private IPath runtimePath;

  private Map<String, IVirtualResource> members = new LinkedHashMap<String, IVirtualResource>();

  public JarVirtualNestedFolder(JarVirtualFolder owner, IPath runtimePath) {
    this.owner = owner;
    this.runtimePath = runtimePath;
  }

  public JarVirtualNestedFolder getOrAddFolder(String name) {
    IVirtualResource folder = members.get(name);
    if(folder == null) {
      folder = new JarVirtualNestedFolder(owner, runtimePath.append(name));
      members.put(name, folder);
    }
    return (JarVirtualNestedFolder) folder;
  }

  public void addMember(IVirtualResource member) {
    members.put(member.getName(), member);
  }

  public IVirtualResource[] members() throws CoreException {
    Collection<IVirtualResource> memberValues = members.values();
    return memberValues.toArray(new IVirtualResource[memberValues.size()]);
  }

  @Override
  public String getName() {
    return runtimePath.lastSegment();
  }

  @Override
  public IPath getRuntimePath() {
    return runtimePath;
  }

  @Override
  public IProject getProject() {
    return owner.getProject();
  }
}
