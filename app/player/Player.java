package app.player;


import app.audio.Collections.AudioCollection;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.LibraryEntry;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public class Player {
    private Enums.RepeatMode repeatMode;
    private boolean shuffle;
    private boolean paused;
    private PlayerSource source;
    private String type;
    private final int skipTime = 90;

    private final ArrayList<PodcastBookmark> podcastBookmarks = new ArrayList<>();


    public Player() {
        this.repeatMode = Enums.RepeatMode.NO_REPEAT;
        this.paused = true;
    }

    /**
     *
     */
    public void stop() {
        if ("podcast".equals(this.type)) {
            bookmarkPodcast();
        }

        repeatMode = Enums.RepeatMode.NO_REPEAT;
        paused = true;
        source = null;
        shuffle = false;
    }

    /**
     *
     */
    private void bookmarkPodcast() {
        if (source != null && source.getAudioFile() != null) {
            PodcastBookmark currentBookmark = new PodcastBookmark(
                    source.getAudioCollection().getName(),
                    source.getIndex(),
                    source.getDuration()
            );

            podcastBookmarks.removeIf(bookmark -> bookmark.getName()
                    .equals(currentBookmark.getName()));
            podcastBookmarks.add(currentBookmark);
        }
    }


    /**
     * @param type
     * @param entry
     * @return
     */
    public static PlayerSource createSource(final String type, final LibraryEntry entry,
                                            final List<PodcastBookmark> podcastBookmarks) {
         return switch (type) {
        //PlayerSource source = switch (type) {
            case "song" -> new PlayerSource(Enums.PlayerSourceType.LIBRARY, (AudioFile) entry);
            case "playlist" -> new PlayerSource(Enums.PlayerSourceType.PLAYLIST,
                    (AudioCollection) entry);
            case "podcast" -> createPodcastSource((AudioCollection) entry, podcastBookmarks);
            case "album" -> new PlayerSource(Enums.PlayerSourceType.ALBUM, (AudioCollection) entry);
            default -> null;
        };

//        // Increment listens when an audio file starts playing
//        if (source != null && source.getAudioFile() != null) {
//            source.getAudioFile().incrementListens();
//        }
//
//        return source;
    }


    /**
     * @param collection
     * @param podcastBookmarks
     * @return
     */
    private static PlayerSource createPodcastSource(final AudioCollection collection,
                                                    final List<PodcastBookmark> podcastBookmarks) {
        return podcastBookmarks.stream()
                .filter(bookmark -> bookmark.getName().equals(collection.getName()))
                .findFirst()
                .map(bookmark -> new PlayerSource(Enums.PlayerSourceType.PODCAST,
                        collection, bookmark))
                .orElse(new PlayerSource(Enums.PlayerSourceType.PODCAST, collection));
    }


    /**
     *
     */
    public void setSource(final LibraryEntry entry, final String type) {
        if ("podcast".equals(this.type)) {
            bookmarkPodcast();
        }

        this.type = type;
        this.source = createSource(type, entry, podcastBookmarks);
        this.repeatMode = Enums.RepeatMode.NO_REPEAT;
        this.shuffle = false;
        this.paused = true;
    }

    /**
     *
     */
    public void pause() {
        paused = !paused;
    }

    /**
     * @param seed
     */
    public void shuffle(final Integer seed) {
        if (seed != null) {
            source.generateShuffleOrder(seed);
        }

        shuffle = !shuffle && (source.getType() == Enums.PlayerSourceType.PLAYLIST
                || source.getType() == Enums.PlayerSourceType.ALBUM);

        if (shuffle) {
            source.updateShuffleIndex();
        }
    }


    /**
     *
     */
    public Enums.RepeatMode repeat() {
        switch (repeatMode) {
            case NO_REPEAT:
                repeatMode = (source.getType() == Enums.PlayerSourceType.LIBRARY
                        || source.getType() == Enums.PlayerSourceType.ALBUM)
                        ? Enums.RepeatMode.REPEAT_ONCE
                        : Enums.RepeatMode.REPEAT_ALL;
                break;

            case REPEAT_ONCE:
                repeatMode = Enums.RepeatMode.REPEAT_INFINITE;
                break;

            case REPEAT_ALL:
                repeatMode = Enums.RepeatMode.REPEAT_CURRENT_SONG;
                break;

            default:
                repeatMode = Enums.RepeatMode.NO_REPEAT;
        }

        return repeatMode;
    }




    /**
     * @param time
     */
    public void simulatePlayer(int time) {
        if (source != null) {
            if (!paused) {
                while (time >= source.getDuration()) {
                    time -= source.getDuration();

                    //incrementListens();

                    next();
                    if (paused) {
                        break;
                    }
                }
                if (!paused) {
                    source.skip(-time);
                }
            }
        }




    }

//    private void incrementListens() {
//        AudioFile currentAudioFile = getCurrentAudioFile();
//
//        if (currentAudioFile instanceof Episode currentEpisode) {
//            currentEpisode.incrementListens();
//        }
//    }

    /**
     *
     */
    public void next() {
        paused = source.setNextAudioFile(repeatMode, shuffle);

        if (repeatMode == Enums.RepeatMode.REPEAT_ONCE) {
            repeatMode = Enums.RepeatMode.NO_REPEAT;
        }

        if (source.getDuration() == 0 && paused) {
            stop();
        }
    }


    /**
     *
     */
    public void prev() {
        source.setPrevAudioFile(shuffle);
        paused = false;
    }

    /**
     * @param duration
     */
    private void skip(final int duration) {
        source.skip(duration);
        paused = false;
    }

    /**
     *
     */
    public void skipNext() {
        if (source.getType() == Enums.PlayerSourceType.PODCAST) {
            skip(-skipTime);
        }
    }

    /**
     *
     */
    public void skipPrev() {
        if (source.getType() == Enums.PlayerSourceType.PODCAST) {
            skip(skipTime);
        }
    }

    /**
     * @return
     */
    public AudioFile getCurrentAudioFile() {
        if (source == null) {
            return null;
        }

        return source.getAudioFile();
    }

    /**
     * @return
     */
    public boolean getPaused() {
        return paused;
    }

    /**
     * @return
     */
    public boolean getShuffle() {
        return shuffle;
    }

    /**
     * @return
     */
    public PlayerStats getStats() {
        String filename = "";
        int duration = 0;

        if (source != null && source.getAudioFile() != null) {
            filename = source.getAudioFile().getName();
            duration = source.getDuration();
        } else {
            stop();
        }

        return new PlayerStats(filename, duration, repeatMode, shuffle, paused);
    }


}
