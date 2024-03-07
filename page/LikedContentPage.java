package page;

import app.user.User;

import java.util.stream.Collectors;

public class LikedContentPage extends Page {
    public LikedContentPage() {
    }

    /**
     * @param user
     * @return
     */
    @Override
    public String printPageContent(final User user) {
        String likedSongsOutput = user.getLikedSongs().stream()
                .map(song -> "%s - %s".formatted(song.getName(), song.getArtist()))
                .collect(Collectors.joining(", ", "Liked songs:\n\t[", "]"));

        String followedPlaylistsOutput = user.getFollowedPlaylists().stream()
                .map(playlist -> "%s - %s".formatted(playlist.getName(), playlist.getOwner()))
                .collect(Collectors.joining(", ", "\n\nFollowed playlists:\n\t[", "]"));

        return likedSongsOutput + followedPlaylistsOutput;
    }
}
