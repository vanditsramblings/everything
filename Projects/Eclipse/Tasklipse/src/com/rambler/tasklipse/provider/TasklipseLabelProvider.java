package com.rambler.tasklipse.provider;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

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
		return element!=null?element.toString():"";
	}

}
