package ua.vboden.controllers;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

@Component
public class MainWindowController implements ApplicationContextAware {
	private ApplicationContext applicationContext;
	@FXML
	private MenuItem menuQuit;

	@FXML
	public void quit() {
		Platform.exit();
		System.exit(0);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (this.applicationContext != null) {
			return;
		}

		this.applicationContext = applicationContext;
	}

}
