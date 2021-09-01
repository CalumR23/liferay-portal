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

/**
 * @author Calum Ragan
 */
public class Revenue {

	public static void main(String[] args) {
		startShopping(
			"1 book at 12.49\n1 music CD at 14.99\n1 chocolate bar at 0.85");
		startShopping(
			"1 imported box of chocolates at 10.00\n" +
				"1 imported bottle of perfume at 47.50");
		startShopping(
			"1 imported bottle of perfume at 27.99\n" +
				"1 bottle of perfume at 18.99\n" +
					"1 packet of headache pills at 9.75\n" +
						"1 imported box of chocolates at 11.25");
	}

	public static void startShopping(String input) {
		String[] parsedArray = input.split("\n");

		Basket basket = new Basket();

		for (String productInput : parsedArray) {
			basket.addItem(new Product(productInput));
		}

		basket.checkout();
	}

}