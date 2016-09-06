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

import search.MessagePatternMatcher;

import com.rambler.tasklipse.model.Task;

/**
 * @author vandit
 * 
 */

public class TasklipseUtils {


	public static final Pattern TASKLIPSE_PATTERN_TASK = Pattern.compile(".*#\\$.*");

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

	public static  ArrayList<Task>  getTaskListForAllProjects() throws CoreException{
		ArrayList<Task> taskList=new ArrayList<Task>();
		for(IProject project:getAllProjects()){
			IMarker[] markers = project.findMarkers(IMarker.TASK, true, IResource.DEPTH_INFINITE);
			for(IMarker marker:markers){
				Task task=getTask(marker);
				if(task!=null)
					taskList.add(task);
			}
		}
		return taskList;
	}

	private static Task getTask(IMarker marker) throws CoreException {
		long createdTime=0;
		try{
			createdTime=marker.getCreationTime();
		}
		catch(CoreException e){
			createdTime=1;
		}

		String message=marker.getAttribute(IMarker.MESSAGE,"na");
		Matcher matcher = TASKLIPSE_PATTERN_TASK.matcher(message);
		if(!matcher.matches()){
			return null;
		}
		Task task=new Task(marker.getId(),marker.getAttribute(IMarker.MARKER, "na"),"TASK","1",message,"TASK",createdTime,marker.getResource().getFullPath().toOSString(),marker.getAttribute(IMarker.LINE_NUMBER,"1"));
		return task;
	}

	public static String getTaskType(IMarker marker) {
		return "TODO";
	}


}
