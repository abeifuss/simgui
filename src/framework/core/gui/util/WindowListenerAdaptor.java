package framework.core.gui.util;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Class WindowListenerAdaptor overrides all WindowListener Functions with empty
 * Method stubs to let the user of this class only implement the needed methods.
 * 
 * @author Marius Fink
 * @version 30.05.2012
 */
public abstract class WindowListenerAdaptor implements WindowListener {

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }
}
