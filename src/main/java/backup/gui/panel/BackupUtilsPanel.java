package backup.gui.panel;

import javafx.scene.layout.BorderPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BackupUtilsPanel extends BorderPane
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(BackupUtilsPanel.class);

    public abstract String getTabName();
}
