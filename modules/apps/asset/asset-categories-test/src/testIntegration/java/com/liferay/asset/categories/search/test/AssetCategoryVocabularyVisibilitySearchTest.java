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

package com.liferay.asset.categories.search.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.asset.kernel.model.AssetCategory;
import com.liferay.asset.kernel.model.AssetCategoryConstants;
import com.liferay.asset.kernel.model.AssetVocabulary;
import com.liferay.asset.kernel.model.AssetVocabularyConstants;
import com.liferay.asset.kernel.service.AssetCategoryService;
import com.liferay.asset.kernel.service.AssetVocabularyLocalService;
import com.liferay.journal.model.JournalArticle;
import com.liferay.journal.service.JournalArticleLocalService;
import com.liferay.journal.test.util.search.JournalArticleBlueprint;
import com.liferay.journal.test.util.search.JournalArticleContent;
import com.liferay.journal.test.util.search.JournalArticleSearchFixture;
import com.liferay.journal.test.util.search.JournalArticleTitle;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.Hits;
import com.liferay.portal.kernel.search.Indexer;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.SearchException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.rule.DeleteAfterTestRun;
import com.liferay.portal.kernel.test.rule.Sync;
import com.liferay.portal.kernel.test.rule.SynchronousDestinationTestRule;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.LocaleThreadLocal;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.workflow.WorkflowThreadLocal;
import com.liferay.portal.search.localization.SearchLocalizationHelper;
import com.liferay.portal.search.test.util.DocumentsAssert;
import com.liferay.portal.search.test.util.SearchContextTestUtil;
import com.liferay.portal.search.test.util.SearchTestRule;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.portal.test.rule.PermissionCheckerMethodTestRule;
import com.liferay.users.admin.test.util.search.GroupBlueprint;
import com.liferay.users.admin.test.util.search.GroupSearchFixture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Adam Brandizzi
 */
