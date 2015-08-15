package backup.gui.explorer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import backup.api.FileInfo;
import backup.api.FileManager;
import backup.gui.common.StatsPanel;

public class FileExplorer extends JPanel implements PathSelectionListener
{
    private static final long serialVersionUID = 2037529673702627281L;

    private final FileTree fileTree;
    private final PathPanel pathPanel;
    private final FileInfoPanel fileInfoPanel;
    private final StatsPanel statsPanel;
    private final PathsPanel pathsPanel;

    private FileManager fileManager;

    public FileExplorer()
    {
        super(new BorderLayout());

        fileTree = new FileTree();
        fileTree.addListener(this);

        pathPanel = new PathPanel();

        add(fileTree, BorderLayout.CENTER);

        final JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(pathPanel);

        fileInfoPanel = new FileInfoPanel();
        panel.add(fileInfoPanel);

        statsPanel = new StatsPanel();
        panel.add(statsPanel);

        pathsPanel = new PathsPanel();
        panel.add(pathsPanel);

        final JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(fileTree.getExpandAllButton());
        buttonPanel.add(fileTree.getCollapseAllButton());
        panel.add(buttonPanel);

        add(panel, BorderLayout.SOUTH);
    }

    @Override
    public void fileSelected(final FileInfo fileInfo,
                             final String path)
    {
        fileInfoPanel.setFileInfo(fileInfo);
        pathPanel.setPath(path);

        pathsPanel.setFileInfo(fileManager, fileInfo);
    }

    public void setFileManager(final FileManager fileManager)
    {
        this.fileManager = fileManager;
        fileTree.setFileManager(fileManager);
        statsPanel.setFileManager(fileManager);
    }

    public static FileExplorer createAndShowGUI(final FileManager fileManager)
    {
        final JFrame frame = new JFrame("File Tree");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        final FileExplorer tree = new FileExplorer();
        frame.add(tree);

        frame.pack();
        frame.setVisible(true);

        tree.setFileManager(fileManager);

        frame.setSize(new Dimension(800, 600));

        return tree;
    }
}
