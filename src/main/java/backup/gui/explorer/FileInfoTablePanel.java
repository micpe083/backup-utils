package backup.gui.explorer;

import java.util.List;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import backup.api.BackupUtil;
import backup.api.FileInfo;
import backup.api.FileInfoPath;
import backup.api.FileManager;
import backup.gui.common.GuiUtils;

public class FileInfoTablePanel extends BorderPane
{
    private final ObservableList<FileInfoPath> tableItems;

    private final Label label;

    public FileInfoTablePanel()
    {
        tableItems = FXCollections.observableArrayList();

        final TableView<FileInfoPath> tableView = new TableView<>(tableItems);

        final TableColumn<FileInfoPath, String> col1 = new TableColumn<>("Path");
        col1.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getPath()));
        col1.prefWidthProperty().bind(tableView.widthProperty().multiply(0.6));
        tableView.getColumns().add(col1);

        final TableColumn<FileInfoPath, String> col2 = new TableColumn<>("Size");
        col2.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(BackupUtil.humanReadableByteCount(p.getValue().getFileInfo().getSize())));
        col2.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1));
        tableView.getColumns().add(col2);

        final TableColumn<FileInfoPath, String> col3 = new TableColumn<>("Alg");
        col3.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getFileInfo().getDigestAlg().name()));
        col3.prefWidthProperty().bind(tableView.widthProperty().multiply(0.1));
        tableView.getColumns().add(col3);

        final TableColumn<FileInfoPath, String> col4 = new TableColumn<>("Digest");
        col4.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getFileInfo().getDigest()));
        col4.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
        tableView.getColumns().add(col4);

        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //pathList.setVisibleRowCount(4);
        tableView.setPrefHeight(100);

        label = new Label();

        final ScrollPane scrollPane = GuiUtils.createScrollPane(tableView);

        setCenter(scrollPane);
        setBottom(label);

        setFileInfo(null, null);
    }

    public void setFileInfo(final FileManager fileManager,
                            final FileInfo fileInfo)
    {
        tableItems.clear();

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
                final FileInfoPath fileInfoPath = new FileInfoPath(fileInfo, path);

                tableItems.add(fileInfoPath);
            }

            label.setText("File Count: " + paths.size());
        }
    }
}
