/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.frontend.js.minifier.internal;

<<<<<<< HEAD
import com.liferay.portal.test.log.LogCapture;
import com.liferay.portal.test.log.LogEntry;
import com.liferay.portal.test.log.LoggerTestUtil;
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import java.util.List;
import java.util.logging.Level;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
=======
import com.liferay.portal.kernel.test.CaptureHandler;
import com.liferay.portal.kernel.test.JDKLoggerTestUtil;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.Assert;
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
import org.junit.Test;

/**
 * @author Iván Zaera Avellón
 */
public class GoogleJavascriptMinifierTest {

<<<<<<< HEAD
	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

=======
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
	@Test
	public void testMinifierCode() {
		GoogleJavaScriptMinifier googleJavaScriptMinifier =
			new GoogleJavaScriptMinifier();

		String code = "function(){ var invalidFunctionExpression; }";

<<<<<<< HEAD
		try (LogCapture logCapture = LoggerTestUtil.configureJDKLogger(
				GoogleJavaScriptMinifier.class.getName(), Level.SEVERE)) {
=======
		try (CaptureHandler captureHandler =
				JDKLoggerTestUtil.configureJDKLogger(
					GoogleJavaScriptMinifier.class.getName(), Level.SEVERE)) {
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469

			String minifiedJS = googleJavaScriptMinifier.compress("test", code);

			Assert.assertEquals(44, minifiedJS.length());

<<<<<<< HEAD
			List<LogEntry> logEntries = logCapture.getLogEntries();

			Assert.assertEquals(logEntries.toString(), 2, logEntries.size());

			LogEntry logEntry = logEntries.get(0);
=======
			List<LogRecord> logRecords = captureHandler.getLogRecords();

			Assert.assertEquals(logRecords.toString(), 2, logRecords.size());

			LogRecord logRecord = logRecords.get(0);
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469

			Assert.assertEquals(
				"(test:1): Parse error. 'identifier' expected " +
					"[JSC_PARSE_ERROR]",
<<<<<<< HEAD
				logEntry.getMessage());

			logEntry = logEntries.get(1);

			Assert.assertEquals(
				"(test): 1 error(s), 0 warning(s)", logEntry.getMessage());

			logCapture.resetPriority(String.valueOf(Level.SEVERE));
=======
				logRecord.getMessage());

			logRecord = logRecords.get(1);

			Assert.assertEquals(
				"(test): 1 error(s), 0 warning(s)", logRecord.getMessage());

			captureHandler.resetLogLevel(Level.SEVERE);
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
		}
	}

	@Test
	public void testMinifierSpaces() {
		GoogleJavaScriptMinifier googleJavaScriptMinifier =
			new GoogleJavaScriptMinifier();

		String code = " \t\r\n";

		String minifiedJS = googleJavaScriptMinifier.compress("test", code);

		Assert.assertEquals(0, minifiedJS.length());
	}

}