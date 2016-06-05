package backup.gui.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

import com.google.common.base.Strings;

import backup.api.FileInfo;
import backup.api.filter.FileManagerFilter;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

public class FilterItemPanelDir extends FilterItemPanel
{
    private final TextField dirTextField;
    private final CheckBox containsCheckBox;

    public FilterItemPanelDir()
    {
        final HBox hbox = new HBox();

        final Label label = new Label("Dir:");
        hbox.getChildren().add(label);

        dirTextField = new TextField();
        dirTextField.setTooltip(new Tooltip("Directory"));
        hbox.getChildren().add(dirTextField);

        containsCheckBox = new CheckBox("Include");
        containsCheckBox.setSelected(false);
        containsCheckBox.setTooltip(new Tooltip("Include/Not include"));
        hbox.getChildren().add(containsCheckBox);

        getChildren().add(hbox);
    }

    @Override
    public FileManagerFilter getFilter()
    {
        final String text = dirTextField.getText();
        final boolean contains = containsCheckBox.isSelected();

        if (Strings.isNullOrEmpty(text))
        {
            return null;
        }

        final List<String> dirList = new ArrayList<String>();

        final String[] dirArr = text.split(",");

        for (int i = 0; i < dirArr.length; i++)
        {
            final String dir = dirArr[i];

            if (!Strings.isNullOrEmpty(text))
            {
                dirList.add(dir.trim());
            }
        }

        return dirList.isEmpty() ? null : new FileManagerFilter()
        {
            @Override
            public boolean accept(final FileInfo fileInfo,
                                  final List<String> paths)
            {
                boolean ret = true;

                for (final String path : paths)
                {
                    for (final String dir : dirList)
                    {
                        ret = contains == path.startsWith(dir);
                        if (!ret)
                        {
                            return false;
                        }
                    }
                }

                return true;
            }
        };
    }
}
