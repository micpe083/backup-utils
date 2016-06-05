package backup.gui.filter;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import backup.api.filter.FileManagerFilter;
import backup.api.filter.FileManagerFilterCompound;

public class FilterPanel extends VBox
{
    public interface FilterListener
    {
        void onFilterAction(FileManagerFilter filter);
    }

    private final List<FilterItemPanel> list = new ArrayList<>();

    private FilterListener listener;

    public FilterPanel()
    {
        final FlowPane filterPane = new FlowPane();
        add(filterPane, new FilterItemPanelSize());
        add(filterPane, new FilterItemPanelName());
        add(filterPane, new FilterItemPanelDups());
        add(filterPane, new FilterItemPanelDir());
        getChildren().add(filterPane);

        final HBox hbox = new HBox();
        final Button filterButton = new Button("Filter");
        filterButton.setOnAction(x -> filter());
        hbox.getChildren().add(filterButton);

        final Button resetButton = new Button("Reset");
        resetButton.setOnAction(x -> reset());
        hbox.getChildren().add(resetButton);

        getChildren().add(hbox);
    }

    public void setListener(final FilterListener listener)
    {
        this.listener = listener;
    }

    private void reset()
    {
        final FilterListener listener = this.listener;
        if (listener != null)
        {
            listener.onFilterAction(null);
        }
    }

    private void filter()
    {
        final FileManagerFilterCompound filters = new FileManagerFilterCompound();

        for (final FilterItemPanel filterItemPanel : list)
        {
            final FileManagerFilter filter = filterItemPanel.getFilter();

            if (filter != null)
            {
                filters.add(filter);
            }
        }

        final FilterListener listener = this.listener;
        if (listener != null)
        {
            listener.onFilterAction(filters);
        }
    }

    private void add(final FlowPane pane,
                     final FilterItemPanel panel)
    {
        list.add(panel);
        pane.getChildren().add(panel);
    }
}
