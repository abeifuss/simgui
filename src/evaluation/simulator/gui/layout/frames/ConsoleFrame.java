package evaluation.simulator.gui.layout.frames;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import evaluation.simulator.conf.service.UserConfigService;
import evaluation.simulator.gui.customElements.SimConsoleContentPanel;

public class ConsoleFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static ConsoleFrame instance = null;
	private int consoleFrameHeight;
	private int consoleFrameWidth;
	private int consoleFrameXPos;
	private int consoleFrameYPos;
	private SimConsoleContentPanel simConsoleContentPanel;
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

	private void init() {
		this.panel = new JPanel();
		this.simConsoleContentPanel = SimConsoleContentPanel.getInstance();
		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.weighty = 1;
		gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(this, gridBagConstraints);

		this.panel.setLayout(gridBagLayout);
		this.panel.add(this.simConsoleContentPanel, gridBagConstraints);
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

	public void append(String msg) {
		this.simConsoleContentPanel._log = this.simConsoleContentPanel._log
				+ "\n" + msg;
		this.update();
	}

	public void update() {
		this.simConsoleContentPanel.textArea
		.setText(this.simConsoleContentPanel._log);
		// this.textArea.setCaretPosition(0);
		this.simConsoleContentPanel.scroll.getVerticalScrollBar().setValue(
				this.simConsoleContentPanel.scroll.getVerticalScrollBar()
				.getMaximum());
		this.simConsoleContentPanel.scroll.repaint();
	}

	private ConsoleFrame() {

		this.getContentPane().setLayout(new BorderLayout());

		this.init();

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
