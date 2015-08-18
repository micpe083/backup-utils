package backup.gui.panel;

import java.io.File;
import java.io.IOException;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import backup.api.DigestUtil;
import backup.api.DigestUtil.DigestAlg;
import backup.gui.common.BackupSettings;
import backup.gui.common.GuiUtils;
import backup.gui.common.DigestSelectorPanel;
import backup.gui.common.FileChooserPanel;


public class DigestDirectoryPanel extends BackupUtilsPanel
{
    private final FileChooserPanel fileChooserDigestDir;
    private final FileChooserPanel fileChooserOutputDir;

    private final DigestSelectorPanel digestSelectorPanel;

    public DigestDirectoryPanel()
    {
        final VBox pane = new VBox();

        fileChooserDigestDir = new FileChooserPanel("Digest dir:", false);
        fileChooserDigestDir.setSelection(BackupSettings.getInstance().getString(BackupSettings.DIGEST_DIR));

        fileChooserOutputDir = new FileChooserPanel("Output dir", false);
        fileChooserOutputDir.setSelection(BackupSettings.getInstance().getString(BackupSettings.DIGEST_OUTPUT_DIR));

        digestSelectorPanel = new DigestSelectorPanel();
        pane.getChildren().add(digestSelectorPanel);

        final Button executeButton = new Button("Execute");
        executeButton.setOnAction(event -> execute());

        pane.getChildren().add(fileChooserDigestDir);
        pane.getChildren().add(fileChooserOutputDir);
        pane.getChildren().add(executeButton);

        setCenter(pane);
    }

    @Override
    public String getTabName()
    {
        return "Create Digest";
    }

    private void execute()
    {
        try
        {
            final File digestDir = fileChooserDigestDir.getSelectionFile();
            final File outputDir = fileChooserOutputDir.getSelectionFile();

            final DigestAlg digestAlg = digestSelectorPanel.getDigestAlg();

            final DigestUtil digestUtil = new DigestUtil(digestAlg);

            digestUtil.createDigestFile(digestDir,
                                        outputDir);
        }
        catch (IOException e)
        {
            GuiUtils.showErrorMessage(this,
                                      "Failed to process directory: " + e.getMessage(),
                                      "Error",
                                      e);
        }
    }
}
