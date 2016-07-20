/*******************************************************************************
 * Copyright 2016 Guy Davenport
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
 *******************************************************************************/

package org.corehunter.ui;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ViewerColumn;

/**
 * @author Guy Davenport
 *
 */
public abstract class ArrayMatrixDataLabelProvider<ValueType extends Object> extends ColumnLabelProvider {
    private int columnIndex;

    public ArrayMatrixDataLabelProvider(int columnIndex) {
        super();
        
        this.columnIndex = columnIndex;
    }

    public final int getColumnIndex() {
        return columnIndex;
    }

    @Override
    protected void initialize(ColumnViewer viewer, ViewerColumn column) {

    }
}