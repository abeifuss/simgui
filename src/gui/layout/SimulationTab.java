package gui.layout;

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
	
	private JPanel _top;
	private JTabbedPane _bottom;
	private JButton _startButton = new JButton("Start");
	private int _resultCounter;
	
	
	
	public SimulationTab(){
		
		_resultCounter = 1;
		
		JSplitPane verticalSplitPlane = new JSplitPane();
		verticalSplitPlane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		verticalSplitPlane.setOneTouchExpandable(true);
		verticalSplitPlane.setDividerLocation(150);
		
		this.setLayout(new BorderLayout());		
		this.add(verticalSplitPlane, BorderLayout.CENTER);
		
		
		_top = new JPanel();			
		
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
		gbl.setConstraints(_top, gbc);
		_top.setLayout(gbl);
		
		//top.setLayout(new BorderLayout());
		_top.add(wellcomeLabel, gbc);
		_top.add(_startButton);		
		
		_startButton.addActionListener(this);
		
		
		_bottom = new JTabbedPane();		
		//bottom.setLayout(new BorderLayout());
		//bottom.add(ConsolePanel.getInstance(), BorderLayout.CENTER);
		/*
		bottom.addTab("Home", new HomeTab());
		bottom.addTab("Simulator", new SimulationTab());
		*/
		_bottom.addTab("Results_"+_resultCounter, new ChartPanel(LineJFreeChartCreator.createAChart()));
		_resultCounter++;
		_bottom.addTab("Results_"+_resultCounter, new ChartPanel(LineJFreeChartCreator.createAChart()));
		_resultCounter++;
		
		verticalSplitPlane.setTopComponent(_top);
		verticalSplitPlane.setBottomComponent(_bottom);
		
		
	}
	
	 public void actionPerformed(ActionEvent event) {
	        
	       if (event.getSource() == _startButton) {
	    	   String[] params = {"etc/conf/testconf.txt"};
	    	   
	    	   // TODO:
	    	   // This is where the SimPropService must dump the configuration
	    	   // and pass it to the Simulator.
	    	   
	    	   /*
	    	    * 
	    	    */
	            
	    	   @SuppressWarnings("unused")
				gMixBinding callSimulation = new gMixBinding(params);
	    	   	_bottom.addTab("Results_"+_resultCounter, new ChartPanel(LineJFreeChartCreator.createAChart()));
	   			_resultCounter++;	    	   
	    	   }
	        }
	
}
