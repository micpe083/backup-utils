package backup.gui.panel;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import backup.api.FileManager;
import backup.gui.common.DigestFilePanel;
import backup.gui.explorer.FileExplorer;

public class ComparePanel extends BackupUtilsPanel
{
    public ComparePanel()
    {
        final VBox vbox = new VBox();

        setCenter(vbox);

        final DigestFilePanel digestFilePanelOriginal = new DigestFilePanel("Original File:");
        vbox.getChildren().add(digestFilePanelOriginal);

        final DigestFilePanel digestFilePanelCurrent = new DigestFilePanel("Current File:");
        vbox.getChildren().add(digestFilePanelCurrent);

        final Button missingButton = new Button("Missing");
        missingButton.setOnAction(e -> compare(digestFilePanelOriginal,
                                               digestFilePanelCurrent));

        final Button newButton = new Button("New");
        newButton.setOnAction(e -> compare(digestFilePanelCurrent,
                                           digestFilePanelOriginal));

        final HBox buttonPane = new HBox();
        buttonPane.getChildren().add(missingButton);
        buttonPane.getChildren().add(newButton);

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
}
