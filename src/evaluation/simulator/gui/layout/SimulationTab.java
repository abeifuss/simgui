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

@SuppressWarnings("serial")
public class SimulationTab extends JPanel implements ActionListener {

	private final JTabbedPane south;
	private final JPanel north;
	private int resultCounter;
	private final JButton startButton = new JButton("Start");

	public SimulationTab() {

		this.resultCounter = 1;

		JSplitPane verticalSplitPlane = new JSplitPane();
		verticalSplitPlane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		verticalSplitPlane.setOneTouchExpandable(true);
		verticalSplitPlane.setDividerLocation(150);

		this.setLayout(new BorderLayout());
		this.add(verticalSplitPlane, BorderLayout.CENTER);

		this.north = new JPanel();
		this.south = new JTabbedPane();

		JLabel wellcomeLabel = new JLabel("Simulation Test");
		wellcomeLabel.setFont(new Font("arial", 1, 35));

		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = GridBagConstraints.NORTH;
		gridBagConstraints.weightx = 0;
		gridBagConstraints.weighty = 1;
		gridBagConstraints.gridx = GridBagConstraints.RELATIVE;
		gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(this.north, gridBagConstraints);
		this.north.setLayout(gridBagLayout);
		
		this.north.add(wellcomeLabel, gridBagConstraints);
		this.north.add(this.startButton);

		this.startButton.addActionListener(this);
		
		this.south.addTab("Results_" + this.resultCounter, new ChartPanel(
				LineJFreeChartCreator.createAChart()));
		this.resultCounter++;
		
		this.south.addTab("Results_" + this.resultCounter, new ChartPanel(
				LineJFreeChartCreator.createAChart()));
		this.resultCounter++;

		verticalSplitPlane.setTopComponent(this.north);
		verticalSplitPlane.setBottomComponent(this.south);

	}

	@Override
	public void actionPerformed(ActionEvent event) {

		if (event.getSource() == this.startButton) {
			String[] params = { "etc/conf/testconf.txt" };

			// TODO:
			// This is where the SimPropService must dump the configuration
			// and pass it to the Simulator.

			@SuppressWarnings("unused")
			gMixBinding callSimulation = new gMixBinding(params);
			this.south.addTab("Results_" + this.resultCounter,
					ResultPanelFactory.getResultPanel());
			this.resultCounter++;
		}
	}

}
