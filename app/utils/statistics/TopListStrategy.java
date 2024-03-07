package app.utils.statistics;

import java.util.Comparator;
import java.util.List;

public interface TopListStrategy<T> {

    /**
     * @param items
     * @return
     */
    List<String> getTopList(List<T> items);
}
