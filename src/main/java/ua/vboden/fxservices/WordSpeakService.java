package ua.vboden.fxservices;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class WordSpeakService extends Service<Boolean> {
	private Logger logger = LoggerFactory.getLogger(WordSpeakService.class);

	private String word;
	private String language;

	public WordSpeakService(String word, String language) {
		this.word = word;
		this.language = language;
	}

	@Override
	protected Task<Boolean> createTask() {
		return new Task<Boolean>() {
			@Override
			protected Boolean call() {
				String mp3Path = "/Disk_A/Words_mp3/%s/%s/%s.mp3";
				String let = String.valueOf(word.charAt(0)).toLowerCase();
				String filePath = String.format(mp3Path, language, let, word);
				logger.info("In speak word. Playing audio file: " + filePath);
				try {
					Media hit = new Media(new File(filePath).toURI().toString());
					MediaPlayer mediaPlayer = new MediaPlayer(hit);
					mediaPlayer.play();
				} catch (Exception ex) {
					logger.error("Error occured during playback process: " + ex.getMessage());
				}
				return true;
			}
		};
	}
}
