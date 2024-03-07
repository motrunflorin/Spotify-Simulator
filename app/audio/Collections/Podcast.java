package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public final class Podcast extends AudioCollection {
    private final List<Episode> episodes;
    @Setter
    private boolean loaded;

    public Podcast(final String name, final String owner, final List<Episode> episodes) {
        super(name, owner);
        this.episodes = episodes;
        this.loaded = false;
    }

    @Override
    public int getNumberOfTracks() {
        return episodes.size();
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return episodes.get(index);
    }


    @Override
    public String toString() {
        String episodeString = episodes.stream()
                .map(Episode::toString)
                .collect(Collectors.joining(", "));

        return String.format("%s:\n\t[%s]\n", getName(), episodeString);
    }
}
