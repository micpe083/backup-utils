package backup.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import backup.gui.panel.BackupUtilsPanel;
import backup.gui.panel.ComparePanel;
import backup.gui.panel.DigestDirectoryPanel;
import backup.gui.panel.ExplorePanel;
import backup.gui.panel.SettingsPanel;

public class BackupUtilsGui extends Application
{
    //private static final Logger LOGGER = LoggerFactory.getLogger(BackupUtilsGui.class);

    private void addTab(final TabPane tabbedPane,
                        final BackupUtilsPanel panel)
    {
        final Tab tab = new Tab();

        tab.setText(panel.getTabName());
        tab.setContent(panel);
        tab.closableProperty().set(false);

        tabbedPane.getTabs().add(tab);
    }

    @Override
    public void start(final Stage primaryStage) throws Exception
    {
        final TabPane tabbedPane = new TabPane();

        addTab(tabbedPane, new ExplorePanel());
        addTab(tabbedPane, new DigestDirectoryPanel());
        addTab(tabbedPane, new ComparePanel());
        addTab(tabbedPane, new SettingsPanel());

        final BorderPane root = new BorderPane();
        root.setCenter(tabbedPane);

        final Scene scene = new Scene(root, 800, 200);

        primaryStage.setTitle("Backup Utils");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(final String[] args)
    {
        launch(args);
    }
}
