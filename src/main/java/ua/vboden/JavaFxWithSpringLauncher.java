package ua.vboden;

import java.io.IOException;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import javafx.application.Application;
import javafx.stage.Stage;
import ua.vboden.controllers.AbstractController;

@Configuration
@SpringBootApplication
public class JavaFxWithSpringLauncher extends Application {

	private static ConfigurableApplicationContext applicationContext;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() throws Exception {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(JavaFxWithSpringLauncher.class);
		builder.application().setWebApplicationType(WebApplicationType.NONE);
		applicationContext = builder.run(getParameters().getRaw().toArray(new String[0]));
	}

	@Override
	public void stop() throws Exception {
		applicationContext.close();
	}

	@Override
	public void start(Stage stage) throws IOException {
		System.out.println();
		final AppContextInitializer appContextInitializer = (AppContextInitializer) applicationContext
				.getBean("appContextInitializer");
		appContextInitializer.initApp();
		final AbstractController controller = (AbstractController) applicationContext.getBean("mainWindowController");
		controller.showStage(stage);
	}

}
