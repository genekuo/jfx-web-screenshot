package hellofx;

import javafx.application.Platform;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BootFxView extends BorderPane {
    private static final Logger log = LoggerFactory.getLogger(BootFxView.class);

    private final WebView wv1;
    private final WebView wv2;

    TabPane tabPane = new TabPane();
    Tab wv1Tab = new Tab();
    Tab wv2Tab = new Tab();


    public BootFxView(WebView wv1, WebView wv2) {
        this.wv1 = wv1;
        this.wv2 = wv2;

        Platform.runLater(() -> {
            log.info("Initializing  [{}] ", getClass().getSimpleName());

            wv1Tab = new Tab();
            wv1Tab.setText("WV1");
            wv1Tab.setContent(wv1);
            wv1Tab.setClosable(false);

            wv2Tab = new Tab();
            wv2Tab.setText("WV2");
            wv2Tab.setContent(wv2);
            wv2Tab.setClosable(false);

            tabPane.getTabs().addAll(wv1Tab, wv2Tab);
            setCenter(tabPane);
        });
    }

    public Tab getWv1Tab() {
        return wv1Tab;
    }

    public Tab getWv2Tab() {
        return wv2Tab;
    }
}
