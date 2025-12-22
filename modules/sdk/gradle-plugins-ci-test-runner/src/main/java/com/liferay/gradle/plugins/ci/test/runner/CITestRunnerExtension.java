/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.gradle.plugins.ci.test.runner;

import com.liferay.gradle.util.GradleUtil;

import java.io.File;

import org.gradle.api.Project;

/**
 * @author Calum Ragan
 */
public class PoshiRunnerExtension {

	public PoshiRunnerExtension(Project project) {
		_project = project;
	}

	@InputFile
	public File getParentDir() {
		return GradleUtil.toFile(_parentDir);
	}

	public void setParentDir(Object parentDir) {
		_parentDir = parentDir;
	}

	public void setZipName(Object zipName) {
		_zipName = zipName;
	}

	public String getZipName() {
		return _zipName;
	}

	@InputFile
	public File getDir() {
		return GradleUtil.toFile(_dir);
	}

	public void setDir(Object dir) {
		_dir = dir;
	}

	private Object _parentDir;
	private Object _dir;
	private String _zipName;
	private Project _project;

}