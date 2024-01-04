package ua.vboden;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import javafx.application.Application;
import javafx.application.Platform;
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

	private ResourceBundle appProperties = ResourceBundle.getBundle("application");

	private ResourceBundle resources = ResourceBundle.getBundle("bundles/localization");

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
		if (!PreferencesService.isSelectDbOnStart() && lastDB != null && !(new File(lastDB).exists())) {
			infoBox(String.format(resources.getString("start.last.db.not.found").replaceAll("\\n", "\n"), lastDB),
					resources.getString("start.file.not.found"));
		}
		if (PreferencesService.isSelectDbOnStart() || lastDB == null || !(new File(lastDB).exists())) {
			JFileChooser fileChooser = new JFileChooser();
			FileFilter filter = new FileNameExtensionFilter(resources.getString("start.file.filter.name"), "sqlite3");
			fileChooser.setFileFilter(filter);
			fileChooser.setDialogTitle(resources.getString("start.file.select.title"));
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
			} else {

				if (dbPath == null) {
					String defaultDB = (new File(DOT).getAbsolutePath() + appProperties.getString(URL_KEY))
							.replace(DOT + DB_PREFIX, "");
					if (!(new File(defaultDB).exists())) {
						infoBox(String.format(resources.getString("start.file.not.selected.no.default").replaceAll("\\n", "\n"), defaultDB),
								resources.getString("start.file.not.selected.no.default.title"));
						Platform.exit();
						System.exit(0);
					} else {
						infoBox(String.format(resources.getString("start.file.not.selected").replaceAll("\\n", "\n"), defaultDB),
								resources.getString("start.file.not.selected.title"));
					}
					dbPath = defaultDB;
				}
			}
		} else if (lastDB != null && new File(lastDB).exists()) {
			dbPath = lastDB;
			return new String[] { String.format("--%s=%s", URL_KEY, DB_PREFIX + lastDB) };
		}
		return getParameters().getRaw().toArray(new String[0]);
	}

	public static void infoBox(String infoMessage, String titleBar) {
		JOptionPane.showMessageDialog(null, infoMessage, titleBar, JOptionPane.INFORMATION_MESSAGE);
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
		((SessionService) applicationContext.getBean("sessionService")).setCurrentDbFile(dbPath);
		final AbstractController controller = (AbstractController) applicationContext.getBean("mainWindowController");
		controller.showStage(stage);
	}

}
