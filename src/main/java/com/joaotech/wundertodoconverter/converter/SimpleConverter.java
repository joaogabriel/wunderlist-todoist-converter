package com.joaotech.wundertodoconverter.converter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joaotech.wundertodoconverter.node.Note;
import com.joaotech.wundertodoconverter.node.Task;

public class SimpleConverter {

	private File json;

	private Map<String, String> lists;
	private List<Note> nodes;
	private List<Task> tasks;
	
	public SimpleConverter(File json) {
		this.json = json;
		
		lists = new HashMap<String, String>();
		nodes = new ArrayList<Note>();
		tasks = new ArrayList<Task>();
	}

	public void processJson(File generationDir) {
		try {
			ObjectMapper mapper = new ObjectMapper();

			JsonNode node = mapper.readTree(json);

			JsonNode dataNode = node.get("data");

			extractLists(dataNode);
			extractNotes(dataNode);
			extractTasks(dataNode);
			
			generateFiles(generationDir);
		} catch (Exception e) {
			System.err.print("Error on JSON process");
			e.printStackTrace(System.err);
		}
	}

	private void generateFiles(File generationDir) {
		
	}

	private void extractLists(JsonNode dataNode) {
		JsonNode listNode = null;
		JsonNode listsNode = dataNode.get("lists");
		Iterator<JsonNode> elements = listsNode.elements();
		
		while (elements.hasNext()) {
			listNode = elements.next();
			
			lists.put(listNode.get("id").toString(), listNode.get("title").toString());
		}
	}
	
	private void extractNotes(JsonNode dataNode) {
		JsonNode noteNode = null;
		JsonNode notesNode = dataNode.get("notes");
		Iterator<JsonNode> elements = notesNode.elements();
		
		while (elements.hasNext()) {
			noteNode = elements.next();
			
			nodes.add(new Note(noteNode.get("id").toString(), noteNode.get("task_id").toString(), noteNode.get("content").toString()));
		}
	}
	
	private void extractTasks(JsonNode dataNode) {
		Task task = null;
		JsonNode taskNode = null;
		JsonNode tasksNode = dataNode.get("tasks");
		Iterator<JsonNode> elements = tasksNode.elements();
		
		while (elements.hasNext()) {
			taskNode = elements.next();
			
			task = new Task();
			task.setId(taskNode.get("id").asText());
			task.setListId(taskNode.get("list_id").asText());
			task.setTitle(taskNode.get("title").asText());
			task.setCompleted(taskNode.get("completed").asBoolean());
			task.setStarred(taskNode.get("starred").asBoolean());
			
			tasks.add(task);
		}
	}
	
}
