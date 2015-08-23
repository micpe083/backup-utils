package backup.gui.filter;

import backup.api.filter.FileManagerFilter;
import javafx.scene.layout.Pane;

public abstract class FilterItemPanel extends Pane
{
    public abstract FileManagerFilter getFilter();
}
