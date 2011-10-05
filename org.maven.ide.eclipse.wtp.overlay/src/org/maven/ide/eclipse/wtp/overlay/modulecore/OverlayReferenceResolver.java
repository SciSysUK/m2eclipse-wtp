
package org.maven.ide.eclipse.wtp.overlay.modulecore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.URI;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.ComponentcorePackage;
import org.eclipse.wst.common.componentcore.internal.DependencyType;
import org.eclipse.wst.common.componentcore.internal.ReferencedComponent;
import org.eclipse.wst.common.componentcore.resolvers.IReferenceResolver;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualReference;
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.uri.ArchiveOverlayURIResolver;
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.uri.OverlayURI;
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.uri.OverlayURIResolver;
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.uri.ProjectOverlayURIResolver;
import org.maven.ide.eclipse.wtp.overlay.internal.modulecore.uri.SelfOverlayURIResolver;


/**
 * {@link IReferenceResolver} used to resolve references to {@link IOverlayVirtualComponent} projects.
 * 
 * @author Fred Bricon
 */
@SuppressWarnings("restriction")
public class OverlayReferenceResolver implements IReferenceResolver {

  //FIXME replace calls to ModuleURIUtil

  private static final String SCHEME = "module";

  private static final String OVERLAY = "overlay";

  private static final Set<OverlayURIResolver> RESOLVERS;
  static {
    RESOLVERS = new HashSet<OverlayURIResolver>();
    RESOLVERS.add(new ArchiveOverlayURIResolver());
    RESOLVERS.add(new ProjectOverlayURIResolver());
    RESOLVERS.add(new SelfOverlayURIResolver());
  }

  public boolean canResolve(IVirtualReference reference) {
    ComponentURIResolver resolver = getComponentURIResolver(reference);
    return resolver != null;
  }

  public ReferencedComponent resolve(IVirtualReference reference) {
    ComponentURIResolver resolver = getComponentURIResolver(reference);
    if(resolver == null) {
      throw new IllegalStateException("Unable to find resolver to handle overlay reference");
    }
    ReferencedComponent referencedComponent = ComponentcorePackage.eINSTANCE.getComponentcoreFactory()
        .createReferencedComponent();
    referencedComponent.setArchiveName(reference.getArchiveName());
    referencedComponent.setRuntimePath(reference.getRuntimePath());
    referencedComponent.setDependencyType(DependencyType.CONSUMES_LITERAL);
    referencedComponent.setHandle(getUri(resolver.resolve()));
    return referencedComponent;
  }

  /**
   * Returns a {@link ComponentURIResolver} for the given reference or <tt>null</tt> if the reference cannot be
   * resolved.
   * 
   * @param reference the reference to resolve
   * @return a {@link ComponentURIResolver} or <tt>null</tt>
   */
  private ComponentURIResolver getComponentURIResolver(IVirtualReference reference) {
    if(reference != null && reference.getReferencedComponent() instanceof IOverlayVirtualComponent) {
      IOverlayVirtualComponent component = (IOverlayVirtualComponent) reference.getReferencedComponent();
      for(OverlayURIResolver resolver : RESOLVERS) {
        if(resolver.canResolve(component)) {
          return new ComponentURIResolver(resolver, component);
        }
      }
    }
    return null;
  }

  public boolean canResolve(IVirtualComponent context, ReferencedComponent referencedComponent) {
    URIComponentResolver resolver = getURIComponentResolver(context, referencedComponent);
    return resolver != null;
  }

  public IVirtualReference resolve(IVirtualComponent context, ReferencedComponent referencedComponent) {
    URIComponentResolver resolver = getURIComponentResolver(context, referencedComponent);
    IVirtualReference ref = ComponentCore.createReference(context, resolver.resolve());
    ref.setArchiveName(referencedComponent.getArchiveName());
    ref.setRuntimePath(referencedComponent.getRuntimePath());
    ref.setDependencyType(referencedComponent.getDependencyType().getValue());
    return ref;
  }

  /**
   * Returns a {@link URIComponentResolver} for the given referenced component or <tt>null</tt> if the referenced
   * component cannot be resolved.
   * 
   * @param context the context for the resolver
   * @param referencedComponent the referenced component
   * @return a {@link URIComponentResolver} or <tt>null</tt>
   */
  private URIComponentResolver getURIComponentResolver(IVirtualComponent context,
      ReferencedComponent referencedComponent) {
    OverlayURI uri = getOverlayUri(referencedComponent.getHandle());
    if(uri != null) {
      for(OverlayURIResolver resolver : RESOLVERS) {
        if(resolver.canResolve(uri)) {
          return new URIComponentResolver(resolver, context.getProject(), referencedComponent.getRuntimePath(), uri);
        }
      }
    }
    return null;
  }

  /**
   * Returns a {@link OverlayURI} extracted from the specified Eclipse URI. If the URI is not for a module overlay
   * <tt>null</tt> is returned.
   * 
   * @param uri the uri to convert
   * @return an {@link OverlayURI} or <tt>null</tt>
   */
  private OverlayURI getOverlayUri(URI uri) {
    if(SCHEME.equals(uri.scheme()) && uri.segmentCount() >= 2 && OVERLAY.equals(uri.segment(0))) {
      String type = uri.segment(1);
      List<String> segments = new ArrayList<String>();
      for(int i = 2; i < uri.segmentCount(); i++ ) {
        segments.add(uri.segment(i));
      }
      return new OverlayURI(type, segments, getParameters(uri));
    }
    return null;
  }

  /**
   * Returns an eclipse URI build from the specified overlay URI.
   * @param overlayURI
   * @return
   */
  private URI getUri(OverlayURI overlayURI) {
    StringBuilder uri = new StringBuilder(SCHEME);
    uri.append(":/");
    uri.append(OVERLAY);
    uri.append("/");
    uri.append(overlayURI.getType());
    for(String segment : overlayURI.getSegments()) {
      uri.append("/");
      uri.append(segment);
    }
    return URI.createURI(ModuleURIUtil.appendToUri(uri.toString(), overlayURI.getParameters()));
  }
  
  /**
   * Extract query paramters as a Map from the specified URI.
   * @param uri the URI
   * @return a paramter map
   */
  private Map<String, String> getParameters(URI uri) {
    return ModuleURIUtil.getParameters(uri.toString());
  }

  /**
   * Resolver used to convert to a {@link OverlayURI}.
   */
  private static class ComponentURIResolver {

    private OverlayURIResolver resolver;

    private IOverlayVirtualComponent component;

    public ComponentURIResolver(OverlayURIResolver resolver, IOverlayVirtualComponent component) {
      this.resolver = resolver;
      this.component = component;
    }

    public OverlayURI resolve() {
      return resolver.resolve(component);
    }
  }

  /**
   * Resolver used to convert to a {@link IOverlayVirtualComponent}.
   */
  private static class URIComponentResolver {

    private OverlayURIResolver resolver;

    private IProject overlayProject;

    private IPath runtimePath;

    private OverlayURI uri;

    public URIComponentResolver(OverlayURIResolver resolver, IProject overlayProject, IPath runtimePath, OverlayURI uri) {
      this.resolver = resolver;
      this.overlayProject = overlayProject;
      this.runtimePath = runtimePath;
      this.uri = uri;
    }

    public IOverlayVirtualComponent resolve() {
      return resolver.resolve(overlayProject, runtimePath, uri);
    }
  }

}
