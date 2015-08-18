package backup.gui.common;

import java.io.File;

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

    public String getSelectionStr()
    {
        return pathTextField.getText();
    }

    public File getSelectionFile()
    {
        return new File(getSelectionStr());
    }

    private void selectFile()
    {
        final File file;

        if (isFile)
        {
            final FileChooser fileChooser = new FileChooser();

            file = fileChooser.showOpenDialog(getScene().getWindow());
        }
        else
        {
            final DirectoryChooser fileChooser = new DirectoryChooser();

            file = fileChooser.showDialog(getScene().getWindow());
        }

        if (file != null)
        {
            pathTextField.setText(file.getAbsolutePath());
        }
    }
}
