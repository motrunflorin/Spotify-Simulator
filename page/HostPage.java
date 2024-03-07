package page;

import app.Admin;
import app.audio.Collections.Podcast;
import app.user.Announcement;
import app.user.Host;
import app.user.User;

import java.util.List;
import java.util.stream.Collectors;

public class HostPage extends Page {
    public HostPage() {

    }

    /**
     * @param user
     * @return
     */
    @Override
    public String printPageContent(final User user) {
        Host host;

        if (Admin.isHost(user.getUsername())) {
            host = (Host) user;
        } else {
            host = Admin.getHost(user.getOwnerpage());
        }

        if (host == null) {
            return "Host not found";
        }

        StringBuilder content = new StringBuilder();

        // Display podcasts
        List<Podcast> podcasts = host.getPodcasts();
        content.append("Podcasts:\n\t[");
        if (!podcasts.isEmpty()) {
            content.append(podcasts.stream().map(Podcast::toString)
                    .collect(Collectors.joining(", ")));
        }
        content.append("]\n\n");

        // Display announcements
        List<Announcement> announcements = host.getAnnouncements();
        content.append("Announcements:\n\t[");
        if (!announcements.isEmpty()) {
            content.append(announcements.stream().map(Announcement::toString)
                    .collect(Collectors.joining(", ")));
        }
        content.append("]");

        return content.toString();
    }

}
