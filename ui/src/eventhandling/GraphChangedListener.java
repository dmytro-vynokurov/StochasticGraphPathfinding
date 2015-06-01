package eventhandling;

import java.util.EventListener;

public interface GraphChangedListener extends EventListener {
    void graphChanged(GraphChangedEvent event);
}
