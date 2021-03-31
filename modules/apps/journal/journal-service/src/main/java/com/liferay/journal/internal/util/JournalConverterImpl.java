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

package com.liferay.journal.internal.util;

import com.liferay.dynamic.data.mapping.form.field.type.constants.DDMFormFieldTypeConstants;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormFieldOptions;
import com.liferay.dynamic.data.mapping.model.DDMStructure;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.storage.Field;
import com.liferay.dynamic.data.mapping.storage.Fields;
import com.liferay.dynamic.data.mapping.storage.constants.FieldConstants;
import com.liferay.dynamic.data.mapping.util.DDM;
import com.liferay.dynamic.data.mapping.util.DDMFieldsCounter;
import com.liferay.journal.exception.ArticleContentException;
import com.liferay.journal.util.JournalConverter;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.petra.xml.XMLUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
<<<<<<< HEAD
=======
import com.liferay.portal.kernel.json.JSONObject;
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
<<<<<<< HEAD
=======
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.service.LayoutLocalService;
import com.liferay.portal.kernel.util.AggregateResourceBundle;
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.xml.Document;
import com.liferay.portal.kernel.xml.DocumentException;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.kernel.xml.SAXReaderUtil;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.osgi.service.component.annotations.Component;

/**
 * @author Marcellus Tavares
 * @author Bruno Basto
 */
@Component(immediate = true, service = JournalConverter.class)
public class JournalConverterImpl implements JournalConverter {

	@Override
	public String getContent(
			DDMStructure ddmStructure, Fields ddmFields, long groupId)
		throws Exception {

		Document document = SAXReaderUtil.createDocument();

		Element rootElement = document.addElement("root");

		rootElement.addAttribute(
			"available-locales", getAvailableLocales(ddmFields));

		Locale defaultLocale = ddmFields.getDefaultLocale();

		if (!LanguageUtil.isAvailableLocale(groupId, defaultLocale)) {
			defaultLocale = LocaleUtil.getSiteDefault();
		}

		rootElement.addAttribute(
			"default-locale", LocaleUtil.toLanguageId(defaultLocale));

		rootElement.addAttribute("version", "1.0");

		DDMFieldsCounter ddmFieldsCounter = new DDMFieldsCounter();

		DDMForm ddmForm = ddmStructure.getDDMForm();

		for (DDMFormField ddmFormField : ddmForm.getDDMFormFields()) {
			updateDynamicElementElement(
				ddmFields, ddmFieldsCounter, ddmFormField, rootElement, -1);
		}

		try {
			String content = XMLUtil.stripInvalidChars(document.asXML());

			return XMLUtil.formatXML(content);
		}
		catch (Exception exception) {
			throw new ArticleContentException(
				"Unable to read content with an XML parser", exception);
		}
	}

	@Override
	public Fields getDDMFields(DDMStructure ddmStructure, Document document)
		throws PortalException {

		Fields ddmFields = new Fields();

		ddmFields.put(
			new Field(
				ddmStructure.getStructureId(), DDM.FIELDS_DISPLAY_NAME,
				StringPool.BLANK));

		DDMForm ddmForm = ddmStructure.getDDMForm();

		Element rootElement = document.getRootElement();

		String[] availableLanguageIds = StringUtil.split(
			rootElement.attributeValue("available-locales"));
		String defaultLanguageId = rootElement.attributeValue("default-locale");

		Map<String, List<Element>> dynamicElementElementsMap =
			_getDynamicElements(rootElement);

		for (DDMFormField ddmFormField : ddmForm.getDDMFormFields()) {
			addDDMFields(
				availableLanguageIds, defaultLanguageId, ddmFields,
				ddmFormField, ddmStructure, dynamicElementElementsMap);
		}

		return ddmFields;
	}

	@Override
	public Fields getDDMFields(DDMStructure ddmStructure, String content)
		throws PortalException {

		try {
			return getDDMFields(ddmStructure, SAXReaderUtil.read(content));
		}
		catch (DocumentException documentException) {
			throw new PortalException(documentException);
		}
	}

