package backup.gui.common;

import java.io.File;
import java.io.FilenameFilter;

import javafx.beans.property.ObjectProperty;
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

    private final ObjectProperty<String> fileProperty;

    private final FilenameFilter filter;

    public FileChooserComboPanel(final String labelText,
                                 final FilenameFilter filter) throws Exception
    {
        super(labelText);

        this.filter = filter;

        final Label label = new Label(labelText);

        fileList = FXCollections.observableArrayList();
        fileComboBox = new ComboBox<>(fileList);
        fileProperty = fileComboBox.valueProperty();
        //fileComboBox.valueProperty().addListener((x, y, z) -> fileComboBox.setTooltip(new Tooltip(z)));
        fileProperty.addListener((x, y, z) -> fileComboBox.setTooltip(new Tooltip(z)));

        final Button dirButton = new Button("Dir");
        dirButton.setOnAction(e -> selectFile(false));

        final Button fileButton = new Button("File");
        fileButton.setOnAction(e -> selectFile(true));

        final Button refreshButton = new Button("R");
        refreshButton.setTooltip(new Tooltip("Refresh"));
        refreshButton.setOnAction(e -> refresh());

        //final Button viewButton = new Button("V");
        //viewButton.setTooltip(new Tooltip("View File"));
        //viewButton.setOnAction(e -> viewFile());

        setLeft(label);
        setCenter(fileComboBox);

        final HBox buttonPane = new HBox();
        buttonPane.getChildren().add(dirButton);
        buttonPane.getChildren().add(fileButton);
        buttonPane.getChildren().add(refreshButton);
        //buttonPane.getChildren().add(viewButton);
        setRight(buttonPane);
    }

    //private void viewFile()
    //{
    //    final File file = getSelectedFileWarn();
    //
    //    if (file != null)
    //    {
    //        FileViewer.view(file, this);
    //    }
    //}

    public ObjectProperty<String> getFileProperty()
    {
        return fileProperty;
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

        final File[] files;

        if (dir == null)
        {
            files = null;
        }
        else if (filter == null)
        {
            files = dir.listFiles();
        }
        else
        {
            files = dir.listFiles(filter);
        }

        if (files != null)
        {
            for (final File currFile : files)
            {
                fileList.add(currFile.getAbsolutePath());
            }
        }

        FXCollections.sort(fileList);
        FXCollections.reverse(fileList);

        if (select != null)
        {
            fileComboBox.getSelectionModel().select(select);
        }

        //else if (!fileList.isEmpty())
        //{
        //    fileComboBox.getSelectionModel().select(fileList.get(0));
        //}
    }

    protected String getSelectedFileStr2()
    {
        //return fileComboBox.getSelectionModel().getSelectedItem();
        return fileProperty.get();
    }

    private void refresh()
    {
        final File selectedFile = getSelectedFile();

        if (selectedFile == null)
        {
            try
            {
                setSelection(BackupSettings.getInstance().getOutputDir());
            }
            catch (Exception e)
            {
                GuiUtils.showErrorMessage(this,
                                          "Failed to refresh files",
                                          "Error",
                                          e);
            }
        }
        else
        {
            setSelection(selectedFile);
        }
    }

    private void selectFile(final boolean isFile)
    {
        final File file = GuiUtils.selectFile(isFile,
                                              getSelectedFile(),
                                              getScene());

        if (file != null)
        {
            setSelection(file);
        }
    }
}
