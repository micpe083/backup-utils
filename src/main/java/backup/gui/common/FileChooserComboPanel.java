package backup.gui.common;

import java.io.File;
import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;

public class FileChooserComboPanel extends FileChooserAbstract
{
    private final ComboBox<String> fileComboBox;
    private final ObservableList<String> fileList;

    public FileChooserComboPanel(final String labelText) throws IOException
    {
        super(labelText);

        final Label label = new Label(labelText);

        fileList = FXCollections.observableArrayList();
        fileComboBox = new ComboBox<>(fileList);
        fileComboBox.valueProperty().addListener((x, y, z) -> fileComboBox.setTooltip(new Tooltip(z)));

        final Button dirButton = new Button("Dir");
        dirButton.setOnAction(e -> selectFile(false));

        final Button fileButton = new Button("File");
        fileButton.setOnAction(e -> selectFile(true));

        final Button refreshButton = new Button("R");
        refreshButton.setTooltip(new Tooltip("Refresh"));
        refreshButton.setOnAction(e -> refresh());

        setLeft(label);
        setCenter(fileComboBox);

        final HBox buttonPane = new HBox();
        buttonPane.getChildren().add(dirButton);
        buttonPane.getChildren().add(fileButton);
        buttonPane.getChildren().add(refreshButton);
        setRight(buttonPane);
    }

    public void setSelection(final File file)
    {
        fileList.clear();

        if (file == null)
        {
            return;
        }

        final File dir;

        final String select;

        if (file.isFile())
        {
            dir = file.getParentFile();
            select = file.getAbsolutePath();
        }
        else if (file.isDirectory())
        {
            dir = file;
            select = null;
        }
        else
        {
            throw new IllegalArgumentException("file doesn't exist: " + file);
        }

        for (final File currFile : dir.listFiles())
        {
            fileList.add(currFile.getAbsolutePath());
        }

        FXCollections.sort(fileList);
        FXCollections.reverse(fileList);

        if (select != null)
        {
            fileComboBox.getSelectionModel().select(select);
        }
        else if (!fileList.isEmpty())
        {
            fileComboBox.getSelectionModel().select(fileList.get(0));
        }
    }

    protected String getSelectedFileStr2()
    {
        return fileComboBox.getSelectionModel().getSelectedItem();
    }

    private void refresh()
    {
        setSelection(getSelectedFile());
    }

    private void selectFile(final boolean isFile)
    {
        final File file = GuiUtils.selectFile(isFile,
                                              getSelectedFile(),
                                              getScene());

        setSelection(file);
    }
}
