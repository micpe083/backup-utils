package backup.gui.explorer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import backup.api.FileInfo;
import backup.api.FileManager;

public class FileTree extends JPanel implements TreeSelectionListener, ActionListener
{
    private static final long serialVersionUID = 2037529673702627281L;

    private final JTree tree;

    private final DefaultMutableTreeNode root;

    private final List<PathSelectionListener> listeners = new ArrayList<PathSelectionListener>();

    private final JButton expandAllButton = new JButton("Expand All");
    private final JButton collapseAllButton = new JButton("Collapse All");

    public FileTree()
    {
        super(new BorderLayout());

        expandAllButton.addActionListener(this);
        collapseAllButton.addActionListener(this);

        root = new DefaultMutableTreeNode("/");

        tree = new JTree(root);
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        tree.addTreeSelectionListener(this);

        final JScrollPane treeView = new JScrollPane(tree,
                                                     ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                                                     ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);


        final Dimension minimumSize = new Dimension(100, 50);
        treeView.setMinimumSize(minimumSize);

        add(treeView);
    }

    @Override
    public void actionPerformed(final ActionEvent e)
    {
        if (e.getSource() == expandAllButton)
        {
            expandAll(tree, new TreePath(root), true);
        }
        else if (e.getSource() == collapseAllButton)
        {
            expandAll(tree, new TreePath(root), false);
        }
    }

    private void expandAll(final JTree tree, final TreePath parent, final boolean isExpand)
    {
        final TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0)
        {
            for (final Enumeration<?> e = node.children(); e.hasMoreElements();)
            {
                final TreeNode n = (TreeNode) e.nextElement();
                final TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, isExpand);
            }
        }

        if (isExpand)
        {
            tree.expandPath(parent);
        }
        else
        {
            tree.collapsePath(parent);
        }
    }

    public JButton getExpandAllButton()
    {
        return expandAllButton;
    }

    public JButton getCollapseAllButton()
    {
        return collapseAllButton;
    }

    public void addListener(final PathSelectionListener l)
    {
        listeners.add(l);
    }

    private void notifyListeners(final FileInfo fileInfo, final String path)
    {
        for (final PathSelectionListener pathSelectionListener : listeners)
        {
            pathSelectionListener.fileSelected(fileInfo, path);
        }
    }

    public void valueChanged(final TreeSelectionEvent e)
    {
        final DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

        if (node == null)
        {
            return;
        }

        final StringBuilder buf = new StringBuilder();

        FileInfo fileInfo = null;

        for (final TreeNode currNode : node.getPath())
        {
            if (currNode.isLeaf())
            {
                final FileInfoNode fileInfoNode = (FileInfoNode) ((DefaultMutableTreeNode) currNode).getUserObject();

                fileInfo = fileInfoNode.getFileInfo();
                buf.append(fileInfoNode);
            }
            else
            {
                final String name = (String) ((DefaultMutableTreeNode) currNode).getUserObject();

                buf.append(name);
                buf.append("/");
            }
        }

        notifyListeners(fileInfo, buf.toString());
    }

    public void setFileManager(final FileManager fileManager)
    {
        for (final Entry<FileInfo, List<String>> entry : fileManager.getMap().entrySet())
        {
            final FileInfo fileInfo = entry.getKey();
            final List<String> paths = entry.getValue();

            for (final String string : paths)
            {
                addFile(fileInfo, string);
            }
        }
    }

    private void addFile(final FileInfo fileInfo, final String filePath)
    {
        final List<String> list = new ArrayList<String>();

        File file = new File(filePath).getParentFile();
        do
        {
            final String name = file.getName();

            if (name != null && name.length() != 0)
            {
                list.add(name);
            }

            file = file.getParentFile();
        }
        while (file != null);

        Collections.reverse(list);

        DefaultMutableTreeNode currNode = this.root;

        for (final String nodeStr : list)
        {
            DefaultMutableTreeNode childx = null;

            for (int i = 0; i < currNode.getChildCount(); i++)
            {
                final DefaultMutableTreeNode child = (DefaultMutableTreeNode) currNode.getChildAt(i);

                final Object x = child.getUserObject();

                if (nodeStr.equals(x))
                {
                    childx = child;
                }
            }

            if (childx == null)
            {
                childx = new DefaultMutableTreeNode(nodeStr, true);
                currNode.add(childx);
            }

            currNode = childx;
        }

        final DefaultMutableTreeNode childx = new DefaultMutableTreeNode(new FileInfoNode(fileInfo), false);
        currNode.add(childx);
    }

    private static final class FileInfoNode
    {
        private final FileInfo fileInfo;

        private final String name;

        public FileInfoNode(final FileInfo fileInfo)
        {
            this.fileInfo = fileInfo;
            this.name = new File(fileInfo.getFilename()).getName();
        }

        public FileInfo getFileInfo()
        {
            return fileInfo;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }
}
