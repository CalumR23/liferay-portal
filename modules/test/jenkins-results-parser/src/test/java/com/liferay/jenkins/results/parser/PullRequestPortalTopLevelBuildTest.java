/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.jenkins.results.parser;

import java.util.Collections;
import java.util.Map;

import org.json.JSONArray;

import org.junit.After;
import org.junit.Test;

import org.mockito.MockedStatic;
import org.mockito.Mockito;

/**
 * @author Calum Ragan
 */
public class PullRequestPortalTopLevelBuildTest
	extends com.liferay.jenkins.results.parser.Test {

	@After
	@Override
	public void tearDown() {
		super.tearDown();

		ReflectionTestUtil.setFieldValue(
			JenkinsResultsParserUtil.class, "_ciNode", null);
		ReflectionTestUtil.setFieldValue(
			JenkinsResultsParserUtil.class, "_gitDirectoriesJSONArray", null);
		ReflectionTestUtil.setFieldValue(
			JenkinsResultsParserUtil.class, "_gitWorkingDirectoriesJSONArray",
			null);

		Map<String, Workspace> workspaces = ReflectionTestUtil.getFieldValue(
			WorkspaceFactory.class, "_workspaces");

		workspaces.clear();
	}

	@Test
	public void testGetPortalUpstreamBranchName() {
		String portalUpstreamBranchName = RandomTestUtil.randomString();

		_testGetPortalUpstreamBranchName("master-private", "master", null);
		_testGetPortalUpstreamBranchName(
			"master-private", portalUpstreamBranchName,
			portalUpstreamBranchName);
		_testGetPortalUpstreamBranchName(
			RandomTestUtil.randomString(), portalUpstreamBranchName,
			portalUpstreamBranchName);

		_testGetPortalUpstreamBranchName(
			RandomTestUtil.randomString(), null, "");
	}

	@Test
	public void testGetStableJob() {
		BuildDatabaseUtil.setBuildDatabase(Mockito.mock(BuildDatabase.class));

		ReflectionTestUtil.setFieldValue(
			JenkinsResultsParserUtil.class, "_ciNode", true);

		String branchName = RandomTestUtil.randomString();

		_testGetStableJob("master-private", "master");
		_testGetStableJob(branchName, branchName);
	}

	@Test
	public void testGetWorkspace() {
		mockEnvironment(
			Collections.singletonMap(
				"BUILD_DIR", RandomTestUtil.randomString()));

		BuildDatabaseUtil.setBuildDatabase(Mockito.mock(BuildDatabase.class));

		ReflectionTestUtil.setFieldValue(
			JenkinsResultsParserUtil.class, "_gitDirectoriesJSONArray",
			new JSONArray());
		ReflectionTestUtil.setFieldValue(
			JenkinsResultsParserUtil.class, "_gitWorkingDirectoriesJSONArray",
			new JSONArray());

		String portalUpstreamBranchName = RandomTestUtil.randomString();

		_testGetWorkspace(portalUpstreamBranchName, portalUpstreamBranchName);

		_testGetWorkspace(null, "");
		_testGetWorkspace(null, null);
	}

	private void _testGetPortalUpstreamBranchName(
		String branchName, String expectedPortalUpstreamBranchName,
		String portalUpstreamBranchName) {

		PullRequestPortalTopLevelBuild pullRequestPortalTopLevelBuild =
			Mockito.mock(PullRequestPortalTopLevelBuild.class);

		Mockito.doReturn(
			branchName
		).when(
			pullRequestPortalTopLevelBuild
		).getBranchName();

		Mockito.doReturn(
			portalUpstreamBranchName
		).when(
			pullRequestPortalTopLevelBuild
		).getParameterValue(
			"PORTAL_UPSTREAM_BRANCH_NAME"
		);

		Mockito.doCallRealMethod(
		).when(
			pullRequestPortalTopLevelBuild
		).getPortalUpstreamBranchName();

		testEquals(
			expectedPortalUpstreamBranchName,
			pullRequestPortalTopLevelBuild.getPortalUpstreamBranchName());
	}

	private void _testGetStableJob(
		String branchName, String expectedPortalUpstreamBranchName) {

		PullRequestPortalTopLevelBuild pullRequestPortalTopLevelBuild =
			Mockito.mock(PullRequestPortalTopLevelBuild.class);

		Mockito.doReturn(
			branchName
		).when(
			pullRequestPortalTopLevelBuild
		).getBranchName();

		Mockito.doCallRealMethod(
		).when(
			pullRequestPortalTopLevelBuild
		).getPortalUpstreamBranchName();

		Mockito.doReturn(
			"relevant"
		).when(
			pullRequestPortalTopLevelBuild
		).getTestSuiteName();

		Job job = Mockito.mock(Job.class);
		PortalGitWorkingDirectory portalGitWorkingDirectory = Mockito.mock(
			PortalGitWorkingDirectory.class);

		try (MockedStatic<GitWorkingDirectoryFactory>
				gitWorkingDirectoryFactoryMockedStatic = Mockito.mockStatic(
					GitWorkingDirectoryFactory.class);
			MockedStatic<JobFactory> jobFactoryMockedStatic =
				Mockito.mockStatic(JobFactory.class)) {

			gitWorkingDirectoryFactoryMockedStatic.when(
				() -> GitWorkingDirectoryFactory.newPortalGitWorkingDirectory(
					expectedPortalUpstreamBranchName)
			).thenReturn(
				portalGitWorkingDirectory
			);

			jobFactoryMockedStatic.when(
				() -> JobFactory.newJob(
					Mockito.isNull(), Mockito.isNull(), Mockito.isNull(),
					Mockito.eq(portalGitWorkingDirectory), Mockito.isNull(),
					Mockito.eq(expectedPortalUpstreamBranchName),
					Mockito.isNull(), Mockito.isNull(), Mockito.eq("stable"),
					Mockito.eq(branchName))
			).thenReturn(
				job
			);

			testSame(
				job,
				ReflectionTestUtil.invoke(
					pullRequestPortalTopLevelBuild, "_getStableJob",
					new Class<?>[0]));
		}
	}

	private void _testGetWorkspace(
		String expectedPortalUpstreamBranchName,
		String portalUpstreamBranchName) {

		PullRequest pullRequest = Mockito.mock(PullRequest.class);

		String gitRepositoryName = RandomTestUtil.randomString();

		Mockito.doReturn(
			gitRepositoryName
		).when(
			pullRequest
		).getGitRepositoryName();

		PortalWorkspace portalWorkspace = Mockito.mock(PortalWorkspace.class);

		WorkspaceGitRepository workspaceGitRepository = Mockito.mock(
			WorkspaceGitRepository.class);

		Mockito.doReturn(
			workspaceGitRepository
		).when(
			portalWorkspace
		).getPrimaryWorkspaceGitRepository();

		Map<String, Workspace> workspaces = ReflectionTestUtil.getFieldValue(
			WorkspaceFactory.class, "_workspaces");

		workspaces.put(gitRepositoryName, portalWorkspace);

		PullRequestPortalTopLevelBuild pullRequestPortalTopLevelBuild =
			Mockito.mock(PullRequestPortalTopLevelBuild.class);

		Mockito.doReturn(
			portalUpstreamBranchName
		).when(
			pullRequestPortalTopLevelBuild
		).getParameterValue(
			"PORTAL_UPSTREAM_BRANCH_NAME"
		);

		Mockito.doCallRealMethod(
		).when(
			pullRequestPortalTopLevelBuild
		).getPortalUpstreamBranchName();

		Mockito.doReturn(
			pullRequest
		).when(
			pullRequestPortalTopLevelBuild
		).getPullRequest();

		Mockito.doCallRealMethod(
		).when(
			pullRequestPortalTopLevelBuild
		).getWorkspace();

		pullRequestPortalTopLevelBuild.getWorkspace();

		Mockito.verify(
			portalWorkspace
		).setPortalUpstreamBranchName(
			expectedPortalUpstreamBranchName
		);
	}

}