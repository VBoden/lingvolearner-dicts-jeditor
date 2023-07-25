package ua.vboden.services;

import javafx.collections.ObservableList;

public interface EntityService<T, E> {

	void deleteSelected(ObservableList<? extends T> selected);

	E findEntity(T current);

}
