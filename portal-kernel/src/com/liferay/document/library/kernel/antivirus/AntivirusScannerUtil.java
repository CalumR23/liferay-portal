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

package com.liferay.document.library.kernel.antivirus;

import com.liferay.portal.kernel.util.ServiceProxyFactory;

import java.io.File;

/**
 * @author Michael C. Han
 * @author Raymond Augé
 */
public class AntivirusScannerUtil {

<<<<<<< HEAD
=======
	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public static AntivirusScanner getAntivirusScanner() {
		return _antivirusScanner;
	}

>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
	public static boolean isActive() {
		return _antivirusScanner.isActive();
	}

	public static void scan(byte[] bytes) throws AntivirusScannerException {
		if (isActive()) {
			_antivirusScanner.scan(bytes);
		}
	}

	public static void scan(File file) throws AntivirusScannerException {
		if (isActive()) {
			_antivirusScanner.scan(file);
		}
	}

<<<<<<< HEAD
	private static volatile AntivirusScanner _antivirusScanner =
		ServiceProxyFactory.newServiceTrackedInstance(
			AntivirusScanner.class, AntivirusScannerUtil.class,
			"_antivirusScanner", false);
=======
	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public void setAntivirusScanner(AntivirusScanner antivirusScanner) {
		_antivirusScanner = antivirusScanner;
	}

	private static AntivirusScanner _antivirusScanner;
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469

}