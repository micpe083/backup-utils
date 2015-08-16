package backup.gui.panel;

import java.awt.BorderLayout;

import backup.gui.common.DigestFilePanel;

/**
 * @author Michael Peterson
 *
 * TODO: - create file finder (extention, size, date, etc.)
 * TODO: - create copy script
 * TODO: - create delete script
 */
public class ExplorePanel extends BackupValidatorPanel
{
    private static final long serialVersionUID = 3846651217482501682L;

    private final DigestFilePanel digestFilePanel;

    public ExplorePanel()
    {
        setLayout(new BorderLayout());

        digestFilePanel = new DigestFilePanel();
        add(digestFilePanel);
    }

    @Override
    public String getTabName()
    {
        return "Explore";
    }

    @Override
    public void execute()
    {
    }
}
