package evaluation.simulator.gui.customElements;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class VideoPlayer extends JFrame {

	private Player player;
	
	public VideoPlayer(String title, String path) throws IOException, ClassNotFoundException, NoPlayerException
	{
		super(title);
		
		this.addWindowListener( new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				player.stop();
				setVisible(false);
				
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				player.close();
				player.deallocate();
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		Manager.setHint(Manager.LIGHTWEIGHT_RENDERER, true);
		
		try {
			createPlayer(path);
		} catch (CannotRealizeException ex) {
			ex.printStackTrace();
		} catch (NoPlayerException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void createPlayer(String path) throws IOException, NoPlayerException, CannotRealizeException
	{
		try {
			checkJmf();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		File f = new File("etc/html/videos/" + path);
		player = Manager.createRealizedPlayer(f.toURL());
		player.prefetch();
		
		Component comp;
		if ( (comp = player.getVisualComponent()) != null ){
			add(comp, BorderLayout.CENTER);
		}
		if ( (comp = player.getControlPanelComponent()) != null ){
			add(comp, BorderLayout.SOUTH);
		}
		
	}
	
	private static void checkJmf() throws ClassNotFoundException {
		try {
			Class c = Class.forName("javax.media.Manager");
		} catch (ClassNotFoundException e) {
			ClassNotFoundException cnfe = new ClassNotFoundException(
					"JMF nicht installiert!", e);
			throw cnfe;
		}
	}
}
