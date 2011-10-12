/*******************************************************************************
 * Copyright (c) 2011 JBoss by Red Hat.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.maven.ide.eclipse.wtp.overlay.internal.modulecore.resource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.wst.common.componentcore.resources.IVirtualFile;


/**
 * Base class for {@link IVirtualFile} implementations that present a read-only view of the virtual files. Many of the
 * standard {@link IVirtualFile} methods are not supported by this class.
 */
public abstract class PartialReadOnlyVirtualFile extends PartialReadOnlyVirtualResource implements IVirtualFile {

  public IFile[] getUnderlyingFiles() {
    throw new UnsupportedOperationException();
  }

  public IFile getUnderlyingFile() {
    IFile[] files = getUnderlyingFiles();
    if(files == null || files.length == 0) {
      return null;
    }
    return files[0];
  }

  @Override
  public abstract IProject getProject();

  @Override
  @SuppressWarnings("rawtypes")
  public Object getAdapter(Class adapter) {
    if(java.io.File.class.equals(adapter)) {
      IFile file = getUnderlyingFile();
      return file.getLocation().toFile();
    }
    if(IFile.class.equals(adapter)) {
      return getUnderlyingFile();
    }
    return null;
  }
}
