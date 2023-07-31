package ua.vboden.fxservices;

import java.util.List;
import java.util.function.Function;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import ua.vboden.dto.TranslationRow;

public class EntrySearchService extends Service<List<TranslationRow>> {
	private Function<String, List<TranslationRow>> finder;
	private String word;

	public EntrySearchService(Function<String, List<TranslationRow>> finder, String word) {
		this.finder = finder;
		this.word = word;
	}

	@Override
	protected Task<List<TranslationRow>> createTask() {
		return new Task<List<TranslationRow>>() {
			@Override
			protected List<TranslationRow> call() {
				return finder.apply(word);
			}
		};
	}
}
