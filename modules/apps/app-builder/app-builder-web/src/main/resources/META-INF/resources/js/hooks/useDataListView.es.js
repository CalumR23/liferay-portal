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

import {getItem} from 'data-engine-js-components-web/js/utils/client.es';
import {errorToast} from 'data-engine-js-components-web/js/utils/toast.es';
import {DataDefinitionUtils} from 'data-engine-taglib';
import {useEffect, useState} from 'react';

<<<<<<< HEAD
export default function useDataListView(dataListViewId, dataDefinitionId) {
=======
import {getItem} from '../utils/client.es';
import {errorToast} from '../utils/toast.es';

export default function useDataListView(
	dataListViewId,
	dataDefinitionId,
	withPermission
) {
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
	const [state, setState] = useState({
		columns: [],
		dataDefinition: null,
		dataListView: {
			fieldNames: [],
		},
		isLoading: true,
	});

	useEffect(() => {
		if (withPermission) {
			Promise.all([
				getItem(
					`/o/data-engine/v2.0/data-definitions/${dataDefinitionId}`
				),
				getItem(
					`/o/data-engine/v2.0/data-list-views/${dataListViewId}`
				),
			])
				.then(([dataDefinition, dataListView]) => {
					setState((prevState) => ({
						...prevState,
						columns: dataListView.fieldNames.map((column) => {
							const {
								label: value,
							} = DataDefinitionUtils.getDataDefinitionField(
								dataDefinition,
								column
							);

							return {
								key: 'dataRecordValues/' + column,
								sortable: true,
								value,
							};
						}),
						dataDefinition: {
							...prevState.dataDefinition,
							...dataDefinition,
						},
						dataListView: {
							...prevState.dataListView,
							...dataListView,
						},
						isLoading: false,
					}));
				})
				.catch(() => {
					setState((prevState) => ({
						...prevState,
						isLoading: false,
					}));

					errorToast();
				});
		}
	}, [dataDefinitionId, dataListViewId, withPermission]);

	return state;
}
