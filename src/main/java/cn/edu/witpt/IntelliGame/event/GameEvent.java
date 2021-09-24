package cn.edu.witpt.IntelliGame.event;

import javafx.event.Event;
import javafx.event.EventType;
/**
 * @author nIck_
 */
public class GameEvent extends Event {
    public static final EventType<GameEvent> MONSTER_KILLED =
            new EventType<>(ANY, "MONSTER_KILLED");

    public static final EventType<GameEvent> PLAYER_GOT_HIT =
            new EventType<>(ANY, "PLAYER_GOT_HIT");

    public static final EventType<GameEvent> HOME_GOT_HIT =
            new EventType<>(ANY, "HOME_GOT_HIT");

    public GameEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
