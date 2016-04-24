package backup.gui.settings;

import java.util.ArrayList;
import java.util.List;

import backup.gui.common.BackupSettings;
import backup.gui.common.GuiUtils;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SettingsItemPanel extends VBox
{
    private final List<SettingsItem> list = new ArrayList<>();

    public SettingsItemPanel() throws Exception
    {
        addPanel(new SettingsItemFile(BackupSettings.DIGEST_FILE, "Digest File", true));
        addPanel(new SettingsItemFile(BackupSettings.DIGEST_DIR, "Digest Dir", false));
        addPanel(new SettingsItemFile(BackupSettings.getInstance().getOutputDir(), "Output Dir", false));

        final HBox buttonPanel = new HBox();

        final Button applyButton = new Button("Apply");
        buttonPanel.getChildren().add(applyButton);
        applyButton.setOnAction(x -> apply());

        final Button saveButton = new Button("Save");
        buttonPanel.getChildren().add(saveButton);
        saveButton.setOnAction(x -> save());

        getChildren().add(buttonPanel);
    }

    private void apply()
    {
    }

    private void save()
    {
        try
        {
            for (final SettingsItem settingsItem : list)
            {
                BackupSettings.getInstance().setValue(settingsItem.getKey(), settingsItem.getValue());
            }

            BackupSettings.getInstance().save();
        }
        catch (Exception e)
        {
            GuiUtils.showErrorMessage(this,
                                      "Error saving settings: " + e.getMessage(),
                                      "Save Error",
                                      e);
        }
    }

    private void addPanel(final SettingsItem settingsItem)
    {
        settingsItem.init();
        list.add(settingsItem);

        getChildren().add(settingsItem);
    }
}
