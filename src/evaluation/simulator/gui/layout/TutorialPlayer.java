package evaluation.simulator.gui.layout;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.swing.JFrame;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import evaluation.simulator.conf.service.UserConfigService;

/**
 * Video player
 * 
 * @author alex
 *
 */
@SuppressWarnings("serial")
public class TutorialPlayer extends JFrame {
	
	private static Logger logger = Logger.getLogger(TutorialPlayer.class);

	private Player _player;

	/**
	 * @param title
	 * 		(not used)
	 */
	public TutorialPlayer(String title) {
		super();

		// JFileChooser chooser = new JFileChooser();
		// int option = chooser.showOpenDialog(null);

		// if ( option == JFileChooser.APPROVE_OPTION ){

		URL url;
		try {
			// url = chooser.getSelectedFile().toURL();
			url = new URL("file", null, "etc/tutorials/video1.avi");

			Manager.setHint(Manager.LIGHTWEIGHT_RENDERER, true);

			try {
				this.createPlayerAndShowComponents(url);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// }

		// this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		// this.setVisible(true);

		this.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				TutorialPlayer.this._player.stop();
				TutorialPlayer.this._player.deallocate();
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
			}

		});
	}

	private void createPlayerAndShowComponents(URL url) throws IOException,
			NoPlayerException, CannotRealizeException {

		// MediaLocator ml;
		// ml = new MediaLocator(
		// "file://home/alex/repos/simgui/etc/tutorials/video1.avi" );

		this._player = Manager.createRealizedPlayer(url);
		// _player = Manager.createRealizedPlayer( ml );

		Component comp;
		if ((comp = this._player.getVisualComponent()) != null) {
			logger.log(Level.DEBUG, "VISUAL OK");
			this.add(comp, BorderLayout.CENTER);
		}
		if ((comp = this._player.getControlPanelComponent()) != null) {
			this.add(comp, BorderLayout.SOUTH);
			logger.log(Level.DEBUG, "CONTROL OK");
		}

		this._player.start();
	}
}
