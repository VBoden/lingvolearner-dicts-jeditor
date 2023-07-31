package ua.vboden.controllers;

import java.io.IOException;
import java.util.ResourceBundle;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ua.vboden.services.SessionService;

public abstract class AbstractController implements ApplicationContextAware, Initializable {

	@Autowired
	private SessionService sessionService;

	private ApplicationContext applicationContext;

	private ResourceBundle resources;

	private Stage stage;

	abstract String getFXML();

	abstract String getTitle();

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (this.applicationContext != null) {
			return;
		}
		this.applicationContext = applicationContext;
	}

	public void showStage(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(getFXML()));
		resources = ResourceBundle.getBundle("bundles/localization");
		fxmlLoader.setResources(resources);
		fxmlLoader.setController(this);
		Parent parent = fxmlLoader.load();
//		Parent parent = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));

		Scene scene = new Scene(parent);
//		Scene scene = new Scene(parent, 510, 325);
		if (stage == null) {
			stage = new Stage();
		}
		this.stage = stage;
		stage.setTitle(getTitle());
		stage.setScene(scene);

		stage.show();
	}

	public Stage getStage() {
		return stage;
	}

	public SessionService getSessionService() {
		return sessionService;
	}

	public ResourceBundle getResources() {
		return resources;
	}

}
