/**
 * SPDX-FileCopyrightText: (c) 2024 Liferay, Inc. https://liferay.com
 * SPDX-License-Identifier: LGPL-2.1-or-later OR LicenseRef-Liferay-DXP-EULA-2.0.0-2023-06
 */

package com.liferay.jenkins.results.parser.test.clazz.group;

import org.json.JSONObject;

/**
 * @author Calum Ragan
 */
public class QAWebsitesPlaywrightSegmentTestClassGroup
	extends PlaywrightSegmentTestClassGroup {

	protected QAWebsitesPlaywrightSegmentTestClassGroup(
		BatchTestClassGroup parentBatchTestClassGroup) {

		super(parentBatchTestClassGroup);
	}

	protected QAWebsitesPlaywrightSegmentTestClassGroup(
		BatchTestClassGroup parentBatchTestClassGroup, JSONObject jsonObject) {

		super(parentBatchTestClassGroup, jsonObject);
	}

}