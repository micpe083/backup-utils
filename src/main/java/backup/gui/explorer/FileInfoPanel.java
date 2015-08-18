package backup.gui.explorer;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import backup.api.BackupUtil;
import backup.api.FileInfo;

public class FileInfoPanel extends BorderPane
{
    private final TextField pathTextField;

    public FileInfoPanel()
    {
        final Label label = new Label("File info:");
        setLeft(label);

        pathTextField = new TextField("-");
        pathTextField.setEditable(false);
        setCenter(pathTextField);

        setFileInfo(null);
    }

    public void setFileInfo(final FileInfo fileInfo)
    {
        final StringBuilder buf = new StringBuilder();

        if (fileInfo == null)
        {
            buf.append("-");
        }
        else
        {
            final String delim = " / ";

            buf.append(fileInfo.getFilename());
            buf.append(delim);

            buf.append(fileInfo.getHash());
            buf.append(delim);

            buf.append(BackupUtil.humanReadableByteCount(fileInfo.getSize()));
        }

        pathTextField.setText(buf.toString());
    }
}
