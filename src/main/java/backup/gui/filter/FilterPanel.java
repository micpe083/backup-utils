package backup.gui.filter;

import java.util.ArrayList;
import java.util.List;

import backup.api.filter.FileManagerFilter;
import backup.api.filter.FileManagerFilterCompound;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

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
        add(new FilterItemPanelName());
        add(new FilterItemPanelSize());
        add(new FilterItemPanelDups());

        final Button filterButton = new Button("Filter");
        filterButton.setOnAction(x -> filter());
        getChildren().add(filterButton);

        final Button resetButton = new Button("Reset");
        resetButton.setOnAction(x -> reset());
        getChildren().add(resetButton);
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

    private void add(final FilterItemPanel panel)
    {
        list.add(panel);
        getChildren().add(panel);
    }
}
