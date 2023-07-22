package ua.vboden.converters;

import org.springframework.stereotype.Component;

import ua.vboden.dto.TranslationRow;
import ua.vboden.entities.DictionaryEntry;

@Component
public class TranslationConverter extends AbstractConverter<DictionaryEntry, TranslationRow> {

	@Override
	public TranslationRow convert(DictionaryEntry source) {
//		return  new TranslationRow(source.getId(), source.getWord().getWord(), source.getTranslation().getWord());
		TranslationRow row = new TranslationRow();
		row.setRecordId(source.getId());
		row.setWord(source.getWord().getWord());
		row.setTranslation(source.getTranslation().getWord());
		return row;
	}

}
