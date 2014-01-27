package evaluation.simulator.gui.customElements.structure;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import evaluation.simulator.gui.customElements.PluginPanel;


public class HelpPropValues {
	
	private static Logger logger = Logger.getLogger(PluginPanel.class);
	
	private StringTokenizer tokenizer;
	
	private List<String> values;
	private Class type;
	
	public HelpPropValues(String value, Class type){
		
		values = new LinkedList<String>();
		this.type = type;
		
		tokenizer = new StringTokenizer( value, "," );
		while (tokenizer.hasMoreTokens()){
			values.add((String) tokenizer.nextElement());
		}
		
	}
	
	public boolean isValid(){
		
		boolean[] testresults = new boolean[values.size()];
		int i = 0;
		
		for(String s : values){
			
			logger.log(Level.DEBUG, s);
//			logger.log(Level.DEBUG, type.toString());
//			logger.log(Level.DEBUG, Integer.class.toString());
			
			if (this.type == Integer.class){
				logger.log(Level.DEBUG, "checking for Integer");
				testresults[i] = true;
				try{
					Integer.parseInt(s);
				}
				catch(Exception e){
					testresults[i] = false;
					logger.log(Level.DEBUG, s + " is not an Integer");
				}
			}
			
			if (this.type == Boolean.class){
				logger.log(Level.DEBUG, "checking for Boolean");
				testresults[i] = true;
				try{					
					Boolean.parseBoolean(s);
				}
				catch(Exception e){
					testresults[i] = false;
					logger.log(Level.DEBUG, s + " is not a Boolean");
				}
			}
			
			if (this.type == Float.class){
				logger.log(Level.DEBUG, "checking for Float");
				testresults[i] = true;
				try{
					Float.parseFloat(s);
				}
				catch(Exception e){
					testresults[i] = false;
					logger.log(Level.DEBUG, s + " is not a Float");
				}
			}
			
			if (this.type == Double.class){
				logger.log(Level.DEBUG, "checking for Double");
				testresults[i] = true;
				try{
					Double.parseDouble(s);
				}
				catch(Exception e){
					testresults[i] = false;
					logger.log(Level.DEBUG, s + " is not a Double");
				}
			}
			i++;
			
		}		
		
		for(boolean b : testresults){
			if (b != true){
				return false;
			}
		}		
		return true;
	}
	
	public Class getType(){
		return type;
	}
	
	public List<String> getValues(){
		return values;
	}

}
