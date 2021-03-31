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

import React from 'react';

import {useItems} from '../contexts/ItemsContext';
import {MenuItem} from './MenuItem';

export const Menu = () => {
	const items = useItems();

	return (
<<<<<<< HEAD
		<div className="container p-3" role="list">
=======
		<div className="container p-3">
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
			{items.map((item) => (
				<MenuItem item={item} key={item.siteNavigationMenuItemId} />
			))}
		</div>
	);
};
