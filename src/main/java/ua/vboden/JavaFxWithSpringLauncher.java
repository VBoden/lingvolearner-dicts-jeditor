package ua.vboden;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileSystemView;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariConfigMXBean;
import com.zaxxer.hikari.HikariDataSource;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
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
		
//		Properties properties = new Properties();
//        properties.put("spring.datasource.url", "jdbc:sqlite:prod-db.sqlite3");
//        builder.application().setDefaultProperties(properties);
		
//		FileDialog fd = new FileDialog((Frame)null, "Select db file", FileDialog.LOAD);
//        fd.setFilenameFilter((File dir, String name) -> name.endsWith(".sqlite3"));
//        fd.setAlwaysOnTop(true);
//        fd.setVisible(true);
//        String filename = null;
//        if(fd.getDirectory() !=null && fd.getFile() !=null) {
//        	
//        	filename = new File(fd.getDirectory(), fd.getFile()).getAbsolutePath();
//        }
//        System.out.println(filename);
        
		final JFrame iFRAME = new JFrame();
		iFRAME.setAlwaysOnTop(true);    // ****
		iFRAME.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		iFRAME.setLocationRelativeTo(null);
		iFRAME.requestFocus();

		JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		int returnValue = fileChooser.showOpenDialog(iFRAME);
		System.out.println(returnValue);
		iFRAME.dispose();
		File file = fileChooser.getSelectedFile();

		String[] args = getParameters().getRaw().toArray(new String[0]);
		
		if (file != null) {
//			((HikariDataSource) applicationContext.getBean("dataSource"))
//					.setJdbcUrl("jdbc:sqlite:" + file.getAbsolutePath());	
//			args = new String[]{"spring.datasource.hikari.url=jdbc:sqlite:prod-db.sqlite3","spring.datasource.url=jdbc:sqlite:prod-db.sqlite3","spring.datasource-org.springframework.boot.autoconfigure.jdbc.DataSourceProperties.url=jdbc:sqlite:prod-db.sqlite3"};
			args = new String[]{"--spring.datasource.url=jdbc:sqlite:" + file.getAbsolutePath()};
		}

        
		applicationContext = builder.run(args);
//		applicationContext = builder.properties("spring.datasource-org.springframework.boot.autoconfigure.jdbc.DataSourceProperties.url=jdbc:sqlite:prod-db.sqlite3").run(args);
		
//		FileDialog fd = new FileDialog((Frame)null, "Select db file", FileDialog.LOAD);
//      fd.setFilenameFilter((File dir, String name) -> name.endsWith(".sqlite3"));
//      fd.setAlwaysOnTop(true);
//      fd.setVisible(true);
//      String filename = null;
//      if(fd.getDirectory() !=null && fd.getFile() !=null) {
//      	
//      	filename = new File(fd.getDirectory(), fd.getFile()).getAbsolutePath();
//      }
//      if (filename != null) {
//			((HikariDataSource) applicationContext.getBean("dataSource"))
//					.setJdbcUrl("jdbc:sqlite:" + filename);
//		}
		
		
//		FileChooser fileChooser = new FileChooser();
//		fileChooser.setTitle("Select db file");
//		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("db files", "*.sqlite3"));
//		File file = fileChooser.showOpenDialog(new Stage());
//		if (file != null) {
//			((HikariDataSource) applicationContext.getBean("dataSource"))
//					.setJdbcUrl("jdbc:sqlite:" + file.getAbsolutePath());
//		}
	}

	@Override
	public void stop() throws Exception {
		applicationContext.close();
	}

	@Override
	public void start(Stage stage) throws IOException {
		System.out.println();
//		FileChooser fileChooser = new FileChooser();
//		fileChooser.setTitle("Select db file");
//		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("db files", "*.sqlite3"));
//		File file = fileChooser.showOpenDialog(stage);
//		if (file != null) {
//			HikariConfig config = new HikariConfig();
//	         config.setJdbcUrl("jdbc:sqlite:" + file.getAbsolutePath());
//				((HikariConfig) applicationContext.getBean("spring.datasource-org.springframework.boot.autoconfigure.jdbc.DataSourceProperties"))
//				.setJdbcUrl("jdbc:sqlite:" + file.getAbsolutePath());
////			((HikariDataSource) applicationContext.getBean("dataSource"))
////					.setJdbcUrl("jdbc:sqlite:" + file.getAbsolutePath());
//		}
		
		final AppContextInitializer appContextInitializer = (AppContextInitializer) applicationContext
				.getBean("appContextInitializer");
		appContextInitializer.initApp();
		final AbstractController controller = (AbstractController) applicationContext.getBean("mainWindowController");
		controller.showStage(stage);
	}

}
