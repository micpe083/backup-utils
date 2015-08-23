package backup.gui.filter;

import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import backup.api.FileInfo;
import backup.api.filter.FileManagerFilter;

public class FilterItemPanelName extends FilterItemPanel
{
    private final TextField filenameTextField;

    public FilterItemPanelName()
    {
        final HBox hbox = new HBox();

        final Label label = new Label("Filename:");
        hbox.getChildren().add(label);

        filenameTextField = new TextField();
        filenameTextField.setTooltip(new Tooltip("Filename contains"));
        hbox.getChildren().add(filenameTextField);

        getChildren().add(hbox);
    }

    @Override
    public FileManagerFilter getFilter()
    {
        final String text = filenameTextField.getText();

        return text == null || text.isEmpty() ? null : new FileManagerFilter()
        {
            @Override
            public boolean accept(final FileInfo fileInfo, final List<String> paths)
            {

                return fileInfo.getFilename().contains(text);
            }
        };
    }
}
