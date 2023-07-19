package ua.vboden;

import java.io.IOException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ua.vboden.controllers.MainWindowController;

@Configuration
@EnableJpaRepositories("ua.vboden.repositories")
@SpringBootApplication//(exclude = {DataSourceAutoConfiguration.class })
@EnableAutoConfiguration
public class JavaFxWithSpringLauncher extends Application {

	private static ConfigurableApplicationContext applicationContext;
//	private static List<Image> icons = new ArrayList<>();
	
//	@Autowired
////	@Resource
//	private MainWindowController mainWindowController;
	
	public static void main(String[] args) {
		launch(args);
	}
	
//	@Autowired Environment env;
//
//	@Bean
//	public DataSource dataSource() {
//	    final DriverManagerDataSource dataSource = new DriverManagerDataSource();
//	    System.out.println("driverClassName="+env.getDefaultProfiles());
//	    System.out.println("driverClassName="+env.getProperty("driverClassName"));
//	    dataSource.setDriverClassName("org.sqlite.JDBC");
//	    dataSource.setUrl(env.getProperty("url"));
//	    dataSource.setUsername(env.getProperty("user"));
//	    dataSource.setPassword(env.getProperty("password"));
//	    return dataSource;
//	}
	
//	@Bean
//    public DataSource getDataSource() {
//        return DataSourceBuilder.create()
//          .driverClassName("com.mysql.cj.jdbc.Driver")
//          .url("jdbc:mysql://localhost:3306/myDb")
//          .username("user1")
//          .password("pass")
//          .build();	
//    }

	@Override
	public void init() throws Exception {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(JavaFxWithSpringLauncher.class);
		builder.application().setWebApplicationType(WebApplicationType.NONE);
		applicationContext = builder.run(getParameters().getRaw().toArray(new String[0]));
//		final List<String> fsImages = PropertyReaderHelper.get(applicationContext.getEnvironment(), "javafx.appicons");
//		if (!fsImages.isEmpty()) {
//			fsImages.forEach((s) -> icons.add(new Image(getClass().getResource(s).toExternalForm())));
//		} else {
//			icons.add(new Image(getClass().getResource("/images/gear_16x16.png").toExternalForm()));
//			icons.add(new Image(getClass().getResource("/images/gear_24x24.png").toExternalForm()));
//			icons.add(new Image(getClass().getResource("/images/gear_36x36.png").toExternalForm()));
//			icons.add(new Image(getClass().getResource("/images/gear_42x42.png").toExternalForm()));
//			icons.add(new Image(getClass().getResource("/images/gear_64x64.png").toExternalForm()));
//		}
	}
	

	@Override
	public void stop() throws Exception {
		applicationContext.close();
	}


	@Override
	public void start(Stage stage) throws IOException {
		System.out.println();
		Parent parent = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));

        Scene scene = new Scene(parent, 510, 325);
        stage.setScene(scene);

        stage.show();
//        mainWindowController.quit();
        
//        applicationContext.getBean(MainWindowController.class).quit();
	}

}
