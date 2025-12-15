/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.gradle.plugins.ci.test.runner;

import com.liferay.gradle.plugins.LiferayBasePlugin;
import com.liferay.gradle.plugins.ci.test.runner.task.DownloadTomcatBundleTask;
import com.liferay.gradle.util.GradleUtil;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.BasePlugin;

/**
 * @author Calum Ragan
 */
public class CITestRunnerPlugin implements Plugin<Project> {

	@Override
	public void apply(Project project) {
		GradleUtil.applyPlugin(project, BasePlugin.class);

		GradleUtil.applyPlugin(project, LiferayBasePlugin.class);

		DownloadTomcatBundleTask downloadTomcatBundleTask =
			addTaskDownloadTomcatBundle(project);
	}

	protected DownloadTomcatBundleTask addTaskDownloadTomcatBundle(
		Project project) {

		return GradleUtil.addTask(
			project, DOWNLOAD_TOMCAT_BUNDLE_TASK_NAME,
			DownloadTomcatBundleTask.class);
	}

}