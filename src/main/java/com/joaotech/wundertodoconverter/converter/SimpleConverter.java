package com.joaotech.wundertodoconverter.converter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.joaotech.wundertodoconverter.node.Note;
import com.joaotech.wundertodoconverter.node.Task;

/**
 * TODO
 * 	- remover caracteres especiais
 *  - zipar txts
 *  - encoding
 *
 */
public class SimpleConverter {

	private File json;

	private Map<String, String> lists;
	private List<Note> notes;
	private List<Task> tasks;
	
	public SimpleConverter(File json) {
		this.json = json;
		
		lists = new HashMap<String, String>();
		notes = new ArrayList<Note>();
		tasks = new ArrayList<Task>();
	}

	public void processJson(String generationDir) {
		
		try {
			ObjectMapper mapper = new ObjectMapper();

			JsonNode dataNode = mapper.readTree(json).get("data");

			extractLists(dataNode);
			extractNotes(dataNode);
			extractTasks(dataNode);
			
			generateFiles(generationDir);
		} catch (Exception e) {
			System.err.print("Error on JSON process");
			e.printStackTrace(System.err);
		}
		
	}

	private void generateFiles(String generationDir) throws FileNotFoundException, ParseException {
		PrintWriter writerListFile = null;
		StringBuilder taskDescription = null;
		
		// iterar listas
		for (Map.Entry<String, String> list : lists.entrySet()) {
			writerListFile = new PrintWriter(generationDir + File.separator + list.getValue() + ".txt");
			
			// para cada lista pegar as atividades
			for (Task task : tasks) {
				if (task.getListId().equals(list.getKey())) {
					taskDescription = new StringBuilder();
					
					if (!task.isCompleted()) {
						taskDescription.append(task.getTitle());
						
						if (task.getDueDate() != null) {
							// TODO formatar data
							taskDescription.append(" [[date ").append(formatDueDate(task.getDueDate())).append("]]");
						}
						
						writerListFile.println(taskDescription.toString());
						
						// para cada atividade pegar as notas
						for (Note note : notes) {
							if (note.getTaskId().equals(task.getId())) {
								if (note.getContent().contains("\n")) {
									String[] contents = note.getContent().split("\n");
									
									for (String content : contents) {
										writerListFile.println("[[NOTE]]: " + content);
									}
								} else {
									writerListFile.println("[[NOTE]]: " + note.getContent());
								}
							}
						}
					}
				}
			}

			writerListFile.flush();
			writerListFile.close();
		}
		
		
	}
	
	private String formatDueDate(String dueDate) throws ParseException {
		SimpleDateFormat wunderlistDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date wunderlistDueDate = wunderlistDateFormat.parse(dueDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(wunderlistDueDate);
		String day = calendar.get(Calendar.DAY_OF_MONTH) + "";
		String month = getMonthNameForInt(calendar.get(Calendar.MONTH)).substring(0, 3);
		return day + " " + month;
	}
	
	private String getMonthNameForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

	private void extractLists(JsonNode dataNode) {
		JsonNode listNode = null;
		JsonNode listsNode = dataNode.get("lists");
		Iterator<JsonNode> elements = listsNode.elements();
		
		while (elements.hasNext()) {
			listNode = elements.next();
			
			lists.put(listNode.get("id").asText(), listNode.get("title").asText());
		}
	}
	
	private void extractNotes(JsonNode dataNode) {
		JsonNode noteNode = null;
		JsonNode notesNode = dataNode.get("notes");
		Iterator<JsonNode> elements = notesNode.elements();
		
		while (elements.hasNext()) {
			noteNode = elements.next();
			
			notes.add(new Note(noteNode.get("id").asText(), noteNode.get("task_id").asText(), noteNode.get("content").asText()));
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

			if (taskNode.get("due_date") != null) {
				task.setDueDate(taskNode.get("due_date").asText());
			}
			
			tasks.add(task);
		}
	}
	
}
