package gui.customElements;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import javax.swing.JPanel;
import org.xhtmlrenderer.simple.FSScrollPane;
import org.xhtmlrenderer.simple.XHTMLPanel;
import log.LogLevel;
import log.Logger;

@SuppressWarnings("serial")
public class SimHelpContentPanel extends JPanel {

	XHTMLPanel _htmlContent;
	
	private static SimHelpContentPanel _instance = null;
	
	private SimHelpContentPanel(){
		init();
	}
	
	public static SimHelpContentPanel getInstance() {
		if (_instance == null) {
			_instance = new SimHelpContentPanel();
		}
		return _instance;
	}
	
	private void init(){
		
		try {
			_htmlContent = new XHTMLPanel();
			setLayout(new BorderLayout());
			setBackground(Color.WHITE);
			_htmlContent.setDocument(new File("etc/html/index.html"));
			
			add(new FSScrollPane(_htmlContent));
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Some problem has occured" + ex.getMessage());
		}
		
	}
	
	public void loadURL(String url){
		Logger.Log(LogLevel.DEBUG, "Loading help-page: "+url );
		try {
			_htmlContent.setDocument(new File(url));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
