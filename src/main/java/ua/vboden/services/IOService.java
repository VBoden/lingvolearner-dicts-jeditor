package ua.vboden.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import javafx.collections.ObservableList;
import ua.vboden.dto.TranslationRow;

@Service
public class IOService {

	public void exportToFile(ObservableList<TranslationRow> selectedEntries, String fileName) {
		List<TranslationRow> entries = selectedEntries.stream().collect(Collectors.toList());
		Collections.sort(entries, TranslationRow.lastAtEndComparator());
		File directory = new File("io/exports/");
		if (!directory.exists()) {
			directory.mkdirs();
		}
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("io/exports/" + fileName, true))) {
//			Files.createDirectories(Paths.get("io/exports/"));
			for (TranslationRow row : entries) {
				String[] wordParts = row.getWord().split("\n");
				String line = wordParts[0] + "|" + (wordParts.length == 2 ? wordParts[1] : "[]") + "|"
						+ row.getTranslation().replaceAll("\n", " ");
				System.out.println(line);
				writer.write(line);
				writer.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
