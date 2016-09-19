package com.rambler.tasklipse.comparator;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;

import com.rambler.tasklipse.model.Task;


public class TasklipseTableComparator extends ViewerComparator {
	private int propertyIndex;
	private static final int DESCENDING = 1;
	private int direction = DESCENDING;

	public TasklipseTableComparator() {
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
		Task task1 = (Task) e1;
		Task task2 = (Task) e2;
		int rc = 0;
		switch (propertyIndex) {
		case 0:
			rc = task1.getPriority()-task2.getPriority();
			break;
		case 1:
			rc = task1.getTaskType().compareTo(task2.getTaskType());
			break;
		case 2:
			rc = task1.getMessage().compareTo(task2.getMessage());
			break;
		case 3:
			rc = task1.getTaskResource().compareTo(task2.getTaskResource());
			break;
		case 4:
			//TODO: #$ #p:5 add proper comparator for time 
			rc = task1.getCreatedTime().compareTo(task2.getCreatedTime());
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