	protected void addDDMFields(
			String[] availableLanguageIds, String defaultLanguageId,
			Fields ddmFields, DDMFormField ddmFormField,
			DDMStructure ddmStructure,
			Map<String, List<Element>> dynamicElementElementsMap)
		throws PortalException {

		List<Element> dynamicElementElements = dynamicElementElementsMap.get(
			ddmFormField.getName());

		if (dynamicElementElements == null) {
			if (Objects.equals(
					ddmFormField.getType(),
					DDMFormFieldTypeConstants.FIELDSET)) {

				updateFieldsDisplay(
					ddmFields, ddmFormField.getName(),
					String.valueOf(ddmStructure.getStructureId()));
			}

			_addNestedDDMFields(
				availableLanguageIds, defaultLanguageId, ddmFields,
				ddmFormField, ddmStructure, dynamicElementElementsMap);

			return;
		}

		for (Element dynamicElementElement : dynamicElementElements) {
			if (!ddmFormField.isTransient()) {
				Field ddmField = getField(
					dynamicElementElement, ddmStructure, availableLanguageIds,
					defaultLanguageId);

				String fieldName = ddmField.getName();

				Field existingDDMField = ddmFields.get(fieldName);

				if (existingDDMField != null) {
					for (Locale locale : ddmField.getAvailableLocales()) {
						existingDDMField.addValues(
							locale, ddmField.getValues(locale));
					}
				}
				else {
					ddmFields.put(ddmField);
				}
			}

			updateFieldsDisplay(
				ddmFields, ddmFormField.getName(),
				dynamicElementElement.attributeValue("instance-id"));

			_addNestedDDMFields(
				availableLanguageIds, defaultLanguageId, ddmFields,
				ddmFormField, ddmStructure,
				_getDynamicElements(dynamicElementElement));
		}
	}

	protected void addMissingFieldValues(
		Field ddmField, String defaultLanguageId,
		Set<String> missingLanguageIds) {

		if (missingLanguageIds.isEmpty()) {
			return;
		}

		Locale defaultLocale = LocaleUtil.fromLanguageId(defaultLanguageId);

		Serializable fieldValue = ddmField.getValue(defaultLocale);

		for (String missingLanguageId : missingLanguageIds) {
			Locale missingLocale = LocaleUtil.fromLanguageId(missingLanguageId);

			ddmField.setValue(missingLocale, fieldValue);
		}
	}

	protected int countFieldRepetition(
			Fields ddmFields, String fieldName, String parentFieldName,
			int parentOffset)
		throws Exception {

		Field fieldsDisplayField = ddmFields.get(DDM.FIELDS_DISPLAY_NAME);

		String[] fieldsDisplayValues = getDDMFieldsDisplayValues(
			fieldsDisplayField);

		int offset = -1;

		int repetitions = 0;

		for (String fieldDisplayName : fieldsDisplayValues) {
			if (offset > parentOffset) {
				break;
			}

			if (fieldDisplayName.equals(parentFieldName)) {
				offset++;
			}

			if (fieldDisplayName.equals(fieldName) &&
				(offset == parentOffset)) {

				repetitions++;
			}
		}

		return repetitions;
	}

	protected String getAvailableLocales(Fields ddmFields) {
		Set<Locale> availableLocales = ddmFields.getAvailableLocales();

		Locale[] availableLocalesArray = new Locale[availableLocales.size()];

		availableLocalesArray = availableLocales.toArray(availableLocalesArray);

		String[] languageIds = LocaleUtil.toLanguageIds(availableLocalesArray);

		return StringUtil.merge(languageIds);
	}

