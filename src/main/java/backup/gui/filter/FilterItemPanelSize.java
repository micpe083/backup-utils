package backup.gui.filter;

import java.util.List;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import backup.api.FileInfo;
import backup.api.filter.FileManagerFilter;

public class FilterItemPanelSize extends FilterItemPanel
{
    private final TextField textField;

    public FilterItemPanelSize()
    {
        final HBox hbox = new HBox();

        final Label label = new Label("Size Above (bytes):");
        hbox.getChildren().add(label);

        textField = new TextField();
        hbox.getChildren().add(textField);

        // TODO: unit

        // TODO: above/below

        getChildren().add(hbox);
    }

    @Override
    public FileManagerFilter getFilter()
    {
        Long sizeTmp = null;
        try
        {
            final String text = textField.getText();

            sizeTmp = Long.parseLong(text);
        }
        catch (Exception e)
        {
        }

        final Long size = sizeTmp;

        return size == null ? null : new FileManagerFilter()
        {
            @Override
            public boolean accept(final FileInfo fileInfo, final List<String> paths)
            {
                return fileInfo.getSize() > size;
            }
        };
    }
}
