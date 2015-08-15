package backup.gui.explorer;

import javax.swing.JLabel;
import javax.swing.JPanel;

import backup.api.BackupUtil;
import backup.api.FileInfo;

public class FileInfoPanel extends JPanel
{
    private static final long serialVersionUID = 413806726707465428L;

    private final JLabel label = new JLabel();

    public FileInfoPanel()
    {
        add(label);

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

        label.setText(buf.toString());
    }
}
