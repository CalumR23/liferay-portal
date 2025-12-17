/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.gradle.plugins.ci.test.runner.task;

import com.liferay.gradle.plugins.extensions.LiferayExtension;
import com.liferay.gradle.plugins.extensions.TomcatAppServer;

import org.apache.tools.ant.Project;

/**
 * @author Calum Ragan
 */
@CacheableTask
public class UnzipTomcatBundleTask extends JavaExec {

	public setVersion(Object version) {
		_version = version;
	}

	public UnzipTomcatBundleTask() {
		Property<String> mainClass = getMainClass();

		mainClass.set("com.liferay.gradle.plugins.ci.test.runner.CITestRunner");

		LiferayExtension liferayExtension = extensionContainer.getByType(
			LiferayExtension.class);

		TomcatAppServer tomcatAppServer =
			(TomcatAppServer)liferayExtension.getAppServer("tomcat");

		setParentDir(liferayExtension.getAppServerParentDir());

		setDir(tomcatAppServer.getDir());
		setVersion(tomcatAppServer.getVersion());
		setZipName(tomcatAppServer.getZipName());
	}

	@InputFile
	public File getDir() {
		return GradleUtil.toFile(_dir);
	}

	@InputFile
	public File getParentDir() {
		return GradleUtil.toFile(_parentDir);
	}

	@Input
	public String getVersion() {
		return GradleUtil.toString(_version);
	}

	@Input
	public String getZipName() {
		return GradleUtil.toString(_zipName);
	}

	public void setDir(Object dir) {
		_dir = dir;
	}

	public void setParentDir(Object parentDir) {
		_parentDir = parentDir;
	}

	public void setZipName(Object zipName) {
		_zipName = zipName;
	}

	public void unzipTomcatBundle() throws IOException {
		File tomcatAppServerZipFile = new File(getParentDir(), getZipName());
		final Project project = getProject();

		project.copy(
			new Action<CopySpec>() {

				@Override
				public void execute(CopySpec copySpec) {
					String fileName = tomcatAppServerZipFile.getName();

					if (fileName.endsWith(".zip")) {
						copySpec.from(project.zipTree(tomcatAppServerZipFile));
					}
					else {
						copySpec.from(project.tarTree(tomcatAppServerZipFile));
					}

					String tomcatAppServerVersion = getVersion();

					copySpec.include(
						"apache-tomcat-" + tomcatAppServerVersion + "/bin/**");
					copySpec.include(
						"apache-tomcat-" + tomcatAppServerVersion + "/conf/**");
					copySpec.include(
						"apache-tomcat-" + tomcatAppServerVersion + "/lib/**");
					copySpec.include(
						"apache-tomcat-" + tomcatAppServerVersion + "/logs/**");
					copySpec.include(
						"apache-tomcat-" + tomcatAppServerVersion + "/work/**");

					copySpec.eachFile(
						fileCopyDetails -> {
							RelativePath relativePath =
								fileCopyDetails.getRelativePath();

							String[] segments = relativePath.getSegments();

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
										!relativePath.isFile(), newSegments));
							}
						});

					copySpec.setDuplicatesStrategy(DuplicatesStrategy.EXCLUDE);

					copySpec.into(getDir());
				}

			});
	}

	private Object _dir;
	private Object _parentDir;
	private Object _version;
	private Object _zipName;

}