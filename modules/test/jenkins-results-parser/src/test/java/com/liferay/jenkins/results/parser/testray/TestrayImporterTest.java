/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.jenkins.results.parser.testray;

import com.liferay.jenkins.results.parser.PortalGitWorkingDirectory;
import com.liferay.jenkins.results.parser.RandomTestUtil;
import com.liferay.jenkins.results.parser.ReflectionTestUtil;
import com.liferay.jenkins.results.parser.test.clazz.JSUnitJUnitTestClass;
import com.liferay.jenkins.results.parser.test.clazz.TestClass;
import com.liferay.jenkins.results.parser.test.clazz.TestClassFactory;
import com.liferay.jenkins.results.parser.test.clazz.group.JSUnitModulesBatchTestClassGroup;

import java.io.File;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import org.dom4j.Element;

import org.json.JSONArray;
import org.json.JSONObject;

import org.junit.Assert;
import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Michael Hashimoto
 */
public class TestrayImporterTest
	extends com.liferay.jenkins.results.parser.Test {

	@Test
	public void testGetTestcaseElementName() throws Exception {
		String name = StringUtils.repeat(RandomTestUtil.randomString(), 7);

		name = name.substring(0, 250);

		testEquals(name, _getTestcaseName(name));
	}

	@Test
	public void testGetTestcaseElementNameLong() throws Exception {
		String name = StringUtils.repeat(RandomTestUtil.randomString(), 8);

		testEquals(name.substring(0, 250), _getTestcaseName(name));
	}

	@Test
	public void testIsTestClassFileReported() throws Exception {
		JSUnitJUnitTestClass jsUnitJUnitTestClass = _getTestClass(
			"modules/apps/a/b/test/js/c.js");

		jsUnitJUnitTestClass.setTestClassFileReported(true);

		Assert.assertTrue(_isTestClassFileReported(jsUnitJUnitTestClass));
	}

	@Test
	public void testIsTestClassFileReportedNoTestClassMethods()
		throws Exception {

		JSUnitJUnitTestClass jsUnitJUnitTestClass = _getTestClass();

		jsUnitJUnitTestClass.setTestClassFileReported(true);

		Assert.assertFalse(_isTestClassFileReported(jsUnitJUnitTestClass));
	}

	@Test
	public void testIsTestClassFileReportedOtherTestClass() throws Exception {
		Assert.assertFalse(
			_isTestClassFileReported(Mockito.mock(TestClass.class)));
	}

	@Test
	public void testIsTestClassFileReportedTestTask() throws Exception {
		JSUnitJUnitTestClass jsUnitJUnitTestClass = _getTestClass(
			"modules/apps/a/b/test/js/c.js");

		Assert.assertFalse(_isTestClassFileReported(jsUnitJUnitTestClass));
	}

	private JSUnitModulesBatchTestClassGroup _getBatchTestClassGroup() {
		JSUnitModulesBatchTestClassGroup jsUnitModulesBatchTestClassGroup =
			Mockito.mock(JSUnitModulesBatchTestClassGroup.class);

		PortalGitWorkingDirectory portalGitWorkingDirectory = Mockito.mock(
			PortalGitWorkingDirectory.class);

		Mockito.doReturn(
			new File("/x")
		).when(
			portalGitWorkingDirectory
		).getWorkingDirectory();

		Mockito.doReturn(
			portalGitWorkingDirectory
		).when(
			jsUnitModulesBatchTestClassGroup
		).getPortalGitWorkingDirectory();

		return jsUnitModulesBatchTestClassGroup;
	}

	private String _getTestcaseName(String name) throws Exception {
		TestrayCaseResult testrayCaseResult = Mockito.mock(
			TestrayCaseResult.class);

		Mockito.doReturn(
			name
		).when(
			testrayCaseResult
		).getName();

		Mockito.doReturn(
			TestrayCaseResult.Status.PASSED
		).when(
			testrayCaseResult
		).getStatus();

		Element testcaseElement = ReflectionTestUtil.invoke(
			Mockito.mock(TestrayImporter.class), "_getTestcaseElement",
			new Class<?>[] {
				TestrayCaseResult.class, String.class, String[].class
			},
			testrayCaseResult, RandomTestUtil.randomString(), null);

		Element propertiesElement = testcaseElement.element("properties");

		for (Element propertyElement : propertiesElement.elements("property")) {
			if (Objects.equals(
					propertyElement.attributeValue("name"),
					"testray.testcase.name")) {

				return propertyElement.attributeValue("value");
			}
		}

		return null;
	}

	private JSUnitJUnitTestClass _getTestClass(String... methodNames) {
		JSONArray methodsJSONArray = new JSONArray();

		for (String methodName : methodNames) {
			methodsJSONArray.put(
				new JSONObject(
				).put(
					"ignored", false
				).put(
					"name", methodName
				));
		}

		JSONObject jsonObject = new JSONObject(
		).put(
			"file", RandomTestUtil.randomString()
		).put(
			"ignored", false
		).put(
			"methods", methodsJSONArray
		);

		return (JSUnitJUnitTestClass)TestClassFactory.newTestClass(
			_getBatchTestClassGroup(), jsonObject);
	}

	private boolean _isTestClassFileReported(TestClass testClass) {
		return ReflectionTestUtil.invoke(
			Mockito.mock(TestrayImporter.class), "_isTestClassFileReported",
			new Class<?>[] {TestClass.class}, testClass);
	}

}