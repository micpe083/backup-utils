package backup.gui.explorer;

import backup.api.DeleteDuplicatesManager;
import backup.api.FileInfo;
import backup.api.FileManager;
import backup.api.StageFilesManager;
import backup.api.filter.FileManagerFilter;
import backup.gui.common.GuiUtils;
import backup.gui.common.StatsPanel;
import backup.gui.filter.FilterPanel;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FileExplorer extends BorderPane implements PathSelectionListener
{
    private final PathPanel pathPanel;
    private final FileInfoTablePanel pathsPanel;
    private final StatsPanel statsPanel;
    private FileViewer fileViewer;

    private final FileManager fileManagerOrig;
    private FileManager fileManagerFiltered;

    public FileExplorer(final FileManager fileManager)
    {
        this.fileManagerOrig = fileManager;
        this.fileManagerFiltered = fileManager;

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

        final Button treeButton = new Button("Tree View");
        treeButton.setOnAction(x -> setFileView(true));
        buttonPanel.getChildren().add(treeButton);

        final Button listButton = new Button("List View");
        listButton.setOnAction(x -> setFileView(false));
        buttonPanel.getChildren().add(listButton);

        final Button stageAllButton = new Button("Stage All");
        stageAllButton.setOnAction(x -> stage(false));
        buttonPanel.getChildren().add(stageAllButton);

        final Button stageSelectedButton = new Button("Stage Selected");
        stageSelectedButton.setOnAction(x -> stage(true));
        buttonPanel.getChildren().add(stageSelectedButton);

        final Button deleteDupsSelectedButton = new Button("Delete Dups Selected");
        deleteDupsSelectedButton.setOnAction(x -> deleteDups());
        buttonPanel.getChildren().add(deleteDupsSelectedButton);

        vbox.getChildren().add(buttonPanel);

        setBottom(vbox);

        setFileView(true);
    }

    private void stage(final boolean isSelected)
    {
        final FileManager fileManager;

        if (isSelected)
        {
            fileManager = fileViewer.getSelected(fileManagerFiltered);
        }
        else
        {
            fileManager = fileManagerFiltered;
        }

        if (fileManager != null)
        {
            try
            {
                StageFilesManager.stage(fileManager);
            }
            catch (Exception e)
            {
                GuiUtils.showErrorMessage(this,
                                          "Error staging files",
                                          "Error",
                                          e);
            }
        }
    }

    private void deleteDups()
    {
        final FileManager fileManager = fileViewer.getSelected(fileManagerFiltered);

        if (fileManager != null)
        {
            if (GuiUtils.shouldDelete())
            {
                try
                {
                    DeleteDuplicatesManager.deleteDuplicates(fileManager);
                }
                catch (Exception e)
                {
                    GuiUtils.showErrorMessage(this,
                                              "Error deleting duplicate files",
                                              "Error",
                                              e);
                }
            }
        }
    }

    private void setFileView(final boolean isTree)
    {
        final FileViewer fileViewerOld = this.fileViewer;

        if (fileViewerOld != null)
        {
            fileViewerOld.removeListener(this);
        }

        final FileViewer fileViewerNew;

        if (isTree)
        {
            fileViewerNew = new FileViewerTree();
        }
        else
        {
            fileViewerNew = new FileViewerList();
        }

        fileViewerNew.addListener(this);
        centerProperty().set(fileViewerNew);

        this.fileViewer = fileViewerNew;

        setFileManager(fileManagerFiltered);
    }

    private void filter(final FileManagerFilter filter)
    {
        final FileManager fileManager = this.fileManagerOrig.getFileManager(filter);

        setFileManager(fileManager);
    }

    private void setFileManager(final FileManager fileManager)
    {
        this.fileManagerFiltered = fileManager;
        fileViewer.setFileManager(fileManager);
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
