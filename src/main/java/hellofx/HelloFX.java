package hellofx;

import javafx.application.Application;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.web.WebView;
import javafx.beans.binding.Bindings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class HelloFX extends Application {

    private static final Logger log = LoggerFactory.getLogger(HelloFX.class);

    private ConfigurableApplicationContext springContext;

    private Scene scene;

    static DataModel model;

    private File captureFile = new File("cap.png");

    @Override
    public void init() throws Exception {
      springContext = SpringApplication.run(HelloFX.class);
      model = new DataModel();
    }

    @Override
    public void start(Stage stage) {

      log.info("Starting {}!", "BootFx");

      WebView wv1 = new WebView();
      wv1.getEngine().load("https://stackoverflow.com/questions/tagged/javafx");

      WebView wv2 = new WebView();
      wv2.getEngine().load("https://medium.com/");

      BootFxView bootFxView = new BootFxView(wv1, wv2);

      final TextField textField = new TextField();

      Bindings.bindBidirectional(textField.textProperty(), model.firstProperty());

      model.setFirst("100");

      model.firstProperty().addListener(new ChangeListener<String>(){
        @Override
        public void changed(ObservableValue<? extends String> o, String oldVal,
                        String newVal){
          log.info("model old val: " + oldVal);
          log.info("model new val: " + newVal);
        }
      });

      textField.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> o, String oldVal, String newVal) {
          log.info("textField old val: " + oldVal);
          log.info("textField new val: " + newVal);
        }
      });

      bootFxView.setBottom(textField);

      Button snapshot = new Button();
      snapshot.setText("snapshot");
      final ImageView imageView = new ImageView();
      //final ProgressIndicator progress = new ProgressIndicator();
      //progress.setVisible(false);
      snapshot.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          log.info("You clicked snapshot");
          WritableImage image = wv1.snapshot(null, null);
          BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
          try {
            ImageIO.write(bufferedImage, "png", captureFile);
            imageView.setImage(new Image(captureFile.toURI().toURL().toExternalForm()));
            log.info("Captured WebView to: " + captureFile.getAbsoluteFile());
            //progress.setVisible(false);
            snapshot.setDisable(false);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      });

      bootFxView.setTop(snapshot);

      scene = new Scene(bootFxView);

      stage.setTitle("BootFx");
      stage.setHeight(600);
      stage.setWidth(800);
      stage.centerOnScreen();
      stage.setOnCloseRequest(e -> {
        Platform.exit();
        System.exit(0);
      });
      stage.setScene(scene);
      stage.show();
    }

    @Override
  	public void stop() throws Exception {
  		springContext.stop();
  	}

    public static void main(String[] args) {
        launch(HelloFX.class, args);
        //launch();
    }

}


@RestController
class HelloController {

  private static final Logger log = LoggerFactory.getLogger(HelloController.class);

	@GetMapping("/{caseId}")
	public String getMessage(@PathVariable String caseId) {
    log.info(caseId);
    HelloFX.model.setFirst(caseId);
		return HelloFX.model.getFirst();
	}
}
