/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.gradle.plugins.ci.test.runner;

import com.liferay.gradle.util.GradleUtil;

import org.gradle.api.Project;

/**
 * @author Calum Ragan
 */
public class CITestRunnerExtension {

    public CITestExtension(Project project) {
		_project = project;
	}

	public File getBundleDir() {
		return GradleUtil.toFile(_project, _bundleDir);
	}

    private Object _bundleDir = "bundle";
    private final Project _project;

}