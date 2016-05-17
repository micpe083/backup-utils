package backup.gui.common;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import backup.api.FileManager;
import backup.api.FileSum;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class StatsPanel extends HBox
{
    private final Label label = new Label();

    public StatsPanel()
    {
        getChildren().add(label);
        clear();
    }

    public void clear()
    {
        label.setText("-");
    }

    public void setFileManager(final FileManager fileManager)
    {
        final FileSum fileSum = fileManager.getFileSum();

        final LocalTime time = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
        label.setText(fileSum.toString() + "  [" + time + "]");
    }
}
