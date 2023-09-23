package ua.vboden.services;

import java.util.List;

import javafx.collections.ObservableList;

public interface EntityService<T, E> {

	void deleteSelected(ObservableList<? extends T> selected);

	E findEntity(T current);

	void save(E entity);

	void saveAll(List<E> entities);

}
