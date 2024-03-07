package app.utils.statistics;

import app.user.Artist;


import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TopArtistsStrategy implements TopListStrategy<Artist> {
    /**
     * @param artists
     * @return
     */
    @Override
    public List<String> getTopList(final List<Artist> artists) {
        return artists.stream()
                .sorted(Comparator.comparingInt(Artist::getNumberOfLikes).reversed())
                .limit(5)
                .map(Artist::getUsername)
                .collect(Collectors.toList());
    }
}
