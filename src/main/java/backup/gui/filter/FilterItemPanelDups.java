package backup.gui.filter;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Tooltip;
import backup.api.filter.FileManagerFilter;
import backup.api.filter.FileManagerFilterDups;

public class FilterItemPanelDups extends FilterItemPanel
{
    private final CheckBox checkBoxDups;

    public FilterItemPanelDups()
    {
        checkBoxDups = new CheckBox("Dups Only");
        checkBoxDups.setTooltip(new Tooltip("Only show files with duplicates"));

        getChildren().add(checkBoxDups);
    }

    @Override
    public FileManagerFilter getFilter()
    {
        final boolean dups = checkBoxDups.isSelected();

        return dups ? new FileManagerFilterDups() : null;
    }
}
