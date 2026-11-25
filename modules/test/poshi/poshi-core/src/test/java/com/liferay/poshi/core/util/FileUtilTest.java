/**
 * SPDX-FileCopyrightText: (c) 2026 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.poshi.core.util;

import java.io.File;

import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Path;

import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Brian Wulbern
 */
public class FileUtilTest extends TestCase {

	@Before
	@Override
	public void setUp() throws Exception {
		_baseDir = Files.createTempDirectory("poshi-file-util-test");

		for (String fileName : _FILE_NAMES) {
			Path path = _baseDir.resolve(fileName);

			Files.createFile(path);
		}
	}

	@After
	@Override
	public void tearDown() throws Exception {
		if (_baseDir == null) {
			return;
		}

		File baseDir = _baseDir.toFile();

		FileUtil.delete(baseDir);
	}

	@Test
	public void testGetIncludedFilePathsReturnsNothingWhenNothingMatches()
		throws Exception {

		List<String> filePaths = FileUtil.getIncludedFilePaths(
			new String[] {"**/no-such-file*.zip"}, _getBaseDirName());

		assertTrue(filePaths.isEmpty());
	}

	@Test
	public void testGetIncludedFilePathsReturnsUnencodedFileNames()
		throws Exception {

		for (String fileName : _FILE_NAMES) {
			assertEquals(fileName, _getIncludedFileName(fileName));
		}
	}

	@Test
	public void testGetIncludedResourceURLsStillEncodesFileNames()
		throws Exception {

		List<URL> urls = FileUtil.getIncludedResourceURLs(
			new String[] {"**/" + _FILE_NAME_WITH_SPACES}, _getBaseDirName());

		assertEquals(1, urls.size());

		String urlString = String.valueOf(urls.get(0));

		assertTrue(urlString.endsWith("Web%20Content%20Title-en_US.zip"));
	}

	private String _getBaseDirName() {
		return String.valueOf(_baseDir);
	}

	private String _getIncludedFileName(String fileName) throws Exception {
		List<String> filePaths = FileUtil.getIncludedFilePaths(
			new String[] {"**/" + fileName}, _getBaseDirName());

		assertEquals(1, filePaths.size());

		return FileUtil.getFileName(filePaths.get(0));
	}

	private static final String _FILE_NAME_WITH_SPACES =
		"Web Content Title-en_US.zip";

	private static final String[] _FILE_NAMES = {
		_FILE_NAME_WITH_SPACES, "WebContentTitle@!&#-en_US.zip", "日本語.txt",
		"Structure_WC_Structure_Name_1.json"
	};

	private Path _baseDir;

}