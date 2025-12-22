/**
 * SPDX-FileCopyrightText: (c) 2025 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.gradle.plugins.ci.test.runner.task;

import com.liferay.gradle.plugins.extensions.LiferayExtension;
import com.liferay.gradle.plugins.extensions.TomcatAppServer;
import com.liferay.gradle.util.GradleUtil;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.tools.ant.BuildException;

/**
 * @author Calum Ragan
 */
@CacheableTask
public class SetupTomcatBundleTask extends JavaExec {

	public SetupTomcatBundleTask() {
		Property<String> mainClass = getMainClass();

		mainClass.set("com.liferay.gradle.plugins.ci.test.runner.CITestRunner");

		LiferayExtension liferayExtension = extensionContainer.getByType(
			LiferayExtension.class);

		TomcatAppServer tomcatAppServer =
			(TomcatAppServer)liferayExtension.getAppServer("tomcat");

            setDir(tomcatAppServer.getDir());
	}

    @InputFile
	public File getDir() {
		return GradleUtil.toFile(_dir);
	}

    public void setDir(Object dir) {
		_dir = dir;
	}


    public SetupTomcatBundleTask() {
        File webAppsDir = new File(tomcatAppServerDir, "webapps");

					webAppsDir.mkdirs();

					File catalinaPropertiesFile = new File(
						tomcatAppServerDir.getAbsolutePath() +
							"/conf/catalina.properties");

					String catalinaPropertiesFileContent = null;

					try {
						catalinaPropertiesFileContent = FileUtils.readFileToString(
							catalinaPropertiesFile);
					}
					catch (IOException ioException) {
						throw new RuntimeException(
							"Unable to read file " +
								catalinaPropertiesFile.getPath(),
							ioException);
					}

					StringBuilder tokenStringBuilder = new StringBuilder();

					tokenStringBuilder.append("common.loader=\"${catalina.base}/lib\",");
					tokenStringBuilder.append("\"${catalina.base}/lib/*.jar\",");
					tokenStringBuilder.append("\"${catalina.home}/lib\",");
					tokenStringBuilder.append("\"${catalina.home}/lib/*.jar\"");

					StringBuilder replaceTokenStringBuilder = new StringBuilder();

					replaceTokenStringBuilder.append(
						"common.loader=\"${catalina.home}/webapps/ROOT/");
					replaceTokenStringBuilder.append("WEB-INF/lib/support-tomcat.jar\",");
					replaceTokenStringBuilder.append("\"${catalina.base}/lib\",\"");
					replaceTokenStringBuilder.append("${catalina.base}/lib/*.jar\",");
					replaceTokenStringBuilder.append("\"${catalina.home}/lib\",");
					replaceTokenStringBuilder.append("\"${catalina.home}/lib/*.jar\"");

					catalinaPropertiesFileContent =
						catalinaPropertiesFileContent.replace(
							tokenStringBuilder.toString(), replaceTokenStringBuilder.toString());

					StringBuilder sb = new StringBuilder();

					sb.append(
						"tomcat.util.scan.StandardJarScanFilter.jarsToSkip=*");
					sb.append("\n");
					sb.append(
						"tomcat.util.scan.StandardJarScanFilter.jarsToScan=");

					catalinaPropertiesFileContent += sb.toString();

					try {
						Files.write(
							Paths.get(catalinaPropertiesFile.getAbsolutePath()),
							catalinaPropertiesFileContent.getBytes());
					}
					catch (IOException ioException) {
						throw new RuntimeException(
							"Unable to write catalina properties file " +
								catalinePropertiesFile.getPath(),
							ioException);
					}

					File contextXMLFile = new File(
						tomcatAppServerDir.getAbsolutePath() +
							"/conf/context.xml");

					List<String> contextXMLFileLines = null;

					try {
						contextXMLFileLines = Files.readAllLines(
							contextXMLFile.toPath());

						String contextXMLFileContent = String.join("\n", contextXMLLines);

						Files.write(
							contextXMLFile.getPath(),
							contextXMLFileContent.getBytes());
					}
					catch (IOException ioException) {
						throw new RuntimeException(
							"Unable to rewrite context XML file " +
								contextXMLFile.getPath(),
							ioException);
					}

                    String regex = '<\!--\s*Un\D* \/>\s*-->';

                    String replacement = ' <Manager pathname=\"SESSIONS.ser\" />\n';

                    String contentXMLContent = null;

					try {
						contentXMLFileContent = FileUtils.readFileToString(
							contextXMLFile);
					}
					catch (IOException ioException) {
						throw new RuntimeException(
							"Unable to read context XML file " +
								contextXMLFile.getPath(),
							ioException);
					}

                    contentXMLFileContent = contentXMLFileContent.replaceAll(
                        regex, replacement);

                    try {
						Files.write(
							Paths.get(contentXMLFile.getAbsolutePath()),
							contentXMLFileContent.getBytes());
					}
					catch (IOException ioException) {
						throw new RuntimeException(
							"Unable to write content XML file " +
								contentXML.getPath(),
							ioException);
					}

    }
    	private Object _dir;
}