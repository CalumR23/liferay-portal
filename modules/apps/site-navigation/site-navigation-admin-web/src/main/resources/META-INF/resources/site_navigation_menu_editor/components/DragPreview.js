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

import React, {useEffect, useRef, useState} from 'react';
import {useDragLayer} from 'react-dnd';

import {useConstants} from '../contexts/ConstantsContext';
import {useItems} from '../contexts/ItemsContext';
import getDescendantsCount from '../utils/getDescendantsCount';

<<<<<<< HEAD
const HANDLER_OFFSET = 10;

=======
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
const getItemStyles = (currentOffset, ref, rtl) => {
	if (!currentOffset || !ref.current) {
		return {
			display: 'none',
		};
	}

	const rect = ref.current.getBoundingClientRect();
	const x = rtl
<<<<<<< HEAD
		? currentOffset.x + HANDLER_OFFSET - window.innerWidth
		: currentOffset.x - HANDLER_OFFSET;
=======
		? currentOffset.x + rect.width * 0.5 - window.innerWidth
		: currentOffset.x - rect.width * 0.5;
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
	const y = currentOffset.y - rect.height * 0.5;

	const transform = `translate(${x}px, ${y}px)`;

	return {
		WebkitTransform: transform,
		transform,
	};
};

export default function DragPreview() {
	const ref = useRef();

<<<<<<< HEAD
	const {languageId} = useConstants();
	const items = useItems();
	const rtl = Liferay.Language.direction[languageId] === 'rtl';
=======
	const {languageDirection, languageId} = useConstants();
	const items = useItems();
	const rtl = languageDirection[languageId] === 'rtl';
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469

	const {currentOffset, isDragging, itemId} = useDragLayer((monitor) => ({
		currentOffset: monitor.getClientOffset(),
		isDragging: monitor.isDragging(),
		itemId: monitor.getItem()?.id,
	}));

	const [label, setLabel] = useState();

	useEffect(() => {
		const item = items.find(
			(item) => item.siteNavigationMenuItemId === itemId
		);

		if (item) {
			const descendantsCount = getDescendantsCount(items, itemId);

			setLabel(
				descendantsCount
					? Liferay.Util.sub(
							Liferay.Language.get('x-elements'),
							descendantsCount + 1
					  )
					: item.title
			);
		}
	}, [itemId, items]);

	return !isDragging ? null : (
		<div className="site-navigation__drag-preview">
			<div
				className="site-navigation__drag-preview__content"
				ref={ref}
				style={getItemStyles(currentOffset, ref, rtl)}
			>
				{label}
			</div>
		</div>
	);
}
