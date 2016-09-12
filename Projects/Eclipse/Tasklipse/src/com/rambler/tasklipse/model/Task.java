package com.rambler.tasklipse.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author vandit
 * 
 */

public class Task {
	
	private long id=0;
	private String taskName;
	private String taskType;
	private int priority=0;
	private String message;
	private String category;
	private String createdTime;
	private String taskResource;
	private String taskProject;
	private int dueDays=0;
	private int lineNumber=0;
	
	public Task(long id,String taskName,String taskType,String priority,String message,String category,long createdTime,String taskResource,String lineNumber){
		this.id=id;
		this.taskName=taskName;
		this.taskType=taskType;
		try{
			this.priority=Integer.parseInt(priority);
		}
		catch(NumberFormatException e){
			this.priority=1;
		}

		this.message=message;
		this.category=category;
		this.createdTime=getTime(createdTime);
		this.taskResource=taskResource;
		try{
			this.lineNumber=Integer.parseInt(lineNumber);
		}
		catch(NumberFormatException e){
			this.lineNumber=1;
		}
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



}
