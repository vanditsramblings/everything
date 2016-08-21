package com.rambler.tasklipse.model;

public class Task {

	private String taskName;
	private String taskType;
	private int priority=0;
	private String message;
	private String category;
	private String createdTime;
	private String taskResource;
	private String taskProject;
	private int dueDays=0;
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
	
	
	
}
