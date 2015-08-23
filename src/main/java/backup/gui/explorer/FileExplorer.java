package backup.gui.explorer;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import backup.api.FileInfo;
import backup.api.FileManager;
import backup.api.filter.FileManagerFilter;
import backup.gui.common.GuiUtils;
import backup.gui.common.StatsPanel;
import backup.gui.filter.FilterPanel;

public class FileExplorer extends BorderPane implements PathSelectionListener
{
    private final PathPanel pathPanel;
    private final FileInfoTablePanel pathsPanel;
    private final StatsPanel statsPanel;
    private final FileTree fileTree;

    private final FileManager fileManagerOrig;
    private FileManager fileManagerFiltered;

    public FileExplorer(final FileManager fileManager)
    {
        this.fileManagerOrig = fileManager;

        fileTree = new FileTree();
        fileTree.addListener(this);

        setCenter(fileTree);

        final VBox vbox = new VBox();

        pathPanel = new PathPanel();
        vbox.getChildren().add(pathPanel);

        statsPanel = new StatsPanel();
        vbox.getChildren().add(statsPanel);

        pathsPanel = new FileInfoTablePanel();
        vbox.getChildren().add(pathsPanel);

        final FilterPanel filterPanel = new FilterPanel();
        filterPanel.setListener(x -> filter(x));
        vbox.getChildren().add(GuiUtils.createTitledPane("Filters", filterPanel, false));

        final HBox buttonPanel = new HBox();
        buttonPanel.getChildren().add(fileTree.createExpandAllButton());
        buttonPanel.getChildren().add(fileTree.createCollapseAllButton());
        vbox.getChildren().add(buttonPanel);

        setBottom(vbox);

        setFileManager(fileManager);
    }

    private void filter(final FileManagerFilter filter)
    {
        final FileManager fileManager = this.fileManagerOrig.getFileManager(filter);

        setFileManager(fileManager);
    }

    private void setFileManager(final FileManager fileManager)
    {
        this.fileManagerFiltered = fileManager;
        fileTree.setFileManager(fileManager);
        statsPanel.setFileManager(fileManager);
        fileSelected(null, null);
    }

    @Override
    public void fileSelected(final FileInfo fileInfo,
                             final String path)
    {
        pathPanel.setPath(path);
        pathsPanel.setFileInfo(fileManagerFiltered, fileInfo);
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
