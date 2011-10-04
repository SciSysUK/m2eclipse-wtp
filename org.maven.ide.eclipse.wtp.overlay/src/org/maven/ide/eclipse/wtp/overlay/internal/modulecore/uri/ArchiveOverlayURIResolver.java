package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.uri;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.common.componentcore.internal.resources.VirtualArchiveComponent;
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.ArchiveOverlayVirtualComponent;

/**
 * {@link OverlayURIResolver} for {@link ArchiveOverlayVirtualComponent}s.
 */
@SuppressWarnings("restriction")
public class ArchiveOverlayURIResolver extends AbstractOverlayURIResolver<ArchiveOverlayVirtualComponent> {

  private static final String UNPACK_FOLDER = "unpackFolder";
  
  @Override
  protected String getType() {
    return "var";
  }

  @Override
  protected Class<ArchiveOverlayVirtualComponent> getOverlayType() {
    return ArchiveOverlayVirtualComponent.class;
  }
  
  @Override
  protected OverlayURI doResolve(ArchiveOverlayVirtualComponent component) {
    List<String> segments = Arrays.asList(component.getArchivePath().segments());
    Map<String,String> parameters = new HashMap<String, String>();
    parameters.put(UNPACK_FOLDER, component.getUnpackDirPath().toPortableString());
    return newOverlayURI(segments, parameters);
  }
  
  @Override
  protected ArchiveOverlayVirtualComponent doResolve(IProject project, IPath runtimePath, OverlayURI uri) {
    String unpackFolder = uri.getParameters().get(UNPACK_FOLDER);
    StringBuilder archiveLocation = new StringBuilder(VirtualArchiveComponent.VARARCHIVETYPE); 
    for(String segment : uri.getSegments()) {
      archiveLocation.append("/");
      archiveLocation.append(segment);
    }
    IPath unpackDirPath = project.getFolder(unpackFolder).getProjectRelativePath();
    return new ArchiveOverlayVirtualComponent(project, archiveLocation.toString(), runtimePath, unpackDirPath);
  }
}
