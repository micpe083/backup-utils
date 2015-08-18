package backup.gui.common;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import backup.api.BackupUtil;
import backup.api.FileManager;
import backup.gui.explorer.FileExplorer;

public class DigestFilePanel extends VBox
{
    private final FileChooserPanel fileChooserDigestFile;

    private final FileManager fileManager = new FileManager();

    private final StatsPanel statsPanel = new StatsPanel();

    public DigestFilePanel()
    {
        this("Digest file:");
    }

    public DigestFilePanel(final String text)
    {
        fileChooserDigestFile = new FileChooserPanel(text, true);
        getChildren().add(fileChooserDigestFile);

        BackupUtil.setFile(fileChooserDigestFile);

        getChildren().add(statsPanel);

        final Button exploreDupsButton = new Button("Explore Dups");
        exploreDupsButton.setOnAction(e -> FileExplorer.show(fileManager.getFilesWithDups()));

        final Button exploreButton = new Button("Explore All");
        exploreButton.setOnAction(e -> FileExplorer.show(fileManager));

        final Button processButton = new Button("Process File");
        processButton.setOnAction(e -> processFile());

        final HBox buttonPanel = new HBox();
        buttonPanel.getChildren().add(exploreDupsButton);
        buttonPanel.getChildren().add(exploreButton);
        buttonPanel.getChildren().add(processButton);

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
            FileManagerUtil.loadDigestFile(fileChooserDigestFile, fileManager);

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
}
