/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.jenkins.results.parser.monitor;

import com.liferay.jenkins.results.parser.JenkinsResultsParserUtil;
import com.liferay.jenkins.results.parser.RandomTestUtil;
import com.liferay.jenkins.results.parser.Shell;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Calum Ragan
 */
public class ProcessContainerMonitorTest
	extends com.liferay.jenkins.results.parser.Test {

	@Before
	@Override
	public void setUp() throws Exception {
		super.setUp();

		mockEnvironment(Collections.<String, String>emptyMap());
	}

	@Test
	public void testExecuteECSInvalidRunningCount() throws Exception {
		_testExecuteECSInvalidRunningCount("");
		_testExecuteECSInvalidRunningCount("None");
	}

	@Test
	public void testExecuteECSNotRunning() throws Exception {
		Shell shell = mockShell();

		setShellCommandOutput("aws ecs list-tasks", shell, "0");

		MonitorResult monitorResult = _execute(_newECSMonitorProperties());

		testEquals(MonitorResult.Status.CRITICAL, monitorResult.getStatus());
		testEquals(
			JenkinsResultsParserUtil.combine(
				"ECS task ", _ECS_TASK, " is not running in cluster ",
				_ECS_CLUSTER),
			monitorResult.getMessage());

		Map<String, String> metrics = monitorResult.getMetrics();

		testEquals("0", metrics.get("task.running.count"));
	}

	@Test
	public void testExecuteECSOK() throws Exception {
		Shell shell = mockShell();

		setShellCommandOutput("aws ecs list-tasks", shell, "2");

		MonitorResult monitorResult = _execute(_newECSMonitorProperties());

		testEquals(MonitorResult.Status.OK, monitorResult.getStatus());
		testEquals(
			JenkinsResultsParserUtil.combine(
				"ECS task ", _ECS_TASK, " is running in cluster ",
				_ECS_CLUSTER),
			monitorResult.getMessage());

		Map<String, String> metrics = monitorResult.getMetrics();

		testEquals("2", metrics.get("task.running.count"));
	}

	@Test
	public void testExecuteNotRunning() throws Exception {
		_testExecuteNotRunning("");
		_testExecuteNotRunning(_CONTAINER_NAME + "-backup");
	}

	@Test
	public void testExecuteOK() throws Exception {
		Shell shell = mockShell();

		setShellCommandOutput("docker ps", shell, _CONTAINER_NAME);

		MonitorResult monitorResult = _execute(_newMonitorProperties());

		testEquals(MonitorResult.Status.OK, monitorResult.getStatus());
		testEquals(
			JenkinsResultsParserUtil.combine(
				"Container ", _CONTAINER_NAME, " is running on ", _HOST),
			monitorResult.getMessage());

		Map<String, String> metrics = monitorResult.getMetrics();

		testEquals("true", metrics.get("container.running"));
	}

	@Test
	public void testProcessContainerMonitor() {
		_testProcessContainerMonitorMissingProperty(
			_newMonitorProperties(), "monitor[a].parameter[container.name]");
		_testProcessContainerMonitorMissingProperty(
			_newMonitorProperties(), "monitor[a].parameter[host]");
	}

	@Test
	public void testProcessContainerMonitorECS() {
		_testProcessContainerMonitorMissingProperty(
			_newECSMonitorProperties(), "monitor[a].parameter[ecs.cluster]");
		_testProcessContainerMonitorMissingProperty(
			_newECSMonitorProperties(), "monitor[a].parameter[ecs.task]");
	}

	@Test
	public void testProcessContainerMonitorMixedMode() {
		Properties monitorProperties = _newMonitorProperties();

		monitorProperties.setProperty(
			"monitor[a].parameter[ecs.cluster]", _ECS_CLUSTER);
		monitorProperties.setProperty(
			"monitor[a].parameter[ecs.task]", _ECS_TASK);

		_testProcessContainerMonitorExpectedIllegalArgumentException(
			monitorProperties);
	}

	@Test
	public void testProcessContainerMonitorNoParameters() {
		Properties monitorProperties = new Properties();

		monitorProperties.setProperty("monitor[a].type", "process-container");

		_testProcessContainerMonitorExpectedIllegalArgumentException(
			monitorProperties);
	}

	private MonitorResult _execute(Properties monitorProperties) {
		ProcessContainerMonitor processContainerMonitor =
			_newProcessContainerMonitor(monitorProperties);

		return processContainerMonitor.execute();
	}

	private Properties _newECSMonitorProperties() {
		Properties monitorProperties = new Properties();

		monitorProperties.setProperty(
			"monitor[a].parameter[ecs.cluster]", _ECS_CLUSTER);
		monitorProperties.setProperty(
			"monitor[a].parameter[ecs.task]", _ECS_TASK);
		monitorProperties.setProperty("monitor[a].type", "process-container");

		return monitorProperties;
	}

	private Properties _newMonitorProperties() {
		Properties monitorProperties = new Properties();

		monitorProperties.setProperty(
			"monitor[a].parameter[container.name]", _CONTAINER_NAME);
		monitorProperties.setProperty("monitor[a].parameter[host]", _HOST);
		monitorProperties.setProperty("monitor[a].type", "process-container");

		return monitorProperties;
	}

	private ProcessContainerMonitor _newProcessContainerMonitor(
		Properties monitorProperties) {

		List<MonitorConfig> monitorConfigs =
			MonitorConfigLoader.getMonitorConfigs(monitorProperties);

		return new ProcessContainerMonitor(monitorConfigs.get(0));
	}

	private void _testExecuteECSInvalidRunningCount(String standardOut)
		throws Exception {

		Shell shell = mockShell();

		setShellCommandOutput("aws ecs list-tasks", shell, standardOut);

		MonitorResult monitorResult = _execute(_newECSMonitorProperties());

		testEquals(MonitorResult.Status.CRITICAL, monitorResult.getStatus());
		testEquals(
			JenkinsResultsParserUtil.combine(
				"Unable to read the running task count for ECS task ",
				_ECS_TASK, " in cluster ", _ECS_CLUSTER, ": ", standardOut),
			monitorResult.getMessage());
	}

	private void _testExecuteNotRunning(String standardOut) throws Exception {
		Shell shell = mockShell();

		setShellCommandOutput("docker ps", shell, standardOut);

		MonitorResult monitorResult = _execute(_newMonitorProperties());

		testEquals(MonitorResult.Status.CRITICAL, monitorResult.getStatus());
		testEquals(
			JenkinsResultsParserUtil.combine(
				"Container ", _CONTAINER_NAME, " is not running on ", _HOST),
			monitorResult.getMessage());

		Map<String, String> metrics = monitorResult.getMetrics();

		testEquals("false", metrics.get("container.running"));
	}

	private void _testProcessContainerMonitorExpectedIllegalArgumentException(
		Properties monitorProperties) {

		try {
			_newProcessContainerMonitor(monitorProperties);

			Assert.fail("Expected IllegalArgumentException");
		}
		catch (IllegalArgumentException illegalArgumentException) {
		}
	}

	private void _testProcessContainerMonitorMissingProperty(
		Properties monitorProperties, String name) {

		monitorProperties.remove(name);

		_testProcessContainerMonitorExpectedIllegalArgumentException(
			monitorProperties);
	}

	private static final String _CONTAINER_NAME = RandomTestUtil.randomString();

	private static final String _ECS_CLUSTER = RandomTestUtil.randomString();

	private static final String _ECS_TASK = RandomTestUtil.randomString();

	private static final String _HOST = RandomTestUtil.randomString();

}