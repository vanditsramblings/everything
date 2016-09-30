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
	public static final Pattern TASKLIPSE_PATTERN_PRIORITY1 = Pattern.compile(".*#!:(.*?);");
	public static final Pattern TASKLIPSE_PATTERN_PRIORITY2 = Pattern.compile(".*#p:(.*?);");
	public static final Pattern TASKLIPSE_PATTERN_PRIORITY3 = Pattern.compile(".*#priority:(.*?);");
	
	public static final Pattern TASKLIPSE_PATTERN_TYPE1 = Pattern.compile(".*#t:(.*?);");
	public static final Pattern TASKLIPSE_PATTERN_TYPE2 = Pattern.compile(".*#type:(.*?);");
	
	public static final Pattern TASKLIPSE_PATTERN_MESSAGE1 = Pattern.compile(".*#m:(.*?);");
	public static final Pattern TASKLIPSE_PATTERN_MESSAGE2 = Pattern.compile(".*#message:(.*?);");
	public static final Pattern TASKLIPSE_PATTERN_MESSAGE3 = Pattern.compile(".*#desc:(.*?);");
	public static final Pattern TASKLIPSE_PATTERN_MESSAGE4 = Pattern.compile(".*#d:(.*?);");

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
	//asdf
	//TODO updat ehtis subroutine
	public static  ArrayList<Task>  getTaskListForAllProjects(String type) throws CoreException{
		ArrayList<Task> taskList=new ArrayList<Task>();
		for(IProject project:getAllProjects()){
			IMarker[] markers = project.findMarkers(IMarker.TASK, true, IResource.DEPTH_INFINITE);
			for(IMarker marker:markers){
				Task task=getTask(marker,type);
				if(task!=null)
					taskList.add(task);
			}
		}
		return taskList;
	}

	private static Task getTask(IMarker marker, String type2) throws CoreException {
		long createdTime=0;
		String priority="0";
		String type="Todo";
		String parsedMessage="na";
		try{
			createdTime=marker.getCreationTime();
		}
		catch(CoreException e){
			createdTime=1;
		}

		String message=marker.getAttribute(IMarker.MESSAGE,"na");
		Matcher taskMatcher = TASKLIPSE_PATTERN_TASK.matcher(message);
		if(!taskMatcher.matches()){
			return null;
		}
		
		//getting priority of task , defaults to 0
		priority=getPriority(message);
		//getting type/category of task , defaults to "Todo"
		type=getType(message);
		//getting message of task , defaukts to "na"
		parsedMessage=getMessage(message);
		
		Map<String,Object> attrMap=marker.getAttributes();
		Task task=new Task(Long.parseLong(attrMap.get("id").toString()),marker.getAttribute(IMarker.MARKER, "na"),type,priority,parsedMessage,"TASK",createdTime,marker.getResource().getFullPath().toOSString(),attrMap.get("lineNumber").toString());
		return task;
	}

	private static String getMessage(String message) {
		Matcher messageMatcher = TASKLIPSE_PATTERN_MESSAGE1.matcher(message.toLowerCase());
		if(messageMatcher.find()){
			return messageMatcher.group(1);
		}
		messageMatcher = TASKLIPSE_PATTERN_MESSAGE2.matcher(message.toLowerCase());
		if(messageMatcher.find()){
			return messageMatcher.group(1);
		}
		messageMatcher = TASKLIPSE_PATTERN_MESSAGE3.matcher(message.toLowerCase());
		if(messageMatcher.find()){
			return messageMatcher.group(1);
		}
		messageMatcher = TASKLIPSE_PATTERN_MESSAGE4.matcher(message.toLowerCase());
		if(messageMatcher.find()){
			return messageMatcher.group(1);
		}	
		return "na";
	}

	private static String getPriority(String message) {
		Matcher priorityMatcher = TASKLIPSE_PATTERN_PRIORITY1.matcher(message.toLowerCase());
		if(priorityMatcher.find()){
			return priorityMatcher.group(1);
		}
		priorityMatcher = TASKLIPSE_PATTERN_PRIORITY2.matcher(message.toLowerCase());
		if(priorityMatcher.find()){
			return priorityMatcher.group(1);
		}
		priorityMatcher = TASKLIPSE_PATTERN_PRIORITY3.matcher(message.toLowerCase());
		if(priorityMatcher.find()){
			return priorityMatcher.group(1);
		}
		
		return "0";
	}
	
	private static String getType(String message) {
		Matcher matcher = TASKLIPSE_PATTERN_TYPE1.matcher(message.toLowerCase());
		if(matcher.find()){
			return matcher.group(1);
		}
		matcher = TASKLIPSE_PATTERN_TYPE2.matcher(message.toLowerCase());
		if(matcher.find()){
			return matcher.group(1);
		}
		return "Todo";
	}

	public static String getTaskType(IMarker marker) {
		return "TODO";
	}


}
