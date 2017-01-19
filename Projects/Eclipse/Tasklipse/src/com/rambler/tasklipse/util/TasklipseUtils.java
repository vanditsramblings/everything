package com.rambler.tasklipse.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import com.rambler.tasklipse.model.Task;

/**
 * @author vandit
 * 
 */

public class TasklipseUtils {


	public static IProject getProject(String projectName){
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		return project;
	}

	public static List<IProject> getAllProjects(){
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		return Arrays.asList(projects);
	}

	public static List<IMarker> getAllTaskListForProject(IProject project) throws CoreException{
		IMarker[] markers = project.findMarkers(IMarker.TASK, true, IResource.DEPTH_INFINITE);
		return Arrays.asList(markers);
	}

	//TODO update this subroutine
	public static  ArrayList<Task>  getTaskListForAllProjects(String type) throws CoreException{
		ArrayList<Task> taskList=new ArrayList<Task>();
		for(IProject project:getAllProjects()){
			IMarker[] markers = project.findMarkers(IMarker.TASK, true, IResource.DEPTH_INFINITE);
			for(IMarker marker:markers){
				if(!marker.getAttribute(IMarker.DONE,new Boolean(false))){
					Task task=getTask(marker,type);
					if(task!=null)
						taskList.add(task);
				}
			}
		}
		return taskList;
	}

	private static Task getTask(IMarker marker, String tabType) throws CoreException {

		String message=marker.getAttribute(IMarker.MESSAGE,"na");
		Matcher taskMatcher = Task.TASKLIPSE_PATTERN_TASK.matcher(message);
		if(!taskMatcher.matches()){
			return null;
		}

		boolean isArchived= shouldBeAdded(marker,message);
		if(!isArchived && "Archived".equals(tabType)){
			return null;
		}

		Map<String,Object> attrMap=marker.getAttributes();
		Task task=new Task(Long.parseLong(attrMap.get("id").toString()),marker);
		return task;
	}

	private static boolean shouldBeAdded(IMarker marker, String message) {
		Boolean isDone=marker.getAttribute(IMarker.DONE, false);
		if(isDone)
			return false;

		return true;
	}

	public static String getTaskType(IMarker marker) {
		return "TODO";
	}

}
