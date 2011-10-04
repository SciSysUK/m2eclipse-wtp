
package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.uri;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.maven.ide.eclipse.wtp.overlay.modulecore.IOverlayVirtualComponent;

/**
 * Abstract base class for {@link OverlayURIResolver} implementations.  This resolver handles include/exclude paramters.
 * 
 * @param <T> the component type.
 */
public abstract class AbstractOverlayURIResolver<T extends IOverlayVirtualComponent> implements OverlayURIResolver {

  private static final String INCLUDES = "includes";

  private static final String EXCLUDES = "excludes";

  /**
   * Returns the {@link OverlayURI#getType() URI type} that this resolver supports. 
   * @return the URI type
   */
  protected abstract String getType();

  /**
   * Returns the overlay type that this resolver supports.
   * @return the overlay type
   */
  protected abstract Class<T> getOverlayType();

  /**
   * Factory method to create a new {@link OverlayURI}.  
   * @param segments the segments
   * @param parameters the parameters
   * @return a new {@link OverlayURI}
   */
  protected OverlayURI newOverlayURI(List<String> segments, Map<String, String> parameters) {
    return new OverlayURI(getType(), segments, parameters);
  }

  public boolean canResolve(OverlayURI uri) {
    return getType().equals(uri.getType());
  }

  public final IOverlayVirtualComponent resolve(IProject project, IPath runtimePath, OverlayURI uri) {
    Map<String, String> parameters = new HashMap<String, String>(uri.getParameters());
    Set<String> includes = stringToPatternSet(parameters.remove(INCLUDES));
    Set<String> excludes = stringToPatternSet(parameters.remove(EXCLUDES));
    IOverlayVirtualComponent resolved = doResolve(project, runtimePath, new OverlayURI(uri.getType(),
        uri.getSegments(), parameters));
    resolved.setInclusions(includes);
    resolved.setExclusions(excludes);
    return resolved;
  }

  protected abstract T doResolve(IProject project, IPath runtimePath, OverlayURI uri);

  public boolean canResolve(IOverlayVirtualComponent component) {
    return getOverlayType().isInstance(component);
  }

  @SuppressWarnings("unchecked")
  public final OverlayURI resolve(IOverlayVirtualComponent component) {
    OverlayURI resolved = doResolve((T) component);
    Map<String,String> parameters = new HashMap<String, String>(resolved.getParameters());
    parameters.put(INCLUDES, patternSetToString(component.getInclusions()));
    parameters.put(EXCLUDES, patternSetToString(component.getExclusions()));
    return new OverlayURI(resolved.getType(), resolved.getSegments(), parameters);
  }

  protected abstract OverlayURI doResolve(T component);

  private String patternSetToString(Set<String> patterns) {
    StringBuilder string = new StringBuilder();
    if(patterns != null) {
      for(String pattern : patterns) {
        if(string.length() > 0) {
          string.append(";");
        }
        string.append(pattern);
      }
    }
    return string.toString();
  }

  private Set<String> stringToPatternSet(String patterns) {
    if(patterns == null || patterns.trim().length() == 0) {
      return Collections.emptySet();
    }
    Set<String> patternSet = new LinkedHashSet<String>();
    for(String pattern : patterns.split(";")) {
      patternSet.add(pattern);
    }
    return patternSet;
  }

}
