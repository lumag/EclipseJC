/*******************************************************************************
 * Copyright (c) 2015 Dmitry Eremin-Solenikov
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipsejc.core;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.launching.AbstractVMInstallType;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.LibraryLocation;
import org.eclipse.osgi.util.NLS;

public class JCVMInstallType extends AbstractVMInstallType {

	@Override
	public String getName() {
		return "JavaCard VM";
	}

	@Override
	public IStatus validateInstallLocation(File installLocation) {
		if (!installLocation.exists()) {
			return new Status(IStatus.ERROR, JCPlugin.ID_PLUGIN, NLS.bind("Install location doesn't exist: {0}", new String[]{installLocation.getPath()}));
		}
		File apiLocation = getAPILocation(installLocation);
		if (!apiLocation.exists() || !apiLocation.isFile()) {
			return new Status(IStatus.ERROR, JCPlugin.ID_PLUGIN, NLS.bind("Bad api library: {0}", new String[]{apiLocation.getPath()}));
		}
		return new Status(IStatus.OK, JCPlugin.ID_PLUGIN, "Install location correct");
	}

	@Override
	public File detectInstallLocation() {
		return null;
	}

	@Override
	public URL getDefaultJavadocLocation(File installLocation) {
		try {
			return new URL("https://docs.oracle.com/javacard/3.0.5/api/");
		} catch (MalformedURLException e) {
		}
		return null;
	}

	@Override
	public LibraryLocation[] getDefaultLibraryLocations(File installLocation) {
		Path libraryPath = new Path(getAPILocation(installLocation).getAbsolutePath());
		URL javadocURL = getDefaultJavadocLocation(installLocation);
		return new LibraryLocation[]{new LibraryLocation(libraryPath, Path.EMPTY, Path.ROOT, javadocURL)};
	}

	private File getAPILocation(File installLocation) {
		return new File(new File(installLocation, "lib"), "api.jar");
	}

	@Override
	protected IVMInstall doCreateVMInstall(String id) {
		return new JCVMInstall(this, id);
	}

}
