package com.rambler.tasklipse.model;

/**
 * @author vandit
 * 
 */

public enum TaskColumn {
	
	CB(" ","INT",20,0),
	PRIORITY("!","INT",40,1),
	TYPE("Type","STRING",100,2),
	MESSAGE("Message","STRING",350,3),
	RESOURCE("Resource","STRING",300,4),
	CREATEDTIME("Created Time","STRING",100,5),
	ACTIONS("Actions","STRING",50,6);

	private String displayName;
	private String colType;
	private int bounds=0;
	private int colIndex=0;

	private TaskColumn(String displayName,String colType,int bounds,int colIndex){
		this.displayName=displayName;
		this.colType=colType;
		this.bounds=bounds;
		this.colIndex=colIndex;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getColType() {
		return colType;
	}

	public void setColType(String colType) {
		this.colType = colType;
	}

	public int getBounds() {
		return bounds;
	}

	public void setBounds(int bounds) {
		this.bounds = bounds;
	}

	public int getColIndex() {
		return colIndex;
	}

	public void setColIndex(int colIndex) {
		this.colIndex = colIndex;
	}
}
