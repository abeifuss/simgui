package evaluation.simulator.gui.customElements;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class SimConsoleContentPanel extends JPanel {

	private static SimConsoleContentPanel _instance = null;

	public static SimConsoleContentPanel getInstance() {
		if (_instance == null) {
			_instance = new SimConsoleContentPanel();
		}
		return _instance;
	}

	public String _log;
	public JTextArea textArea;
	public JScrollPane scroll;

	private SimConsoleContentPanel() {
		this.init();
	}

	private void init() {

		try {
			this.textArea = new JTextArea();
			this.scroll = new JScrollPane(this.textArea);

			GridBagLayout gridBagLayout = new GridBagLayout();
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.anchor = GridBagConstraints.NORTH;
			gridBagConstraints.weightx = 1;
			gridBagConstraints.weighty = 1;
			gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
			gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
			gridBagLayout.setConstraints(this, gridBagConstraints);

			this.setLayout( gridBagLayout );
			this.textArea.setEditable(false);

			this.add(this.scroll, gridBagConstraints);

			this.textArea.setVisible(true);
			this.scroll.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
			this.scroll.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
			this._log = "";
			this.initConsole();

		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Some problem has occured" + ex.getMessage());
		}

	}

	public void initConsole() {
		this.textArea.append("=============");
		this.textArea.append("Wellcome to gMixSim");
	}
}
