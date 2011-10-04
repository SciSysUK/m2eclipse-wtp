package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v2.converter;

import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.maven.ide.eclipse.wtp.overlay.modulecore.IOverlayVirtualComponent;

/**
 * Converter class responsible for converting {@link IOverlayVirtualComponent}s to and from {@link OverlayComponentURIData}
 * 
 * @author Alex Clarke
 */
public abstract class OverlayVirtualComponentURIConverter {

	/**
	 * Get the {@link IOverlayVirtualComponent class} that is supported by this {@link OverlayVirtualComponentURIConverter}.
	 * @return
	 */
	protected abstract Class<? extends IOverlayVirtualComponent> getSupportedClass();
	
	/**
	 * Get the {@link String} which is the reference type that is supported by this {@link OverlayVirtualComponentURIConverter}.
	 * @return
	 */
	protected abstract String getReferenceType();

	public abstract OverlayComponentURIData createOverlayComponentURIData(IOverlayVirtualComponent component);

	public abstract IOverlayVirtualComponent createOverlayVirtualComponent(IVirtualComponent context, OverlayComponentURIData uri, IPath runtimePath);
	
	public boolean isSupported(IOverlayVirtualComponent component) {
		return component.getClass() == getSupportedClass();
	}

	public boolean isSupported(String referenceType) {
		return getReferenceType().equals(referenceType);
	}
	
	
}