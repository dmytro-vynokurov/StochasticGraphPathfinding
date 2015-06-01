package eventhandling;

import java.util.EventListener;

public interface GraphChangedListener extends EventListener {
    public void graphChanged(GraphChangedEvent event);
}
