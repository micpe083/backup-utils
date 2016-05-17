package backup.gui.common;

import java.io.File;
import java.io.FilenameFilter;

import backup.api.DeleteDuplicatesManager;
import backup.api.DigestUtil;
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
        final FilenameFilter filter = new FilenameFilter()
        {
            @Override
            public boolean accept(final File dir,
                                  final String name)
            {
                return name.endsWith(DigestUtil.DIGEST_FILE_TYPE_ZIP);
            }
        };

        fileChooserDigestFile = new FileChooserComboPanel(text, filter);
        GuiUtils.setFile(fileChooserDigestFile);

        getChildren().add(fileChooserDigestFile);
        getChildren().add(statsPanel);

        final Button exploreButton = new Button("Explore");
        exploreButton.setTooltip(new Tooltip("Explore File"));
        exploreButton.setOnAction(e -> FileExplorer.show(fileManager));

        final Button processButton = new Button("Process");
        processButton.setTooltip(new Tooltip("Process File"));
        processButton.setOnAction(e -> processFile(true));

        final Button viewButton = new Button("View");
        viewButton.setTooltip(new Tooltip("View File"));
        viewButton.setOnAction(e -> viewFile());

        final Button deleteDupsButton = new Button("Delete Dups");
        deleteDupsButton.setTooltip(new Tooltip("Delete duplicate files"));
        deleteDupsButton.setOnAction(e -> deleteDuplicateFiles());

        final HBox buttonPanel = new HBox();
        buttonPanel.getChildren().add(exploreButton);
        buttonPanel.getChildren().add(processButton);
        buttonPanel.getChildren().add(viewButton);
        buttonPanel.getChildren().add(deleteDupsButton);

        getChildren().add(buttonPanel);

        fileChooserDigestFile.getFileProperty().addListener(e -> processFile(false));
    }

    public void setSelection(final String text)
    {
        fileChooserDigestFile.setSelection(text);
    }

    public FileManager getFileManager()
    {
        return fileManager;
    }

    public void processFile(final boolean isWarn)
    {
        try
        {
            final File digestFile;

            if (isWarn)
            {
                digestFile = fileChooserDigestFile.getSelectedFileWarn();
            }
            else
            {
                digestFile = fileChooserDigestFile.getSelectedFile();
            }

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

    private void viewFile()
    {
        final File file = fileChooserDigestFile.getSelectedFileWarn();

        if (file != null)
        {
            FileViewer.view(file, this);
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
