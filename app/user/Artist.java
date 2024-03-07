package app.user;

import app.Admin;
import app.audio.Collections.Album;
import app.audio.Files.Song;
import lombok.Getter;


import java.util.ArrayList;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Artist extends User {
    private final List<Album> albums;
    private ArrayList<Event> events;
    private ArrayList<Merch> merches;

    public Artist(final String username, final int age, final String city) {
        super(username, age, city);
        this.albums = new ArrayList<>();
        this.merches = new ArrayList<>();
        this.events = new ArrayList<>();
    }

    /**
     * @param album
     */
    public void addAlbum(final Album album) {
        albums.add(album);
    }

    /**
     * @param albumName
     * @return
     */
    public Album getAlbumByName(final String albumName) {
        return albums.stream()
                .filter(album -> album.getName().equals(albumName))
                .findFirst()
                .orElse(null);
    }

    /**
     * @return
     */
    public List<String> showAlbums() {
        return albums.stream()
                .map(Album::getName)
                .collect(Collectors.toList());
    }


    /**
     * @param album
     * @return
     */
    public boolean hasDuplicateSongs(final Album album) {
        List<String> songNames = album.getSongs().stream()
                .map(Song::getName)
                .toList();

        return songNames.size() != songNames.stream().distinct().count();
    }

    /**
     * @param name
     * @param description
     * @param date
     * @return
     */
    public String addEvent(final String name, final String description, final String date) {
        // Initialize the events list if it's not initialized
        if (events == null) {
            events = new ArrayList<>();
        }

        Event newEvent = new Event(name, description, date);

        // Check if the date is valid using the isValidDate method from the Event class
        if (!newEvent.isValidDate(date)) {
            return "Event for " + getUsername() + " does not have a valid date.";
        }

        // Check if the event name is unique
        if (events.stream().anyMatch(event -> event.getName().equals(name))) {
            return getUsername() + " has another event with the same name.";
        }

        events.add(newEvent);

        return getUsername() + " has added new event successfully.";
    }

    /**
     * @param name
     * @return
     */
    public String removeEvent(final String name) {
        // Check if the event exists
        if (events == null || events.stream().noneMatch(event -> event.getName().equals(name))) {
            return getUsername() + " doesn't have an event with the given name.";
        }

        events.removeIf(event -> event.getName().equals(name));

        return getUsername() + " deleted the event successfully.";
    }

    /**
     * @param name
     * @param description
     * @param price
     * @return
     */
    public String addMerch(final String name, final String description, final int price) {
        // Initialize the merch list if it's not initialized
        if (merches == null) {
            merches = new ArrayList<>();
        }

        // Check if the merch is unique based on the name
        if (merches.stream().anyMatch(existingMerch -> existingMerch.getName().equals(name))) {
            return getUsername() + " has merchandise with the same name.";
        }

        // Check if the price is negative
        if (price < 0) {
            return "Price for merchandise can not be negative.";
        }

        Merch newMerch = new Merch(name, description, price);
        merches.add(newMerch);

        return getUsername() + " has added new merchandise successfully.";
    }


    /**
     * @return
     */
    public int getNumberOfLikes() {
        return getAlbums().stream()
                .mapToInt(Album::getNumberOfLikes)
                .sum();
    }

    /**
     * @return
     */
    public boolean hasSongPlaying() {
        // Iterate over all users
        for (User user : Admin.getUsers()) {
            // Check if the user's player has a current song playing
            if (user.getPlayer().getCurrentAudioFile() != null) {
                if (Admin.isSong(user.getPlayer().getCurrentAudioFile().getName())) {
                    Song currentSong = (Song) user.getPlayer().getCurrentAudioFile();

                    // Check if the current song matches any song in albums
                    for (Album album : albums) {
                        if (album.getSongs().contains(currentSong)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * @return
     */
    public List<Song> getSongs() {
        return getAlbums().stream()
                .flatMap(album -> album.getSongs().stream())
                .toList();
    }

}
