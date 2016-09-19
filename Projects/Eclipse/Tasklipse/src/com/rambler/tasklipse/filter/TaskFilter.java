package com.rambler.tasklipse.filter;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import com.rambler.tasklipse.model.Task;


public class TaskFilter extends ViewerFilter {

	private String searchString=null;

	public void setSearchText(String s) {
		if(s!=null&&!s.trim().isEmpty())
			this.searchString = ".*" + s + ".*";
		else
			this.searchString=null;
	}

	@Override
	public boolean select(Viewer viewer,
			Object parentElement,
			Object element) {
		if (searchString == null || searchString.length() == 0) {
			return true;
		}
		Task task = (Task) element;
		if (task.getTaskType().matches(searchString)) {
			return true;
		}
		if (task.getTaskResource().matches(searchString)) {
			return true;
		}
		if (task.getMessage().matches(searchString)) {
			return true;
		}

		return false;
	}
}