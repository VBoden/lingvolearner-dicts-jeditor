package ua.vboden.entities;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "words_dictionaryentry")
public class DictionaryEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@OneToOne
	private Word word;
	private String transcription;
	@OneToOne
	private Word translation;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "words_dictionaryentry_dictionary", joinColumns = @JoinColumn(name = "dictionaryentry_id"), inverseJoinColumns = @JoinColumn(name = "dictionary_id"))
	private List<Dictionary> dictionary;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Word getWord() {
		return word;
	}

	public void setWord(Word word) {
		this.word = word;
	}

	public String getTranscription() {
		return transcription;
	}

	public void setTranscription(String transcription) {
		this.transcription = transcription;
	}

	public Word getTranslation() {
		return translation;
	}

	public void setTranslation(Word translation) {
		this.translation = translation;
	}

	public List<Dictionary> getDictionary() {
		return dictionary;
	}

	public void setDictionary(List<Dictionary> dictionary) {
		this.dictionary = dictionary;
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
		DictionaryEntry other = (DictionaryEntry) obj;
		return id == other.id;
	}

}
