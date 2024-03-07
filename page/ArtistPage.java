package page;

import app.Admin;
import app.audio.Collections.Album;
import app.user.Artist;
import app.user.Event;
import app.user.Merch;
import app.user.User;

import java.util.List;
import java.util.stream.Collectors;

public class ArtistPage extends Page {
    public ArtistPage() {

    }

    /**
     * @param user The user that is currently logged in
     * @return
     */
    @Override
    public String printPageContent(final User user) {
        Artist artist = Admin.getArtist(user.getLastSelected() != null
                ? user.getLastSelected().toString() : null);


        if (artist == null) {
            return "Artist not found";
        }

        StringBuilder content = new StringBuilder();

        // Display albums
        List<Album> albums = artist.getAlbums();
        content.append("Albums:\n\t[");
        if (!albums.isEmpty()) {
            content.append(albums.stream().map(Album::toString).collect(Collectors.joining(", ")));
        }
        content.append("]\n\n");

        // Display merchandise
        List<Merch> merches = artist.getMerches();
        content.append("Merch:\n\t[");
        if (!merches.isEmpty()) {
            content.append(merches.stream().map(Merch::toString).collect(Collectors.joining(", ")));
        }
        content.append("]\n\n");

        // Display events
        List<Event> events = artist.getEvents();
        content.append("Events:\n\t[");
        if (!events.isEmpty()) {
            content.append(events.stream().map(Event::toString).collect(Collectors.joining(", ")));
        }
        content.append("]");

        return content.toString();
    }
}
