package backup.gui.filter;

import java.util.List;

import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import backup.api.FileInfo;
import backup.api.filter.FileManagerFilter;

public class FilterItemPanelName extends FilterItemPanel
{
    private final TextField filenameTextField;
    private final CheckBox containsCheckBox;

    public FilterItemPanelName()
    {
        final HBox hbox = new HBox();

        final Label label = new Label("Filename:");
        hbox.getChildren().add(label);

        filenameTextField = new TextField();
        filenameTextField.setTooltip(new Tooltip("Filename"));
        hbox.getChildren().add(filenameTextField);

        containsCheckBox = new CheckBox("Contains");
        containsCheckBox.setSelected(true);
        containsCheckBox.setTooltip(new Tooltip("Contains/Does not contain"));
        hbox.getChildren().add(containsCheckBox);

        getChildren().add(hbox);
    }

    @Override
    public FileManagerFilter getFilter()
    {
        final String text = filenameTextField.getText();
        final boolean contains = containsCheckBox.isSelected();

        return text == null || text.isEmpty() ? null : new FileManagerFilter()
        {
            @Override
            public boolean accept(final FileInfo fileInfo,
                                  final List<String> paths)
            {
                return contains == fileInfo.getFilename().contains(text);
            }
        };
    }
}
