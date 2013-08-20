package gui.layout.Frames;

import gui.customElements.SimHelpContentPanel;
import gui.customElements.SimHelpMenuPanel;
import gui.service.GuiService;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

@SuppressWarnings("serial")
public class HelpFrame extends JFrame {

	private static HelpFrame instance = null;
	JPanel panel;
	
	private HelpFrame() {
		
		this.getContentPane().setLayout(new BorderLayout());
		
		this.init();
		
		this.setTitle("Help Tool");
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
				GuiService.getInstance().toogleHelpTools();
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {
			}
		});
	}
	
	public static HelpFrame getInstance() {
		if (instance == null) {
			instance = new HelpFrame();
		}
		return instance;
	}
	
	public void init(){
		panel = new JPanel();
		panel.setLayout( new BorderLayout() );
		JSplitPane splitPlane = new JSplitPane();
		// splitPlane.setLayout(new BorderLayout());
		
		SimHelpContentPanel content = SimHelpContentPanel.getInstance();
		SimHelpMenuPanel navigation = SimHelpMenuPanel.getInstance();
		
		splitPlane.setLeftComponent(navigation);
		splitPlane.setRightComponent(content);
		
		panel.add(splitPlane);
		add(panel);
		
		this.pack();
		this.setBounds(1200, 10, 500, 500);
	}

	public JPanel getPanel() {
		return panel;
	}
}
