package backup.gui.common;

import java.io.File;

import com.google.common.base.Strings;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class FileChooserPanel extends BorderPane
{
    private final TextField pathTextField;

    private final boolean isFile;

    public FileChooserPanel(final String labelText,
                            final boolean isFile)
    {
        this.isFile = isFile;

        final Label label = new Label(labelText);

        pathTextField = new TextField();
        final Button browseButton = new Button("Browse...");
        browseButton.setOnAction(e -> selectFile());

        setLeft(label);
        setCenter(pathTextField);
        setRight(browseButton);
    }

    public void setSelection(final String text)
    {
        pathTextField.setText(text);
    }

    public String getSelectedFileStr()
    {
        return Strings.nullToEmpty(pathTextField.getText());
    }

    public File getSelectedFile()
    {
        return new File(getSelectedFileStr());
    }

    private void selectFile()
    {
        final File file;

        final File selectedFile = getSelectedFile();

        if (isFile)
        {
            final FileChooser fileChooser = new FileChooser();

            if (selectedFile.getParentFile().isDirectory())
            {
                fileChooser.setInitialDirectory(selectedFile.getParentFile());
            }

            file = fileChooser.showOpenDialog(getScene().getWindow());
        }
        else
        {
            final DirectoryChooser fileChooser = new DirectoryChooser();

            if (selectedFile.isDirectory())
            {
                fileChooser.setInitialDirectory(selectedFile);
            }

            file = fileChooser.showDialog(getScene().getWindow());
        }

        if (file != null)
        {
            pathTextField.setText(file.getAbsolutePath());
        }
    }
}
