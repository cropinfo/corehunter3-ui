package org.corehunter.ui;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.corehunter.services.CorehunterRun;

public class CorehunterRunComparator extends ViewerComparator {
  private int propertyIndex;
  private static final int DESCENDING = 1;
  private int direction = DESCENDING;

  public CorehunterRunComparator() {
    this.propertyIndex = 0;
    direction = DESCENDING;
  }

  public int getDirection() {
    return direction == 1 ? SWT.DOWN : SWT.UP;
  }

  public void setColumn(int column) {
    if (column == this.propertyIndex) {
      // Same column as last sort; toggle the direction
      direction = 1 - direction;
    } else {
      // New column; do an ascending sort
      this.propertyIndex = column;
      direction = DESCENDING;
    }
  }

  @Override
  public int compare(Viewer viewer, Object e1, Object e2) {
    CorehunterRun run1 = (CorehunterRun) e1;
    CorehunterRun run2 = (CorehunterRun) e2;
    int rc = 0;
    switch (propertyIndex) {
    case 0:
      rc = run1.getName().compareTo(run2.getName());
      break;
    case 1:
      rc = run1.getStartDate().compareTo(run2.getStartDate());
      break;
    case 2:
      rc = run1.getEndDate().compareTo(run2.getEndDate());
      break;
    case 3:
      rc = run1.getStatus().compareTo(run2.getStatus());
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