@RunWith(Arquillian.class)
@Sync
public class AssetCategoryVocabularyVisibilitySearchTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new AggregateTestRule(
			new LiferayIntegrationTestRule(),
			PermissionCheckerMethodTestRule.INSTANCE,
			SynchronousDestinationTestRule.INSTANCE);

	@Before
	public void setUp() throws Exception {
		WorkflowThreadLocal.setEnabled(false);

		_groupSearchFixture = new GroupSearchFixture();

		_group = _groupSearchFixture.addGroup(new GroupBlueprint());

		_journalArticleSearchFixture = new JournalArticleSearchFixture(
			_journalArticleLocalService);
	}

	@Test
	public void testVisibitityTypeInternal() throws Exception {
<<<<<<< HEAD
		AssetCategory assetCategory = _addAssetCategory(
=======
		AssetCategory assetCategory = addAssetCategory(
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
			AssetVocabularyConstants.VISIBILITY_TYPE_INTERNAL);

		String keyword = "example";

<<<<<<< HEAD
		_addJournalArticle(assetCategory, keyword);

		_assertSearchInternalFields(
			keyword, _getAssetCategoryIds(assetCategory),
			_getAssetCategoryTitles(assetCategory));
		_assertSearchPublicFields(
			keyword, Collections.emptyList(), Collections.emptyList());
=======
		addJournalArticle(assetCategory, keyword);

		assertSearchPublicFields(
			keyword, Collections.emptyList(), Collections.emptyList());

		assertSearchInternalFields(
			keyword, getAssetCategoryIds(assetCategory),
			getAssetCategoryTitles(assetCategory));
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
	}

	@Test
	public void testVisibitityTypePublic() throws Exception {
<<<<<<< HEAD
		AssetCategory assetCategory = _addAssetCategory(
=======
		AssetCategory assetCategory = addAssetCategory(
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
			AssetVocabularyConstants.VISIBILITY_TYPE_PUBLIC);

		String keyword = "example";

<<<<<<< HEAD
		_addJournalArticle(assetCategory, keyword);

		_assertSearchInternalFields(
			keyword, Collections.emptyList(), Collections.emptyList());
		_assertSearchPublicFields(
			keyword, _getAssetCategoryIds(assetCategory),
			_getAssetCategoryTitles(assetCategory));
=======
		addJournalArticle(assetCategory, keyword);

		assertSearchPublicFields(
			keyword, getAssetCategoryIds(assetCategory),
			getAssetCategoryTitles(assetCategory));

		assertSearchInternalFields(
			keyword, Collections.emptyList(), Collections.emptyList());
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
	}

	@Rule
	public SearchTestRule searchTestRule = new SearchTestRule();

<<<<<<< HEAD
	private AssetCategory _addAssetCategory(int visibilityType)
=======
	protected AssetCategory addAssetCategory(int visibilityType)
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
		throws Exception {

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(_group.getGroupId());

		Map<Locale, String> titleMap = HashMapBuilder.put(
			LocaleUtil.US, RandomTestUtil.randomString()
		).build();

		Locale previousLocale = LocaleThreadLocal.getSiteDefaultLocale();

		LocaleThreadLocal.setSiteDefaultLocale(LocaleUtil.US);

		try {
<<<<<<< HEAD
			AssetVocabulary assetVocabulary = _addAssetVocabulary(
				visibilityType);

			AssetCategory assetCategory = _assetCategoryService.addCategory(
=======
			AssetVocabulary assetVocabulary = addAssetVocabulary(
				visibilityType);

			AssetCategory assetCategory = assetCategoryService.addCategory(
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
				_group.getGroupId(),
				AssetCategoryConstants.DEFAULT_PARENT_CATEGORY_ID, titleMap,
				new HashMap<>(), assetVocabulary.getVocabularyId(),
				new String[0], serviceContext);

			_assetCategories.add(assetCategory);

			return assetCategory;
		}
		finally {
			LocaleThreadLocal.setSiteDefaultLocale(previousLocale);
		}
	}

<<<<<<< HEAD
	private AssetVocabulary _addAssetVocabulary(int visibilityType)
		throws Exception {

		AssetVocabulary assetVocabulary =
			_assetVocabularyLocalService.addDefaultVocabulary(
=======
	protected AssetVocabulary addAssetVocabulary(int visibilityType)
		throws Exception {

		AssetVocabulary assetVocabulary =
			assetVocabularyLocalService.addDefaultVocabulary(
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
				_group.getGroupId());

		assetVocabulary.setTitle(RandomTestUtil.randomString());
		assetVocabulary.setVisibilityType(visibilityType);

<<<<<<< HEAD
		assetVocabulary = _assetVocabularyLocalService.updateAssetVocabulary(
=======
		assetVocabulary = assetVocabularyLocalService.updateAssetVocabulary(
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
			assetVocabulary);

		_assetVocabularies.add(assetVocabulary);

		return assetVocabulary;
	}

<<<<<<< HEAD
	private void _addJournalArticle(AssetCategory assetCategory, String title) {
=======
	protected void addJournalArticle(
		AssetCategory assetCategory, String title) {

>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
		_journalArticleSearchFixture.addArticle(
			new JournalArticleBlueprint() {
				{
					setAssetCategoryIds(
						new long[] {assetCategory.getCategoryId()});
					setGroupId(_group.getGroupId());
					setJournalArticleContent(
						new JournalArticleContent() {
							{
								put(LocaleUtil.US, title);

								setDefaultLocale(LocaleUtil.US);
								setName("content");
							}
						});
					setJournalArticleTitle(
						new JournalArticleTitle() {
							{
								put(
									LocaleUtil.US,
									RandomTestUtil.randomString());
							}
						});
				}
			});
	}

<<<<<<< HEAD
	private void _assertSearch(
=======
	protected void assertSearch(
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
			String keyword, String assetCategoryIdsFieldName,
			List<Long> expectedAssetCategoryIds,
			String assetCategoryTitlesFieldName,
			List<String> expectedAssetCategoryTitles)
		throws Exception, SearchException {

		SearchContext searchContext = SearchContextTestUtil.getSearchContext(
			_group.getGroupId());

		searchContext.setKeywords(keyword);

		QueryConfig queryConfig = searchContext.getQueryConfig();

		queryConfig.addSelectedFieldNames(
			assetCategoryIdsFieldName, Field.ASSET_CATEGORY_TITLES);

<<<<<<< HEAD
		Hits hits = _indexer.search(searchContext);

		DocumentsAssert.assertValuesIgnoreRelevance(
			(String)searchContext.getAttribute("queryString"), hits.getDocs(),
			assetCategoryIdsFieldName, _asStringList(expectedAssetCategoryIds));
=======
		Hits hits = indexer.search(searchContext);

		DocumentsAssert.assertValuesIgnoreRelevance(
			(String)searchContext.getAttribute("queryString"), hits.getDocs(),
			assetCategoryIdsFieldName, asStringList(expectedAssetCategoryIds));
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469

		DocumentsAssert.assertValuesIgnoreRelevance(
			(String)searchContext.getAttribute("queryString"), hits.getDocs(),
			assetCategoryTitlesFieldName, expectedAssetCategoryTitles);
	}

<<<<<<< HEAD
	private void _assertSearchInternalFields(
=======
	protected void assertSearchInternalFields(
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
			String keyword, List<Long> assetCategoryIds,
			List<String> assetCategoryTitles)
		throws Exception, SearchException {

<<<<<<< HEAD
		_assertSearch(
=======
		assertSearch(
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
			keyword, Field.ASSET_INTERNAL_CATEGORY_IDS, assetCategoryIds,
			Field.getLocalizedName(
				LocaleUtil.US, Field.ASSET_INTERNAL_CATEGORY_TITLES),
			assetCategoryTitles);
	}

<<<<<<< HEAD
	private void _assertSearchPublicFields(
=======
	protected void assertSearchPublicFields(
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
			String keyword, List<Long> assetCategoryIds,
			List<String> assetCategoryTitles)
		throws Exception, SearchException {

<<<<<<< HEAD
		_assertSearch(
=======
		assertSearch(
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
			keyword, Field.ASSET_CATEGORY_IDS, assetCategoryIds,
			Field.getLocalizedName(LocaleUtil.US, Field.ASSET_CATEGORY_TITLES),
			assetCategoryTitles);
	}

<<<<<<< HEAD
	private List<String> _asStringList(List<Long> expectedAssetCategoryIds) {
=======
	protected List<String> asStringList(List<Long> expectedAssetCategoryIds) {
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
		Stream<Long> stream = expectedAssetCategoryIds.stream();

		return stream.map(
			String::valueOf
		).collect(
			Collectors.toList()
		);
	}

<<<<<<< HEAD
	private List<Long> _getAssetCategoryIds(AssetCategory assetCategory) {
		return Arrays.asList(assetCategory.getCategoryId());
	}

	private List<String> _getAssetCategoryTitles(AssetCategory assetCategory) {
=======
	protected List<Long> getAssetCategoryIds(AssetCategory assetCategory) {
		return Arrays.asList(assetCategory.getCategoryId());
	}

	protected List<String> getAssetCategoryTitles(AssetCategory assetCategory) {
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
		Map<Locale, String> titleMap = assetCategory.getTitleMap();

		Collection<String> titles = titleMap.values();

		Stream<String> stream = titles.stream();

		return stream.map(
			String::toLowerCase
		).collect(
			Collectors.toList()
		);
	}

<<<<<<< HEAD
	@Inject
	private static AssetCategoryService _assetCategoryService;

	@Inject
	private static AssetVocabularyLocalService _assetVocabularyLocalService;
=======
	protected SearchContext getSearchContext(
		String keywords, Locale locale, Group group) {

		SearchContext searchContext = new SearchContext();

		searchContext.setCompanyId(group.getCompanyId());
		searchContext.setGroupIds(new long[] {group.getGroupId()});
		searchContext.setKeywords(keywords);
		searchContext.setLocale(locale);

		return searchContext;
	}

	@Inject
	protected static AssetCategoryService assetCategoryService;

	@Inject
	protected static AssetVocabularyLocalService assetVocabularyLocalService;
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469

	@Inject(
		filter = "component.name=com.liferay.journal.internal.search.JournalArticleIndexer"
	)
<<<<<<< HEAD
	private static Indexer<JournalArticle> _indexer;
=======
	protected static Indexer<JournalArticle> indexer;
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469

	@DeleteAfterTestRun
	private List<AssetCategory> _assetCategories = new ArrayList<>();

	@DeleteAfterTestRun
	private List<AssetVocabulary> _assetVocabularies = new ArrayList<>();

	@DeleteAfterTestRun
	private Group _group;

	private GroupSearchFixture _groupSearchFixture;

	@Inject
	private JournalArticleLocalService _journalArticleLocalService;

	private JournalArticleSearchFixture _journalArticleSearchFixture;

	@Inject
	private SearchLocalizationHelper _searchLocalizationHelper;

}