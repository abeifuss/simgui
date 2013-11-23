package evaluation.simulator.gui.layout.frames;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import evaluation.simulator.conf.service.UserConfigService;
import evaluation.simulator.gui.customElements.SimHelpContentPanel;
import evaluation.simulator.gui.customElements.SimHelpMenuPanel;
import evaluation.simulator.gui.service.GuiService;
import framework.core.userDatabase.User;
// import log.Level;
// import log.Logger;

@SuppressWarnings("serial")
public class HelpFrame extends JFrame {

	private static HelpFrame _instance = null;

	public static HelpFrame getInstance() {
		if (_instance == null) {
			_instance = new HelpFrame();
		}
		return _instance;
	}

	private int helpFrameHeight;
	private int helpFrameWidth;
	private int helpFrameXPos;

	private int helpFrameYPos;

	private JPanel panel;

	private HelpFrame() {

		this.getContentPane().setLayout(new BorderLayout());

		this.init();

		this.setTitle("Help Tool");
		this.setIconImage(Toolkit.getDefaultToolkit().createImage(
				"etc/img/icons/icon128.png"));

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setVisible(false);

		this.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				HelpFrame.this.safeProperties();
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				HelpFrame.this.safeProperties();
				GuiService.getInstance().toogleHelpTools();
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				HelpFrame.this.safeProperties();
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

	public JPanel getPanel() {
		return this.panel;
	}

	public void init() {
		this.panel = new JPanel();
		this.panel.setLayout(new BorderLayout());
		JSplitPane splitPlane = new JSplitPane();
		// splitPlane.setLayout(new BorderLayout());

		SimHelpContentPanel content = SimHelpContentPanel.getInstance();
		SimHelpMenuPanel navigation = SimHelpMenuPanel.getInstance();

		splitPlane.setLeftComponent(navigation);
		splitPlane.setRightComponent(content);

		this.panel.add(splitPlane);
		this.add(this.panel);

		this.pack();

		this.helpFrameXPos = UserConfigService.getHELPFRAME_XPOS();
		this.helpFrameYPos = UserConfigService.getHELPFRAME_XPOS();
		this.helpFrameWidth = UserConfigService.getHELPFRAME_WIDTH();
		this.helpFrameHeight = UserConfigService.getHELPFRAME_HEIGHT();

		this.setBounds(this.helpFrameXPos, this.helpFrameYPos,
				this.helpFrameWidth, this.helpFrameHeight);
	}

	private void safeProperties() {
		UserConfigService.setHELPFRAME_HEIGHT(this.getHeight());
		UserConfigService.setHELPFRAME_WIDTH(this.getWidth());
		UserConfigService.setHELPFRAME_XPOS(this.getX());
		UserConfigService.setHELPFRAME_YPOS(this.getY());
	}
}
