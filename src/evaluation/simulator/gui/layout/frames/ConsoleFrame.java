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
import evaluation.simulator.gui.service.GuiService;

@SuppressWarnings("serial")
public class ConsoleFrame extends JFrame {

	private static ConsoleFrame instance = null;

	public static ConsoleFrame getInstance() {
		if (instance == null) {
			instance = new ConsoleFrame();
		}
		return instance;
	}

	private int consoleFrameHeight;
	private int consoleFrameWidth;
	private int consoleFrameXPos;
	private int consoleFrameYPos;

	private SimConsoleContentPanel simConsoleContentPanel;

	private JPanel panel;

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
				GuiService.getInstance().toggleConsole();
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

	public JPanel getPanel() {
		return this.panel;
	}

	public void init() {
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

		this.panel.setLayout( gridBagLayout );
		this.panel.add(this.simConsoleContentPanel,gridBagConstraints);
		this.add(this.panel);

		try {
			this.consoleFrameXPos = UserConfigService.getInstance().getInteger(
					"CONSOLEFRAME_XPOS");
		} catch (Exception e) {
			this.consoleFrameXPos = 600;
		}

		try {
			this.consoleFrameYPos = UserConfigService.getInstance().getInteger(
					"CONSOLEFRAME_YPOS");
		} catch (Exception e) {
			this.consoleFrameYPos = 100;
		}

		try {
			this.consoleFrameWidth = UserConfigService.getInstance().getInteger(
					"CONSOLEFRAME_WIDTH");
		} catch (Exception e) {
			this.consoleFrameWidth = 700;
		}

		try {
			this.consoleFrameHeight = UserConfigService.getInstance().getInteger(
					"CONSOLEFRAME_HEIGTH");
		} catch (Exception e) {
			this.consoleFrameHeight = 750;
		}

		this.setBounds(this.consoleFrameXPos, this.consoleFrameYPos,
				this.consoleFrameWidth, this.consoleFrameHeight);
	}

	private void safeProperties() {
		UserConfigService.getInstance().setInteger("CONSOLEFRAME_XPOS",
				this.getX());
		UserConfigService.getInstance().setInteger("CONSOLEFRAME_YPOS",
				this.getY());
		UserConfigService.getInstance().setInteger("CONSOLEFRAME_WIDTH",
				this.getWidth());
		UserConfigService.getInstance().setInteger("CONSOLEFRAME_HEIGTH",
				this.getHeight());
	}

	public void append(String msg) {
		this.simConsoleContentPanel._log = this.simConsoleContentPanel._log + "\n" + msg;
		this.update();
	}

	public void update() {
		this.simConsoleContentPanel.textArea.setText(this.simConsoleContentPanel._log);
		//	this.textArea.setCaretPosition(0);
		this.simConsoleContentPanel.scroll.getVerticalScrollBar().setValue( this.simConsoleContentPanel.scroll.getVerticalScrollBar().getMaximum() );
		this.simConsoleContentPanel.scroll.repaint();
	}


}
