package ua.vboden.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import ua.vboden.dto.CodeString;
import ua.vboden.dto.JsonIoObject;
import ua.vboden.entities.Category;
import ua.vboden.entities.Dictionary;
import ua.vboden.entities.DictionaryEntry;
import ua.vboden.entities.Language;
import ua.vboden.entities.Word;

@Service
public class ImportService {

	private static Pattern PATTERN_WITH_PERCENT = Pattern.compile("(.*?)\\|(\\[.*?\\])\\|(.*?)%");
	private static Pattern PATTERN_WITHOUT_PERCENT = Pattern.compile("(.*?)\\|(\\[.*?\\])\\|(.*?)$");

	@Autowired
	private LanguageService languageService;

	@Autowired
	private EntryService entryService;

	@Autowired
	private WordService wordService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private DictionaryService dictionaryService;

	public int importFromFile(File file, CodeString from, CodeString to, String dictionaryName) {
		int count = 0;
		try (InputStream inputStream = new FileInputStream(file)) {
			boolean linesWithPercent = false;
			StringBuilder stringBuilder = new StringBuilder();
			try (BufferedReader reader = new BufferedReader(
					new InputStreamReader(Objects.requireNonNull(inputStream), "UTF8"))) {
				String line;
				Language languageFrom = languageService.findEntity(from);
				Language languageTo = languageService.findEntity(to);
				Dictionary dict = createDictionary(dictionaryName, languageFrom, languageTo);
				List<DictionaryEntry> entries = new ArrayList<>();
				while ((line = reader.readLine()) != null) {
					if (linesWithPercent || line.contains("%")) {
						linesWithPercent = true;
						stringBuilder.append(line);
					} else {
						entries.addAll(
								createDictionaryEntries(line, PATTERN_WITHOUT_PERCENT, languageFrom, languageTo, dict));
					}
				}
				if (linesWithPercent) {
					entries.addAll(createDictionaryEntries(stringBuilder.toString(), PATTERN_WITH_PERCENT, languageFrom,
							languageTo, dict));
				}
				entryService.saveAll(entries);
				count = entries.size();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return count;
	}

	private List<DictionaryEntry> createDictionaryEntries(String line, Pattern pattern, Language languageFrom,
			Language languageTo, Dictionary dict) {

		Matcher matcher = pattern.matcher(line);
		List<DictionaryEntry> entries = new ArrayList<>();
		while (matcher.find()) {
			DictionaryEntry entry = createEntry(languageFrom, languageTo, matcher);
			if (dict != null) {
				entry.setDictionary(List.of(dict));
			}
			entries.add(entry);
		}
		return entries;
	}

	private Dictionary createDictionary(String dictionaryName, Language languageFrom, Language languageTo) {
		Dictionary dict = null;
		if (StringUtils.isNotBlank(dictionaryName)) {
			dict = new Dictionary();
			dict.setName(dictionaryName);
			dict.setLanguageFrom(languageFrom);
			dict.setLanguageTo(languageTo);
			dictionaryService.save(dict);
		}
		return dict;
	}

	private DictionaryEntry createEntry(Language languageFrom, Language languageTo, Matcher matcher) {
		Word wordEntity = createWord(languageFrom, matcher.group(1).trim(), null);
		Word translationEntity = createWord(languageTo, matcher.group(3).trim(), null);
		DictionaryEntry entry = new DictionaryEntry();
		entry.setWord(wordEntity);
		entry.setTranslation(translationEntity);
		entry.setTranscription(matcher.group(2).trim().replaceAll("[\\[\\]]", ""));
		return entry;
	}

	private Word createWord(Language languageFrom, String text, String notes) {
		Word wordEntity = new Word();
		wordEntity.setWord(text);
		wordEntity.setLanguage(languageFrom);
		wordEntity.setNotes(notes);
		wordService.save(wordEntity);
		return wordEntity;
	}

	public int importFromJsonFile(File file, String dictionaryName) {
		int count = 0;
		try {
			JsonIoObject ioObject = new ObjectMapper().readValue(file, JsonIoObject.class);
			List<Language> existingLanguages = languageService.findAll();
			for (Language lang : ioObject.getLanguages()) {
				if (!existingLanguages.contains(lang)) {
					System.out.println(lang.toString() + " not exists! Will be created.");
					languageService.save(lang);
				}
			}
			List<DictionaryEntry> entries = ioObject.getEntries();
			Language languageFrom = entries.get(0).getWord().getLanguage();
			Language languageTo = entries.get(0).getTranslation().getLanguage();
			Dictionary dict = createDictionary(dictionaryName, languageFrom, languageTo);
			for (DictionaryEntry entry : entries) {
				saveWordAsNew(entry.getWord());
				saveWordAsNew(entry.getTranslation());
				entry.setId(0);
				for (Dictionary dictionary : entry.getDictionary()) {
					Dictionary result = dictionaryService.findEntityByName(dictionary.getName());
					if (result == null) {
						dictionary.setId(0);
						dictionaryService.save(dictionary);
					}
				}
				entry.getDictionary().add(dict);
			}
			entryService.saveAll(entries);
			count = entries.size();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return count;
	}

	private Word saveWordAsNew(Word wordEntity) {
		wordEntity.setId(0);
		for (Category category : wordEntity.getCategory()) {
			Category result = categoryService.findEntityByName(category.getName());
			if (result == null) {
				category.setId(0);
				categoryService.save(category);
			}
		}
		wordService.save(wordEntity);
		return wordEntity;
	}
}
