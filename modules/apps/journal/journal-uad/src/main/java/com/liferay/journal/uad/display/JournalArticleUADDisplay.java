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

package com.liferay.journal.uad.display;

import com.liferay.journal.constants.JournalPortletKeys;
import com.liferay.journal.model.JournalArticle;
<<<<<<< HEAD
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
=======
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.user.associated.data.display.UADDisplay;

import javax.portlet.PortletRequest;
<<<<<<< HEAD
=======
import javax.portlet.PortletURL;
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Balázs Sáfrány-Kovalik
 */
@Component(immediate = true, service = UADDisplay.class)
public class JournalArticleUADDisplay extends BaseJournalArticleUADDisplay {

	@Override
	public String getEditURL(
			JournalArticle journalArticle,
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse)
		throws Exception {

		if (journalArticle.isInTrash()) {
			return StringPool.BLANK;
		}

<<<<<<< HEAD
		return PortletURLBuilder.create(
			_portal.getControlPanelPortletURL(
				liferayPortletRequest, JournalPortletKeys.JOURNAL,
				PortletRequest.RENDER_PHASE)
		).setMVCPath(
			"/edit_article.jsp"
		).setRedirect(
			_portal.getCurrentURL(liferayPortletRequest)
		).setParameter(
			"groupId", journalArticle.getGroupId()
		).setParameter(
			"articleId", journalArticle.getArticleId()
		).setParameter(
			"version", journalArticle.getVersion()
		).buildString();
=======
		PortletURL portletURL = _portal.getControlPanelPortletURL(
			liferayPortletRequest, JournalPortletKeys.JOURNAL,
			PortletRequest.RENDER_PHASE);

		portletURL.setParameter("mvcPath", "/edit_article.jsp");
		portletURL.setParameter(
			"redirect", _portal.getCurrentURL(liferayPortletRequest));
		portletURL.setParameter(
			"groupId", String.valueOf(journalArticle.getGroupId()));
		portletURL.setParameter("articleId", journalArticle.getArticleId());
		portletURL.setParameter(
			"version", String.valueOf(journalArticle.getVersion()));

		return portletURL.toString();
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
	}

	@Reference
	private Portal _portal;

}