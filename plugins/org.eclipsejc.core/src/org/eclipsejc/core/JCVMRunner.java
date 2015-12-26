/*******************************************************************************
 * Copyright (c) 2015 Dmitry Eremin-Solenikov
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipsejc.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.jdt.launching.AbstractVMRunner;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMRunner;
import org.eclipse.jdt.launching.VMRunnerConfiguration;

public class JCVMRunner extends AbstractVMRunner implements IVMRunner {

	@SuppressWarnings("unused")
	private IVMInstall vmInstall;

	public JCVMRunner(IVMInstall vmInstall) {
		this.vmInstall = vmInstall;
	}

	@Override
	protected String getPluginIdentifier() {
		return JCPlugin.ID_PLUGIN;
	}

	@Override
	public void run(VMRunnerConfiguration configuration, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		// TODO Auto-generated method stub

	}

}
