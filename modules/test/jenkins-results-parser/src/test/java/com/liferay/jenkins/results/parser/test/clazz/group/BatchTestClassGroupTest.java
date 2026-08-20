/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.jenkins.results.parser.test.clazz.group;

import com.liferay.jenkins.results.parser.RandomTestUtil;
import com.liferay.jenkins.results.parser.Shell;
import com.liferay.jenkins.results.parser.test.clazz.TestClass;
import com.liferay.jenkins.results.parser.test.clazz.TestClassFactory;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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

	private List<Integer> _getAxisCounts(
		List<SegmentTestClassGroup> segmentTestClassGroups) {

		List<Integer> axisCounts = new ArrayList<>();

		for (SegmentTestClassGroup segmentTestClassGroup :
				segmentTestClassGroups) {

			axisCounts.add(segmentTestClassGroup.getAxisCount());
		}

		return axisCounts;
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