	protected String[] getDDMFieldsDisplayValues(Field ddmFieldsDisplayField)
		throws PortalException {

		try {
			DDMStructure ddmStructure = ddmFieldsDisplayField.getDDMStructure();

			List<String> fieldsDisplayValues = new ArrayList<>();

			String[] values = splitFieldsDisplayValue(ddmFieldsDisplayField);

			for (String value : values) {
				String fieldName = StringUtil.extractFirst(
					value, DDM.INSTANCE_SEPARATOR);

				if (ddmStructure.hasField(fieldName)) {
					fieldsDisplayValues.add(fieldName);
				}
			}

			return fieldsDisplayValues.toArray(new String[0]);
		}
		catch (Exception exception) {
			throw new PortalException(exception);
		}
	}

	protected Field getField(
			Element dynamicElementElement, DDMStructure ddmStructure,
			String[] availableLanguageIds, String defaultLanguageId)
		throws PortalException {

		Field ddmField = new Field();

		ddmField.setDDMStructureId(ddmStructure.getStructureId());

		Locale defaultLocale = null;

		if (defaultLanguageId == null) {
			defaultLocale = LocaleUtil.getSiteDefault();
		}
		else {
			defaultLocale = LocaleUtil.fromLanguageId(defaultLanguageId);
		}

		ddmField.setDefaultLocale(defaultLocale);

		String name = dynamicElementElement.attributeValue("name");

		if (!GetterUtil.getBoolean(
				ddmStructure.getFieldProperty(name, "localizable"))) {

			availableLanguageIds = StringPool.EMPTY_ARRAY;
		}

		ddmField.setName(name);

		DDMFormField ddmFormField = ddmStructure.getDDMFormField(name);

		Set<String> missingLanguageIds = SetUtil.fromArray(
			availableLanguageIds);

		missingLanguageIds.remove(defaultLanguageId);

		List<Element> dynamicContentElements = dynamicElementElement.elements(
			"dynamic-content");

		for (Element dynamicContentElement : dynamicContentElements) {
			Locale locale = defaultLocale;

			String languageId = dynamicContentElement.attributeValue(
				"language-id");

			if (Validator.isNotNull(languageId)) {
				locale = LocaleUtil.fromLanguageId(languageId, true, false);

				if (locale == null) {
					continue;
				}

				missingLanguageIds.remove(languageId);
			}

			Serializable serializable = getFieldValue(
				ddmFormField, dynamicContentElement);

			ddmField.addValue(locale, serializable);
		}

		addMissingFieldValues(ddmField, defaultLanguageId, missingLanguageIds);

		return ddmField;
	}

	protected String getFieldInstanceId(
		Fields ddmFields, String fieldName, int index) {

		Field fieldsDisplayField = ddmFields.get(DDM.FIELDS_DISPLAY_NAME);

		String prefix = fieldName.concat(DDM.INSTANCE_SEPARATOR);

		String[] fieldsDisplayValues = StringUtil.split(
			(String)fieldsDisplayField.getValue());

		for (String fieldsDisplayValue : fieldsDisplayValues) {
			if (fieldsDisplayValue.startsWith(prefix)) {
				index--;

				if (index < 0) {
					return StringUtil.extractLast(
						fieldsDisplayValue, DDM.INSTANCE_SEPARATOR);
				}
			}
		}

		return null;
	}

	protected Serializable getFieldValue(
<<<<<<< HEAD
		DDMFormField ddmFormField, Element dynamicContentElement) {

		if (Objects.equals(
				DDMFormFieldTypeConstants.CHECKBOX_MULTIPLE,
				ddmFormField.getType())) {

			return _getCheckboxMultipleValue(
				ddmFormField, dynamicContentElement);
		}

		if (Objects.equals(
				DDMFormFieldTypeConstants.SELECT, ddmFormField.getType())) {

			return _getSelectValue(dynamicContentElement);
		}

		return FieldConstants.getSerializable(
			ddmFormField.getDataType(), dynamicContentElement.getText());
=======
		String dataType, String type, Element dynamicContentElement,
		Locale defaultLocale) {

		if (Objects.equals(DDMFormFieldType.DOCUMENT_LIBRARY, type) ||
			Objects.equals(DDMFormFieldType.IMAGE, type)) {

			return _getFileEntryValue(defaultLocale, dynamicContentElement);
		}

		if (Objects.equals(DDMFormFieldType.JOURNAL_ARTICLE, type)) {
			return _getJournalArticleValue(
				defaultLocale, dynamicContentElement);
		}

		if (Objects.equals(DDMFormFieldType.LINK_TO_PAGE, type)) {
			return _getLinkToLayoutValue(defaultLocale, dynamicContentElement);
		}

		if (Objects.equals(DDMFormFieldType.SELECT, type)) {
			return _getSelectValue(dynamicContentElement);
		}

		return FieldConstants.getSerializable(
			dataType, dynamicContentElement.getText());
	}

