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
CommerceShipmentDisplayContext commerceShipmentDisplayContext = (CommerceShipmentDisplayContext)request.getAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT);

CommerceShipment commerceShipment = commerceShipmentDisplayContext.getCommerceShipment();

CommerceAddress shippingAddress = commerceShipmentDisplayContext.getShippingAddress();
%>

<portlet:actionURL name="/commerce_shipment/edit_commerce_shipment" var="editCommerceShipmentURL" />

<commerce-ui:modal-content
	title='<%= LanguageUtil.format(request, "edit-x", "shipping-address") %>'
>
<<<<<<< HEAD
	<aui:form action="<%= editCommerceShipmentURL %>" cssClass="container-fluid container-fluid-max-xl p-0" method="post" name="fm">
=======
	<aui:form action="<%= editCommerceShipmentURL %>" cssClass="container-fluid-1280 p-0" method="post" name="fm">
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
		<aui:input name="<%= Constants.CMD %>" type="hidden" value="address" />
		<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
		<aui:input name="commerceShipmentId" type="hidden" value="<%= commerceShipment.getCommerceShipmentId() %>" />

		<aui:model-context bean="<%= shippingAddress %>" model="<%= CommerceAddress.class %>" />

		<aui:input name="name" />

		<aui:input name="street1" />

		<aui:input name="street2" />

		<aui:input name="street3" />

		<aui:input name="city" />

		<aui:input label="postal-code" name="zip" />

<<<<<<< HEAD
		<aui:select label="country" name="countryId" showEmptyOption="<%= true %>">

			<%
			List<Country> countries = commerceShipmentDisplayContext.getCountries();

			for (Country country : countries) {
			%>

				<aui:option label="<%= country.getTitle(locale) %>" selected="<%= shippingAddress.getCountryId() == country.getCountryId() %>" value="<%= country.getCountryId() %>" />
=======
		<aui:select label="country" name="commerceCountryId" showEmptyOption="<%= true %>">

			<%
			List<CommerceCountry> commerceCountries = commerceShipmentDisplayContext.getCommerceCountries();

			for (CommerceCountry commerceCountry : commerceCountries) {
			%>

				<aui:option label="<%= commerceCountry.getName(LanguageUtil.getLanguageId(locale)) %>" selected="<%= shippingAddress.getCommerceCountryId() == commerceCountry.getCommerceCountryId() %>" value="<%= commerceCountry.getCommerceCountryId() %>" />
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469

			<%
			}
			%>

		</aui:select>

<<<<<<< HEAD
		<aui:select label="region" name="regionId" showEmptyOption="<%= true %>">

			<%
			List<Region> regions = commerceShipmentDisplayContext.getRegions(shippingAddress.getCountryId());

			for (Region region : regions) {
			%>

				<aui:option label="<%= region.getName() %>" selected="<%= shippingAddress.getRegionId() == region.getRegionId() %>" value="<%= shippingAddress.getRegionId() %>" />
=======
		<aui:select label="region" name="commerceRegionId" showEmptyOption="<%= true %>">

			<%
			List<CommerceRegion> commerceRegions = commerceShipmentDisplayContext.getCommerceRegions(shippingAddress.getCommerceCountryId());

			for (CommerceRegion commerceRegion : commerceRegions) {
			%>

				<aui:option label="<%= commerceRegion.getName() %>" selected="<%= shippingAddress.getCommerceRegionId() == commerceRegion.getCommerceRegionId() %>" value="<%= shippingAddress.getCommerceRegionId() %>" />
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469

			<%
			}
			%>

		</aui:select>

		<aui:input name="phoneNumber" />
	</aui:form>
</commerce-ui:modal-content>

<aui:script use="aui-base,liferay-dynamic-select">
	new Liferay.DynamicSelect([
		{
<<<<<<< HEAD
			select: '<portlet:namespace />countryId',
			selectData: function (callback) {
				Liferay.Service(
					'/commerce.commercecountrymanagerimpl/get-shipping-countries',
=======
			select: '<portlet:namespace />commerceCountryId',
			selectData: function (callback) {
				Liferay.Service(
					'/commerce.commercecountry/get-shipping-commerce-countries',
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
					{
						active: true,
						companyId: <%= company.getCompanyId() %>,
						shippingAllowed: true,
					},
					callback
				);
			},
			selectDesc: 'nameCurrentValue',
<<<<<<< HEAD
			selectId: 'countryId',
			selectSort: '<%= true %>',
			selectVal: '<%= shippingAddress.getCountryId() %>',
		},
		{
			select: '<portlet:namespace />regionId',
			selectData: function (callback, selectKey) {
				Liferay.Service(
					'/region/get-regions',
					{
						active: true,
						countryId: Number(selectKey),
=======
			selectId: 'commerceCountryId',
			selectSort: '<%= true %>',
			selectVal: '<%= shippingAddress.getCommerceCountryId() %>',
		},
		{
			select: '<portlet:namespace />commerceRegionId',
			selectData: function (callback, selectKey) {
				Liferay.Service(
					'/commerce.commerceregion/get-commerce-regions',
					{
						active: true,
						commerceCountryId: Number(selectKey),
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
					},
					callback
				);
			},
			selectDesc: 'name',
<<<<<<< HEAD
			selectId: 'regionId',
			selectVal: '<%= shippingAddress.getRegionId() %>',
=======
			selectId: 'commerceRegionId',
			selectVal: '<%= shippingAddress.getCommerceRegionId() %>',
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
		},
	]);
</aui:script>