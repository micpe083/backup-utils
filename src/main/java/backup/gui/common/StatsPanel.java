package backup.gui.common;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import backup.api.FileManager;
import backup.api.FileSum;

public class StatsPanel extends HBox
{
    private final Label label = new Label("-");

    public StatsPanel()
    {
        getChildren().add(label);
    }

    public void setFileManager(final FileManager fileManager)
    {
        final FileSum fileSum = fileManager.getFileSum();

        label.setText(fileSum.toString());
    }
}
