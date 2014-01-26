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
		boolean valid = true;
		
		for(String s : values){
			
			logger.log(Level.DEBUG, s);
			logger.log(Level.DEBUG, type.toString());
			logger.log(Level.DEBUG, Integer.class.toString());
			
			if (this.type == Integer.class){
				logger.log(Level.DEBUG, "checking for int");
				try{
					logger.log(Level.DEBUG, "int");
					int i = Integer.parseInt(s);
				}
				catch(Exception e){
					valid = false;
					logger.log(Level.DEBUG, "was false");
				}
			}
			
			if (this.type == Boolean.class){
				logger.log(Level.DEBUG, "checking for int");
				try{
					logger.log(Level.DEBUG, "bool");
					boolean b = Boolean.parseBoolean(s);
				}
				catch(Exception e){
					valid = false;
					logger.log(Level.DEBUG, "was false");
				}
			}
			
			if (this.type == Float.class){
				logger.log(Level.DEBUG, "checking for int");
				try{
					logger.log(Level.DEBUG, "float");
					float f = Float.parseFloat(s);
				}
				catch(Exception e){
					valid = false;
					logger.log(Level.DEBUG, "was false");
				}
			}
			
			if (this.type == Double.class){
				logger.log(Level.DEBUG, "checking for int");
				try{
					logger.log(Level.DEBUG, "double");
					double d = Double.parseDouble(s);
				}
				catch(Exception e){
					valid = false;
					logger.log(Level.DEBUG, "was false");
				}
			}
			
		}		
		
		return valid;
	}
	
	public Class getType(){
		return type;
	}
	
	public List<String> getValues(){
		return values;
	}

}
