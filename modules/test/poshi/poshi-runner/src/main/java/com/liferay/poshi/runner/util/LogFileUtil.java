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