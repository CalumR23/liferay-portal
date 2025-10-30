/**
 * SPDX-FileCopyrightText: (c) 2000 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.gradle.plugins.ci.test.runner;

import com.liferay.ant.mirrors.get.MirrorsGetTask;

import com.liferay.gradle.util.GradleUtil;
import com.liferay.gradle.util.FileUtil;

import java.io.File;
import java.io.IOException;

import org.gradle.api.Action;
import org.gradle.api.Plugin; 
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.BasePlugin;


/**
 * @author Calum Ragan
 */
public class CITestRunnerPlugin implements Plugin<Project> {
    	public static final String DOWNLOAD_TOMCAT_ZIP_TASK_NAME =
        "downloadTomcatZip";

    @Override
	public void apply(Project project) {
		GradleUtil.applyPlugin(project, BasePlugin.class);

		final CITestRunnerExtension ciTestRunnerExtension =
			GradleUtil.addExtension(
				project, "ciTestRunner", CITestRunnerExtension.class);

			_addTaskDownloadTomcatZip(
				project, ciTestRunnerExtension);
    }

    private Task _addTaskDownloadTomcatZip(
		final Project project, CITestRunnerExtension ciTestRunnerExtension) {

		Task task = GradleUtil.addTask(
			project, DOWNLOAD_TOMCAT_ZIP_TASK_NAME, Task.class);

		task.doLast(
			new Action<Task>() {

				@Override
				public void execute(Task task) {
                    File bundleDir = ciTestRunnerExtension.getBundleDir();
                    String url = "https://archive.apache.org/dist/tomcat/tomcat-10/v10.1.42/bin/apache-tomcat-10.1.42.zip";
                    try{
                    FileUtil.get(project, url, bundleDir);
                    }catch(IOException e){
                        System.out.println(e.getMessage());
                    }
                };
            });

            return task;
        }
            
}