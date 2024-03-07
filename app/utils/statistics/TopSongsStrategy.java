package app.utils.statistics;

import app.audio.Files.Song;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TopSongsStrategy implements TopListStrategy<Song> {
    /**
     * @param songs
     * @return
     */
    @Override
    public List<String> getTopList(final List<Song> songs) {
        return songs.stream()
                .sorted(Comparator.comparingInt(Song::getLikes).reversed())
                .limit(5)
                .map(Song::getName)
                .collect(Collectors.toList());
    }
}

