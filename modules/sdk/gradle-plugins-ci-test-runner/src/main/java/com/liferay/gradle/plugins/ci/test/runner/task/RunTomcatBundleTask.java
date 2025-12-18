/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.gradle.plugins.ci.test.runner.task;

import com.liferay.gradle.plugins.extensions.LiferayExtension;
import com.liferay.gradle.plugins.extensions.TomcatAppServer;
import com.liferay.gradle.util.GradleUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * @author Calum Ragan
 */
@CacheableTask
public class RunTomcatBundleTask extends JavaExec {

	public RunTomcatBundleTask() {
		Property<String> mainClass = getMainClass();

		mainClass.set("com.liferay.gradle.plugins.ci.test.runner.CITestRunner");

		LiferayExtension liferayExtension = extensionContainer.getByType(
			LiferayExtension.class);

		TomcatAppServer tomcatAppServer =
			(TomcatAppServer)liferayExtension.getAppServer("tomcat");

		setBinDir(tomcatAppServer.getBinDir());
	}

	@InputFile
	public File getBinDir() {
		return GradleUtil.toFile(_binDir);
	}

	public void runTomcatBundle() {
		try {
			ProcessBuilder pb = new ProcessBuilder(
				"bash", "-c", "./run catalina.sh");

			pb.directory(getBinDir());

			pb.redirectErrorStream(true);

			Process process = pb.start();

			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(process.getInputStream()))) {

				String line;

				while ((line = reader.readLine()) != null) {
					System.out.println(line);
				}
			}

			int exitCode = process.waitFor();

			System.out.println("\nExited with error code: " + exitCode);
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public void setBinDir(Object binDir) {
		_binDir = binDir;
	}

	private Object _binDir;

}