package com.rambler.tasklipse.service;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import com.rambler.tasklipse.model.Task;

/**
 * @author vandit
 * 
 */

public interface TaskService {
	
	public IProject getProject(String projectName);

	public List<IProject> getAllProjects();

	public List<IMarker> getAllTaskListForProject(IProject project) throws CoreException;

	public ArrayList<Task>  getTaskListForAllProjects(String type) throws CoreException;

	public Task getTask(IMarker marker, String Type) throws CoreException;
		
	public boolean archiveTasks(List list);
	
	public boolean archiveTask(Object element);
	
	public void deleteTask(Object element);

	public void deleteSelectedTasks(List list);
	
}
