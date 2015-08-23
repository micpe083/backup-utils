package backup.gui.filter;

import javafx.scene.control.CheckBox;
import backup.api.filter.FileManagerFilter;
import backup.api.filter.FileManagerFilterDups;

public class FilterItemPanelDups extends FilterItemPanel
{
    private final CheckBox checkBoxDups;

    public FilterItemPanelDups()
    {
        checkBoxDups = new CheckBox("Dups");

        getChildren().add(checkBoxDups);
    }

    @Override
    public FileManagerFilter getFilter()
    {
        final boolean dups = checkBoxDups.isSelected();

        return dups ? new FileManagerFilterDups() : null;
    }
}
