package ua.vboden.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.collections.ObservableList;
import ua.vboden.dto.JsonIoObject;
import ua.vboden.dto.TranslationRow;
import ua.vboden.entities.DictionaryEntry;
import ua.vboden.entities.Language;
import ua.vboden.entities.Word;

@Service
public class IOService {

	@Autowired
	private EntryService entryService;

	public void exportToFile(ObservableList<TranslationRow> selectedEntries, String fileName) {
		List<TranslationRow> entries = selectedEntries.stream().collect(Collectors.toList());
		Collections.sort(entries, TranslationRow.lastAtEndComparator());
		File directory = new File("io/exports/");
		if (!directory.exists()) {
			directory.mkdirs();
		}
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("io/exports/" + fileName, true))) {
//			Files.createDirectories(Paths.get("io/exports/"));
			for (TranslationRow row : entries) {
				String[] wordParts = row.getWord().split("\n");
				String line = wordParts[0] + "|" + (wordParts.length == 2 ? wordParts[1] : "[]") + "|"
						+ row.getTranslation().replaceAll("\n", " ");
				writer.write(line);
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void exportToFile(List<DictionaryEntry> entries, String dir, String fileName) {
		Collections.sort(entries, (a, b) -> a.getId() - b.getId());
		File directory = new File("io/exports/" + dir);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("io/exports/" + dir + fileName, true))) {
			for (DictionaryEntry entry : entries) {
				String line = entry.getWord().getWord() + "|["
						+ (entry.getTranscription() != null ? entry.getTranscription() : "") + "]|"
						+ entry.getTranslation().getWord()
						+ (StringUtils.isNotBlank(entry.getTranslation().getNotes())
								? " (" + entry.getTranslation().getNotes() + ")"
								: "");
				writer.write(line);
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void exportEntries(String fileName, List<DictionaryEntry> entries, String dirName) {
		Map<String, List<DictionaryEntry>> grouped = groupByLanguages(entries);
		for (Entry<String, List<DictionaryEntry>> entry : grouped.entrySet()) {
			List<DictionaryEntry> groupedEntries = entry.getValue();
			if (groupedEntries.size() > 0) {
				String name = entry.getKey().replace(">", "-") + '_';
				name += fileName.replaceAll(" ", "_") + "_" + getCurrentDate() + ".vcb";
				exportToFile(groupedEntries, dirName + getCurrentDate() + "/", name);
			}
		}
	}

	private String getCurrentDate() {
		return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
	}

	private Map<String, List<DictionaryEntry>> groupByLanguages(List<DictionaryEntry> entries) {
		Map<String, List<DictionaryEntry>> grouped = new HashMap<>();
		for (DictionaryEntry entry : entries) {
			String lang = getLanguage(entry.getWord()) + ">" + getLanguage(entry.getTranslation());
			grouped.putIfAbsent(lang, new ArrayList<>());
			grouped.get(lang).add(entry);
		}
		return grouped;
	}

	private String getLanguage(Word word) {
		return word.getLanguage().getCode();
	}

	public void exportToJsonFile(ObservableList<TranslationRow> selectedEntries, String fileName) {
		String filePath = "io/exports_json/";
		File directory = new File(filePath);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		JsonIoObject ioObject = new JsonIoObject();
		ioObject.setEntries(entryService.getAllBySelected(selectedEntries));
		Set<Language> languages = new HashSet<>();
		ioObject.getEntries().forEach(entry ->languages.add(entry.getWord().getLanguage()));
		ioObject.getEntries().forEach(entry ->languages.add(entry.getTranslation().getLanguage()));
		ioObject.setLanguages(languages);
		try {
			new ObjectMapper().writeValue(new File(filePath + fileName), ioObject);
		} catch (IOException e) {
			e.printStackTrace();
		}
//		try {
//			String result = new ObjectMapper().writeValueAsString(ioObject);
//			try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath + fileName, true))) {
//					writer.write(result);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		} catch (JsonProcessingException je) {
//			je.printStackTrace();
//		}
	}

}
