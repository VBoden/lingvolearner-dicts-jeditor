package ua.vboden.converters;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.vboden.dto.WordData;
import ua.vboden.entities.Word;
import ua.vboden.services.SessionService;

@Component
public class WordConverter extends AbstractConverter<Word, WordData> {

	@Autowired
	private SessionService sessionService;

	@Override
	public WordData convert(Word source) {
		WordData target = new WordData();
		target.setId(source.getId());
		target.setWord(source.getWord());
		target.setNotes(source.getNotes());
		target.setLanguage(source.getLanguage().getName());
		target.setCategories(source.getCategory() == null ? null
				: source.getCategory().stream().map(cat -> cat.getName()).reduce((a, b) -> a + "\n" + b).orElse(null));
		Map<Integer, Integer> wordUsages = sessionService.getWordUsages();
		int id = target.getId();
		if (wordUsages.containsKey(id)) {
			target.setUsages(wordUsages.get(id));
		} else {
			wordUsages.put(id, 0);
		}
		return target;
	}
}
