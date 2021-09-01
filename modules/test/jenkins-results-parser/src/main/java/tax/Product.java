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

import com.liferay.poshi.core.util.GetterUtil;

/**
 * @author Calum Ragan
 */
public class Product {

	public Product(String input) {
		parseInput(input);
	}

	public double getAmount() {
		return _amount;
	}

	public boolean getExported() {
		return _exempt;
	}

	public boolean getImported() {
		return _imported;
	}

	public String getName() {
		return _name;
	}

	public double getTaxedAmount() {
		return _taxedAmount;
	}

	public void parseInput(String product) {
		String[] productArray = product.split(" ");

		_quantity = GetterUtil.getInteger(productArray[0]);

		_amount = GetterUtil.getDouble(productArray[productArray.length - 1]);

		_name = product;

		for (String word : productArray) {
			if (word.equals("book") || word.contains("chocolate") ||
				word.equals("pills")) {

				_exempt = true;
			}

			if (word.equals("imported")) {
				_imported = true;
			}
		}
	}

	public void setTaxedAmount(double newAmount) {
		_taxedAmount = newAmount;
	}

	private double _amount;
	private boolean _exempt;
	private boolean _imported;
	private String _name;
	private int _quantity;
	private double _taxedAmount;

}