	protected void getJournalMetadataElement(Element metadataElement) {
		removeAttribute(metadataElement, "locale");

		Element dynamicElementElement = metadataElement.getParent();

		// Required

		boolean required = GetterUtil.getBoolean(
			dynamicElementElement.attributeValue("required"));

		addMetadataEntry(metadataElement, "required", String.valueOf(required));

		// Tooltip

		Element tipElement = fetchMetadataEntry(metadataElement, "name", "tip");

		if (tipElement != null) {
			tipElement.addAttribute("name", "instructions");

			addMetadataEntry(metadataElement, "displayAsTooltip", "true");
		}
	}

	protected void removeAttribute(Element element, String attributeName) {
		Attribute attribute = element.attribute(attributeName);

		if (attribute == null) {
			return;
		}

		element.remove(attribute);
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
	}

	protected String[] splitFieldsDisplayValue(Field fieldsDisplayField) {
		String value = (String)fieldsDisplayField.getValue();

		return StringUtil.split(value);
	}

	protected void updateContentDynamicElement(
		int count, DDMFormField ddmFormField, Element dynamicElementElement,
		Field field) {

		for (Locale locale : field.getAvailableLocales()) {
			Element dynamicContentElement = dynamicElementElement.addElement(
				"dynamic-content");

			dynamicContentElement.addAttribute(
				"language-id", LocaleUtil.toLanguageId(locale));

			Serializable fieldValue = field.getValue(locale, count);

			if (fieldValue == null) {
				fieldValue = field.getValue(field.getDefaultLocale(), count);
			}

			String valueString = String.valueOf(fieldValue);

			updateDynamicContentValue(
				ddmFormField, dynamicContentElement, ddmFormField.getName(),
				ddmFormField.getType(), valueString.trim(),
				ddmFormField.isMultiple());
		}
	}

