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

package com.liferay.style.book.internal.upgrade;

import com.liferay.portal.kernel.upgrade.DummyUpgradeStep;
<<<<<<< HEAD
import com.liferay.portal.kernel.upgrade.UpgradeCTModel;
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;
import com.liferay.style.book.internal.upgrade.v1_1_0.StyleBookEntryUpgradeProcess;
=======
import com.liferay.portal.upgrade.registry.UpgradeStepRegistrator;
import com.liferay.style.book.internal.upgrade.v1_1_0.UpgradeStyleBookEntry;
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469

import org.osgi.service.component.annotations.Component;

/**
 * @author Jürgen Kappler
 */
@Component(
	immediate = true,
	service = {StyleBookServiceUpgrade.class, UpgradeStepRegistrator.class}
)
public class StyleBookServiceUpgrade implements UpgradeStepRegistrator {

	@Override
	public void register(Registry registry) {
		registry.register("0.0.1", "1.0.0", new DummyUpgradeStep());

<<<<<<< HEAD
		registry.register("1.0.0", "1.1.0", new StyleBookEntryUpgradeProcess());

		registry.register(
			"1.1.0", "1.2.0", new UpgradeCTModel("StyleBookEntry"));
=======
		registry.register("1.0.0", "1.1.0", new UpgradeStyleBookEntry());
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
	}

}