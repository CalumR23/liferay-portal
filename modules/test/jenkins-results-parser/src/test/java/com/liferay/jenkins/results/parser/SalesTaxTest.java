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

package com.liferay.jenkins.results.parser;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import tax.Register;

/**
 * @author Calum Ragan
 */
public class SalesTaxTest {

    @Test
    public void testRegister() throws IOException {
        for (int i = 1; i <= 3; i++) {
            String itemListFileName = JenkinsResultsParserUtil.combine(
                    "input", String.valueOf(i), ".txt");

            String fileContent = _readDependencyFile(itemListFileName);

            String output = Register.shopping(fileContent);

            String expectedOutputFilename = JenkinsResultsParserUtil.combine(
                    "expected_output", String.valueOf(i), ".txt");

            String expected = _readDependencyFile(expectedOutputFilename);

            if (expected.equals(output)) {
                continue;
            }

            String errorMessage = JenkinsResultsParserUtil.combine(
                    "String mismatch\nExpected:\n", expected, "\n\nActual:\n",
                    output, "\n\n");

            System.out.println(errorMessage);


        }
    }
        private String _readDependencyFile(String dependencyFilename) {


            Class<?> clazz = SalesTaxTest.class;

            try (InputStream inputStream = clazz.getResourceAsStream(
                    "/dependencies/SalesTaxTest/" + dependencyFilename)) {
                return JenkinsResultsParserUtil.readInputStream(inputStream);
            }
            catch (IOException ioException) {
                throw new RuntimeException(
                        "Unable to read dependency file " + dependencyFilename,
                        ioException);
            }
        }
    }

