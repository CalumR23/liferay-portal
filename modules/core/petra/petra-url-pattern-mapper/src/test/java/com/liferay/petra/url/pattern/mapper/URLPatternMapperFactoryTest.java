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

package com.liferay.petra.url.pattern.mapper;

import com.liferay.petra.url.pattern.mapper.internal.DynamicSizeTrieURLPatternMapper;
import com.liferay.petra.url.pattern.mapper.internal.StaticSizeTrieURLPatternMapper;
<<<<<<< HEAD
import com.liferay.portal.test.rule.LiferayUnitTestRule;
=======
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
<<<<<<< HEAD
import org.junit.ClassRule;
import org.junit.Rule;
=======
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
import org.junit.Test;

/**
 * @author Arthur Chan
 */
public class URLPatternMapperFactoryTest {

<<<<<<< HEAD
	@ClassRule
	@Rule
	public static final LiferayUnitTestRule liferayUnitTestRule =
		LiferayUnitTestRule.INSTANCE;

=======
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
	@Test
	public void testCreate() {
		Map<String, String> map = new HashMap<>();

		for (int i = 0; i < 64; i++) {
			map.put("*.key" + i, "value" + i);
		}

		Assert.assertTrue(
			URLPatternMapperFactory.create(map) instanceof
				StaticSizeTrieURLPatternMapper<?>);

		map.put("*.key" + 64, "value" + 64);

		Assert.assertTrue(
			URLPatternMapperFactory.create(map) instanceof
				DynamicSizeTrieURLPatternMapper<?>);
	}

}