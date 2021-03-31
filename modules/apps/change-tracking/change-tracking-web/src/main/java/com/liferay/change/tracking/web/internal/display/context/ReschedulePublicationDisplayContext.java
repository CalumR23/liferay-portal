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

package com.liferay.change.tracking.web.internal.display.context;

import com.liferay.change.tracking.model.CTCollection;
import com.liferay.change.tracking.web.internal.scheduler.ScheduledPublishInfo;
<<<<<<< HEAD
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
=======
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.json.JSONUtil;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.CalendarFactoryUtil;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;

import java.time.Instant;

import java.util.Calendar;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

<<<<<<< HEAD
=======
import javax.portlet.ActionRequest;
import javax.portlet.PortletURL;
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Samuel Trong Tran
 */
public class ReschedulePublicationDisplayContext {

	public ReschedulePublicationDisplayContext(
		CTCollection ctCollection, Language language, Portal portal,
		RenderRequest renderRequest, RenderResponse renderResponse,
		ScheduledPublishInfo scheduledPublishInfo) {

		_ctCollection = ctCollection;
		_language = language;
		_portal = portal;

		_renderRequest = renderRequest;

		_httpServletRequest = _portal.getHttpServletRequest(_renderRequest);
		_themeDisplay = (ThemeDisplay)_renderRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		_renderResponse = renderResponse;
		_scheduledPublishInfo = scheduledPublishInfo;
	}

	public Map<String, Object> getReactData() {
		Calendar calendar = CalendarFactoryUtil.getCalendar(
			_themeDisplay.getTimeZone(), _themeDisplay.getLocale());

		calendar.setTime(_scheduledPublishInfo.getStartDate());

		return HashMapBuilder.<String, Object>put(
			"redirect", getRedirect()
		).put(
			"rescheduleURL",
<<<<<<< HEAD
			() -> PortletURLBuilder.createActionURL(
				_renderResponse
			).setActionName(
				"/change_tracking/reschedule_publication"
			).setRedirect(
				getRedirect()
			).setParameter(
				"ctCollectionId", _ctCollection.getCtCollectionId()
			).buildString()
=======
			() -> {
				PortletURL scheduleURL = _renderResponse.createActionURL();

				scheduleURL.setParameter(
					ActionRequest.ACTION_NAME,
					"/change_tracking/reschedule_publication");
				scheduleURL.setParameter("redirect", getRedirect());
				scheduleURL.setParameter(
					"ctCollectionId",
					String.valueOf(_ctCollection.getCtCollectionId()));

				return scheduleURL.toString();
			}
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
		).put(
			"scheduledDate",
			StringBundler.concat(
				String.valueOf(calendar.get(Calendar.YEAR)), StringPool.DASH,
				String.valueOf(calendar.get(Calendar.MONTH) + 1),
				StringPool.DASH,
				String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)))
		).put(
			"scheduledTime",
			JSONUtil.put(
				"hours", calendar.get(Calendar.HOUR_OF_DAY)
			).put(
				"minutes", calendar.get(Calendar.MINUTE)
			)
		).put(
			"spritemap", _themeDisplay.getPathThemeImages() + "/clay/icons.svg"
		).put(
			"timeZone",
			() -> {
				TimeZone timeZone = _themeDisplay.getTimeZone();

				if (Objects.equals(timeZone.getID(), StringPool.UTC)) {
					return "GMT";
				}

				Instant instant = Instant.now();

				return "GMT" +
					String.format("%tz", instant.atZone(timeZone.toZoneId()));
			}
		).put(
			"unscheduleURL",
<<<<<<< HEAD
			() -> PortletURLBuilder.createActionURL(
				_renderResponse
			).setActionName(
				"/change_tracking/unschedule_publication"
			).setRedirect(
				getRedirect()
			).setParameter(
				"ctCollectionId", _ctCollection.getCtCollectionId()
			).buildString()
=======
			() -> {
				PortletURL scheduleURL = _renderResponse.createActionURL();

				scheduleURL.setParameter(
					ActionRequest.ACTION_NAME,
					"/change_tracking/unschedule_publication");
				scheduleURL.setParameter("redirect", getRedirect());
				scheduleURL.setParameter(
					"ctCollectionId",
					String.valueOf(_ctCollection.getCtCollectionId()));

				return scheduleURL.toString();
			}
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
		).build();
	}

	public String getRedirect() {
		String redirect = ParamUtil.getString(_renderRequest, "redirect");

		if (Validator.isNotNull(redirect)) {
			return redirect;
		}

<<<<<<< HEAD
		return PortletURLBuilder.createRenderURL(
			_renderResponse
		).setMVCRenderCommandName(
			"/change_tracking/view_scheduled"
		).buildString();
=======
		PortletURL portletURL = _renderResponse.createRenderURL();

		portletURL.setParameter(
			"mvcRenderCommandName", "/change_tracking/view_scheduled");

		return portletURL.toString();
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
	}

	public String getTitle() {
		return StringBundler.concat(
			_language.get(_httpServletRequest, "reschedule"), ": ",
			_ctCollection.getName());
	}

	private final CTCollection _ctCollection;
	private final HttpServletRequest _httpServletRequest;
	private final Language _language;
	private final Portal _portal;
	private final RenderRequest _renderRequest;
	private final RenderResponse _renderResponse;
	private final ScheduledPublishInfo _scheduledPublishInfo;
	private final ThemeDisplay _themeDisplay;

}