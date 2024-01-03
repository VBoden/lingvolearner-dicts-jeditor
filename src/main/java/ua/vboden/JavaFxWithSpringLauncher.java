package ua.vboden;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javafx.application.Application;
import javafx.stage.Stage;
import ua.vboden.controllers.AbstractController;
import ua.vboden.services.PreferencesService;
import ua.vboden.services.SessionService;

@Configuration
@SpringBootApplication
public class JavaFxWithSpringLauncher extends Application {

	private static final String URL_KEY = "spring.datasource.url";

	private static final String DB_PREFIX = "jdbc:sqlite:";

	private static final String DOT = ".";

	private static ConfigurableApplicationContext applicationContext;

	private String dbPath;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() throws Exception {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(JavaFxWithSpringLauncher.class);
		builder.application().setWebApplicationType(WebApplicationType.NONE);
		applicationContext = builder.run(selectDbFile());
	}

	private String[] selectDbFile() {
		String lastDB = PreferencesService.getLastDB();
		if (PreferencesService.isSelectDbOnStart() || lastDB == null || !(new File(lastDB).exists())) {
			JFileChooser fileChooser = new JFileChooser();
			FileFilter filter = new FileNameExtensionFilter("db files", "sqlite3");
			fileChooser.setFileFilter(filter);
			fileChooser.setDialogTitle("Select DB file");
			if (lastDB != null && new File(lastDB).exists()) {
				fileChooser.setCurrentDirectory(new File(lastDB));
			} else {
				fileChooser.setCurrentDirectory(new File("."));
			}
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fileChooser.showOpenDialog(null);
			File file = fileChooser.getSelectedFile();
			if (file != null) {
				dbPath = file.getAbsolutePath();
				PreferencesService.setLastDB(dbPath);
				return new String[] { String.format("--%s=%s", URL_KEY, DB_PREFIX + file.getAbsolutePath()) };
			}
		} else if (lastDB != null && new File(lastDB).exists()) {
			dbPath = lastDB;
			return new String[] { String.format("--%s=%s", URL_KEY, DB_PREFIX + lastDB) };
		}
		return getParameters().getRaw().toArray(new String[0]);
	}

	@Override
	public void stop() throws Exception {
		applicationContext.close();
	}

	@Override
	public void start(Stage stage) throws IOException {
		final AppContextInitializer appContextInitializer = (AppContextInitializer) applicationContext
				.getBean("appContextInitializer");
		appContextInitializer.initApp();
		if (dbPath == null) {
			dbPath = (new File(DOT).getAbsolutePath()
					+ ((Environment) applicationContext.getBean("environment")).getProperty(URL_KEY))
					.replace(DOT + DB_PREFIX, "");
		}
		((SessionService) applicationContext.getBean("sessionService")).setCurrentDbFile(dbPath);
		final AbstractController controller = (AbstractController) applicationContext.getBean("mainWindowController");
		controller.showStage(stage);
	}

}
