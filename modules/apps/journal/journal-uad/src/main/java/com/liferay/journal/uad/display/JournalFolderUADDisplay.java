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
import com.liferay.journal.model.JournalFolder;
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
public class JournalFolderUADDisplay extends BaseJournalFolderUADDisplay {

	@Override
	public String getEditURL(
			JournalFolder journalFolder,
			LiferayPortletRequest liferayPortletRequest,
			LiferayPortletResponse liferayPortletResponse)
		throws Exception {

		if (journalFolder.isInTrash()) {
			return StringPool.BLANK;
		}

<<<<<<< HEAD
		return PortletURLBuilder.create(
			_portal.getControlPanelPortletURL(
				liferayPortletRequest, JournalPortletKeys.JOURNAL,
				PortletRequest.RENDER_PHASE)
		).setMVCPath(
			"/edit_folder.jsp"
		).setRedirect(
			_portal.getCurrentURL(liferayPortletRequest)
		).setParameter(
			"groupId", journalFolder.getGroupId()
		).setParameter(
			"folderId", journalFolder.getFolderId()
		).buildString();
=======
		PortletURL portletURL = _portal.getControlPanelPortletURL(
			liferayPortletRequest, JournalPortletKeys.JOURNAL,
			PortletRequest.RENDER_PHASE);

		portletURL.setParameter("mvcPath", "/edit_folder.jsp");
		portletURL.setParameter(
			"redirect", _portal.getCurrentURL(liferayPortletRequest));
		portletURL.setParameter(
			"groupId", String.valueOf(journalFolder.getGroupId()));
		portletURL.setParameter(
			"folderId", String.valueOf(journalFolder.getFolderId()));

		return portletURL.toString();
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
	}

	@Reference
	private Portal _portal;

}