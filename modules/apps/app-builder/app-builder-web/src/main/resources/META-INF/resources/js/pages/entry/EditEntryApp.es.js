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

import React, {useState} from 'react';

import {AppContextProvider} from '../../AppContext.es';
import useLazy from '../../hooks/useLazy.es';
<<<<<<< HEAD
import PermissionTunnel from './PermissionTunnel.es';
=======
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
import {PermissionsContextProvider} from './PermissionsContext.es';
import PortalEntry, {getStorageLanguageId} from './PortalEntry.es';

export default ({appTab, ...props}) => {
	const EditPage = useLazy(true);
	const {appId, dataDefinitionId} = props;
	const defaultLanguageId = getStorageLanguageId(appId);
	const [userLanguageId, setUserLanguageId] = useState(defaultLanguageId);

	const newProps = {
		...props,
		userLanguageId,
	};

	return (
<<<<<<< HEAD
		<AppContextProvider {...newProps}>
=======
		<AppContextProvider {...props}>
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
			<PermissionsContextProvider dataDefinitionId={dataDefinitionId}>
				<PortalEntry
					dataDefinitionId={props.dataDefinitionId}
					setUserLanguageId={setUserLanguageId}
					userLanguageId={userLanguageId}
				/>

<<<<<<< HEAD
				<PermissionTunnel permissionType={['add', 'update']}>
					<EditPage module={appTab.editEntryPoint} props={newProps} />
				</PermissionTunnel>
=======
				<EditPage module={appTab.editEntryPoint} props={props} />
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
			</PermissionsContextProvider>
		</AppContextProvider>
	);
};
