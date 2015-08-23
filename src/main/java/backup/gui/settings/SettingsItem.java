package backup.gui.settings;

import javafx.scene.layout.BorderPane;
import backup.gui.common.BackupSettings;

public abstract class SettingsItem extends BorderPane
{
    private final String key;

    protected SettingsItem(final String key)
    {
        this.key = key;
    }

    public void init()
    {
        final String value = BackupSettings.getInstance().getValue(key);

        setValue(value);
    }

    public String getKey()
    {
        return key;
    }

    public abstract String getValue();
    public abstract void setValue(String value);
}
