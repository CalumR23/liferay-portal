/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.gradle.plugins.ci.test.runner;

import com.liferay.ant.mirrors.get.MirrorsGetTask;
import com.liferay.gradle.plugins.LiferayBasePlugin;
import com.liferay.gradle.plugins.extensions.LiferayExtension;
import com.liferay.gradle.plugins.extensions.TomcatAppServer;
import com.liferay.gradle.util.GradleUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.BuildException;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.file.CopySpec;
import org.gradle.api.file.DuplicatesStrategy;
import org.gradle.api.file.RelativePath;
import org.gradle.api.plugins.BasePlugin;
import org.gradle.api.plugins.ExtensionContainer;

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

		GradleUtil.applyPlugin(project, LiferayBasePlugin.class);

		ExtensionContainer extensionContainer = project.getExtensions();

		LiferayExtension liferayExtension = extensionContainer.getByType(
			LiferayExtension.class);

		TomcatAppServer tomcatAppServer =
			(TomcatAppServer)liferayExtension.getAppServer("tomcat");

		_addConfigurationCITestRunner(project);

		_addTaskDownloadTomcatZip(liferayExtension, project, tomcatAppServer);
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
		LiferayExtension liferayExtension, Project project,
		TomcatAppServer tomcatAppServer) {

		Task task = GradleUtil.addTask(
			project, DOWNLOAD_TOMCAT_ZIP_TASK_NAME, Task.class);

		task.doLast(
			new Action<Task>() {

				@Override
				public void execute(Task task) {
					MirrorsGetTask mirrorsGetTask = new MirrorsGetTask();

					org.apache.tools.ant.Project antProject =
						new org.apache.tools.ant.Project();

					antProject.init();

					mirrorsGetTask.setProject(antProject);

					mirrorsGetTask.setVerbose(true);

					mirrorsGetTask.setSrc(tomcatAppServer.getZipUrl());

					File tomcatAppServerZipFile = new File(
						liferayExtension.getAppServerParentDir(),
						tomcatAppServer.getZipName());

					mirrorsGetTask.setDest(tomcatAppServerZipFile);

					try {
						mirrorsGetTask.execute();
					}
					catch (BuildException buildException) {
						throw new RuntimeException(buildException);
					}

					String tomcatAppServerVersion =
						tomcatAppServer.getVersion();

					File tomcatAppServerDir = tomcatAppServer.getDir();

					project.copy(
						new Action<CopySpec>() {

							@Override
							public void execute(CopySpec copySpec) {
								String fileName =
									tomcatAppServerZipFile.getName();

								if (fileName.endsWith(".zip")) {
									copySpec.from(
										project.zipTree(
											tomcatAppServerZipFile));
								}
								else {
									copySpec.from(
										project.tarTree(
											tomcatAppServerZipFile));
								}

								copySpec.include(
									"apache-tomcat-" + tomcatAppServerVersion +
										"/bin/**");
								copySpec.include(
									"apache-tomcat-" + tomcatAppServerVersion +
										"/conf/**");
								copySpec.include(
									"apache-tomcat-" + tomcatAppServerVersion +
										"/lib/**");
								copySpec.include(
									"apache-tomcat-" + tomcatAppServerVersion +
										"/logs/**");
								copySpec.include(
									"apache-tomcat-" + tomcatAppServerVersion +
										"/work/**");

								copySpec.eachFile(
									fileCopyDetails -> {
										RelativePath relativePath =
											fileCopyDetails.getRelativePath();

										String[] segments =
											relativePath.getSegments();

										if ((segments.length > 1) &&
											segments[0].equals(
												"apache-tomcat-" +
													tomcatAppServerVersion)) {

											String[] newSegments =
												new String[segments.length - 1];

											System.arraycopy(
												segments, 1, newSegments, 0,
												newSegments.length);

											fileCopyDetails.setRelativePath(
												new RelativePath(
													!relativePath.isFile(),
													newSegments));
										}
									});

								copySpec.setDuplicatesStrategy(
									DuplicatesStrategy.EXCLUDE);

								copySpec.into(tomcatAppServerDir);
							}

						});

					File apacheDir = new File(
						tomcatAppServerDir,
						"apache-tomcat-" + tomcatAppServerVersion);

					try {
						FileUtils.deleteDirectory(apacheDir);
					}
					catch (IOException ioException) {
						throw new RuntimeException(ioException);
					}

					File webAppsDir = new File(tomcatAppServerDir, "webapps");

					webAppsDir.mkdirs();

					File catalinaProperties = new File(
						tomcatAppServerDir.getAbsolutePath() +
							"/conf/catalina.properties");

					String catalinaPropertiesContent = null;

					try {
						catalinaPropertiesContent = FileUtils.readFileToString(
							catalinaProperties);
					}
					catch (IOException ioException) {
						throw new RuntimeException(ioException);
					}

					StringBuilder token = new StringBuilder();

					token.append("common.loader=\"${catalina.base}/lib\",");
					token.append("\"${catalina.base}/lib/*.jar\",");
					token.append("\"${catalina.home}/lib\",");
					token.append("\"${catalina.home}/lib/*.jar\"");

					StringBuilder replaceToken = new StringBuilder();

					replaceToken.append(
						"common.loader=\"${catalina.home}/webapps/ROOT/");
					replaceToken.append("WEB-INF/lib/support-tomcat.jar\",");
					replaceToken.append("\"${catalina.base}/lib\",\"");
					replaceToken.append("${catalina.base}/lib/*.jar\",");
					replaceToken.append("\"${catalina.home}/lib\",");
					replaceToken.append("\"${catalina.home}/lib/*.jar\"");

					catalinaPropertiesContent.replace(
						token.toString(), replaceToken.toString());

					try {
						Files.write(
							catalinaProperties.toAbsolutePath(),
							catalinaPropertiesContent.getBytes());
					}
					catch (IOException ioException) {
						throw new RuntimeException(ioException);
					}
				}

			});

		return task;
	}

}