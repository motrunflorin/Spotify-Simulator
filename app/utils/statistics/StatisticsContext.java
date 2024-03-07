package app.utils.statistics;


import java.util.List;

public class StatisticsContext<T> {
    private final TopListStrategy<T> strategy;
    public StatisticsContext(final TopListStrategy<T> strategy) {
        this.strategy = strategy;
    }

    /**
     * @param items
     * @return
     */
    public List<String> getTopList(final List<T> items) {
        return strategy.getTopList(items);
    }
}
