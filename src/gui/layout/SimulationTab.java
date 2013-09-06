package gui.layout;

import gui.console.ConsolePanel;
import gui.results.LineJFreeChartCreator;

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

import core.binding.gMixBinding;

//import core.binding.gMixBinding;

@SuppressWarnings("serial")
public class SimulationTab extends JPanel implements ActionListener{
	
	JPanel top;
	JTabbedPane bottom;
	JButton startButton = new JButton("Start");
	int resultcounter;
	
	
	
	public SimulationTab(){
		
		resultcounter = 1;
		
		JSplitPane verticalSplitPlane = new JSplitPane();
		verticalSplitPlane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		verticalSplitPlane.setOneTouchExpandable(true);
		verticalSplitPlane.setDividerLocation(150);
		
		this.setLayout(new BorderLayout());		
		this.add(verticalSplitPlane, BorderLayout.CENTER);
		
		
		top = new JPanel();			
		
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
		gbl.setConstraints(top, gbc);
		top.setLayout(gbl);
		
		//top.setLayout(new BorderLayout());
		top.add(wellcomeLabel, gbc);
		top.add(startButton);		
		
		startButton.addActionListener(this);
		
		
		bottom = new JTabbedPane();		
		//bottom.setLayout(new BorderLayout());
		//bottom.add(ConsolePanel.getInstance(), BorderLayout.CENTER);
		/*
		bottom.addTab("Home", new HomeTab());
		bottom.addTab("Simulator", new SimulationTab());
		*/
		bottom.addTab("Results_"+resultcounter, new ChartPanel(LineJFreeChartCreator.createAChart()));
		resultcounter++;
		bottom.addTab("Results_"+resultcounter, new ChartPanel(LineJFreeChartCreator.createAChart()));
		resultcounter++;
		
		verticalSplitPlane.setTopComponent(top);
		verticalSplitPlane.setBottomComponent(bottom);
		
		
	}
	
	 public void actionPerformed(ActionEvent event) {
	        
	       if (event.getSource() == startButton) {
	    	   String[] params = {"etc/conf/testconf.txt"};
	    	   
	    	   // TODO:
	    	   // This is where the SimPropService must dump the configuration
	    	   // and pass it to the Simulator.
	    	   
	    	   /*
	    	    * 
	    	    */
	            
	    	   @SuppressWarnings("unused")
				gMixBinding callSimulation = new gMixBinding(params);
	    	   	bottom.addTab("Results_"+resultcounter, new ChartPanel(LineJFreeChartCreator.createAChart()));
	   			resultcounter++;	    	   
	    	   }
	        }
	
}
