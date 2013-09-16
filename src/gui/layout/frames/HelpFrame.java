package gui.layout.frames;

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

import log.LogLevel;
import log.Logger;

import conf.service.UserConfigService;

@SuppressWarnings("serial")
public class HelpFrame extends JFrame {

	private static HelpFrame _instance = null;
	private int _helpFrameXPos;
	private int _helpFrameYPos;
	private int _helpFrameWidth;
	private int _helpFrameHeight;
	
	private JPanel panel;
	
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
				safeProperties();
			}
			
			@Override
			public void windowClosing(WindowEvent arg0) {
				safeProperties();
				GuiService.getInstance().toogleHelpTools();
			}
			
			@Override
			public void windowClosed(WindowEvent arg0) {
				safeProperties();
			}
			
			@Override
			public void windowActivated(WindowEvent arg0) {
			}
		});
	}
	
	public static HelpFrame getInstance() {
		if (_instance == null) {
			_instance = new HelpFrame();
		}
		return _instance;
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
		
		try {
			_helpFrameXPos = UserConfigService.getInstance().getInteger("HELPFRAME_XPOS");
		} catch (Exception e) {
			_helpFrameXPos = 600;
		}
		
		try {
			_helpFrameYPos = UserConfigService.getInstance().getInteger("HELPFRAME_YPOS");
		} catch (Exception e) {
			_helpFrameYPos = 100;
		}
		
		try {
			_helpFrameWidth = UserConfigService.getInstance().getInteger("HELPFRAME_WIDTH");
		} catch (Exception e) {
			_helpFrameWidth = 700;
		}
		
		try {
			_helpFrameHeight = UserConfigService.getInstance().getInteger("HELPFRAME_HEIGTH");
		} catch (Exception e) {
			_helpFrameHeight = 750;
		}

		this.setBounds(_helpFrameXPos, _helpFrameYPos, _helpFrameWidth, _helpFrameHeight);
	}

	public JPanel getPanel() {
		return panel;
	}
	
	private void safeProperties(){
		UserConfigService.getInstance().setInteger("HELPFRAME_XPOS", this.getX());
		UserConfigService.getInstance().setInteger("HELPFRAME_YPOS", this.getY());
		UserConfigService.getInstance().setInteger("HELPFRAME_WIDTH", this.getWidth());
		UserConfigService.getInstance().setInteger("HELPFRAME_HEIGTH", this.getHeight());
	}
}
