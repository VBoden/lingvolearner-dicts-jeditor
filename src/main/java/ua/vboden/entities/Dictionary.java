package ua.vboden.entities;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "words_dictionary")
public class Dictionary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	@OneToOne
	@JoinColumn(name = "language_from_id")
	private Language languageFrom;
	@OneToOne
	@JoinColumn(name = "language_to_id")
	private Language languageTo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Language getLanguageFrom() {
		return languageFrom;
	}

	public void setLanguageFrom(Language languageFrom) {
		this.languageFrom = languageFrom;
	}

	public Language getLanguageTo() {
		return languageTo;
	}

	public void setLanguageTo(Language languageTo) {
		this.languageTo = languageTo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dictionary other = (Dictionary) obj;
		return id == other.id;
	}

}
