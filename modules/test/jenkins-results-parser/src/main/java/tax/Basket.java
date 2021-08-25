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
public class Basket {
    Product[] obj = new Product[3];
    public void addItem(Product item) {
//			obj[0] = new Product(item);
    }

    public void getTotal(Basket basket){
        double totalTax = 0;
        double totalAmount = 0;
        for(Product item : basket.obj){
            double itemTaxAmount = 0;
            if(!item.exempt && item.imported){
                itemTaxAmount = item.amount * 0.15;
                totalTax += itemTaxAmount;
                item.amount += itemTaxAmount;
                totalAmount += item.amount;
            }else if(item.imported){
                itemTaxAmount = item.amount * 0.05;
                totalTax += itemTaxAmount;
                item.amount *= itemTaxAmount;
                totalAmount += item.amount;
            }else if(item.exempt){
                totalAmount += item.amount;
            } else {
                itemTaxAmount = item.amount * 0.10;
                totalTax += itemTaxAmount;
                item.amount *= itemTaxAmount;
                totalAmount += item.amount;
            }
        }
    }
}
