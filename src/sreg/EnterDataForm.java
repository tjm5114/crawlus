package sreg;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;
import java.lang.reflect.*;
import java.io.File;
import java.util.regex.Pattern;
import sreg.InputDataBean;

/**
 * ActionForm associated with web page for entering data 
 *
 * @author Richard Newton
 */

public  class EnterDataForm extends ActionForm {
    
    public EnterDataForm() { }
    
    private String procFlag;
    public String getProcFlag() {return this.procFlag;}
    public void setProcFlag(String procFlag) { this.procFlag = procFlag;}
    
    public boolean isNumeric(String inputtext){
        boolean isnum = false;
        // check if begins with a number or + or - and has only numbers with perhaps a . and perhaps e or E or + or - and an integer
        isnum = Pattern.matches("\\A\\s*[\\+\\-]?((\\d+)|(\\d+\\.)|(\\d+\\.\\d+)|(\\.\\d+))([eE]{1}[\\+\\-]?(\\d+))?\\s*\\Z", inputtext);
        return isnum;
    }
    
    public boolean isNa(String inputtext){
        boolean isna = false;
        isna = Pattern.matches("\\A\\s*(NA{1})|(Na{1})|(na{1})|(nA{1})\\s*\\Z", inputtext);
        return isna;
    }
    
    public boolean isHex(String inputtext){
	    boolean ishex = false;
	    ishex = Pattern.matches("\\A\\s*[A-F\\d]{15,16}\\s*", inputtext);
	    return ishex;
	}
    
    public boolean isHexZip(String inputtext, String oarappname){
	    boolean ishex = false;
	    ishex = Pattern.matches("\\A\\s*"+ oarappname+"_[A-F\\d]{15,16}\\.zip\\s*", inputtext);
	    return ishex;
	}
    
    public String getTopdir(HttpServletRequest request){
        String os = System.getProperty("os.name").toLowerCase(); 
        String userdir; 
        if(os.indexOf("windows") > -1){  
            userdir = request.getSession().getServletContext().getRealPath("\\"); 
        }else{ 
            userdir = request.getSession().getServletContext().getRealPath("/"); 
        } 
        int indx = userdir.lastIndexOf(File.separator, userdir.length()-2);
        String topdir = userdir.substring(0, indx);
        return topdir;
    }
    
    
    public boolean isText(String inputtext){
        boolean istext = true;
        
        // Uncomment and add a regex if required.
        //boolean istext = false;
        //istext = Pattern.matches("ADD REGEX HERE", inputtext);
        
        return istext;
    }
    
	 private String startingbar; 
	 public String getStartingbar() {return (this.startingbar);} 
	 public void setStartingbar(String startingbar) {this.startingbar = startingbar;}
	 private String costpref; 
	 public String getCostpref() {return (this.costpref);} 
	 public void setCostpref(String costpref) {this.costpref = costpref;}
	 private String crowdpref; 
	 public String getCrowdpref() {return (this.crowdpref);} 
	 public void setCrowdpref(String crowdpref) {this.crowdpref = crowdpref;}
	 private String alcpref; 
	 public String getAlcpref() {return (this.alcpref);} 
	 public void setAlcpref(String alcpref) {this.alcpref = alcpref;}
	 private String nobot; 
	 public String getNobot() {return (this.nobot);} 
	 public void setNobot(String nobot) {this.nobot = nobot;}
	 String fname=""; 
	 int fname_length=0; 
	 String fname_disk=""; 
	 int fname_disk_length=0;
	 int item_server_length=0; 

	 public ActionErrors validate(ActionMapping mapping, HttpServletRequest request){ 
		 request.getSession().setAttribute(Constants.ANALYSIS_FLAG, "running"); 
		 request.getSession().setAttribute( Constants.CANCEL_FLAG, ""); 
		 request.getSession().setAttribute(Constants.CANCELLED_FLAG, ""); 
		 request.getSession().setAttribute( Constants.RSTARTED_FLAG, ""); 
		 request.getSession().setAttribute(Constants.RFINISHED_FLAG, ""); 
		 ActionErrors errors = new ActionErrors(); 
		 InputDataBean idb = new InputDataBean(); 
		 if(request.getSession().getAttribute(Constants.INPUT_DATA) != null){ 
			 idb = (InputDataBean) request.getSession().getAttribute(Constants.INPUT_DATA);} 





		 if(!errors.isEmpty()){ 
			 request.getSession().setAttribute(Constants.ANALYSIS_FLAG, ""); 
		} 
		 return errors; 
	} 
 
	 public void reset(ActionMapping mapping, HttpServletRequest request){ 
		 procFlag = "enter"; 
		 startingbar= "";
		 costpref= "";
		 crowdpref= "";
		 alcpref= "";
		 nobot= "";
	}
}
