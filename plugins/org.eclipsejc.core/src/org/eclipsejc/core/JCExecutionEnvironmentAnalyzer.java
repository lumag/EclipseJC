/*******************************************************************************
 * Copyright (c) 2015 Dmitry Eremin-Solenikov
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipsejc.core;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.environments.CompatibleEnvironment;
import org.eclipse.jdt.launching.environments.IExecutionEnvironment;
import org.eclipse.jdt.launching.environments.IExecutionEnvironmentAnalyzerDelegate;
import org.eclipse.jdt.launching.environments.IExecutionEnvironmentsManager;

public class JCExecutionEnvironmentAnalyzer implements IExecutionEnvironmentAnalyzerDelegate {

	private void addEnvironment(ArrayList<CompatibleEnvironment> result, String id, boolean strict) {
		IExecutionEnvironmentsManager manager = JavaRuntime.getExecutionEnvironmentsManager();
		IExecutionEnvironment env = manager.getEnvironment(id);
		if (env != null) {
			result.add(new CompatibleEnvironment(env, strict));
		}
	}

	@Override
	public CompatibleEnvironment[] analyze(IVMInstall vm, IProgressMonitor monitor) throws CoreException {
		if (!(vm instanceof JCVMInstall))
			return new CompatibleEnvironment[0];

		ArrayList<CompatibleEnvironment> result = new ArrayList<CompatibleEnvironment>();
		addEnvironment(result, "JavaCard", true); // false for non-strict results
		return result.toArray(new CompatibleEnvironment[result.size()]);
	}

}
