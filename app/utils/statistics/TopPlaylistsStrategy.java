package app.utils.statistics;

import app.audio.Collections.Playlist;


import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TopPlaylistsStrategy implements TopListStrategy<Playlist> {
    /**
     * @param playlists
     * @return
     */
    @Override
    public List<String> getTopList(final List<Playlist> playlists) {
        return playlists.stream()
                .sorted(Comparator
                        .comparingInt(Playlist::getFollowers)
                        .reversed()
                        .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()))
                .limit(5)
                .map(Playlist::getName)
                .collect(Collectors.toList());
    }
}
