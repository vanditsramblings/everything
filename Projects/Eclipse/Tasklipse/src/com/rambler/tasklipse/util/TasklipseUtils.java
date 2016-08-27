package com.rambler.tasklipse.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

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

	public static  List<IMarker>  getTaskListForAllProjects() throws CoreException{
		ArrayList<IMarker> markersList=new ArrayList<IMarker>();
		for(IProject project:getAllProjects()){
			IMarker[] markers = project.findMarkers(IMarker.TASK, true, IResource.DEPTH_INFINITE);
			markersList.addAll(Arrays.asList(markers));
		}
		return markersList;
	}

	public static String getTaskType(IMarker marker) {
		return "TODO";
	}


}
