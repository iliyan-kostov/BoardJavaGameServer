package apps;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class GameManager implements PropertyChangeListener {

    protected final NetServer server;

    public GameManager(NetServer server) {
        this.server = server;
        this.server.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    }
}
