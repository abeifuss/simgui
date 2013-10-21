package evaluation.simulator.gui.layout;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartPanel;

import evaluation.simulator.core.binding.gMixBinding;
import evaluation.simulator.gui.results.LineJFreeChartCreator;
import evaluation.simulator.gui.results.ResultPanelFactory;

//import core.binding.gMixBinding;

@SuppressWarnings("serial")
public class SimulationTab extends JPanel implements ActionListener {

	private final JTabbedPane _bottom;
	private int _resultCounter;
	private final JButton _startButton = new JButton("Start");
	private final JPanel _top;

	public SimulationTab() {

		this._resultCounter = 1;

		JSplitPane verticalSplitPlane = new JSplitPane();
		verticalSplitPlane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		verticalSplitPlane.setOneTouchExpandable(true);
		verticalSplitPlane.setDividerLocation(150);

		this.setLayout(new BorderLayout());
		this.add(verticalSplitPlane, BorderLayout.CENTER);

		this._top = new JPanel();

		JLabel wellcomeLabel = new JLabel("Simulation Test");
		wellcomeLabel.setFont(new Font("arial", 1, 35));

		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.weightx = 0;
		gbc.weighty = 1;
		gbc.gridx = GridBagConstraints.RELATIVE;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbl.setConstraints(this._top, gbc);
		this._top.setLayout(gbl);

		// top.setLayout(new BorderLayout());
		this._top.add(wellcomeLabel, gbc);
		this._top.add(this._startButton);

		this._startButton.addActionListener(this);

		this._bottom = new JTabbedPane();
		// bottom.setLayout(new BorderLayout());
		// bottom.add(ConsolePanel.getInstance(), BorderLayout.CENTER);
		/*
		 * bottom.addTab("Home", new HomeTab()); bottom.addTab("Simulator", new
		 * SimulationTab());
		 */
		this._bottom.addTab("Results_" + this._resultCounter, new ChartPanel(
				LineJFreeChartCreator.createAChart()));
		this._resultCounter++;
		this._bottom.addTab("Results_" + this._resultCounter, new ChartPanel(
				LineJFreeChartCreator.createAChart()));
		this._resultCounter++;

		verticalSplitPlane.setTopComponent(this._top);
		verticalSplitPlane.setBottomComponent(this._bottom);

	}

	@Override
	public void actionPerformed(ActionEvent event) {

		if (event.getSource() == this._startButton) {
			String[] params = { "etc/conf/testconf.txt" };

			// TODO:
			// This is where the SimPropService must dump the configuration
			// and pass it to the Simulator.

			/*
	    	    * 
	    	    */

			@SuppressWarnings("unused")
			gMixBinding callSimulation = new gMixBinding(params);
			this._bottom.addTab("Results_" + this._resultCounter,
					ResultPanelFactory.getResultPanel());
			this._resultCounter++;
		}
	}

}
