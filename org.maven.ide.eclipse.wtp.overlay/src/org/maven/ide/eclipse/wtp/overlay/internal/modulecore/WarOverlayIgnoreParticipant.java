
package org.maven.ide.eclipse.wtp.overlay.internal.modulecore;

import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.jst.common.jdt.internal.javalite.IJavaProjectLite;
import org.eclipse.jst.common.jdt.internal.javalite.JavaCoreLite;
import org.eclipse.jst.common.jdt.internal.javalite.JavaLiteUtilities;
import org.eclipse.wst.common.componentcore.internal.flat.AbstractFlattenParticipant;
import org.eclipse.wst.common.componentcore.internal.flat.FlatVirtualComponent.FlatComponentTaskModel;
import org.eclipse.wst.common.componentcore.internal.flat.IFlatFile;
import org.eclipse.wst.common.componentcore.internal.flat.IFlatResource;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;


@SuppressWarnings("restriction")
public class WarOverlayIgnoreParticipant extends AbstractFlattenParticipant {

  private List<IContainer> javaOutputContainers;

  private boolean isWebContent;

  public WarOverlayIgnoreParticipant(String classifier) {
    this.isWebContent = "webcontent".equals(classifier);
  }

  @Override
  public void initialize(IVirtualComponent component, FlatComponentTaskModel dataModel, List<IFlatResource> resources) {
    IJavaProjectLite liteProj = JavaCoreLite.create(component.getProject());
    if(liteProj != null) {
      javaOutputContainers = JavaLiteUtilities.getJavaOutputContainers(liteProj);
    }
  }

  @Override
  public boolean shouldAddExportableFile(IVirtualComponent rootComponent, IVirtualComponent currentComponent,
      FlatComponentTaskModel dataModel, IFlatFile file) {
    boolean javaOutput = isJavaOutput(rootComponent, currentComponent, file);
    if(isWebContent) {
      return !javaOutput;
    }
    return javaOutput;
  }

  private boolean isJavaOutput(IVirtualComponent rootComponent, IVirtualComponent currentComponent, IFlatFile file) {
    if(rootComponent == currentComponent) {
      if(javaOutputContainers != null) {
        IFile t = (IFile) file.getAdapter(IFile.class);
        for(IContainer container : javaOutputContainers) {
          if(container.getFullPath().isPrefixOf(t.getFullPath())) {
            return true;
          }
        }
      }
    }
    return false;
  }

}
