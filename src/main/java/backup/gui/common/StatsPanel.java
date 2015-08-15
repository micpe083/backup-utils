package backup.gui.common;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import backup.api.FileManager;
import backup.api.FileSum;

public class StatsPanel extends JPanel
{
    private static final long serialVersionUID = -742770667890617193L;

    private final FileSumPanel fileSumPanelSize = new FileSumPanel();
    private final FileSumPanel fileSumPanelCount = new FileSumPanel();

    public StatsPanel()
    {
        setLayout(new FlowLayout());

        add(fileSumPanelSize);
        add(fileSumPanelCount);
    }

    public void setFileManager(final FileManager fileManager)
    {
        final FileSum[] fileSums = fileManager.getFileSum();

        fileSumPanelCount.setFileSum(fileSums[0]);
        fileSumPanelSize.setFileSum(fileSums[1]);
    }

    private static class FileSumPanel extends JPanel
    {
        private static final long serialVersionUID = 3928748558266623965L;

        private final JLabel label = new JLabel("-");

        public FileSumPanel()
        {
            super(new BorderLayout());

            add(label, BorderLayout.CENTER);
        }

        public void setFileSum(final FileSum fileSum)
        {
            final String str = fileSum.getType() + " - Tot: " + fileSum.getTotalStr() + " - Unique: " + fileSum.getUniqueStr() + " - Dups: " + fileSum.getDuplicateStr();

            label.setText(str);
        }
    }
}
