package backup.gui.settings;

import backup.gui.common.FileChooserPanel;

public class SettingsItemFile extends SettingsItem
{
    private final FileChooserPanel fileChooserPanel;

    public SettingsItemFile(final String key,
                            final String text,
                            final boolean isFile)
    {
        super(key);

        fileChooserPanel = new FileChooserPanel(text, isFile);
        setCenter(fileChooserPanel);
    }

    @Override
    public void setValue(final String value)
    {
        fileChooserPanel.setSelection(value);
    }

    @Override
    public String getValue()
    {
        return fileChooserPanel.getSelectedFileStr();
    }
}
