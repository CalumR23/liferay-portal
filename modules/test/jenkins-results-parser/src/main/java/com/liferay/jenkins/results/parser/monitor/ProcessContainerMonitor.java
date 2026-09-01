/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.jenkins.results.parser.monitor;

import com.liferay.jenkins.results.parser.JenkinsResultsParserUtil;

import java.io.File;
import java.io.IOException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * @author Calum Ragan
 */
public class ProcessContainerMonitor extends BaseMonitor {

	public ProcessContainerMonitor(MonitorConfig monitorConfig) {
		super(monitorConfig);

		Map<String, String> parameters = monitorConfig.getParameters();

		boolean ecsConfigured = _isConfigured(
			parameters, "ecs.cluster", "ecs.task");

		if (ecsConfigured &&
			_isConfigured(parameters, "container.name", "host")) {

			throw new IllegalArgumentException(
				JenkinsResultsParserUtil.combine(
					"Unable to configure both a container and an ECS task for ",
					"monitor[", monitorConfig.getId(), "]"));
		}

		_ecsMode = ecsConfigured;

		if (_ecsMode) {
			_containerName = null;
			_ecsCluster = getRequiredParameter("ecs.cluster", parameters);
			_ecsTask = getRequiredParameter("ecs.task", parameters);
			_host = null;

			_command = JenkinsResultsParserUtil.combine(
				"aws ecs list-tasks --cluster ", _ecsCluster, " --family ",
				_ecsTask, " --desired-status RUNNING --output text ",
				"--query \"length(taskArns)\"");

			return;
		}

		_containerName = getRequiredParameter("container.name", parameters);
		_ecsCluster = null;
		_ecsTask = null;

		_host = getRequiredParameter("host", parameters);

		_command = JenkinsResultsParserUtil.combine(
			"ssh root@", _host, " \"docker ps --filter name=", _containerName,
			" --format {{.Names}}\"");
	}

	@Override
	public MonitorResult execute() {
		long currentTimeMillis =
			JenkinsResultsParserUtil.getCurrentTimeMillis();

		if (_ecsMode) {
			return _executeECS(currentTimeMillis);
		}

		return _executeHost(currentTimeMillis);
	}

	private String _executeCommand() throws IOException, TimeoutException {
		Process process = JenkinsResultsParserUtil.executeBashCommands(
			new File("."), true, false, getSingleAttemptTimeoutMillis(),
			_command);

		int exitValue = process.exitValue();

		if (exitValue != 0) {
			throw new IOException("Command exited with the value " + exitValue);
		}

		String output = JenkinsResultsParserUtil.readInputStream(
			process.getInputStream());

		output = output.replace("Finished executing Bash commands.", "");

		return output.trim();
	}

	private MonitorResult _executeECS(long currentTimeMillis) {
		String output;

		try {
			output = _executeCommand();
		}
		catch (Exception exception) {
			return new MonitorResult(
				JenkinsResultsParserUtil.combine(
					"Unable to check ECS task ", _ecsTask, " in cluster ",
					_ecsCluster, ": ", _getExceptionMessage(exception)),
				null, MonitorResult.Status.CRITICAL, currentTimeMillis);
		}

		Integer runningCount = _getRunningCount(output);

		if (runningCount == null) {
			return new MonitorResult(
				JenkinsResultsParserUtil.combine(
					"Unable to read the running task count for ECS task ",
					_ecsTask, " in cluster ", _ecsCluster, ": ", output),
				null, MonitorResult.Status.CRITICAL, currentTimeMillis);
		}

		Map<String, String> metrics = new LinkedHashMap<>();

		metrics.put("task.running.count", String.valueOf(runningCount));

		if (runningCount == 0) {
			return new MonitorResult(
				JenkinsResultsParserUtil.combine(
					"ECS task ", _ecsTask, " is not running in cluster ",
					_ecsCluster),
				metrics, MonitorResult.Status.CRITICAL, currentTimeMillis);
		}

		return new MonitorResult(
			JenkinsResultsParserUtil.combine(
				"ECS task ", _ecsTask, " is running in cluster ", _ecsCluster),
			metrics, MonitorResult.Status.OK, currentTimeMillis);
	}

	private MonitorResult _executeHost(long currentTimeMillis) {
		String output;

		try {
			output = _executeCommand();
		}
		catch (Exception exception) {
			return new MonitorResult(
				JenkinsResultsParserUtil.combine(
					"Unable to check container ", _containerName, " on ", _host,
					": ", _getExceptionMessage(exception)),
				null, MonitorResult.Status.CRITICAL, currentTimeMillis);
		}

		boolean running = _isContainerRunning(output);

		Map<String, String> metrics = new LinkedHashMap<>();

		metrics.put("container.running", String.valueOf(running));

		if (!running) {
			return new MonitorResult(
				JenkinsResultsParserUtil.combine(
					"Container ", _containerName, " is not running on ", _host),
				metrics, MonitorResult.Status.CRITICAL, currentTimeMillis);
		}

		return new MonitorResult(
			JenkinsResultsParserUtil.combine(
				"Container ", _containerName, " is running on ", _host),
			metrics, MonitorResult.Status.OK, currentTimeMillis);
	}

	private String _getExceptionMessage(Exception exception) {
		String message = exception.getMessage();

		if (message == null) {
			Class<?> clazz = exception.getClass();

			return clazz.getName();
		}

		return message;
	}

	private Integer _getRunningCount(String output) {
		if (JenkinsResultsParserUtil.isNullOrEmpty(output)) {
			return null;
		}

		try {
			return Integer.parseInt(output);
		}
		catch (NumberFormatException numberFormatException) {
			return null;
		}
	}

	private boolean _isConfigured(
		Map<String, String> parameters, String... names) {

		for (String name : names) {
			String value = parameters.get(name);

			if (!JenkinsResultsParserUtil.isNullOrEmpty(value)) {
				return true;
			}
		}

		return false;
	}

	private boolean _isContainerRunning(String output) {
		if (JenkinsResultsParserUtil.isNullOrEmpty(output)) {
			return false;
		}

		for (String name : output.split("\n")) {
			if (_containerName.equals(name.trim())) {
				return true;
			}
		}

		return false;
	}

	private final String _command;
	private final String _containerName;
	private final String _ecsCluster;
	private final boolean _ecsMode;
	private final String _ecsTask;
	private final String _host;

}