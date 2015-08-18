package backup.gui.explorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

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

    private final TreeItem<Object> root;

    public FileTree()
    {
        root = new TreeItem<Object>("/");

        final TreeView<Object> tree = new TreeView<Object>(root);
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

    private void expandAll(final TreeItem<Object> treeItem,
                           final boolean isExpand)
    {
        if (treeItem == null)
        {
            return;
        }

        treeItem.setExpanded(isExpand);

        for (final TreeItem<Object> currTreeItem : treeItem.getChildren())
        {
            currTreeItem.setExpanded(isExpand);

            expandAll(currTreeItem, isExpand);
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

        TreeItem<Object> currParent = this.root;

        for (final String nodeStr : list)
        {
            TreeItem<Object> currChild = null;

            for (final TreeItem<Object> testChild : currParent.getChildren())
            {
                final Object obj = testChild.getValue();

                if (nodeStr.equals(obj))
                {
                    currChild = testChild;
                    break;
                }
            }

            if (currChild == null)
            {
                currChild = new TreeItem<Object>(nodeStr);

                currParent.getChildren().add(currChild);
            }

            currParent = currChild;
        }

        currParent.getChildren().add(new TreeItem<Object>(new FileInfoNode(fileInfo)));
    }

    private void treeItemSelected(final TreeItem<Object> selectedTreeItem)
    {
        if (selectedTreeItem == null)
        {
            return;
        }

        FileInfo fileInfo = null;

        TreeItem<Object> curr = selectedTreeItem;

        final List<String> pathList = new ArrayList<>();

        do
        {
            final Object obj = curr.getValue();

            if (obj instanceof String)
            {
                final String pathElement = (String) obj;
                pathList.add(pathElement);
            }
            else if (obj instanceof FileInfoNode)
            {
                final FileInfoNode fileInfoNode = (FileInfoNode) obj;
                fileInfo = fileInfoNode.getFileInfo();
            }

            curr = curr.getParent();
        }
        while (curr != null);

        Collections.reverse(pathList);

        final StringBuilder buf = new StringBuilder();

        for (final String pathElement : pathList)
        {
            buf.append(pathElement);
            buf.append("/");
        }

        if (fileInfo != null)
        {
            buf.append(fileInfo.getFilename());
        }

        notifyListeners(fileInfo, buf.toString());
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
