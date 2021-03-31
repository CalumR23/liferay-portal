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

package com.liferay.frontend.token.definition.internal;

import com.liferay.frontend.token.definition.FrontendToken;
import com.liferay.frontend.token.definition.FrontendTokenCategory;
import com.liferay.frontend.token.definition.FrontendTokenDefinition;
import com.liferay.frontend.token.definition.FrontendTokenMapping;
import com.liferay.frontend.token.definition.FrontendTokenSet;
import com.liferay.frontend.token.definition.internal.json.JSONLocalizer;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactory;
import com.liferay.portal.kernel.json.JSONObject;
<<<<<<< HEAD
import com.liferay.portal.kernel.resource.bundle.ResourceBundleLoader;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
=======
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.resource.bundle.ResourceBundleLoader;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469

/**
 * @author Iván Zaera
 */
public class FrontendTokenDefinitionImpl implements FrontendTokenDefinition {

	public FrontendTokenDefinitionImpl(
		JSONObject jsonObject, JSONFactory jsonFactory,
<<<<<<< HEAD
=======
		ResourceBundleLoader resourceBundleLoader, String themeId) {

		_jsonFactory = jsonFactory;
		_resourceBundleLoader = resourceBundleLoader;
		_themeId = themeId;

		_json = _jsonFactory.looseSerializeDeep(jsonObject);
		_jsonLocalizer = createJSONLocalizer(jsonObject);

		JSONArray frontendTokenCategoriesJSONArray = jsonObject.getJSONArray(
			"frontendTokenCategories");

		if (frontendTokenCategoriesJSONArray == null) {
			return;
		}

		frontendTokenizer(frontendTokenCategoriesJSONArray);
	}

	public FrontendTokenDefinitionImpl(
		String json, JSONFactory jsonFactory,
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
		ResourceBundleLoader resourceBundleLoader, String themeId) {

		_jsonFactory = jsonFactory;
		_resourceBundleLoader = resourceBundleLoader;
		_themeId = themeId;
<<<<<<< HEAD

		_jsonLocalizer = createJSONLocalizer(jsonObject);

		JSONArray frontendTokenCategoriesJSONArray = jsonObject.getJSONArray(
			"frontendTokenCategories");

		if (frontendTokenCategoriesJSONArray == null) {
			return;
		}

		for (int i = 0; i < frontendTokenCategoriesJSONArray.length(); i++) {
			FrontendTokenCategory frontendTokenCategory =
				new FrontendTokenCategoryImpl(
					this, frontendTokenCategoriesJSONArray.getJSONObject(i));
=======

		JSONObject jsonObject = null;

		try {
			jsonObject = jsonFactory.createJSONObject(json);
		}
		catch (JSONException jsonException) {
			_log.error(
				"Unable to parse JSON for theme " + _themeId, jsonException);
		}

		_jsonLocalizer = createJSONLocalizer(jsonObject);

		JSONArray frontendTokenCategoriesJSONArray = jsonObject.getJSONArray(
			"frontendTokenCategories");

		if (frontendTokenCategoriesJSONArray == null) {
			return;
		}

		frontendTokenizer(frontendTokenCategoriesJSONArray);
	}

	@Override
	public Collection<FrontendTokenCategory> getFrontendTokenCategories() {
		return _frontendTokenCategories;
	}

	@Override
	public Collection<FrontendTokenMapping> getFrontendTokenMappings() {
		return _frontendTokenMappings;
	}

	@Override
	public Collection<FrontendToken> getFrontendTokens() {
		return _frontendTokens;
	}

	@Override
	public Collection<FrontendTokenSet> getFrontendTokenSets() {
		return _frontendTokenSets;
	}

	@Override
	public String getJSON(Locale locale) {
		return _jsonLocalizer.getJSON(locale);
	}
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469

			_frontendTokenCategories.add(frontendTokenCategory);

<<<<<<< HEAD
			_frontendTokenMappings.addAll(
				frontendTokenCategory.getFrontendTokenMappings());
=======
	protected JSONLocalizer createJSONLocalizer(JSONObject jsonObject) {
		return new JSONLocalizer(
			_jsonFactory.looseSerializeDeep(jsonObject), _jsonFactory,
			_resourceBundleLoader, _themeId);
	}

	protected void frontendTokenizer(
		JSONArray frontendTokenCategoriesJSONArray) {

		for (int i = 0; i < frontendTokenCategoriesJSONArray.length(); i++) {
			FrontendTokenCategory frontendTokenCategory =
				new FrontendTokenCategoryImpl(
					this, frontendTokenCategoriesJSONArray.getJSONObject(i));

			_frontendTokenCategories.add(frontendTokenCategory);

			_frontendTokenMappings.addAll(
				frontendTokenCategory.getFrontendTokenMappings());

			_frontendTokens.addAll(frontendTokenCategory.getFrontendTokens());

			_frontendTokenSets.addAll(
				frontendTokenCategory.getFrontendTokenSets());
		}
	}

	protected String translateJSON(Locale locale) {
		if (_resourceBundleLoader == null) {
			return _json;
		}

		try {
			JSONObject jsonObject = _jsonFactory.createJSONObject(_json);
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469

			_frontendTokens.addAll(frontendTokenCategory.getFrontendTokens());

			_frontendTokenSets.addAll(
				frontendTokenCategory.getFrontendTokenSets());
		}
	}

	@Override
	public Collection<FrontendTokenCategory> getFrontendTokenCategories() {
		return _frontendTokenCategories;
	}

	@Override
	public Collection<FrontendTokenMapping> getFrontendTokenMappings() {
		return _frontendTokenMappings;
	}

	@Override
	public Collection<FrontendToken> getFrontendTokens() {
		return _frontendTokens;
	}

	@Override
	public Collection<FrontendTokenSet> getFrontendTokenSets() {
		return _frontendTokenSets;
	}

	@Override
	public String getJSON(Locale locale) {
		return _jsonLocalizer.getJSON(locale);
	}

	public String getThemeId() {
		return _themeId;
	}

	protected JSONLocalizer createJSONLocalizer(JSONObject jsonObject) {
		return new JSONLocalizer(
			_jsonFactory.looseSerializeDeep(jsonObject), _jsonFactory,
			_resourceBundleLoader, _themeId);
	}

	private final Collection<FrontendTokenCategory> _frontendTokenCategories =
		new ArrayList<>();
	private final Collection<FrontendTokenMapping> _frontendTokenMappings =
		new ArrayList<>();
	private final Collection<FrontendToken> _frontendTokens = new ArrayList<>();
	private final Collection<FrontendTokenSet> _frontendTokenSets =
		new ArrayList<>();
<<<<<<< HEAD
=======
	private final String _json;
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
	private final JSONFactory _jsonFactory;
	private final JSONLocalizer _jsonLocalizer;
	private final ResourceBundleLoader _resourceBundleLoader;
	private final String _themeId;

}