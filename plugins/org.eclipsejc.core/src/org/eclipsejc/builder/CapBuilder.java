/*******************************************************************************
 * Copyright (c) 2015 Dmitry Eremin-Solenikov
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipsejc.builder;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class CapBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "org.eclipsejc.core.capBuilder";

	@SuppressWarnings("rawtypes")
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) {
		if (kind == IncrementalProjectBuilder.FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	private IPath getConverterPath(IJavaProject javaProject) throws JavaModelException {
		IClasspathEntry[] resolvedClasspath = javaProject.getResolvedClasspath(false);

		for (IClasspathEntry entry : resolvedClasspath) {
			if (entry.getEntryKind() != IClasspathEntry.CPE_LIBRARY)
				continue;

			IPath path = entry.getPath();
			if (!path.lastSegment().equals("api.jar"))
				continue;

			IPath libPath = path.removeLastSegments(1);
			return libPath.append("converter.jar");
		}
		return null;
	}

	private void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) {
		System.out.println("incremental build on " + delta);
		try {
			delta.accept(new IResourceDeltaVisitor() {
				public boolean visit(IResourceDelta delta) {
					System.out.println("changed: " + delta.getResource().getRawLocation());
					return true; // visit children too
				}
			});
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	private void fullBuild(IProgressMonitor monitor) {
		System.out.println("full build");

		IJavaProject javaProject = JavaCore.create(getProject());
		IPath converterPath;
		try {
			converterPath = getConverterPath(javaProject);
		} catch (JavaModelException e) {
			e.printStackTrace();
			return;
		}
		if (converterPath == null)
			return;


	}
}
