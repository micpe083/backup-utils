package backup.gui.explorer;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import backup.api.FileInfo;
import backup.api.FileManager;

public class PathsPanel extends JPanel
{
    private static final long serialVersionUID = -1009246994340416847L;

    private final JList<String> pathList;
    private final DefaultListModel<String> pathListModel;

    private final JLabel label;

    public PathsPanel()
    {
        setLayout(new BorderLayout());

        pathListModel = new DefaultListModel<String>();

        //Create the list and put it in a scroll pane.
        pathList = new JList<String>(pathListModel);
        pathList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pathList.setVisibleRowCount(4);

        label = new JLabel("-");

        final JScrollPane scrollPane = new JScrollPane(pathList,
                                                       JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                                       JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);
        add(label, BorderLayout.SOUTH);
    }

    public void setFileInfo(final FileManager fileManager,
                            final FileInfo fileInfo)
    {
        pathListModel.clear();

        if (fileManager == null ||
            fileInfo == null)
        {
            label.setText("-");
        }
        else
        {
            final List<String> paths = fileManager.getMap().get(fileInfo);

            for (final String path : paths)
            {
                pathListModel.addElement(path);
            }

            label.setText("File Count: " + paths.size());
        }
    }
}
