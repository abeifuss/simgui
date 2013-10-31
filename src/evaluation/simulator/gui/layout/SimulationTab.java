package evaluation.simulator.gui.layout;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartPanel;

import evaluation.simulator.core.binding.gMixBinding;
import evaluation.simulator.gui.results.LineJFreeChartCreator;
import evaluation.simulator.gui.results.ResultPanelFactory;

@SuppressWarnings("serial")
public class SimulationTab extends JPanel implements ActionListener {

	private final JTabbedPane south;
	private final JPanel north;
	private int resultCounter;
	private final JButton startButton = new JButton("Start Simulation");
	private final JButton addExperiment = new JButton(">>");
	private final JButton delteExperiment = new JButton("<<");

	final DefaultListModel<File> availableExperimentsModel;
	final DefaultListModel<File> runExperimentsModel;
	JList<File> availableExperiments;
	JList<File> runExperiments;

	public SimulationTab() {

		availableExperimentsModel = new DefaultListModel<File>();
		runExperimentsModel = new DefaultListModel<File>();

		this.resultCounter = 1;

		JSplitPane verticalSplitPlane = new JSplitPane();
		verticalSplitPlane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		verticalSplitPlane.setOneTouchExpandable(true);
		verticalSplitPlane.setDividerLocation(150);

		this.setLayout(new BorderLayout());
		this.add(verticalSplitPlane, BorderLayout.CENTER);

		this.north = new JPanel();
		this.south = new JTabbedPane();

		GridBagLayout gridBagLayout = new GridBagLayout();
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;

		this.north.setLayout(gridBagLayout);

		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.anchor = GridBagConstraints.EAST;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.ipadx = 0;
		gridBagConstraints.gridheight = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new Insets(3, 3, 3, 3);

		availableExperiments = new JList<File>(availableExperimentsModel);
		runExperiments = new JList<File>(runExperimentsModel);

		this.north.add(availableExperiments, gridBagConstraints);

		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.anchor = GridBagConstraints.CENTER;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.ipadx = 0;
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.weighty = 0.5;
		this.north.add(addExperiment, gridBagConstraints);

		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.ipadx = 0;
		gridBagConstraints.gridheight = 1;
		this.north.add(delteExperiment, gridBagConstraints);

		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.ipadx = 0;
		gridBagConstraints.gridheight = 2;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 0.0;
		this.north.add(runExperiments, gridBagConstraints);

		// this.north.add(wellcomeLabel, gridBagConstraints);
		gridBagConstraints.anchor = GridBagConstraints.SOUTH;
		gridBagConstraints.fill = GridBagConstraints.VERTICAL;
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridheight = 1;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.weightx = 0.0;
		gridBagConstraints.weighty = 0.0;
		this.north.add(this.startButton, gridBagConstraints);

		this.startButton.addActionListener(this);

		this.south.addTab("Results_" + this.resultCounter, new ChartPanel(
				LineJFreeChartCreator.createAChart()));
		this.resultCounter++;

		this.south.addTab("Results_" + this.resultCounter, new ChartPanel(
				LineJFreeChartCreator.createAChart()));
		this.resultCounter++;

		verticalSplitPlane.setTopComponent(this.north);
		verticalSplitPlane.setBottomComponent(this.south);

		addExperiment.addActionListener(this);
		delteExperiment.addActionListener(this);
		
		update();

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

		if (event.getSource() == this.addExperiment) {
			try {
				int index = availableExperiments.getSelectedIndex();
				if (index != -1) {
					runExperimentsModel.addElement(availableExperiments
							.getSelectedValue());
					availableExperimentsModel.remove(index);
					this.updateUI();
				}
			} catch (Exception e) {

			}
		}

		if (event.getSource() == this.delteExperiment) {
			try {
				int index = runExperiments.getSelectedIndex();
				if (index != -1) {
					availableExperimentsModel.addElement(runExperiments
							.getSelectedValue());
					runExperimentsModel.remove(index);
					this.updateUI();
				}
			} catch (Exception e) {

			}
		}
	}
	
	void update(){
		
		final File folder = new File("etc/experiments/");
		final File[] listOfFiles = folder.listFiles(new FileFilter() {

			@Override
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".cfg");
			}
		});
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				for (File f : listOfFiles) {
					boolean insertFlag = true;
					for (int i = 0; i < availableExperiments.getModel().getSize(); i++){
						if ( availableExperiments.getModel().getElementAt(i).equals(f) ){
							insertFlag = false;
							break;
						}	
					}
					
					for (int i = 0; i < runExperiments.getModel().getSize(); i++){
						if ( runExperiments.getModel().getElementAt(i).equals(f) ){
							insertFlag = false;
							break;
						}
					}
					
					if (insertFlag){
						availableExperimentsModel.addElement(f);
					}
				}
			}
		});
	}
}
