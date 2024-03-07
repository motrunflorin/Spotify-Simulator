package page;

import app.audio.Collections.Playlist;
import app.audio.Files.Song;
import app.user.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class HomePage extends Page {

    public HomePage() {

    }

    /**
     * @param user
     * @return
     */
    @Override
    public String printPageContent(final User user) {
        List<Song> topLikedSongs = user.getLikedSongs().stream()
                .sorted(Comparator.comparingInt(Song::getLikes).reversed())
                .limit(5)
                .toList();

        List<Playlist> topFollowedPlaylists = user.getFollowedPlaylists().stream()
                .sorted(Comparator.comparingInt(Playlist::getTotalLikes).reversed())
                .limit(5)
                .toList();

        String likedSongsOutput = topLikedSongs.stream()
                .map(Song::getName)
                .collect(Collectors.joining(", ", "[", "]"));

        String followedPlaylistsOutput = topFollowedPlaylists.stream()
                .map(Playlist::getName)
                .collect(Collectors.joining(", ", "[", "]"));

        return "Liked songs:\n\t%s\n\nFollowed playlists:\n\t%s"
                .formatted(likedSongsOutput, followedPlaylistsOutput);
    }
}
