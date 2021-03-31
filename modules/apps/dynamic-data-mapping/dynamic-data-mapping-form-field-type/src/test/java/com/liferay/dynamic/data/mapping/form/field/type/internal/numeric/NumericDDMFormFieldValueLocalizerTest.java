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

package com.liferay.dynamic.data.mapping.form.field.type.internal.numeric;

import com.liferay.portal.kernel.util.LocaleUtil;
<<<<<<< HEAD
import com.liferay.portal.test.rule.LiferayUnitTestRule;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
=======

import org.junit.Assert;
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
import org.junit.Test;

/**
 * @author Marcos Martins
 */
public class NumericDDMFormFieldValueLocalizerTest {

<<<<<<< HEAD
	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

=======
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
	@Test
	public void testLocalize() {
		String localizedValue = _numericDDMFormFieldValueLocalizer.localize(
			"1000.95", LocaleUtil.US);

		Assert.assertEquals("1000.95", localizedValue);
	}

	@Test
	public void testLocalizeWithEditingValueEndingWithPeriod() {
		_numericDDMFormFieldValueLocalizer.setEditingFieldValue(true);

		String localizedValue = _numericDDMFormFieldValueLocalizer.localize(
			"1000.", LocaleUtil.US);

		Assert.assertEquals("1000.", localizedValue);
	}

	@Test
	public void testLocalizeWithNotEditingValueEndingWithPeriod() {
		_numericDDMFormFieldValueLocalizer.setEditingFieldValue(false);

		String localizedValue = _numericDDMFormFieldValueLocalizer.localize(
			"1000.", LocaleUtil.US);

		Assert.assertEquals("1000", localizedValue);
	}

	private final NumericDDMFormFieldValueLocalizer
		_numericDDMFormFieldValueLocalizer =
			new NumericDDMFormFieldValueLocalizer();

}