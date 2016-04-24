package backup.gui.panel;

import backup.api.FileManager;
import backup.api.StageFilesManager;
import backup.gui.common.DigestFilePanel;
import backup.gui.common.GuiUtils;
import backup.gui.explorer.FileExplorer;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ComparePanel extends BackupUtilsPanel
{
    public ComparePanel() throws Exception
    {
        final VBox vbox = new VBox();

        setCenter(vbox);

        final DigestFilePanel digestFilePanelSource = new DigestFilePanel("Source File:");
        vbox.getChildren().add(digestFilePanelSource);

        final DigestFilePanel digestFilePanelTarget = new DigestFilePanel("Target File:");
        vbox.getChildren().add(digestFilePanelTarget);

        final Button missingButton = new Button("Missing");
        missingButton.setOnAction(e -> compare(digestFilePanelSource,
                                               digestFilePanelTarget));

        final Button newButton = new Button("New");
        newButton.setOnAction(e -> compare(digestFilePanelTarget,
                                           digestFilePanelSource));

        final Button stageButton = new Button("Stage");
        stageButton.setTooltip(new Tooltip("Copy missing files to staging area"));
        stageButton.setOnAction(e -> stage(digestFilePanelSource,
                                           digestFilePanelTarget));

        final HBox buttonPane = new HBox();
        buttonPane.getChildren().add(missingButton);
        buttonPane.getChildren().add(newButton);
        buttonPane.getChildren().add(stageButton);

        vbox.getChildren().add(buttonPane);
    }

    @Override
    public String getTabName()
    {
        return "Compare";
    }

    private void compare(final DigestFilePanel digestFilePanel1,
                         final DigestFilePanel digestFilePanel2)
    {
        final FileManager fileManager1 = digestFilePanel1.getFileManager();
        final FileManager fileManager2 = digestFilePanel2.getFileManager();

        final FileManager fileManagerMissing = fileManager1.getMissing(fileManager2);

        FileExplorer.show(fileManagerMissing);
    }

    private void stage(final DigestFilePanel digestFilePanel1,
                       final DigestFilePanel digestFilePanel2)
    {
        final FileManager fileManager1 = digestFilePanel1.getFileManager();
        final FileManager fileManager2 = digestFilePanel2.getFileManager();

        try
        {
            final FileManager copiedFiles = StageFilesManager.stageMissingFiles(fileManager1,
                                                                                fileManager2);

            FileExplorer.show(copiedFiles);
        }
        catch (Exception e)
        {
            GuiUtils.showErrorMessage(this,
                                      "Failed to copy files",
                                      "Error",
                                      e);
        }
    }
}
