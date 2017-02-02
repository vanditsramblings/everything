package com.rambler.tasklipse.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewSite;

import com.rambler.tasklipse.model.Task;
import com.rambler.tasklipse.service.TaskService;

public class TaskServiceImpl implements TaskService {
	
	IViewSite viewSite=null;
	
	public TaskServiceImpl(IViewSite viewSite) {
		this.viewSite=viewSite;
	}

	@Override
	public IProject getProject(String projectName) {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		return project;
	}

	@Override
	public List<IProject> getAllProjects() {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		return Arrays.asList(projects);
	}

	@Override
	public List<IMarker> getAllTaskListForProject(IProject project)
			throws CoreException {
		IMarker[] markers = project.findMarkers(IMarker.TASK, true, IResource.DEPTH_INFINITE);
		return Arrays.asList(markers);
	}

	@Override
	public ArrayList<Task> getTaskListForAllProjects(String type)
			throws CoreException {
		ArrayList<Task> taskList=new ArrayList<Task>();
		for(IProject project:getAllProjects()){
			IMarker[] markers = project.findMarkers(IMarker.TASK, true, IResource.DEPTH_INFINITE);
			for(IMarker marker:markers){

				Object property=marker.getResource().getPersistentProperty(new QualifiedName(marker.getResource().getName()+"tasklipse:"+marker.getAttribute(IMarker.MESSAGE, "msg"), "tasklipse"));

				if(property==null || !"done".equals(property.toString())){
					Task task=getTask(marker,type);
					if(task!=null)
						taskList.add(task);
				}
			}
		}
		return taskList;
	}

	@Override
	public Task getTask(IMarker marker, String type) throws CoreException {
		String message=marker.getAttribute(IMarker.MESSAGE,"na");
		Matcher taskMatcher = Task.TASKLIPSE_PATTERN_TASK.matcher(message);
		if(!taskMatcher.matches()){
			return null;
		}

		boolean isArchived= shouldBeAdded(marker,message);
		if(!isArchived && "Archived".equals(type)){
			return null;
		}

		Map<String,Object> attrMap=marker.getAttributes();
		Task task=new Task(Long.parseLong(attrMap.get("id").toString()),marker);
		return task;
	}

	@Override
	public void deleteTask(Object element) {

		MessageDialog dialog = new MessageDialog(
				null, "Delete Task", null, "Are you sure you want to delete the selected task??",
				MessageDialog.QUESTION,
				new String[] {"Yes", "No"},
				0); 
		int result = dialog.open();

		if(result==0 && element instanceof Task){
			boolean ret=((Task)element).delete();
			/*if(ret){
				IActionBars bars = getViewSite().getActionBars();
				bars.getStatusLineManager().setMessage("Tasklipse:Selected task(s) deleted.");
			}*/
		}
	}

	@Override
	public boolean archiveTasks(List list) {
		for(Object task:list){
			archiveTask(task);
		}
		return true;
	}

	@Override
	public boolean archiveTask(Object element) {
		if(element instanceof Task){
			if(((Task)element).archive()){
				IActionBars bars = viewSite.getActionBars();
				bars.getStatusLineManager().setMessage("Tasklipse:Selected task(s) archived.");
			}
		}

		
		return false;
	}

	@Override
	public void deleteSelectedTasks(List list) {
		MessageDialog dialog = new MessageDialog(
				null, "Delete Task", null, "Are you sure you want to delete the selected task(s)??",
				MessageDialog.QUESTION,
				new String[] {"Yes", "No"},
				0); 
		int result = dialog.open();
		if(result==0){
			for(Object obj:list){
				if(obj instanceof Task){
					((Task)obj).delete();
				}
			}
		}
	}


	private boolean shouldBeAdded(IMarker marker, String message) {
		Boolean isDone=marker.getAttribute(IMarker.DONE, false);
		if(isDone)
			return false;

		return true;
	}

}
