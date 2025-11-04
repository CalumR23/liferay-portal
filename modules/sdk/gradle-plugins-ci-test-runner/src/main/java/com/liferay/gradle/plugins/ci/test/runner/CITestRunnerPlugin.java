/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.gradle.plugins.ci.test.runner;

import com.liferay.ant.mirrors.get.MirrorsGetTask;
import com.liferay.gradle.util.GradleUtil;
import com.liferay.gradle.plugins.extensions.TomcatAppServer;

import org.apache.tools.ant.BuildException;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.plugins.BasePlugin;

/**
 * @author Calum Ragan
 */
public class CITestRunnerPlugin implements Plugin<Project> {

	public static final String CI_TEST_RUNNER_CONFIGURATION_NAME =
		"ciTestRunner";

	public static final String DOWNLOAD_TOMCAT_ZIP_TASK_NAME =
		"downloadTomcatZip";

	@Override
	public void apply(Project project) {
		GradleUtil.applyPlugin(project, BasePlugin.class);

		CITestRunnerExtension ciTestRunnerExtension = GradleUtil.addExtension(
			project, "ciTestRunner", CITestRunnerExtension.class);

			ExtensionContainer extensionContainer = project.getExtensions();

		LiferayExtension liferayExtension = extensionContainer.getByType(
			LiferayExtension.class);

		TomcatAppServer tomcatAppServer =
			(TomcatAppServer)liferayExtension.getAppServer("tomcat");

		_addConfigurationCITestRunner(project);

		_addTaskDownloadTomcatZip(project, ciTestRunnerExtension, tomcatAppServer);
	}

	private Configuration _addConfigurationCITestRunner(Project project) {
		Configuration configuration = GradleUtil.addConfiguration(
			project, CI_TEST_RUNNER_CONFIGURATION_NAME);

		configuration.setDescription(
			"Configures CI Test Runner for this project.");
		configuration.setVisible(false);

		return configuration;
	}

	private Task _addTaskDownloadTomcatZip(
		Project project, CITestRunnerExtension ciTestRunnerExtension, TomcatAppServer tomcatAppServer) {

		Task task = GradleUtil.addTask(
			project, DOWNLOAD_TOMCAT_ZIP_TASK_NAME, Task.class);

		task.doLast(
			new Action<Task>() {

				@Override
				public void execute(Task task) {
					org.apache.tools.ant.Project antProject = new org.apache.tools.ant.Project();
        			antProject.init();

					MirrorsGetTask mirrorsGetTask = new MirrorsGetTask();

				
					mirrorsGetTask.setProject(antProject);

					mirrorsGetTask.setVerbose(true);

					System.out.println("zipurl: " + tomcatAppServer.getZipUrl());

					mirrorsGetTask.setSrc(tomcatAppServer.getZipUrl());

					mirrorsGetTask.setDest(
						ciTestRunnerExtension.getBundleDir());

					try {
						mirrorsGetTask.execute();
					}
					catch (BuildException buildException) {
						buildException.printStackTrace();
					}
				}

			});

		return task;
	}

}