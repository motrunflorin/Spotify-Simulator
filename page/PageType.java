
package page;

import java.util.Arrays;

public enum PageType {
    HOME("Home"),
    LIKEDCONTENT("LikedContent"),
    ARTIST("Artist"),
    HOST("Host"),
    DEFAULT("Home");

    private final String val;

    PageType(final String val) {
        this.val = val;
    }

    /**
     * @param string
     * @return
     */
    public static PageType fromString(final String string) {
        return Arrays.stream(PageType.values())
                .filter(pageType -> pageType.val.equalsIgnoreCase(string))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("No enum constant " + string + " found"));
    }

    @Override
    public String toString() {
        return val;
    }
}
