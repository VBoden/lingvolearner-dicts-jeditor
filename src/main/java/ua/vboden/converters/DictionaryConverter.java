package ua.vboden.converters;

import org.springframework.stereotype.Component;

import ua.vboden.dto.DictionaryData;
import ua.vboden.entities.Dictionary;

@Component
public class DictionaryConverter extends AbstractConverter<Dictionary, DictionaryData> {

	@Override
	public DictionaryData convert(Dictionary source) {
		DictionaryData row = new DictionaryData();
		row.setId(source.getId());
		row.setTitle(source.getName());
		row.setLangFrom(source.getLanguageFrom().getName());
		row.setLangTo(source.getLanguageTo().getName());
		return row;
	}

}
