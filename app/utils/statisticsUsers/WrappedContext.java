//package app.utils.statisticsUsers;
//
//public class WrappedContext {
//    private StatisticsStrategy strategy;
//
//    public void setStrategy(StatisticsStrategy strategy) {
//        this.strategy = strategy;
//    }
//
//    public String generateStatistics(User user, long timestamp) {
//        if (strategy == null) {
//            throw new IllegalStateException("Strategy not set.");
//        }
//        return strategy.generateStatistics(user, timestamp);
//    }
//
//
////    private final TopListStrategy<T> strategy;
////    public StatisticsContext(final TopListStrategy<T> strategy) {
////        this.strategy = strategy;
////    }
////
////    /**
////     * @param items
////     * @return
////     */
////    public List<String> getTopList(final List<T> items) {
////        return strategy.getTopList(items);
////    }
//}
