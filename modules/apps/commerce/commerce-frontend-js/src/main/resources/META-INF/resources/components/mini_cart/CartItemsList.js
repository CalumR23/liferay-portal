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

import ClayIcon from '@clayui/icon';
import PropTypes from 'prop-types';
import React, {useContext} from 'react';

import MiniCartContext from './MiniCartContext';
import {ADD_PRODUCT} from './util/constants';

<<<<<<< HEAD
function CartItemsList() {
=======
function CartItemsList({items}) {
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
	const {
		CartViews,
		cartState,
		isUpdating,
		labels,
		spritemap,
		summaryDataMapper,
	} = useContext(MiniCartContext);

<<<<<<< HEAD
	const {cartItems = [], summary = {}} = cartState;

	return (
		<div className="mini-cart-items-list">
			<CartViews.ItemsListActions numberOfItems={cartItems.length} />
=======
	const {summary = {}} = cartState;
	const numberOfItems = items?.length || 0;

	return (
		<div className={'mini-cart-items-list'}>
			<CartViews.ItemsListActions numberOfItems={numberOfItems} />
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469

			{cartItems.length > 0 ? (
				<>
<<<<<<< HEAD
					<div className="mini-cart-cart-items">
						{cartItems.map((item) => (
=======
					<div className={'mini-cart-cart-items'}>
						{items.map((item) => (
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
							<CartViews.Item item={item} key={item.id} />
						))}
					</div>

					<>
						<CartViews.Summary
							dataMapper={summaryDataMapper}
							isLoading={isUpdating}
							summaryData={summary}
						/>
					</>
				</>
			) : (
				<div className="empty-cart">
					<div className="empty-cart-icon mb-3">
						<ClayIcon
							spritemap={spritemap}
							symbol="shopping-cart"
						/>
					</div>

					<p className="empty-cart-label">{labels[ADD_PRODUCT]}</p>
				</div>
			)}
		</div>
	);
}

CartItemsList.propTypes = {
	items: PropTypes.array,
};

export default CartItemsList;
