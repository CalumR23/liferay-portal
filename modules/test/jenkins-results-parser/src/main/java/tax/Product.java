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
public class Product {
    int quantity;
    boolean exempt = false;
    boolean imported = false;
    double amount;

    public void parseProducts(String input){
      String[] parsedArray = input.split("/n");
      for (String product : parsedArray){
          createProduct(product);
      }
    };

    public void createProduct(String product){
        String[] productArray = product.split(" ");
        quantity = Integer.parseInt(productArray[0]);
        amount = Double.parseDouble(
                productArray[productArray.length - 1]);

        for (String word : productArray) {
            if (word.equals("book") || word.equals("chocolate") ||
                    word.equals("pills")) {

                exempt = true;
            };

            if (word.equals("imported")) {
                imported = true;
            };
        };



    }
};
