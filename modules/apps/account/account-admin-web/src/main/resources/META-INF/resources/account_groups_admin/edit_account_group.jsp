<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
AccountGroupDisplay accountGroupDisplay = (AccountGroupDisplay)request.getAttribute(AccountWebKeys.ACCOUNT_GROUP_DISPLAY);
<<<<<<< HEAD
=======

PortletURL portletURL = renderResponse.createRenderURL();

portletURL.setParameter("mvcRenderCommandName", "/account_admin/edit_account_group");
portletURL.setParameter("accountGroupId", String.valueOf(accountGroupDisplay.getAccountGroupId()));
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
%>

<liferay-frontend:screen-navigation
	context="<%= accountGroupDisplay %>"
	key="<%= AccountScreenNavigationEntryConstants.SCREEN_NAVIGATION_KEY_ACCOUNT_GROUP %>"
	portletURL='<%=
		PortletURLBuilder.createRenderURL(
			renderResponse
		).setMVCRenderCommandName(
			"/account_admin/edit_account_group"
		).setParameter(
			"accountGroupId", String.valueOf(accountGroupDisplay.getAccountGroupId())
		).build()
	%>'
/>