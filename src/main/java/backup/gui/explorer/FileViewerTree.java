package backup.gui.explorer;

import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import backup.api.FileInfo;
import backup.api.FileManager;
import backup.gui.common.GuiUtils;

public class FileViewerTree extends FileViewer
{
    private static final Pattern PATH_PATTERN = Pattern.compile("\\\\|/");

    private final TreeView<FileTreeNode> tree;
    private final TreeItem<FileTreeNode> root;

    public FileViewerTree()
    {
        root = new TreeItem<>(new FileTreeNode("x", "x"));

        tree = new TreeView<>(root);
        tree.showRootProperty().set(false);
        tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> treeItemSelected(newValue));

        final ScrollPane scrollPane = GuiUtils.createScrollPane(tree);

        setCenter(scrollPane);

        final HBox buttonPanel = new HBox();

        final Button expandAllButton = new Button("Expand All");
        expandAllButton.setOnAction(e -> expandAll(root, true));
        buttonPanel.getChildren().add(expandAllButton);

        final Button collapseAllButton = new Button("Collapse All");
        collapseAllButton.setOnAction(e -> expandAll(root, false));
        buttonPanel.getChildren().add(collapseAllButton);

        setBottom(buttonPanel);
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

    @Override
    public FileManager getSelected(final FileManager fileManager)
    {
        // not supported in tree view
        return null;
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

        boolean pathExists = true;
        int startPos = 0;
        while (matcher.find())
        {
            final int endPos = matcher.start();

            final String pathElement = startPos == endPos ? "/" : filePath.substring(startPos, endPos);
            startPos = matcher.end();

            final String fullPath = filePath.substring(0, startPos);

            TreeItem<FileTreeNode> currChild = null;

            if (pathExists)
            {
                for (final TreeItem<FileTreeNode> testChild : currParent.getChildren())
                {
                    final FileTreeNode node = testChild.getValue();

                    if (pathElement.equals(node.getName()))
                    {
                        currChild = testChild;
                        break;
                    }
                }
            }

            if (currChild == null)
            {
                pathExists = false;

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
