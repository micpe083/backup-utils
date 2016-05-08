package backup.gui.common;

import java.io.File;

import backup.api.DeleteDuplicatesManager;
import backup.api.FileManager;
import backup.gui.explorer.FileExplorer;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DigestFilePanel extends VBox
{
    private final FileChooserComboPanel fileChooserDigestFile;

    private final FileManager fileManager = new FileManager();

    private final StatsPanel statsPanel = new StatsPanel();

    public DigestFilePanel() throws Exception
    {
        this("Digest file:");
    }

    public DigestFilePanel(final String text) throws Exception
    {
        fileChooserDigestFile = new FileChooserComboPanel(text);
        GuiUtils.setFile(fileChooserDigestFile);

        getChildren().add(fileChooserDigestFile);
        getChildren().add(statsPanel);

        final Button exploreButton = new Button("Explore");
        exploreButton.setOnAction(e -> FileExplorer.show(fileManager));

        final Button processButton = new Button("Process File");
        processButton.setOnAction(e -> processFile());

        final Button deleteDupsButton = new Button("Delete Dups");
        deleteDupsButton.setTooltip(new Tooltip("Delete duplicate files"));
        deleteDupsButton.setOnAction(e -> deleteDuplicateFiles());

        final HBox buttonPanel = new HBox();
        buttonPanel.getChildren().add(exploreButton);
        buttonPanel.getChildren().add(processButton);
        buttonPanel.getChildren().add(deleteDupsButton);

        getChildren().add(buttonPanel);
    }

    public void setSelection(final String text)
    {
        fileChooserDigestFile.setSelection(text);
    }

    public FileManager getFileManager()
    {
        return fileManager;
    }

    public void processFile()
    {
        try
        {
            final File digestFile = fileChooserDigestFile.getSelectedFileWarn();

            if (digestFile != null)
            {
                fileManager.loadDigestFile(digestFile);
            }

            statsPanel.setFileManager(fileManager);
        }
        catch (Exception e)
        {
            GuiUtils.showErrorMessage(this,
                                      "Failed to process directory: " + e.getMessage(),
                                      "Error",
                                      e);
        }
    }

    public void deleteDuplicateFiles()
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
                                          "Failed to delete duplicates: " + e.getMessage(),
                                          "Error",
                                          e);
            }
        }
    }
}
