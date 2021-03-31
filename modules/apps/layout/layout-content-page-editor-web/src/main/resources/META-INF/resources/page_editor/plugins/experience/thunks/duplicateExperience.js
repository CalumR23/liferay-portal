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

import ExperienceService from '../../../app/services/ExperienceService';
<<<<<<< HEAD
import createExperienceAction from '../actions/createExperience';
=======
import duplicateExperienceAction from '../actions/duplicateExperience';
import selectExperienceAction from '../actions/selectExperience';
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469

export default function duplicateExperience({segmentsExperienceId}) {
	return (dispatch) => {
		return ExperienceService.duplicateExperience({
			body: {
				segmentsExperienceId,
			},
			dispatch,
		}).then(({fragmentEntryLinks, layoutData, segmentsExperience}) => {
<<<<<<< HEAD
			return ExperienceService.selectExperience({
=======
			ExperienceService.selectExperience({
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
				body: {
					segmentsExperienceId:
						segmentsExperience.segmentsExperienceId,
				},
				dispatch,
			})
				.then((portletIds) => {
					return dispatch(
<<<<<<< HEAD
						createExperienceAction({
							fragmentEntryLinks,
							layoutData,
							portletIds,
							segmentsExperience,
=======
						selectExperienceAction({
							portletIds,
							segmentsExperienceId:
								segmentsExperience.segmentsExperienceId,
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
						})
					);
				})
				.catch((error) => {
					return error;
				});
<<<<<<< HEAD
=======

			return dispatch(
				duplicateExperienceAction({
					fragmentEntryLinks,
					layoutData,
					segmentsExperience,
				})
			);
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
		});
	};
}
