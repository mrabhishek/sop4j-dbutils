/*
 * Copyright (C) 2013 SOP4J
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sop4j.dbutils;


import java.beans.PropertyDescriptor;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;


/**
 * Provides generous name matching between DB columns and Java Bean properties by matching with and without underscores.
 */
public class GenerousBeanProcessor extends BeanProcessor {

    @Override
    protected int[] mapColumnsToProperties(final ResultSetMetaData rsmd,
            final PropertyDescriptor[] props) throws SQLException {

        final int cols = rsmd.getColumnCount();
        final int[] columnToProperty = new int[cols + 1];
        Arrays.fill(columnToProperty, PROPERTY_NOT_FOUND);

        for (int col = 1; col <= cols; col++) {
            String columnName = rsmd.getColumnLabel(col);

            if (null == columnName || 0 == columnName.length()) {
                columnName = rsmd.getColumnName(col);
            }

            final String generousColumnName = columnName.replace("_", "");

            for (int i = 0; i < props.length; i++) {
                final String propName = props[i].getName();

                // see if either the column name, or the generous one matches
                if (columnName.equalsIgnoreCase(propName) ||
                        generousColumnName.equalsIgnoreCase(propName)) {
                    columnToProperty[col] = i;
                    break;
                }
            }
        }

        return columnToProperty;
    }

}