	protected void updateDynamicContentValue(
<<<<<<< HEAD
		DDMFormField ddmFormField, Element dynamicContentElement,
		String fieldName, String fieldType, String fieldValue,
		boolean multiple) {

		if (Objects.equals(
				DDMFormFieldTypeConstants.CHECKBOX_MULTIPLE, fieldType)) {

			try {
				DDMFormFieldOptions ddmFormFieldOptions =
					(DDMFormFieldOptions)ddmFormField.getProperty("options");

				Map<String, LocalizedValue> options =
					ddmFormFieldOptions.getOptions();

				if (options.size() > 1) {
					dynamicContentElement.addCDATA(fieldValue);

					return;
				}

				JSONArray fieldValueJSONArray = JSONFactoryUtil.createJSONArray(
					fieldValue);

				if (fieldValueJSONArray.length() == 1) {
					fieldValue = Boolean.TRUE.toString();
				}
				else {
					fieldValue = StringPool.BLANK;
				}
			}
			catch (PortalException portalException) {
				if (_log.isDebugEnabled()) {
					_log.debug(
						"Unable to get dynamic data mapping form field for " +
							fieldName,
						portalException);
				}
=======
		Element dynamicContentElement, String fieldType, boolean multiple,
		String fieldValue) {

		if (DDMFormFieldType.CHECKBOX.equals(fieldType)) {
			if (fieldValue.equals(Boolean.FALSE.toString())) {
				fieldValue = StringPool.BLANK;
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
			}

			dynamicContentElement.addCDATA(fieldValue);
		}
<<<<<<< HEAD
		else if (Objects.equals(DDMFormFieldTypeConstants.SELECT, fieldType) &&
=======
		else if (DDMFormFieldType.SELECT.equals(fieldType) &&
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
				 Validator.isNotNull(fieldValue)) {

			JSONArray jsonArray = null;

			try {
				jsonArray = JSONFactoryUtil.createJSONArray(fieldValue);
			}
			catch (JSONException jsonException) {
				if (_log.isDebugEnabled()) {
					_log.debug("Unable to parse object", jsonException);
				}

				return;
			}

			if (multiple) {
				for (int i = 0; i < jsonArray.length(); i++) {
					Element optionElement = dynamicContentElement.addElement(
						"option");

					optionElement.addCDATA(jsonArray.getString(i));
				}
			}
			else {
				dynamicContentElement.addCDATA(jsonArray.getString(0));
			}
		}
		else {
			dynamicContentElement.addCDATA(fieldValue);
		}
	}

	protected void updateDynamicElementElement(
			Fields ddmFields, DDMFieldsCounter ddmFieldsCounter,
			DDMFormField ddmFormField, Element dynamicElementElement,
			int parentOffset)
		throws Exception {

		String fieldName = ddmFormField.getName();

		int count = ddmFieldsCounter.get(fieldName);

		int repetitions = countFieldRepetition(
			ddmFields, fieldName, dynamicElementElement.attributeValue("name"),
			parentOffset);

		for (int i = 0; i < repetitions; i++) {
			Element childDynamicElementElement =
				dynamicElementElement.addElement("dynamic-element");

			childDynamicElementElement.addAttribute(
				"index-type", ddmFormField.getIndexType());

			childDynamicElementElement.addAttribute(
				"instance-id",
				getFieldInstanceId(ddmFields, fieldName, count + i));

			childDynamicElementElement.addAttribute("name", fieldName);
			childDynamicElementElement.addAttribute(
				"type", ddmFormField.getType());

			List<DDMFormField> nestedDDMFormFields =
				ddmFormField.getNestedDDMFormFields();

			Field field = ddmFields.get(fieldName);

			if (!Objects.equals(
					ddmFormField.getType(),
					DDMFormFieldTypeConstants.FIELDSET) &&
				!ddmFormField.isTransient() && (field != null)) {

				updateContentDynamicElement(
					ddmFieldsCounter.get(fieldName), ddmFormField,
					childDynamicElementElement, field);
			}
			else if (ListUtil.isNotEmpty(nestedDDMFormFields)) {
				for (DDMFormField nestedDDMFormField : nestedDDMFormFields) {
					updateDynamicElementElement(
						ddmFields, ddmFieldsCounter, nestedDDMFormField,
						childDynamicElementElement, count + i);
				}
			}

			ddmFieldsCounter.incrementKey(fieldName);
		}
	}

	protected void updateFieldsDisplay(
		Fields ddmFields, String fieldName, String instanceId) {

		if (Validator.isNull(instanceId)) {
			instanceId = StringUtil.randomString();
		}

		String fieldsDisplayValue = StringBundler.concat(
			fieldName, DDM.INSTANCE_SEPARATOR, instanceId);

		Field fieldsDisplayField = ddmFields.get(DDM.FIELDS_DISPLAY_NAME);

		String[] fieldsDisplayValues = StringUtil.split(
			(String)fieldsDisplayField.getValue());

		fieldsDisplayValues = ArrayUtil.append(
			fieldsDisplayValues, fieldsDisplayValue);

		fieldsDisplayField.setValue(StringUtil.merge(fieldsDisplayValues));
	}

	private void _addNestedDDMFields(
			String[] availableLanguageIds, String defaultLanguageId,
			Fields ddmFields, DDMFormField ddmFormField,
			DDMStructure ddmStructure,
			Map<String, List<Element>> dynamicElementElementsMap)
		throws PortalException {

		for (DDMFormField nestedDDMFormField :
				ddmFormField.getNestedDDMFormFields()) {

			addDDMFields(
				availableLanguageIds, defaultLanguageId, ddmFields,
				nestedDDMFormField, ddmStructure, dynamicElementElementsMap);
		}
	}

	private Serializable _getCheckboxMultipleValue(
		DDMFormField ddmFormField, Element dynamicContentElement) {

		DDMFormFieldOptions ddmFormFieldOptions =
			(DDMFormFieldOptions)ddmFormField.getProperty("options");

		Map<String, LocalizedValue> options = ddmFormFieldOptions.getOptions();

		if (options.size() == 1) {
			if (GetterUtil.getBoolean(dynamicContentElement.getText())) {
				Set<Map.Entry<String, LocalizedValue>> entrySet =
					options.entrySet();

				Iterator<Map.Entry<String, LocalizedValue>> iterator =
					entrySet.iterator();

				Map.Entry<String, LocalizedValue> entry = iterator.next();

				return JSONUtil.putAll(
					entry.getKey()
				).toJSONString();
			}

			return StringPool.BLANK;
		}

		return FieldConstants.getSerializable(
			ddmFormField.getDataType(), dynamicContentElement.getText());
	}

	private Map<String, List<Element>> _getDynamicElements(
		Element rootElement) {

		Map<String, List<Element>> dynamicElementElementsMap = new HashMap<>();

		for (Element dynamicElement : rootElement.elements("dynamic-element")) {
			List<Element> dynamicElementElements =
				dynamicElementElementsMap.computeIfAbsent(
					dynamicElement.attributeValue("name"),
					key -> new ArrayList<>());

			dynamicElementElements.add(dynamicElement);
		}

		return dynamicElementElementsMap;
	}

	private String _getSelectValue(Element dynamicContentElement) {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		List<Element> optionElements = dynamicContentElement.elements("option");

		if (!optionElements.isEmpty()) {
			for (Element optionElement : optionElements) {
				jsonArray.put(optionElement.getText());
			}
		}
		else {
			jsonArray.put(dynamicContentElement.getText());
		}

<<<<<<< HEAD
		return jsonArray.toString();
=======
	private String _getFileEntryValue(
		Locale defaultLocale, Element dynamicContentElement) {

		JSONObject jsonObject = null;

		try {
			jsonObject = JSONFactoryUtil.createJSONObject(
				dynamicContentElement.getText());
		}
		catch (JSONException jsonException) {
			return StringPool.BLANK;
		}

		if (jsonObject == null) {
			return StringPool.BLANK;
		}

		String uuid = jsonObject.getString("uuid");
		long groupId = jsonObject.getLong("groupId");

		if (Validator.isNull(uuid) || (groupId <= 0)) {
			return StringPool.BLANK;
		}

		try {
			if (!ExportImportThreadLocal.isImportInProcess()) {
				FileEntry fileEntry =
					_dlAppLocalService.getFileEntryByUuidAndGroupId(
						uuid, groupId);

				String title = fileEntry.getTitle();

				if (fileEntry.isInTrash()) {
					title = _trashHelper.getOriginalTitle(fileEntry.getTitle());

					jsonObject.put(
						"message",
						LanguageUtil.get(
							_getResourceBundle(defaultLocale),
							"the-selected-document-was-moved-to-the-recycle-" +
								"bin"));
				}

				jsonObject.put("title", title);
			}
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					StringBundler.concat(
						"Unable to get file entry for UUID ", uuid,
						" and group ID ", groupId));
			}

			jsonObject.put(
				"message",
				LanguageUtil.get(
					_getResourceBundle(defaultLocale),
					"the-selected-document-was-deleted"));
		}

		return jsonObject.toString();
	}

	private String _getJournalArticleValue(
		Locale defaultLocale, Element dynamicContentElement) {

		try {
			JSONObject jsonObject = JSONFactoryUtil.createJSONObject(
				dynamicContentElement.getText());

			long classPK = jsonObject.getLong("classPK");

			if (classPK <= 0) {
				return jsonObject.toString();
			}

			JournalArticle article =
				_journalArticleLocalService.fetchLatestArticle(classPK);

			if (article != null) {
				jsonObject.put("groupId", article.getGroupId());

				String title = article.getTitle(defaultLocale);

				if (article.isInTrash()) {
					jsonObject.put(
						"message",
						LanguageUtil.get(
							_getResourceBundle(defaultLocale),
							"the-selected-web-content-was-moved-to-the-" +
								"recycle-bin"));
				}

				jsonObject.put(
					"title", title
				).put(
					"titleMap", article.getTitleMap()
				).put(
					"uuid", article.getUuid()
				);
			}
			else {
				if (_log.isWarnEnabled()) {
					_log.warn("Unable to get article for  " + classPK);
				}

				jsonObject.put(
					"message",
					LanguageUtil.get(
						_getResourceBundle(defaultLocale),
						"the-selected-web-content-was-deleted"));
			}

			return jsonObject.toString();
		}
		catch (JSONException jsonException) {
			return StringPool.BLANK;
		}
	}

	private String _getLinkToLayoutValue(
		Locale defaultLocale, Element dynamicContentElement) {

		String value = dynamicContentElement.getText();

		if (JSONUtil.isValid(value)) {
			return value;
		}

		String[] values = StringUtil.split(
			dynamicContentElement.getText(), CharPool.AT);

		if (ArrayUtil.isEmpty(values)) {
			return StringPool.BLANK;
		}

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		long layoutId = GetterUtil.getLong(values[0]);
		boolean privateLayout = !Objects.equals(values[1], "public");

		if (values.length > 2) {
			long groupId = GetterUtil.getLong(values[2]);

			jsonObject.put("groupId", groupId);

			Layout layout = _layoutLocalService.fetchLayout(
				groupId, privateLayout, layoutId);

			if (layout != null) {
				jsonObject.put("name", layout.getName(defaultLocale));
			}
		}

		jsonObject.put(
			"layoutId", layoutId
		).put(
			"privateLayout", privateLayout
		);

		return jsonObject.toString();
	}

	private ResourceBundle _getResourceBundle(Locale locale) {
		ResourceBundle classResourceBundle = ResourceBundleUtil.getBundle(
			locale, "com.liferay.journal.lang");

		return new AggregateResourceBundle(
			classResourceBundle, _portal.getResourceBundle(locale));
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
	}

	private String _getSelectValue(Element dynamicContentElement) {
		JSONArray jsonArray = JSONFactoryUtil.createJSONArray();

		List<Element> optionElements = dynamicContentElement.elements("option");

		if (!optionElements.isEmpty()) {
			for (Element optionElement : optionElements) {
				jsonArray.put(optionElement.getText());
			}
		}
		else {
			jsonArray.put(dynamicContentElement.getText());
		}

		return jsonArray.toString();
	}

	private static final Log _log = LogFactoryUtil.getLog(
		JournalConverterImpl.class);

<<<<<<< HEAD
=======
	private final Map<String, String> _ddmDataTypes;
	private final Map<String, String> _ddmMetadataAttributes;
	private final Map<String, String> _ddmTypesToJournalTypes;

	@Reference
	private DLAppLocalService _dlAppLocalService;

	@Reference
	private FieldsToDDMFormValuesConverter _fieldsToDDMFormValuesConverter;

	@Reference
	private Http _http;

	@Reference
	private JournalArticleLocalService _journalArticleLocalService;

	private final Map<String, String> _journalTypesToDDMTypes;

	@Reference(unbind = "-")
	private LayoutLocalService _layoutLocalService;

	@Reference
	private Portal _portal;

	@Reference
	private TrashHelper _trashHelper;

>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
}