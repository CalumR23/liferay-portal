/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.jenkins.results.parser.test.clazz.group;

import com.liferay.jenkins.results.parser.JenkinsResultsParserUtil;
import com.liferay.jenkins.results.parser.RandomTestUtil;
import com.liferay.jenkins.results.parser.Shell;
import com.liferay.jenkins.results.parser.test.clazz.TestClass;
import com.liferay.jenkins.results.parser.test.clazz.TestClassFactory;

import java.io.File;

import java.nio.file.Files;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import org.mockito.Mockito;

/**
 * @author Kenji Heigel
 */
public class BatchTestClassGroupTest
	extends com.liferay.jenkins.results.parser.Test {

	@Before
	public void setUpGitRemotes() throws Exception {
		Shell shell = mockShell();

		setShellCommandOutput("git branch | grep", shell, "* master\n");
		setShellCommandOutput(
			"git remote -v", shell,
			BatchTestClassGroupTestUtil.getGitRemotesShellCommandOutput());
	}

	@Test
	public void testGetAxisCount() {
		_testGetAxisCount("-1", null, 3, 12);
		_testGetAxisCount("0", null, 0, 12);
		_testGetAxisCount("7", null, 7, 12);
		_testGetAxisCount("abc", null, 3, 12);
		_testGetAxisCount(null, "", 1, 12);
		_testGetAxisCount(null, "-3", 1, 12);
		_testGetAxisCount(null, "abc", 1, 12);
		_testGetAxisCount(null, null, 0, 0);
		_testGetAxisCount(null, null, 1, 1);
		_testGetAxisCount(null, null, 3, 12);
	}

	@Test
	public void testGetAxisCountAutoBalanceTests() throws Exception {
		BatchTestClassGroupTestUtil.resetCaches();

		String className = "SampleAutoBalanceTest";

		File workingDirectory = _newAutoBalanceWorkingDirectory(className);

		Properties jobProperties = new Properties();

		jobProperties.setProperty(
			"test.class.names.auto.balance",
			"com/liferay/" + className + ".java");

		JUnitBatchTestClassGroup jUnitBatchTestClassGroup =
			new JUnitBatchTestClassGroup(
				"unit",
				BatchTestClassGroupTestUtil.getPortalTestClassJob(
					jobProperties,
					Collections.singletonList(
						new File(workingDirectory, "Modified.java")),
					workingDirectory)) {

				@Override
				protected void setTestClasses() {
				}

			};

		testEquals(1, jUnitBatchTestClassGroup.getAxisCount());

		List<AxisTestClassGroup> axisTestClassGroups =
			jUnitBatchTestClassGroup.getAxisTestClassGroups();

		testEquals(1, axisTestClassGroups.size());

		AxisTestClassGroup axisTestClassGroup = axisTestClassGroups.get(0);

		List<TestClass> testClasses = axisTestClassGroup.getTestClasses();

		testEquals(1, testClasses.size());
	}

	@Test
	public void testGetAxisCountAxisMaxSizeZero() {
		BatchTestClassGroup batchTestClassGroup = _newBatchTestClassGroup(
			null, "0", null, 12);

		try {
			batchTestClassGroup.getAxisCount();

			Assert.fail();
		}
		catch (RuntimeException runtimeException) {
			testEquals(
				"'test.batch.axis.max.size' cannot be 0 or less",
				runtimeException.getMessage());
		}
	}

	@Test
	public void testGetAxisMaxSize() {
		_testGetAxisMaxSize("", 5000);
		_testGetAxisMaxSize("-3", 5000);
		_testGetAxisMaxSize("0", 0);
		_testGetAxisMaxSize("5", 5);
		_testGetAxisMaxSize("abc", 5000);
	}

	@Test
	public void testGetAxisTestClassGroups() {
		BatchTestClassGroupTestUtil.resetCaches();

		BatchTestClassGroup batchTestClassGroup = new BatchTestClassGroup(
			"default", BatchTestClassGroupTestUtil.getPortalTestClassJob()) {
		};

		int axisMaxSize = batchTestClassGroup.getAxisMaxSize();

		for (int i = 0; i < (axisMaxSize + 2); i++) {
			batchTestClassGroup.addTestClass(
				TestClassFactory.newTestClass(
					batchTestClassGroup,
					new File(RandomTestUtil.randomString())));
		}

		batchTestClassGroup.setAxisTestClassGroups();

		List<TestClass> testClasses = batchTestClassGroup.getTestClasses();

		List<AxisTestClassGroup> axisTestClassGroups =
			batchTestClassGroup.getAxisTestClassGroups();

		Assert.assertEquals(
			axisTestClassGroups.toString(),
			(int)Math.ceil((double)testClasses.size() / axisMaxSize),
			axisTestClassGroups.size());

		List<TestClass> axisTestClasses = new ArrayList<>();

		for (AxisTestClassGroup axisTestClassGroup : axisTestClassGroups) {
			axisTestClasses.addAll(axisTestClassGroup.getTestClasses());
		}

		Collections.sort(axisTestClasses);

		Assert.assertEquals(testClasses, axisTestClasses);
	}

	@Test
	public void testGetSegmentMaxChildren() {
		_testGetSegmentMaxChildren(0, "0");
		_testGetSegmentMaxChildren(3, "3");
		_testGetSegmentMaxChildren(25, "");
		_testGetSegmentMaxChildren(25, "-2");
		_testGetSegmentMaxChildren(25, "abc");
	}

	@Test
	public void testSetAxisTestClassGroups() {
		_testSetAxisTestClassGroups("10", null, new int[] {1, 1, 1}, 3);
		_testSetAxisTestClassGroups("4", null, new int[] {2, 2, 2, 1}, 7);
		_testSetAxisTestClassGroups(null, null, new int[0], 0);
		_testSetAxisTestClassGroups(null, null, new int[] {4, 4, 4}, 12);
		_testSetAxisTestClassGroups(null, null, new int[] {5, 5, 3}, 13);
	}

	@Test
	public void testSetAxisTestClassGroupsBalancesByWeight() throws Exception {
		BatchTestClassGroupTestUtil.resetCaches();

		Properties jobProperties = new Properties();

		jobProperties.setProperty("test.batch.axis.count", "2");

		CompileModulesBatchTestClassGroup compileModulesBatchTestClassGroup =
			BatchTestClassGroupTestUtil.newCompileModulesBatchTestClassGroup(
				jobProperties, _newModuleDir("aaa-module", 2),
				_newModuleDir("aab-module", 2), _newModuleDir("zzy-module", 3),
				_newModuleDir("zzz-module", 3));

		List<AxisTestClassGroup> axisTestClassGroups =
			compileModulesBatchTestClassGroup.getAxisTestClassGroups();

		testEquals(2, axisTestClassGroups.size());

		for (AxisTestClassGroup axisTestClassGroup : axisTestClassGroups) {
			testEquals(5L, _getWeight(axisTestClassGroup));
		}
	}

	@Test
	public void testSetAxisTestClassGroupsTargetAxisDuration()
		throws Exception {

		_testSetAxisTestClassGroupsTargetAxisDuration(new int[] {4, 3}, "");
		_testSetAxisTestClassGroupsTargetAxisDuration(
			new int[] {3, 3, 1}, "3000");
	}

	@Test
	public void testSetSegmentTestClassGroups() {
		File testBaseDir = new File(RandomTestUtil.randomString());

		Integer minimumSlaveRAM = RandomTestUtil.randomInt();
		String slaveLabel = RandomTestUtil.randomString();

		_testSetSegmentTestClassGroups(
			1, new Integer[] {minimumSlaveRAM, minimumSlaveRAM},
			new String[] {slaveLabel, slaveLabel},
			new File[] {testBaseDir, testBaseDir});

		_testSetSegmentTestClassGroups(
			2, new Integer[] {minimumSlaveRAM, RandomTestUtil.randomInt()},
			new String[] {slaveLabel, slaveLabel},
			new File[] {testBaseDir, testBaseDir});

		_testSetSegmentTestClassGroups(
			2, new Integer[] {minimumSlaveRAM, minimumSlaveRAM},
			new String[] {slaveLabel, RandomTestUtil.randomString()},
			new File[] {testBaseDir, testBaseDir});

		_testSetSegmentTestClassGroups(
			2, new Integer[] {minimumSlaveRAM, minimumSlaveRAM},
			new String[] {slaveLabel, slaveLabel},
			new File[] {testBaseDir, new File(RandomTestUtil.randomString())});

		_testSetSegmentTestClassGroups(
			2, new Integer[] {minimumSlaveRAM, minimumSlaveRAM},
			new String[] {slaveLabel, slaveLabel},
			new File[] {testBaseDir, null});
	}

	@Test
	public void testSetSegmentTestClassGroupsEmpty() {
		BatchTestClassGroup batchTestClassGroup = _newBatchTestClassGroup(
			null, null, null, 0);

		batchTestClassGroup.setSegmentTestClassGroups();

		testEquals(
			Collections.emptyList(),
			batchTestClassGroup.getSegmentTestClassGroups());
	}

	@Test
	public void testSetSegmentTestClassGroupsMaxChildren() {
		BatchTestClassGroup batchTestClassGroup = _newBatchTestClassGroup(
			null, null, "3", 0);

		Integer minimumSlaveRAM = RandomTestUtil.randomInt();
		String slaveLabel = RandomTestUtil.randomString();

		for (int i = 0; i < 7; i++) {
			batchTestClassGroup.addAxisTestClassGroup(
				_mockAxisTestClassGroup(minimumSlaveRAM, slaveLabel, null));
		}

		batchTestClassGroup.setSegmentTestClassGroups();

		testEquals(
			Arrays.asList(3, 3, 1),
			_getAxisCounts(batchTestClassGroup.getSegmentTestClassGroups()));
	}

	@Test
	public void testSetSegmentTestClassGroupsMaxChildrenZero() {
		BatchTestClassGroup batchTestClassGroup = _newBatchTestClassGroup(
			null, null, "0", 0);

		batchTestClassGroup.addAxisTestClassGroup(
			_mockAxisTestClassGroup(
				RandomTestUtil.randomInt(), RandomTestUtil.randomString(),
				null));

		try {
			batchTestClassGroup.setSegmentTestClassGroups();

			Assert.fail();
		}
		catch (IllegalArgumentException illegalArgumentException) {
		}
	}

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	private List<Integer> _getAxisCounts(
		List<SegmentTestClassGroup> segmentTestClassGroups) {

		List<Integer> axisCounts = new ArrayList<>();

		for (SegmentTestClassGroup segmentTestClassGroup :
				segmentTestClassGroups) {

			axisCounts.add(segmentTestClassGroup.getAxisCount());
		}

		return axisCounts;
	}

	private File _getParentFile(List<File> files) {
		File file = files.get(0);

		return file.getParentFile();
	}

	private long _getWeight(AxisTestClassGroup axisTestClassGroup) {
		long weight = 0;

		for (TestClass testClass : axisTestClassGroup.getTestClasses()) {
			weight += testClass.getWeight();
		}

		return weight;
	}

	private AxisTestClassGroup _mockAxisTestClassGroup(
		Integer minimumSlaveRAM, String slaveLabel, File testBaseDir) {

		AxisTestClassGroup axisTestClassGroup = Mockito.mock(
			AxisTestClassGroup.class);

		Mockito.doReturn(
			minimumSlaveRAM
		).when(
			axisTestClassGroup
		).getMinimumSlaveRAM();

		Mockito.doReturn(
			slaveLabel
		).when(
			axisTestClassGroup
		).getBaseSlaveLabel();

		Mockito.doReturn(
			testBaseDir
		).when(
			axisTestClassGroup
		).getTestBaseDir();

		return axisTestClassGroup;
	}

	private File _newAutoBalanceWorkingDirectory(String className)
		throws Exception {

		File workingDirectory = new File(
			JenkinsResultsParserUtil.getCanonicalPath(
				temporaryFolder.newFolder(
					RandomTestUtil.randomString(
					).substring(
						0, 8
					))));

		File packageDir = new File(workingDirectory, "com/liferay");

		packageDir.mkdirs();

		Files.write(
			new File(
				packageDir, className + ".java"
			).toPath(),
			JenkinsResultsParserUtil.combine(
				"public class ", className, " {\n\n\t@Test\n\tpublic ",
				"void testSample() {\n\t}\n\n}"
			).getBytes(
				"UTF-8"
			));

		return workingDirectory;
	}

	private BatchTestClassGroup _newBatchTestClassGroup(
		String axisCount, String axisMaxSize, String segmentMaxChildren,
		int testClassCount) {

		BatchTestClassGroupTestUtil.resetCaches();

		Properties jobProperties = new Properties();

		if (axisCount != null) {
			jobProperties.setProperty("test.batch.axis.count", axisCount);
		}

		if (axisMaxSize != null) {
			jobProperties.setProperty("test.batch.axis.max.size", axisMaxSize);
		}

		if (segmentMaxChildren != null) {
			jobProperties.setProperty(
				"test.batch.segment.max.children", segmentMaxChildren);
		}

		BatchTestClassGroup batchTestClassGroup = new BatchTestClassGroup(
			"default",
			BatchTestClassGroupTestUtil.getPortalTestClassJob(jobProperties)) {
		};

		for (int i = 0; i < testClassCount; i++) {
			batchTestClassGroup.addTestClass(
				TestClassFactory.newTestClass(
					batchTestClassGroup,
					new File(RandomTestUtil.randomString())));
		}

		return batchTestClassGroup;
	}

	private List<File> _newJUnitTestClassFiles(int testClassCount)
		throws Exception {

		List<File> testClassFiles = new ArrayList<>();

		File testDir = temporaryFolder.newFolder(
			RandomTestUtil.randomString(
			).substring(
				0, 8
			));

		for (int i = 0; i < testClassCount; i++) {
			String className = "Sample" + i + "Test";

			File testClassFile = new File(testDir, className + ".java");

			Files.write(
				testClassFile.toPath(),
				JenkinsResultsParserUtil.combine(
					"public class ", className, " {\n\n\t@Test\n\tpublic ",
					"void testSample() {\n\t}\n\n}"
				).getBytes(
					"UTF-8"
				));

			testClassFiles.add(testClassFile);
		}

		return testClassFiles;
	}

	private File _newModuleDir(String name, int modulesProjectDirCount)
		throws Exception {

		File moduleDir = temporaryFolder.newFolder(name);

		File lfrBuildPortalFile = new File(moduleDir, ".lfrbuild-portal");

		lfrBuildPortalFile.createNewFile();

		for (int i = 0; i < modulesProjectDirCount; i++) {
			File modulesProjectDir = new File(moduleDir, "project-" + i);

			modulesProjectDir.mkdirs();

			File bndBndFile = new File(modulesProjectDir, "bnd.bnd");

			bndBndFile.createNewFile();

			File buildGradleFile = new File(modulesProjectDir, "build.gradle");

			buildGradleFile.createNewFile();
		}

		return moduleDir;
	}

	private void _testGetAxisCount(
		String axisCount, String axisMaxSize, int expectedAxisCount,
		int testClassCount) {

		BatchTestClassGroup batchTestClassGroup = _newBatchTestClassGroup(
			axisCount, axisMaxSize, null, testClassCount);

		testEquals(expectedAxisCount, batchTestClassGroup.getAxisCount());
	}

	private void _testGetAxisMaxSize(
		String axisMaxSize, int expectedAxisMaxSize) {

		BatchTestClassGroup batchTestClassGroup = _newBatchTestClassGroup(
			null, axisMaxSize, null, 0);

		testEquals(expectedAxisMaxSize, batchTestClassGroup.getAxisMaxSize());
	}

	private void _testGetSegmentMaxChildren(
		int expectedSegmentMaxChildren, String segmentMaxChildren) {

		BatchTestClassGroup batchTestClassGroup = _newBatchTestClassGroup(
			null, null, segmentMaxChildren, 0);

		testEquals(
			expectedSegmentMaxChildren,
			batchTestClassGroup.getSegmentMaxChildren());
	}

	private void _testSetAxisTestClassGroups(
		String axisCount, String axisMaxSize, int[] expectedAxisSizes,
		int testClassCount) {

		BatchTestClassGroup batchTestClassGroup = _newBatchTestClassGroup(
			axisCount, axisMaxSize, null, testClassCount);

		batchTestClassGroup.setAxisTestClassGroups();

		List<AxisTestClassGroup> axisTestClassGroups =
			batchTestClassGroup.getAxisTestClassGroups();

		List<Integer> axisSizes = new ArrayList<>();

		List<TestClass> axisTestClasses = new ArrayList<>();

		for (AxisTestClassGroup axisTestClassGroup : axisTestClassGroups) {
			List<TestClass> testClasses = axisTestClassGroup.getTestClasses();

			axisSizes.add(testClasses.size());

			axisTestClasses.addAll(testClasses);
		}

		List<Integer> expectedAxisSizesList = new ArrayList<>();

		for (int expectedAxisSize : expectedAxisSizes) {
			expectedAxisSizesList.add(expectedAxisSize);
		}

		testEquals(expectedAxisSizesList, axisSizes);

		Collections.sort(axisTestClasses);

		testEquals(batchTestClassGroup.getTestClasses(), axisTestClasses);
	}

	private void _testSetAxisTestClassGroupsTargetAxisDuration(
			int[] expectedAxisSizes, String targetAxisDuration)
		throws Exception {

		BatchTestClassGroupTestUtil.resetCaches();

		Properties jobProperties = new Properties();

		jobProperties.setProperty("test.batch.default.test.duration", "1000");
		jobProperties.setProperty(
			"test.batch.default.test.overhead.duration", "0");
		jobProperties.setProperty(
			"test.batch.target.axis.duration", targetAxisDuration);

		final List<File> testClassFiles = _newJUnitTestClassFiles(7);

		JUnitBatchTestClassGroup jUnitBatchTestClassGroup =
			new JUnitBatchTestClassGroup(
				"unit",
				BatchTestClassGroupTestUtil.getPortalTestClassJob(
					jobProperties, new ArrayList<File>(),
					_getParentFile(testClassFiles))) {

				@Override
				protected void setTestClasses() {
					for (File testClassFile : testClassFiles) {
						addTestClass(
							TestClassFactory.newTestClass(this, testClassFile));
					}
				}

			};

		List<Integer> axisSizes = new ArrayList<>();

		for (AxisTestClassGroup axisTestClassGroup :
				jUnitBatchTestClassGroup.getAxisTestClassGroups()) {

			List<TestClass> testClasses = axisTestClassGroup.getTestClasses();

			axisSizes.add(testClasses.size());
		}

		List<Integer> expectedAxisSizesList = new ArrayList<>();

		for (int expectedAxisSize : expectedAxisSizes) {
			expectedAxisSizesList.add(expectedAxisSize);
		}

		Collections.sort(axisSizes, Collections.reverseOrder());

		testEquals(expectedAxisSizesList, axisSizes);
	}

	private void _testSetSegmentTestClassGroups(
		int expectedSegmentCount, Integer[] minimumSlaveRAMs,
		String[] slaveLabels, File[] testBaseDirs) {

		BatchTestClassGroup batchTestClassGroup = _newBatchTestClassGroup(
			null, null, null, 0);

		for (int i = 0; i < minimumSlaveRAMs.length; i++) {
			batchTestClassGroup.addAxisTestClassGroup(
				_mockAxisTestClassGroup(
					minimumSlaveRAMs[i], slaveLabels[i], testBaseDirs[i]));
		}

		batchTestClassGroup.setSegmentTestClassGroups();

		List<SegmentTestClassGroup> segmentTestClassGroups =
			batchTestClassGroup.getSegmentTestClassGroups();

		testEquals(expectedSegmentCount, segmentTestClassGroups.size());

		List<AxisTestClassGroup> segmentAxisTestClassGroups = new ArrayList<>();

		for (SegmentTestClassGroup segmentTestClassGroup :
				segmentTestClassGroups) {

			List<AxisTestClassGroup> axisTestClassGroups =
				segmentTestClassGroup.getAxisTestClassGroups();

			segmentAxisTestClassGroups.addAll(axisTestClassGroups);

			AxisTestClassGroup firstAxisTestClassGroup =
				axisTestClassGroups.get(0);

			for (AxisTestClassGroup axisTestClassGroup : axisTestClassGroups) {
				testEquals(
					firstAxisTestClassGroup.getMinimumSlaveRAM(),
					axisTestClassGroup.getMinimumSlaveRAM());
				testEquals(
					firstAxisTestClassGroup.getBaseSlaveLabel(),
					axisTestClassGroup.getBaseSlaveLabel());
				testEquals(
					firstAxisTestClassGroup.getTestBaseDir(),
					axisTestClassGroup.getTestBaseDir());
			}
		}

		testEquals(
			batchTestClassGroup.getAxisTestClassGroups(
			).size(),
			segmentAxisTestClassGroups.size());

		testEquals(
			true,
			segmentAxisTestClassGroups.containsAll(
				batchTestClassGroup.getAxisTestClassGroups()));
	}

}