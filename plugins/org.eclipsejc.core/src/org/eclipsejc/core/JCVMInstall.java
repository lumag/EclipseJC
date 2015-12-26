/*******************************************************************************
 * Copyright (c) 2015 Dmitry Eremin-Solenikov
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipsejc.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.AbstractVMInstall;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.IVMInstallType;
import org.eclipse.jdt.launching.IVMRunner;

public class JCVMInstall extends AbstractVMInstall implements IVMInstall {

	public JCVMInstall(IVMInstallType type, String id) {
		super(type, id);
	}

	@Override
	public IVMRunner getVMRunner(String mode) {
		if (ILaunchManager.RUN_MODE.equals(mode)) {
			return new JCVMRunner(this);
		} else if (ILaunchManager.DEBUG_MODE.equals(mode)) {
			return new JCVMDebugger(this);
		}
		return null;
	}

	// system properties are cached in user preferences prefixed with this key, followed
	// by VM type, VM id, and system property name
	private static final String PREF_VM_INSTALL_SYSTEM_PROPERTY = "PREF_VM_INSTALL_SYSTEM_PROPERTY"; //$NON-NLS-1$

	/**
	 * Generates a key used to cache system property for this VM in this plug-ins
	 * preference store.
	 *
	 * @param property system property name
	 * @return preference store key
	 */
	private String getSystemPropertyKey(String property) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(PREF_VM_INSTALL_SYSTEM_PROPERTY);
		buffer.append("."); //$NON-NLS-1$
		buffer.append(getVMInstallType().getId());
		buffer.append("."); //$NON-NLS-1$
		buffer.append(getId());
		buffer.append("."); //$NON-NLS-1$
		buffer.append(property);
		return buffer.toString();
	}

	@Override
	public Map<String, String> evaluateSystemProperties(String[] properties, IProgressMonitor monitor) throws CoreException {
		//locate the launching support jar - it contains the main program to run
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}
		Map<String, String> map = new HashMap<String, String>();

		// first check cache (preference store) to avoid launching VM
		boolean cached = true;
		IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(JCPlugin.ID_PLUGIN);
		if(prefs != null) {
			for (int i = 0; i < properties.length; i++) {
				String property = properties[i];
				String key = getSystemPropertyKey(property);
				String value = prefs.get(key, null);
				if (value != null) {
					map.put(property, value);
				} else {
					map.clear();
					cached = false;
					break;
				}
			}
		}
		if (!cached) {
			// FIXME: fill

			// cache for future reference
			Iterator<String> keys = map.keySet().iterator();
			while (keys.hasNext()) {
				String property = keys.next();
				String value = map.get(property);
				String key = getSystemPropertyKey(property);
				prefs.put(key, value);
			}
		}
		monitor.done();
		return map;
	}

}
