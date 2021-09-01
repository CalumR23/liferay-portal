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

package tax;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Calum Ragan
 */
public class Basket {

	public void addItem(Product item) {
		_products.add(item);
	}

	public void checkout() {
		double totalTax = 0;
		double totalAmount = 0;

		for (Product item : _products) {
			double itemTaxAmount = 0;

			if (!item.getExported()) {
				itemTaxAmount = item.getAmount() * 0.10;
			}

			if (item.getImported()) {
				itemTaxAmount = itemTaxAmount + (item.getAmount() * 0.05);
			}

			itemTaxAmount = Math.ceil(itemTaxAmount * 20.0) / 20.0;

			totalTax += itemTaxAmount;

			item.setTaxedAmount(item.getAmount() + itemTaxAmount);

			totalAmount += item.getTaxedAmount();

			String itemName = item.getName();

			int index = itemName.indexOf(" at ");

			String output =
				itemName.substring(0, index) + " : " +
					_decimalFormat.format(item.getTaxedAmount());

			System.out.println(output);
		}

		System.out.println("Sales Tax : " + _decimalFormat.format(totalTax));
		System.out.println("Total : " + _decimalFormat.format(totalAmount));
	}

	private static final DecimalFormat _decimalFormat = new DecimalFormat("0.00");

	private final List<Product> _products = new ArrayList<>();

}