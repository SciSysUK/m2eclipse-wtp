
package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.uri;

import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * Represents the part of a URI that references an overlay. Overlay URIs consist of one or more <tt>segments</tt> and an
 * optional set of <tt>parameters</tt>.  
 * 
 * @see OverlayURIResolver
 */
public class OverlayURI {

  private String type;

  private List<String> segments;

  private Map<String, String> parameters;

  /**
   * Create a new {@link OverlayURI} instance.
   * @param type the type
   * @param segments the segments (or <tt>null</tt>)
   * @param parameters the paramters (or <tt>null</tt>)
   */
  public OverlayURI(String type, List<String> segments, Map<String, String> parameters) {
    if(type == null) {
      throw new IllegalArgumentException("Type must not be null");
    }
    this.type = type;
    this.segments = segments == null ? Collections.<String> emptyList() : segments;
    this.parameters = parameters == null ? Collections.<String, String> emptyMap() : parameters;
  }

  /**
   * @return The type identifier of the URI
   */
  public String getType() {
    return type;
  }

  /**
   * @return segments of the URI
   */
  public List<String> getSegments() {
    return segments;
  }

  /**
   * @return Paramters
   */
  public Map<String, String> getParameters() {
    return parameters;
  }
}
