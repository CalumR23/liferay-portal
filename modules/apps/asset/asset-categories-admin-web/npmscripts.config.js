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

<<<<<<< HEAD
=======
const preset = require('@liferay/npm-scripts/src/presets/standard');

>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
module.exports = {
	build: {
		bundler: {
			config: {
				imports: {
					'asset-taglib': {
						'/': '>=1.0.0',
					},
				},
			},
		},
		dependencies: ['asset-taglib'],
	},
	federation: {
		exposes: [
			'<inputDir>/js/ActionsComponentPropsTransformer.js',
			'<inputDir>/js/AssetCategoriesManagementToolbarPropsTransformer.js',
			'<inputDir>/js/AssetCategoriesSelectorTag.es.js',
			'<inputDir>/js/DetailsItemSelector.js',
		],
		remotes: ['asset-taglib'],
	},
<<<<<<< HEAD
=======
	preset: '@liferay/npm-scripts/src/presets/standard',
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
};
