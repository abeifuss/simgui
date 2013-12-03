package evaluation.simulator.gui.layout.frames;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import evaluation.simulator.conf.service.UserConfigService;
import evaluation.simulator.gui.customElements.ConsolePanel;

public class ConsoleFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static ConsoleFrame instance = null;
	private int consoleFrameHeight;
	private int consoleFrameWidth;
	private int consoleFrameXPos;
	private int consoleFrameYPos;
	private ConsolePanel simConsolePanel;
	private JPanel panel;

	public static ConsoleFrame getInstance() {
		if (instance == null) {
			instance = new ConsoleFrame();
		}
		return instance;
	}

	public JPanel getPanel() {
		return this.panel;
	}

	private void initialize() {
		this.panel = new JPanel();

		this.panel.setLayout(new BorderLayout());
		this.simConsolePanel = ConsolePanel.getInstance();
		this.panel.add(this.simConsolePanel,BorderLayout.CENTER);
		this.add(this.panel);

		this.consoleFrameXPos = UserConfigService.getCONSOLEFRAME_XPOS();
		this.consoleFrameYPos = UserConfigService.getCONSOLEFRAME_YPOS();
		this.consoleFrameWidth = UserConfigService.getCONSOLEFRAME_WIDTH();
		this.consoleFrameHeight =  UserConfigService.getCONSOLEFRAME_HEIGHT();

		this.setBounds(this.consoleFrameXPos, this.consoleFrameYPos,
				this.consoleFrameWidth, this.consoleFrameHeight);
	}

	private void safeProperties() {
		UserConfigService.setCONSOLEFRAME_HEIGHT(this.getHeight());
		UserConfigService.setCONSOLEFRAME_WIDTH(this.getWidth());
		UserConfigService.setCONSOLEFRAME_XPOS(this.getX());
		UserConfigService.setCONSOLEFRAME_YPOS(this.getY());
	}

	private ConsoleFrame() {

		this.initialize();

		this.setTitle("Console");
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
				ConsoleFrame.this.safeProperties();
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				ConsoleFrame.this.safeProperties();
				//				GuiService.getInstance().toggleConsole();
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				ConsoleFrame.this.safeProperties();
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

}
