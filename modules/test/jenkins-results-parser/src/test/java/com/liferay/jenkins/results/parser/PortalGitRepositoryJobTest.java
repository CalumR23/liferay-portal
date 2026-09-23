/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.jenkins.results.parser;

import org.json.JSONObject;

import org.junit.Test;

import org.mockito.Mockito;

/**
 * @author Calum Ragan
 */
public class PortalGitRepositoryJobTest
	extends com.liferay.jenkins.results.parser.Test {

	@Test
	public void testGetPortalUpstreamBranchName() {
		PortalGitRepositoryJob portalGitRepositoryJob = Mockito.mock(
			PortalGitRepositoryJob.class);

		String upstreamBranchName = RandomTestUtil.randomString();

		Mockito.doReturn(
			upstreamBranchName
		).when(
			portalGitRepositoryJob
		).getUpstreamBranchName();

		String portalUpstreamBranchName = RandomTestUtil.randomString();

		_testGetPortalUpstreamBranchName(
			portalUpstreamBranchName,
			new JSONObject(
			).put(
				"branch",
				new JSONObject(
				).put(
					"upstream_branch_name", portalUpstreamBranchName
				)
			),
			portalGitRepositoryJob);

		_testGetPortalUpstreamBranchName(
			upstreamBranchName, new JSONObject(), portalGitRepositoryJob);
		_testGetPortalUpstreamBranchName(
			upstreamBranchName,
			new JSONObject(
			).put(
				"branch", new JSONObject()
			),
			portalGitRepositoryJob);
	}

	private void _testGetPortalUpstreamBranchName(
		String expectedPortalUpstreamBranchName, JSONObject jsonObject,
		PortalGitRepositoryJob portalGitRepositoryJob) {

		testEquals(
			expectedPortalUpstreamBranchName,
			ReflectionTestUtil.invoke(
				portalGitRepositoryJob, "_getPortalUpstreamBranchName",
				new Class<?>[] {JSONObject.class}, jsonObject));
	}

}