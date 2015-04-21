package com.joaotech.wundertodoconverter.wunderlist.parser;

import java.io.File;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joaotech.wundertodoconverter.wunderlist.json.Data;
import com.joaotech.wundertodoconverter.wunderlist.json.Note;
import com.joaotech.wundertodoconverter.wunderlist.json.Task;

public class WunderlistJSONParser {

	private File json;
	private Data data;
	
	public WunderlistJSONParser(File json) {
		this.json = json;
	}
	
	public Data parseData() {
		
		try {
			ObjectMapper dataMapper = new ObjectMapper();

			JsonNode dataNode = dataMapper.readTree(json).get("data");

			extractLists(dataNode.get("lists").elements());
			extractNotes(dataNode.get("notes").elements());
			extractTasks(dataNode.get("tasks").elements());
			
		} catch (Exception e) {
			System.err.print("Error on JSON process");
			e.printStackTrace(System.err);
		}
		
		return data;
	}
	
	private void extractLists(Iterator<JsonNode> elements) {
		JsonNode listNode = null;
		
		while (elements.hasNext()) {
			listNode = elements.next();
			
			//lists.put(listNode.get("id").asText(), listNode.get("title").asText());
		}
	}
	
	private void extractNotes(Iterator<JsonNode> elements) {
		JsonNode noteNode = null;
		
		while (elements.hasNext()) {
			noteNode = elements.next();
			
			data.getNotes().add(new Note(noteNode.get("id").asText(), noteNode.get("task_id").asText(), noteNode.get("content").asText()));
		}
	}
	
	private void extractTasks(Iterator<JsonNode> elements) {
		Task task = null;
		JsonNode taskNode = null;
		
		while (elements.hasNext()) {
			taskNode = elements.next();
			
			task = new Task();
			task.setId(taskNode.get("id").asText());
			task.setListId(taskNode.get("list_id").asText());
			task.setTitle(taskNode.get("title").asText());
			task.setCompleted(taskNode.get("completed").asBoolean());
			task.setStarred(taskNode.get("starred").asBoolean());

			if (taskNode.get("due_date") != null) {
				task.setDueDate(taskNode.get("due_date").asText());
			}
			
			data.getTasks().add(task);
		}
	}
	
}
