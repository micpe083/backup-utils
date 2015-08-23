package backup.gui.filter;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import backup.api.FileInfo;
import backup.api.filter.FileManagerFilter;

public class FilterItemPanelSize extends FilterItemPanel
{
    public enum SizeUnit
    {
        B(1),
        KB(1000),
        MB(1000_000),
        GB(1000_000_000);

        private final long div;

        SizeUnit(final long div)
        {
            this.div = div;
        }

        public long toBytes(final double size)
        {
            return Math.round(size * div);
        }
    }

    private final TextField sizeTextField;
    private final CheckBox sizeAboveCheckBox;
    private final ComboBox<SizeUnit> sizeUnitComboBox;

    public FilterItemPanelSize()
    {
        final HBox hbox = new HBox();

        final Label label = new Label("Size:");
        hbox.getChildren().add(label);

        sizeTextField = new TextField();
        sizeTextField.setTooltip(new Tooltip("File size"));
        hbox.getChildren().add(sizeTextField);

        sizeUnitComboBox = new ComboBox<SizeUnit>(FXCollections.observableArrayList(SizeUnit.values()));
        sizeUnitComboBox.setTooltip(new Tooltip("Size Unit"));
        sizeUnitComboBox.getSelectionModel().select(SizeUnit.MB);
        hbox.getChildren().add(sizeUnitComboBox);

        sizeAboveCheckBox = new CheckBox("Above");
        sizeAboveCheckBox.setSelected(true);
        sizeAboveCheckBox.setTooltip(new Tooltip("Size is above or below"));
        hbox.getChildren().add(sizeAboveCheckBox);

        getChildren().add(hbox);
    }

    @Override
    public FileManagerFilter getFilter()
    {
        final Double size = getSize();

        if (size == null)
        {
            return null;
        }

        final long sizeBytes = sizeUnitComboBox.getSelectionModel().getSelectedItem().toBytes(size);

        final boolean isAbove = sizeAboveCheckBox.isSelected();

        return new FileManagerFilter()
        {
            @Override
            public boolean accept(final FileInfo fileInfo,
                                  final List<String> paths)
            {
                return isAbove == (fileInfo.getSize() >= sizeBytes);
            }
        };
    }

    private Double getSize()
    {
        Double sizeTmp = null;
        try
        {
            final String text = sizeTextField.getText();

            sizeTmp = Double.parseDouble(text);
        }
        catch (Exception e)
        {
        }
        return sizeTmp;
    }
}
