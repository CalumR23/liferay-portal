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

import {ClayButtonWithIcon} from '@clayui/button';
import {Align, ClayDropDownWithItems} from '@clayui/drop-down';
import React from 'react';

<<<<<<< HEAD
export default ({dropdownItems, spritemap}) => {
=======
export default function DropdownMenu({dropdownItems, spritemap}) {
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
	return (
		<>
			<ClayDropDownWithItems
				alignmentPosition={Align.BottomLeft}
				items={dropdownItems}
				spritemap={spritemap}
				trigger={
					<ClayButtonWithIcon
						displayType="unstyled"
						small
						spritemap={spritemap}
						symbol="ellipsis-v"
					/>
				}
			/>
		</>
	);
<<<<<<< HEAD
};
=======
}
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
