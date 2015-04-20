package com.joaotech.wundertodoconverter.node;

public class Note {

	private String id;
	private String taskId;
	private String content;

	public Note(String id, String taskId, String content) {
		super();
		this.id = id;
		this.taskId = taskId;
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
