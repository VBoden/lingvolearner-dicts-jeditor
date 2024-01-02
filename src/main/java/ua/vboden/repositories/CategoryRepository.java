package ua.vboden.repositories;

import org.springframework.data.repository.CrudRepository;

import ua.vboden.entities.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
	Category findByName(String name);
}
