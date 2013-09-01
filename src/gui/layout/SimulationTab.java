package gui.layout;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import core.binding.gMixBinding;

//import core.binding.gMixBinding;

@SuppressWarnings("serial")
public class SimulationTab extends JPanel implements ActionListener{
	
	JButton startButton = new JButton("Start");
	
	public SimulationTab(){
		
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
		gbl.setConstraints(this, gbc);
		setLayout(gbl);
		
		
		
		this.add(wellcomeLabel, gbc);
		this.add(startButton);
		startButton.addActionListener(this);
		
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
	    	   }
	        }
	
}
