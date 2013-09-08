package gui.layout.Frames;

import gui.customElements.SimConfigPanel;
import gui.service.GuiService;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class ToolFrame extends JFrame{

	private static ToolFrame _instance = null;
	
	private ToolFrame() {
		
		this.getContentPane().setLayout(new BorderLayout());
		this.init();
		this.setTitle("Configuration Tool");
		this.setIconImage(Toolkit.getDefaultToolkit().createImage(
				"etc/img/icons/icon128.png"));
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setVisible(true);
		
		this.addWindowListener( new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent arg0) {
			}
			
			@Override
			public void windowIconified(WindowEvent arg0) {
			}
			
			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}
			
			@Override
			public void windowDeactivated(WindowEvent arg0) {
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				GuiService.getInstance().toogleConfTools();
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {
			}
		});
	}

	public static ToolFrame getInstance() {
		if (_instance == null) {
			_instance = new ToolFrame();
		}
		return _instance;
	}
	
	public void init(){
		SimConfigPanel panel = SimConfigPanel.getInstance();
		this.add(panel);
		this.pack();
		this.setBounds(10, 10, 325, 600);
	}
}
