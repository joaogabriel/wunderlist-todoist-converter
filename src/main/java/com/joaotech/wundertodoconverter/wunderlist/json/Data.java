package com.joaotech.wundertodoconverter.wunderlist.json;

public class Data {

	private String userId;
	private String exported;

	private java.util.List<List> lists;
	private java.util.List<Note> notes;
	private java.util.List<Task> tasks;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getExported() {
		return exported;
	}

	public void setExported(String exported) {
		this.exported = exported;
	}

	public java.util.List<List> getLists() {
		return lists;
	}

	public void setLists(java.util.List<List> lists) {
		this.lists = lists;
	}

	public java.util.List<Note> getNotes() {
		return notes;
	}

	public void setNotes(java.util.List<Note> notes) {
		this.notes = notes;
	}

	public java.util.List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(java.util.List<Task> tasks) {
		this.tasks = tasks;
	}

}
