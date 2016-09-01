package com.rambler.tasklipse.provider;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.rambler.tasklipse.model.Task;

/**
 * @author vandit
 * 
 */

public class TasklipseLabelProvider extends LabelProvider implements ITableLabelProvider{

	@Override
	public String getText(Object element) {
		String text = null;

		return text != null ? text : "";
	}

	@Override
	public Image getImage(Object element) {
		Image img = null;

		if (element instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) element;
			element = selection.getFirstElement();
		}		
		return img != null ? img : super.getImage(element);
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		String result = "";
		Task task = (Task) element;
		switch (columnIndex) {
			case 0 :
				result = task.getPriority()+"";
				break;
			case 1 :
				result = task.getTaskName();
				break;
			case 2 :
				result = task.getTaskType();
				break;
			case 3 :
				result = task.getTaskResource();
				break;
			case 4 :
				result = task.getMessage();
				break;
			case 5 :
				result = "-";
				break;
			case 6 :
				result = task.getCreatedTime();
				break;	
			default :
				break; 	
		}
		return result;
	}


}
