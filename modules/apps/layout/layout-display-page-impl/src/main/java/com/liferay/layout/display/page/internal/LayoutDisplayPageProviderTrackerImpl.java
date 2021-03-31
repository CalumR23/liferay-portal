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

package com.liferay.layout.display.page.internal;

import com.liferay.layout.display.page.LayoutDisplayPageProvider;
import com.liferay.layout.display.page.LayoutDisplayPageProviderTracker;
<<<<<<< HEAD
=======
import com.liferay.osgi.service.tracker.collections.ServiceTrackerMapBuilder;
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
import com.liferay.osgi.service.tracker.collections.map.PropertyServiceReferenceComparator;
import com.liferay.osgi.service.tracker.collections.map.ServiceTrackerMap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

/**
 * @author Jorge Ferrer
 */
@Component(service = LayoutDisplayPageProviderTracker.class)
public class LayoutDisplayPageProviderTrackerImpl
	implements LayoutDisplayPageProviderTracker {

	@Override
	public LayoutDisplayPageProvider<?> getLayoutDisplayPageProviderByClassName(
		String className) {

		return _layoutDisplayPageProviderByClassNameServiceTrackerMap.
			getService(className);
	}

	@Override
	public LayoutDisplayPageProvider<?>
		getLayoutDisplayPageProviderByURLSeparator(String urlSeparator) {

		return _layoutDisplayPageProviderByURLSeparatorServiceTrackerMap.
			getService(urlSeparator);
	}

	@Override
	public List<LayoutDisplayPageProvider<?>> getLayoutDisplayPageProviders() {
		return new ArrayList(
			_layoutDisplayPageProviderByClassNameServiceTrackerMap.values());
	}

	@Activate
	protected void activate(BundleContext bundleContext) {
		_layoutDisplayPageProviderByClassNameServiceTrackerMap =
			ServiceTrackerMapBuilder.SelectorFactory.newSelector(
				bundleContext,
				(Class<LayoutDisplayPageProvider<?>>)
					(Class<?>)LayoutDisplayPageProvider.class
			).<String>map(
				(serviceReference, emitter) -> {
					LayoutDisplayPageProvider<?> layoutDisplayPageProvider =
						bundleContext.getService(serviceReference);

					try {
						emitter.emit(layoutDisplayPageProvider.getClassName());
					}
					finally {
						bundleContext.ungetService(serviceReference);
					}
<<<<<<< HEAD
				},
				new PropertyServiceReferenceComparator<>("service.ranking"));
=======
				}
			).collectSingleValue(
				Collections.reverseOrder(
					new PropertyServiceReferenceComparator<>("service.ranking"))
			).build();

>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
		_layoutDisplayPageProviderByURLSeparatorServiceTrackerMap =
			ServiceTrackerMapBuilder.SelectorFactory.newSelector(
				bundleContext,
				(Class<LayoutDisplayPageProvider<?>>)
					(Class<?>)LayoutDisplayPageProvider.class
			).<String>map(
				(serviceReference, emitter) -> {
					LayoutDisplayPageProvider<?> layoutDisplayPageProvider =
						bundleContext.getService(serviceReference);

					try {
						emitter.emit(
							layoutDisplayPageProvider.getURLSeparator());
					}
					finally {
						bundleContext.ungetService(serviceReference);
					}
<<<<<<< HEAD
				},
				new PropertyServiceReferenceComparator<>("service.ranking"));
=======
				}
			).collectSingleValue(
				Collections.reverseOrder(
					new PropertyServiceReferenceComparator<>("service.ranking"))
			).build();
>>>>>>> 3cc350081830d5b3ed7848d769d3985a6bbf0469
	}

	private ServiceTrackerMap<String, LayoutDisplayPageProvider<?>>
		_layoutDisplayPageProviderByClassNameServiceTrackerMap;
	private ServiceTrackerMap<String, LayoutDisplayPageProvider<?>>
		_layoutDisplayPageProviderByURLSeparatorServiceTrackerMap;

}