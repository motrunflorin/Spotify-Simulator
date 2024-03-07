package app.player;

import app.utils.Enums;
import lombok.Getter;

@Getter
public class PlayerStats {
    private final String name;
    private final int remainedTime;
    private final String repeat;
    private final boolean shuffle;
    private final boolean paused;

    public PlayerStats(final String name, final int remainedTime,
                       final Enums.RepeatMode repeatMode,
                       final boolean shuffle, final boolean paused) {
        this.name = name;
        this.remainedTime = remainedTime;
        this.paused = paused;
        this.repeat = switch (repeatMode) {
            case REPEAT_ALL -> "Repeat All";
            case REPEAT_ONCE -> "Repeat Once";
            case REPEAT_INFINITE -> "Repeat Infinite";
            case REPEAT_CURRENT_SONG -> "Repeat Current Song";
            case NO_REPEAT -> "No Repeat";
            default -> "unknown";
        };
        this.shuffle = shuffle;
    }
}
