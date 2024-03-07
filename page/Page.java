package page;

import app.user.User;

import java.util.ArrayList;
import java.util.List;

public abstract class Page {

    public Page() {
    }

    /**
     * @param user
     * @return
     */
    public abstract String printPageContent(User user);
}
