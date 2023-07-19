package ua.vboden.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;

@Entity
public class DictionaryEntry {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne
	private Word word;
	private String transcription;
	@OneToOne
	private Word translation;
	@ManyToMany
	private List<Dictionary> dictionary;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

}
