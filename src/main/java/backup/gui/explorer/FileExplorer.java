package backup.gui.explorer;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import backup.api.FileInfo;
import backup.api.FileManager;
import backup.gui.common.StatsPanel;

public class FileExplorer extends BorderPane implements PathSelectionListener
{
    private final PathPanel pathPanel;
    private final FileInfoPanel fileInfoPanel;
    private final PathsPanel2 pathsPanel;

    private final FileManager fileManager;

    public FileExplorer(final FileManager fileManager)
    {
        this.fileManager = fileManager;

        final FileTree fileTree = new FileTree();
        fileTree.addListener(this);
        fileTree.setFileManager(fileManager);

        setCenter(fileTree);

        final VBox vbox = new VBox();

        pathPanel = new PathPanel();
        vbox.getChildren().add(pathPanel);

        fileInfoPanel = new FileInfoPanel();
        vbox.getChildren().add(fileInfoPanel);

        final StatsPanel statsPanel = new StatsPanel();
        statsPanel.setFileManager(fileManager);
        vbox.getChildren().add(statsPanel);

        pathsPanel = new PathsPanel2();
        vbox.getChildren().add(pathsPanel);

        final HBox buttonPanel = new HBox();
        buttonPanel.getChildren().add(fileTree.createExpandAllButton());
        buttonPanel.getChildren().add(fileTree.createCollapseAllButton());
        vbox.getChildren().add(buttonPanel);

        setBottom(vbox);
    }

    @Override
    public void fileSelected(final FileInfo fileInfo,
                             final String path)
    {
        pathPanel.setPath(path);
        fileInfoPanel.setFileInfo(fileInfo);
        pathsPanel.setFileInfo(fileManager, fileInfo);
    }

    public static FileExplorer show(final FileManager fileManager)
    {
        final FileExplorer tree = new FileExplorer(fileManager);

        final Scene scene = new Scene(tree, 800, 600);

        final Stage stage = new Stage();
        stage.setTitle("File Tree");
        stage.setScene(scene);
        stage.show();

        return tree;
    }
}
