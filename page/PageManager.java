package page;

import app.Admin;
import app.user.User;

import java.util.HashMap;
import java.util.Map;

public final class PageManager {
    private static final Map<PageType, Page> PAGE_TYPE_MAP = new HashMap<>();

    static {
        PAGE_TYPE_MAP.put(PageType.ARTIST, new ArtistPage());
        PAGE_TYPE_MAP.put(PageType.LIKEDCONTENT, new LikedContentPage());
        PAGE_TYPE_MAP.put(PageType.HOST, new HostPage());
        PAGE_TYPE_MAP.put(PageType.DEFAULT, new HomePage());
    }

    private PageManager() {
    }

    /**
     * @param username
     * @return
     */
    public static String printCurrentPage(final String username) {
        User user = Admin.getUser(username);
        PageType currentPageType = user.getCurrentPageType();

        Page currentPage = PAGE_TYPE_MAP.getOrDefault(currentPageType,
                PAGE_TYPE_MAP.get(PageType.DEFAULT));

        return currentPage.printPageContent(user);
    }
}
