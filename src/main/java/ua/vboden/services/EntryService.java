package ua.vboden.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ua.vboden.entities.DictionaryEntry;
import ua.vboden.repositories.EntryRepository;

@Service
public class EntryService {

	@Autowired
	private EntryRepository entryRepository;
	
	public List<DictionaryEntry> getAllEntries(){
		List<DictionaryEntry> result = new ArrayList<>();
		entryRepository.findAll().forEach(result::add);
		return result;		
	}
	
}
