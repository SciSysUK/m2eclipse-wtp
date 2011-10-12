/*******************************************************************************
 * Copyright (c) 2011 JBoss by Red Hat.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;
import org.eclipse.wst.common.componentcore.resources.IVirtualResource;


/**
 * Adapts {@link File}s containing {@link JarFile jar} content into {@link IVirtualFolder}s.
 * 
 * @see JarVirtualNestedFolder
 * @see JarVirtualFile
 */
public class JarVirtualFolder extends PartialReadOnlyVirtualFolder {

  private IProject project;

  private File archive;

  private IPath unpackDirPath;

  private JarVirtualNestedFolder rootFolder;

  public JarVirtualFolder(IProject project, File archive, IPath unpackDirPath) throws IOException {
    this.project = project;
    this.archive = archive;
    this.unpackDirPath = unpackDirPath;
    this.rootFolder = new JarVirtualNestedFolder(this, IVirtualComponent.ROOT);
    buildMembersFromJarEntries();
  }

  private void buildMembersFromJarEntries() throws IOException {
    JarFile jarFile = new JarFile(archive);
    try {
      Enumeration<JarEntry> entries = jarFile.entries();
      while(entries.hasMoreElements()) {
        JarEntry entry = entries.nextElement();
        addJarEntry(entry);
      }
    } finally {
      jarFile.close();
    }
  }

  private void addJarEntry(JarEntry entry) {
    String name = removeTrailingSlash(entry.getName());
    JarVirtualNestedFolder current = rootFolder;
    while(name.indexOf("/") >= 0) {
      String parentName = name.substring(0, name.indexOf("/"));
      current = current.getOrAddFolder(parentName);
      name = name.substring(name.indexOf("/") + 1);
    }
    current.addMember(adaptEntry(entry, name, current));
  }

  private String removeTrailingSlash(String name) {
    if(name.endsWith("/")) {
      return name.substring(0, name.length() - 1);
    }
    return name;
  }

  private IVirtualResource adaptEntry(JarEntry entry, String name, JarVirtualNestedFolder folder) {
    IPath childRuntimePath = folder.getRuntimePath().append(name);
    if(entry.isDirectory()) {
      return new JarVirtualNestedFolder(this, childRuntimePath);
    }
    return new JarVirtualFile(this, entry, childRuntimePath);
  }

  /**
   * Unpack a {@link JarEntry} and return a {@link IFile} that can be used from within the workspace.
   * 
   * @param jarEntry the entry to unpack
   * @return a {@link IFile}
   */
  IFile unpackJarEntry(JarEntry jarEntry) {
    IFolder archiveUnpackFolder = getProject().getFolder(unpackDirPath).getFolder(archive.getName());
    IFile destination = archiveUnpackFolder.getFile(jarEntry.getName());

    if(!destination.exists() || isOlderThanArchive(destination)) {
      try {
        unpackFile(jarEntry, destination);
        destination.refreshLocal(IFile.DEPTH_ZERO, null);
      } catch(Exception e) {
        throw new RuntimeException(e.getLocalizedMessage(), e);
      }
    }
    return destination;
  }

  private boolean isOlderThanArchive(IFile destination) {
    return archive.lastModified() > destination.getLocalTimeStamp();
  }

  private void unpackFile(JarEntry jarEntry, IFile unpackedFile) throws IOException, CoreException {
    JarFile jarFile = new JarFile(archive);
    try {
      InputStream inputStream = jarFile.getInputStream(jarEntry);
      if(unpackedFile.exists()) {
        unpackedFile.setContents(jarFile.getInputStream(jarEntry), IResource.FORCE | IResource.DERIVED, null);
      } else {
        ensureContainerExists(unpackedFile.getParent());
        unpackedFile.create(inputStream, IResource.FORCE | IResource.DERIVED, null);
      }
    } finally {
      jarFile.close();
    }
  }

  private void ensureContainerExists(IContainer resource) throws CoreException {
    if(!resource.exists() && resource instanceof IFolder) {
      ensureContainerExists(resource.getParent());
      ((IFolder) resource).create(IResource.FORCE | IResource.DERIVED, true, null);
    }
  }

  public IVirtualResource[] members() throws CoreException {
    return rootFolder.members();
  }

  @Override
  public IPath getRuntimePath() {
    return rootFolder.getRuntimePath();
  }

  @Override
  public IProject getProject() {
    return project;
  }
}
