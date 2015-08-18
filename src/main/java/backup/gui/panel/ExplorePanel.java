package backup.gui.panel;

import backup.gui.common.DigestFilePanel;

/**
 * @author Michael Peterson
 *
 * TODO: - create file finder (extention, size, date, etc.)
 * TODO: - create copy script
 * TODO: - create delete script
 */
public class ExplorePanel extends BackupUtilsPanel
{
    public ExplorePanel()
    {
        final DigestFilePanel digestFilePanel = new DigestFilePanel();
        setCenter(digestFilePanel);
    }

    @Override
    public String getTabName()
    {
        return "Explore";
    }
}
