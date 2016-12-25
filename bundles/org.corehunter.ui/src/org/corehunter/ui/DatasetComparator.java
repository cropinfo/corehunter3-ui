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

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

import uno.informatics.data.Dataset;

public class DatasetComparator extends ViewerComparator {
    private int propertyIndex;
    private static final int DESCENDING = 1;
    private int direction = DESCENDING;

    public DatasetComparator() {
        this.propertyIndex = 0;
        direction = DESCENDING;
    }

    public int getDirection() {
        return direction == 1 ? SWT.DOWN : SWT.UP;
    }

    public void setColumn(int column) {
        if (column == this.propertyIndex) {
            // Same column as last sort; toggle the direction
            direction = -1 * direction;
        } else {
            // New column; do an ascending sort
            this.propertyIndex = column;
            direction = DESCENDING;
        }
    }

    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        Dataset dataset1 = (Dataset) e1;
        Dataset dataset2 = (Dataset) e2;
        int rc = 0;
        switch (propertyIndex) {
            case 0:
                rc = dataset1.getName().compareTo(dataset2.getName());
                break;
            case 1:
            	if (dataset1.getDescription() != null && dataset2.getDescription() != null) {
	                rc = dataset1.getDescription().compareTo(dataset2.getDescription());
            	} else {
                	if (dataset1.getDescription() != null) {
    	                rc = 1 ;
                	} else {
                		rc = -1 ;
                	}
            	}
                break;
            default:
                rc = 0;
        }
        // If descending order, flip the direction
        if (direction == DESCENDING) {
            rc = -rc;
        }
        return rc;
    }

}