package app.utils.statistics;

import app.audio.Collections.Album;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TopAlbumsStrategy implements TopListStrategy<Album> {
    /**
     * @param albums
     * @return
     */
    @Override
    public List<String> getTopList(final List<Album> albums) {
        return albums.stream()
                .sorted(Comparator
                        .comparingInt(Album::getNumberOfLikes)
                        .reversed()
                        .thenComparing(Album::getName, Comparator.naturalOrder()))
                .limit(5)
                .map(Album::getName)
                .collect(Collectors.toList());
    }
}
