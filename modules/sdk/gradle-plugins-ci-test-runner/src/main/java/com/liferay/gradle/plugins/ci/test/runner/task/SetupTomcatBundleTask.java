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

					File catalinaProperties = new File(
						tomcatAppServerDir.getAbsolutePath() +
							"/conf/catalina.properties");

					String catalinaPropertiesContent = null;

					try {
						catalinaPropertiesContent = FileUtils.readFileToString(
							catalinaProperties);
					}
					catch (IOException ioException) {
						throw new RuntimeException(
							"Unable to read file " +
								catalinaPropertiesFile.getPath(),
							ioException);
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

					catalinaPropertiesContent =
						catalinaPropertiesContent.replace(
							token.toString(), replaceToken.toString());

					StringBuilder sb = new StringBuilder();

					sb.append(
						"tomcat.util.scan.StandardJarScanFilter.jarsToSkip=*");
					sb.append("\n");
					sb.append(
						"tomcat.util.scan.StandardJarScanFilter.jarsToScan=");

					catalinaPropertiesContent += sb.toString();

					try {
						Files.write(
							Paths.get(catalinaProperties.getAbsolutePath()),
							catalinaPropertiesContent.getBytes());
					}
					catch (IOException ioException) {
						throw new RuntimeException(
							"Unable to write catalina properties file " +
								catalinePropertiesFile.getPath(),
							ioException);
					}

					File contextXML = new File(
						tomcatAppServerDir.getAbsolutePath() +
							"/conf/context.xml");

					List<String> contextXMLLines = null;

					try {
						contextXMLLines = Files.readAllLines(
							contextXML.toPath());

						String contentLF = String.join("\n", contextXMLLines);

						Files.write(
							filePath,
							contentLF.getBytes());
					}
					catch (IOException ioException) {
						throw new RuntimeException(
							"Unable to rewrite context XML file " +
								contextXML.getPath(),
							ioException);
					}

                    String regex = '<\!--\s*Un\D* \/>\s*-->';

                    String replacement = ' <Manager pathname=\"SESSIONS.ser\" />\n';

                    String contentXMLContent = null;

					try {
						contentXMLContent = FileUtils.readFileToString(
							contextXML);
					}
					catch (IOException ioException) {
						throw new RuntimeException(
							"Unable to read context XML file " +
								contextXMLFile.getPath(),
							ioException);
					}

                    contentXMLContent = contentXMLContent.replaceAll(
                        regex, replacement);

                    try {
						Files.write(
							Paths.get(contentXML.getAbsolutePath()),
							contentXMLContent.getBytes());
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