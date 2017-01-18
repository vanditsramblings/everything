package com.rambler.tasklipse.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

/**
 * @author vandit
 * 
 */

public class Task {

	private long id=0;
	private String taskName;
	private String taskType="TODO";
	private int priority=0;
	private String message="na";
	private String category;
	private String createdTime="0";
	private String taskResource;
	private String taskProject;
	private int dueDays=0;
	private int lineNumber=0;
	private IMarker marker=null;
	private boolean archived=false;

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

	public static final Pattern TASKLIPSE_PATTERN_ARCHIVED1 = Pattern.compile(".*#archived");
	public static final Pattern TASKLIPSE_PATTERN_ARCHIVED2 = Pattern.compile(".*#a");

	public Task(long id, IMarker marker) {

		this.marker=marker;
		this.id=id;
		try{
			createdTime=getTime(marker.getCreationTime());
		}
		catch(CoreException e){
			createdTime="1";
		}

		String fullMessage=marker.getAttribute(IMarker.MESSAGE,"na");

		//getting priority of task , defaults to 0
		String priorityStr=getPriority(fullMessage);

		try{
			this.priority=Integer.parseInt(priorityStr);
		}
		catch(NumberFormatException e){
			this.priority=1;
		}

		//getting type/category of task , defaults to "Todo"
		taskType=getType(fullMessage);
		//getting message of task , defaults to "na"
		message=getMessage(fullMessage);

	}

	private String getTime(long createdTime) {
		Date date = new Date(createdTime);
		DateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");
		String dateFormatted = formatter.format(date);
		return dateFormatted;
	}

	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getTaskResource() {
		return taskResource;
	}
	public void setTaskResource(String taskResource) {
		this.taskResource = taskResource;
	}
	public String getTaskProject() {
		return taskProject;
	}
	public void setTaskProject(String taskProject) {
		this.taskProject = taskProject;
	}
	public int getDueDays() {
		return dueDays;
	}
	public void setDueDays(int dueDays) {
		this.dueDays = dueDays;
	}
	public int getLinenumber() {
		return lineNumber;
	}
	public void setLinenumber(int linenumber) {
		this.lineNumber = linenumber;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
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

	public boolean delete(){
		try{
			if(marker!=null)
			{
				marker.setAttribute(IMarker.USER_EDITABLE, "true");
				marker.delete();
			}
			//marker.getResource().deleteMarkers(""+marker.getId(), true, IProject.DEPTH_INFINITE);
			return true;
		}
		catch(CoreException e){
			return false;
		}
	}

	public boolean archive() {
		try {
			if(marker!=null){
				String fullMessage=marker.getAttribute(IMarker.MESSAGE,"na");
				fullMessage.concat(" #archived");
				marker.setAttribute(IMarker.MESSAGE, fullMessage);
				archived=true;
				return true;
			}
		}
		catch (CoreException e) {
			e.printStackTrace();
		}
		return false;
	}

}
