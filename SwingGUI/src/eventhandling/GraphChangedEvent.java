package eventhandling;

import java.util.EventObject;

public class GraphChangedEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public GraphChangedEvent(Object source) {
        super(source);
    }
}
