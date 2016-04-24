package backup.gui.panel;

import backup.gui.settings.SettingsItemPanel;


public class SettingsPanel extends BackupUtilsPanel
{
    public SettingsPanel() throws Exception
    {
        setCenter(new SettingsItemPanel());
    }

    @Override
    public String getTabName()
    {
        return "Settings";
    }
}
