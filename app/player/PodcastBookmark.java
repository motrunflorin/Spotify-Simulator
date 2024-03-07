package app.player;

import lombok.Getter;

@Getter
public class PodcastBookmark {
    private final String name;
    private final int id;
    private final int timestamp;

    public PodcastBookmark(final String name, final int id, final int timestamp) {
        this.name = name;
        this.id = id;
        this.timestamp = timestamp;
    }
}
