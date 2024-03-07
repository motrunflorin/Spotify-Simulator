package app.user;

import lombok.Getter;


@Getter
public class Merch {
    private final String name;
    private final String description;
    private final int price;

    public Merch(final String name, final String description, final int price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }


    /**
     * @return
     */
    @Override
    public String toString() {
        return String.format("%s - %d:\n\t%s", getName(), getPrice(), getDescription());
    }

}
