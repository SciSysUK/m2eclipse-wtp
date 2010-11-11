/*******************************************************************************
 * Copyright (c) 2010 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.maven.ide.eclipse.wtp.overlay.modulecore;

import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.wst.common.componentcore.ComponentCore;
import org.eclipse.wst.common.componentcore.internal.flat.FlatVirtualComponent;
import org.eclipse.wst.common.componentcore.internal.flat.FlatVirtualComponent.FlatComponentTaskModel;
import org.eclipse.wst.common.componentcore.internal.flat.IFlattenParticipant;
import org.eclipse.wst.common.componentcore.internal.resources.VirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualComponent;
import org.eclipse.wst.common.componentcore.resources.IVirtualFolder;

/**
 * Overlay Virtual Component
 * 
 * @author Fred Bricon
 */
@SuppressWarnings("restriction")
public class OverlayVirtualComponent extends VirtualComponent implements
		IOverlayVirtualComponent {

	protected IProject project;

	public OverlayVirtualComponent(IProject project) {
		super(project, ROOT);
		this.project = project;
	}

	public IVirtualFolder getRootFolder() {
		IVirtualFolder root = null;
		if (project != null) {
			IVirtualComponent component = ComponentCore.createComponent(project);
			if (component != null) {
				//FlatVirtualComponent will build the project structure from the definition in .component
				FlatVirtualComponent flatVirtualComponent = new FlatVirtualComponent(component, getOptions());
				//we need to convert the component into a VirtualResource
				root = new CompositeVirtualFolder(flatVirtualComponent, ROOT);
			}
		}
		return root;
	}

	private FlatComponentTaskModel getOptions() {
		FlatComponentTaskModel options = new FlatComponentTaskModel();
		//Participants produce IFlatResources[]
		IFlattenParticipant[] participants = new IFlattenParticipant[] { 
	    	       //new SingleRootExportParticipant(new JavaEESingleRootCallback()), 
	    	       //new JEEHeirarchyExportParticipant(), 
	    	       new AddClasspathLibReferencesParticipant(), 
	    	       new AddClasspathFoldersParticipant(), 
	    	       new AddMappedOutputFoldersParticipant() 
	    	       //,new IgnoreJavaInSourceFolderParticipant() 
	    	       };
		options.put(FlatVirtualComponent.PARTICIPANT_LIST, Arrays.asList(getParticipants()));
		return options;
	}

}
