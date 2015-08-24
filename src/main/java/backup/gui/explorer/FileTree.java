package backup.gui.explorer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import backup.api.FileInfo;
import backup.api.FileManager;
import backup.gui.common.GuiUtils;

public class FileTree extends BorderPane
{
    private final List<PathSelectionListener> listeners = new ArrayList<PathSelectionListener>();

    private static final Pattern PATH_PATTERN = Pattern.compile("\\\\|/");

    private final TreeView<FileTreeNode> tree;
    private final TreeItem<FileTreeNode> root;

    public FileTree()
    {
        root = new TreeItem<>(new FileTreeNode("/", "/"));

        tree = new TreeView<>(root);
        tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> treeItemSelected(newValue));

        final ScrollPane scrollPane = GuiUtils.createScrollPane(tree);

        setCenter(scrollPane);
    }

    public Button createExpandAllButton()
    {
        final Button expandAllButton = new Button("Expand All");
        expandAllButton.setOnAction(e -> expandAll(root, true));
        return expandAllButton;
    }

    public Button createCollapseAllButton()
    {
        final Button collapseAllButton = new Button("Collapse All");
        collapseAllButton.setOnAction(e -> expandAll(root, false));
        return collapseAllButton;
    }

    public void addListener(final PathSelectionListener l)
    {
        synchronized (listeners)
        {
            listeners.add(l);
        }
    }

    public void setFileManager(final FileManager fileManager)
    {
        root.getChildren().clear();

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

    private void notifyListeners(final FileInfo fileInfo, final String path)
    {
        synchronized (listeners)
        {
            for (final PathSelectionListener pathSelectionListener : listeners)
            {
                pathSelectionListener.fileSelected(fileInfo, path);
            }
        }
    }

    private void expandAll(final TreeItem<FileTreeNode> treeItem,
                           final boolean isExpand)
    {
        if (treeItem == null)
        {
            return;
        }

        treeItem.setExpanded(isExpand);

        for (final TreeItem<FileTreeNode> currTreeItem : treeItem.getChildren())
        {
            currTreeItem.setExpanded(isExpand);

            expandAll(currTreeItem, isExpand);
        }
    }

    private void addFile(final FileInfo fileInfo, final String filePath)
    {
        TreeItem<FileTreeNode> currParent = this.root;

        final Matcher matcher = PATH_PATTERN.matcher(filePath);

        int startPos = 0;
        while (matcher.find())
        {
            final int endPos = matcher.start();

            final String pathElement = filePath.substring(startPos, endPos);
            startPos = matcher.end();

            if (pathElement.isEmpty())
            {
                continue;
            }

            final String fullPath = filePath.substring(0, endPos);

            TreeItem<FileTreeNode> currChild = null;

            for (final TreeItem<FileTreeNode> testChild : currParent.getChildren())
            {
                final FileTreeNode obj = testChild.getValue();

                if (pathElement.equals(obj.getName()))
                {
                    currChild = testChild;
                    break;
                }
            }

            if (currChild == null)
            {
                final FileTreeNode node = new FileTreeNode(fullPath,
                                                           pathElement);

                currChild = addChild(currParent, node);
            }

            currParent = currChild;
        }

        final FileTreeNode node = new FileTreeNode(fileInfo,
                                                   filePath,
                                                   fileInfo.getFilename());

        addChild(currParent, node);
    }

    private TreeItem<FileTreeNode> addChild(final TreeItem<FileTreeNode> parent,
                                            final FileTreeNode node)
    {
        final TreeItem<FileTreeNode> child = new TreeItem<>(node);

        parent.getChildren().add(child);

        return child;
    }

    private void treeItemSelected(final TreeItem<FileTreeNode> selectedTreeItem)
    {
        if (selectedTreeItem == null)
        {
            return;
        }

        notifyListeners(selectedTreeItem.getValue().getFileInfo(),
                        selectedTreeItem.getValue().getFullPath());
    }

    private static final class FileTreeNode
    {
        private final FileInfo fileInfo;
        private final String fullPath;
        private final String name;

        public FileTreeNode(final String fullPath,
                            final String name)
        {
            this(null, fullPath, name);
        }

        public FileTreeNode(final FileInfo fileInfo,
                            final String fullPath,
                            final String name)
        {
            this.fileInfo = fileInfo;
            this.fullPath = fullPath;
            this.name = name;
        }

        public FileInfo getFileInfo()
        {
            return fileInfo;
        }

        public String getFullPath()
        {
            return fullPath;
        }

        public String getName()
        {
            return name;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }
}
