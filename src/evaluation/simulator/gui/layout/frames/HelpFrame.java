package evaluation.simulator.gui.layout.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import evaluation.simulator.conf.service.UserConfigService;
import evaluation.simulator.gui.customElements.SimHelpPanel;
import evaluation.simulator.gui.service.GuiService;
// import log.Level;
// import log.Logger;

@SuppressWarnings("serial")
public class HelpFrame extends JFrame {

	private static HelpFrame instance = null;
	private JPanel panel;

	public static HelpFrame getInstance() {
		if (instance == null) {
			instance = new HelpFrame();
		}
		return instance;
	}

	private int helpFrameHeight;
	private int helpFrameWidth;
	private int helpFrameXPos;
	private int helpFrameYPos;

	private HelpFrame() {

		this.initialize();

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

	public void initialize() {

		this.loadProperties();
		this.getContentPane().setLayout(new BorderLayout());

		this.setPanel(SimHelpPanel.getInstance());
		this.add(this.getPanel());

		this.setTitle("Help Tool");
		this.setIconImage(Toolkit.getDefaultToolkit().createImage(
				"etc/img/icons/icon128.png"));

		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setPreferredSize(new Dimension(this.helpFrameWidth, this.helpFrameHeight));
		this.pack();
		this.setVisible(false);

	}

	private void safeProperties() {
		UserConfigService.setHELPFRAME_HEIGHT(this.getHeight());
		UserConfigService.setHELPFRAME_WIDTH(this.getWidth());
		UserConfigService.setHELPFRAME_XPOS(this.getX());
		UserConfigService.setHELPFRAME_YPOS(this.getY());
	}

	private void loadProperties(){
		this.helpFrameXPos = UserConfigService.getHELPFRAME_XPOS();
		this.helpFrameYPos = UserConfigService.getHELPFRAME_XPOS();
		this.helpFrameWidth = UserConfigService.getHELPFRAME_WIDTH();
		this.helpFrameHeight = UserConfigService.getHELPFRAME_HEIGHT();

		System.out.println(this.helpFrameXPos);
		System.out.println(this.helpFrameYPos);
		System.out.println(this.helpFrameWidth);
		System.out.println(this.helpFrameHeight);

	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

}
