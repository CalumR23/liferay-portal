/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.gradle.plugins.ci.test.runner;

import com.liferay.ant.mirrors.get.MirrorsGetTask;

import com.liferay.gradle.util.FileUtil;

import org.gradle.api.Action;
import org.gradle.api.Plugin; 
import org.gradle.api.Project;


/**
 * @author Calum Ragan
 */
public class CITestRunnerPlugin implements Plugin<Project> {
    	public static final String DOWNLOAD_TOMCAT_ZIP_TASK_NAME =
        "downloadTomcatZip"

        @Override
	public void apply(Project project) {
		GradleUtil.applyPlugin(project, BasePlugin.class);

		final CITestRunnerExtension CITestRunnerExtension =
			GradleUtil.addExtension(
				project, "ciTestRunner", CITestRunnerExtension.class);
    }

    private Task _addTaskDownloadTomcatZip(
		final Project project, CITestRunnerExtension ciTestRunnerExtension) {

		Task task = GradleUtil.addTask(
			project, DOWNLOAD_TOMCAT_ZIP_TASK_NAME, Task.class);

		task.doLast(
			new Action<Task>() {

				@Override
				public void execute(Task task) {
                    String url = "https://archive.apache.org/dist/tomcat/tomcat-10/v10.1.42/bin/apache-tomcat-10.1.42.zip";
                    FileUtil.get(project, url, CITestRunnerExtension.getBundleDir());
                };
            });

            return task;
        }
            
}