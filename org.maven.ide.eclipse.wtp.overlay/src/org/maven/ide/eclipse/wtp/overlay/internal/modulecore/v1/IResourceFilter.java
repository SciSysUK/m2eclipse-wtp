package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v1;


public interface IResourceFilter {

	boolean accepts(String path, boolean isFile);
}
