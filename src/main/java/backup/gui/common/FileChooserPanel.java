package backup.gui.common;

import java.io.File;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FileChooserPanel extends FileChooserAbstract
{
    private final TextField pathTextField;

    private final boolean isFile;

    public FileChooserPanel(final String labelText,
                            final boolean isFile)
    {
        super(labelText);

        this.isFile = isFile;

        final Label label = new Label(labelText);

        pathTextField = new TextField();
        final Button browseButton = new Button("Browse...");
        browseButton.setOnAction(e -> selectFile());

        setLeft(label);
        setCenter(pathTextField);
        setRight(browseButton);
    }

    public void setSelection(final File file)
    {
        pathTextField.setText(file == null ? "" : file.getAbsolutePath());
    }

    protected String getSelectedFileStr2()
    {
        return pathTextField.getText();
    }

    private void selectFile()
    {
        final File selectedFile = getSelectedFile();

        final File file = GuiUtils.selectFile(isFile,
                                              selectedFile,
                                              getScene());

        if (file != null)
        {
            pathTextField.setText(file.getAbsolutePath());
        }
    }
}
