package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.v2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualResource;


/**
 * A {@link JarVirtualFolder} for a {@link JarFile}.
 * 
 */
public class JarFileVirtualFolder extends JarVirtualFolder {
	
	private IPath unpackPath;
	
	private File archive;
	
	public JarFileVirtualFolder(IProject project, File archive, IPath unpackPath) throws IOException {
		super(project, new JarFile(archive), null, IVirtualComponent.ROOT);
		this.unpackPath = unpackPath;
		this.archive = archive;
		
		Enumeration<JarEntry> entries = getJarFile().entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			addJarEntry(entry);
		}
	}
	
	private void addJarEntry(JarEntry entry) {
		String name = removeTrailingSlash(entry.getName());
		JarVirtualFolder current = this;
		while (name.indexOf("/") >= 0) {
			String parentName = name.substring(0, name.indexOf("/"));
			current = current.getOrAddFolder(parentName);
			name = name.substring(name.indexOf("/") + 1);
		}
		current.addMember(adaptEntry(entry, name, current));
	}

	private String removeTrailingSlash(String name) {
		if (name.endsWith("/")) {
			return name.substring(0, name.length() - 1);
		}
		return name;
	}
	
	private IVirtualResource adaptEntry(JarEntry entry, String name, JarVirtualFolder folder) {
		if (entry.isDirectory()) {
			return new JarVirtualFolder(getProject(), getJarFile(), name, folder.getRuntimePath().append(name));
		}
		return new JarVirtualFile(this, entry, name, folder.getRuntimePath().append(name));
	}
	
	public IFile unpackJarEntry(JarEntry jarEntry) {
		IFolder archiveUnpackFolder = getProject().getFolder(unpackPath).getFolder(archive.getName());
		IFile destination = archiveUnpackFolder.getFile(jarEntry.getName());
		
		File unpackedFile = destination.getFullPath().toFile();
		long lastModified = archive.lastModified();
		if (!unpackedFile.exists() || unpackedFile.lastModified() < lastModified) {
			try {
				unpackFile(jarEntry, unpackedFile);
			} catch (IOException e) {
				throw new RuntimeException(e.getLocalizedMessage(),e);
			}
		}
		return destination;
	}
	
	public void unpackFile(JarEntry jarEntry, File unpackedFile) throws IOException {
		InputStream inputStream = getJarFile().getInputStream(jarEntry);
		unpackedFile.getParentFile().mkdirs();
		try {
			FileOutputStream outputStream = new FileOutputStream(unpackedFile);
			try {
				copy(inputStream, outputStream);
			} finally {
				outputStream.close();
			}
		} finally {
			inputStream.close();
		}
		//FIXME close jar file?
	}

	private void copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[1024];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
	}

}
