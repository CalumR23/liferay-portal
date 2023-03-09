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

package com.liferay.poshi.runner.util;

import com.liferay.poshi.core.util.PropsValues;
import com.liferay.poshi.runner.exception.PoshiRunnerWarningException;
import com.liferay.poshi.runner.selenium.LiferaySeleniumUtil;

/**
 * @author Calum Ragan
 */
public class LogFileUtil {

	public static void assertConsoleTextNotPresent(String text)
		throws Exception {

		Condition consoleTextNotPresentCondition =
			getConsoleTextNotPresentCondition(text);

		consoleTextNotPresentCondition.assertTrue();
	}

	public static void assertConsoleTextPresent(String text) throws Exception {
		Condition consoleTextPresentCondition = getConsoleTextPresentCondition(
			text);

		consoleTextPresentCondition.assertTrue();
	}

	public static void waitForConsoleTextNotPresent(String text)
		throws Exception {

		Condition consoleTextNotPresentCondition =
			getConsoleTextNotPresentCondition(text);

		consoleTextNotPresentCondition.waitFor();
	}

	public static void waitForConsoleTextPresent(String text) throws Exception {
		Condition consoleTextPresentCondition = getConsoleTextPresentCondition(
			text);

		consoleTextPresentCondition.waitFor();
	}

	public abstract static class Condition {

		public Condition() {
			this("");
		}

		public Condition(String message) {
			_message = message;
		}

		public void assertTrue() throws Exception {
			if (!evaluate()) {
				throw new Exception(_message);
			}
		}

		public abstract boolean evaluate() throws Exception;

		public void verify() throws Exception {
			if (!evaluate()) {
				throw new PoshiRunnerWarningException(
					"VERIFICATION_WARNING: " + _message);
			}
		}

		public void waitFor() throws Exception {
			waitFor("true");
		}

		public void waitFor(String throwException) throws Exception {
			int timeout = PropsValues.TIMEOUT_EXPLICIT_WAIT * 1000;
			int wait = 500;

			for (int millisecond = 0; millisecond < timeout;
				 millisecond += wait) {

				try {
					if (evaluate()) {
						return;
					}
				}
				catch (Exception exception) {
				}

				Thread.sleep(wait);
			}

			if ((throwException == null) ||
				Boolean.parseBoolean(throwException)) {

				assertTrue();
			}
		}

		private final String _message;

	}

	protected static Condition getConsoleTextNotPresentCondition(String text) {
		String message = "\"" + text + "\" is present in console";

		return new Condition(message) {

			public boolean evaluate() throws Exception {
				return !LiferaySeleniumUtil.isConsoleTextPresent(text);
			}

		};
	}

	protected static Condition getConsoleTextPresentCondition(String text) {
		String message = "\"" + text + "\" is not present in console";

		return new Condition(message) {

			@Override
			public boolean evaluate() throws Exception {
				return LiferaySeleniumUtil.isConsoleTextPresent(text);
			}

		};
	}

}