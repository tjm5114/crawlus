package sreg;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestHandler;
import org.apache.struts.util.ImageButtonBean;
import java.lang.reflect.*;
import java.io.File;
import java.util.regex.Pattern;
import sreg.InputDataBean;

/**
 * ActionForm associated with web page for entering data 
 *
 * @author Richard Newton
 */

public  class ImageMapForm extends ActionForm {
    
    public ImageMapForm() { }
        
    private String which;
    public String getWhich() {return which;}
    public void setWhich(String which) { this.which = which;}
    
    private String subid;
    public String getSubid() {return subid;}
    public void setSubid(String subid) { this.subid = subid;}
    
    public void reset(ActionMapping mapping, HttpServletRequest request){
	}




		 public String[] getSelected() { 
		 String[] ret = new String[3]; 

		 return null; 
		} 
	} 

