/*******************************************************************************
 * Copyright (c) 2011 JBoss by Red Hat.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.maven.ide.eclipse.wtp;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.war.overlay.WebContentResolver;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;

/**
 * 
 *
 * @author Alex Clarke
 * @author Phillip Webb
 */
public class M2EWebContentResolver implements WebContentResolver {

  /* (non-Javadoc)
   * @see org.apache.maven.plugin.war.overlay.WebContentResolver#getResolvedWebContent(org.apache.maven.artifact.Artifact)
   */
  public Artifact getResolvedWebContent(Artifact webClassesArtifact) throws MojoExecutionException {

      try {
        IMaven maven = MavenPlugin.getMaven();
        return maven.resolve(webClassesArtifact.getGroupId(), webClassesArtifact.getArtifactId(), webClassesArtifact.getVersion(), "war-overlay", "webcontent", maven.getArtifactRepositories(), new NullProgressMonitor());
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
  }
}
