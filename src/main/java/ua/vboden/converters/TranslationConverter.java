package ua.vboden.converters;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import ua.vboden.dto.TranslationRow;
import ua.vboden.entities.DictionaryEntry;

@Component
public class TranslationConverter extends AbstractConverter<DictionaryEntry, TranslationRow> {

	@Override
	public List<TranslationRow> convertAll(List<DictionaryEntry> sources) {
		List<TranslationRow> targets = new ArrayList<>();
		int num = 1;
		for (DictionaryEntry source : sources) {
			TranslationRow converted = convert(source);
			converted.setNumber(num);
			num++;
			targets.add(converted);
		}
		return targets;
	}

	@Override
	public TranslationRow convert(DictionaryEntry source) {
		TranslationRow row = new TranslationRow();
		row.setRecordId(source.getId());
		if(source.getWord()!=null) {
		row.setWord(source.getWord().getWord());
		row.setCategories(source.getWord().getCategory() == null ? null
				: source.getWord().getCategory().stream().map(cat -> cat.getName()).reduce((a, b) -> a + "\n" + b)
				.orElse(null));
		}
		row.setTranslation(source.getTranslation().getWord());
		row.setTransCategories(source.getTranslation().getCategory() == null ? null
				: source.getTranslation().getCategory().stream().map(cat -> cat.getName()).reduce((a, b) -> a + "\n" + b)
				.orElse(null));
		row.setDictionaries(source.getDictionary() == null ? null
				: source.getDictionary().stream().map(cat -> cat.getName()).reduce((a, b) -> a + "\n" + b)
						.orElse(null));
		return row;
	}

}
