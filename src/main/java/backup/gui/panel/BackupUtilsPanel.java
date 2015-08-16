package backup.gui.panel;

import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BackupUtilsPanel extends JPanel
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(BackupUtilsPanel.class);

    private static final long serialVersionUID = 8983128722402990989L;

    public abstract String getTabName();

    public abstract void execute();
}
