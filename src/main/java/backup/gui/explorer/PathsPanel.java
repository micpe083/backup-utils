package backup.gui.explorer;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import backup.api.FileInfo;
import backup.api.FileManager;

public class PathsPanel extends JPanel
{
    private static final long serialVersionUID = -1009246994340416847L;

    private final JTextArea textArea;
    private final JLabel label;

    public PathsPanel()
    {
        setLayout(new BorderLayout());

        textArea = new JTextArea(3, 20);
        textArea.setEditable(false);

        label = new JLabel("-");

        final JScrollPane scrollPane = new JScrollPane(textArea,
                                                       JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                       JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);
        add(label, BorderLayout.SOUTH);
    }

    public void setFileInfo(final FileManager fileManager,
                            final FileInfo fileInfo)
    {
        if (fileManager == null ||
            fileInfo == null)
        {
            textArea.setText("");
            label.setText("-");
        }
        else
        {
            final List<String> paths = fileManager.getMap().get(fileInfo);

            final StringBuilder buf = new StringBuilder();
            for (final String path : paths)
            {
                buf.append(path);
                buf.append('\n');
            }

            textArea.setText(buf.toString());
            label.setText("File Count: " + paths.size());
        }
    }
}
