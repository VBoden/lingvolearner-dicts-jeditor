package ua.vboden.converters;

import org.springframework.stereotype.Component;

import ua.vboden.dto.WordData;
import ua.vboden.entities.Word;

@Component
public class WordConverter extends AbstractConverter<Word, WordData> {

	@Override
	public WordData convert(Word source) {
		WordData target = new WordData();
		target.setId(source.getId());
		target.setWord(source.getWord());
		target.setNotes(source.getNotes());
		target.setLanguage(source.getLanguage().getName());
		target.setCategories(source.getCategory() == null ? null
				: source.getCategory().stream().map(cat -> cat.getName()).reduce((a, b) -> a + "; " + b).orElse(null));
		return target;
	}

}
