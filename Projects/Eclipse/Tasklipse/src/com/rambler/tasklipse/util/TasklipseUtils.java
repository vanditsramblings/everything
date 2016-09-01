package com.rambler.tasklipse.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	public static  ArrayList<Task>  getTaskListForAllProjects() throws CoreException{
		ArrayList<Task> taskList=new ArrayList<Task>();
		for(IProject project:getAllProjects()){
			IMarker[] markers = project.findMarkers(IMarker.TASK, true, IResource.DEPTH_INFINITE);
			for(IMarker marker:markers){
				Task task=getTask(marker);
				taskList.add(task);
			}
		}
		return taskList;
	}

	private static Task getTask(IMarker marker) {
		String type="";
		long createdTime=0;
		try{
			type=marker.getType();
		}
		catch(CoreException e){
			type="na";
		}
		try{
			createdTime=marker.getCreationTime();
		}
		catch(CoreException e){
			createdTime=1;
		}
		
		Task task=new Task(type,type,"1",marker.getAttribute(IMarker.MESSAGE,"na"),marker.getAttribute(IMarker.TASK,"na"),createdTime,marker.getAttribute(IMarker.LOCATION,"1"),marker.getAttribute(IMarker.LINE_NUMBER,"1"));
		return task;
	}

	public static String getTaskType(IMarker marker) {
		return "TODO";
	}


}
