package sreg;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;

import javax.servlet.ServletException;  
import javax.servlet.ServletContext;	
import javax.servlet.ServletConfig;		
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ForwardingActionForward;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import java.beans.PropertyDescriptor;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.BeanUtils;

import org.apache.struts.upload.FormFile;

import org.apache.struts.util.MessageResources;


/**
 * Action associated with web page for entering data
 *
 * @author Richard Newton
 */

public class ImageMapAction extends Action {
    
    public ActionForward execute(ActionMapping       mapping,
            ActionForm          form,
            HttpServletRequest  request,
            HttpServletResponse response)
    throws Exception, IOException {
        InputDataBean inputDataB = new InputDataBean();
        if(request.getSession().getAttribute(Constants.INPUT_DATA) != null){
            inputDataB = (InputDataBean) request.getSession().getAttribute(Constants.INPUT_DATA);
        }
        // Getting data file from Form
        ImageMapForm inputF = (ImageMapForm) form;
        String mapinfname = "blank.txt";  
        String fwd = (String) inputF.getWhich();
        String[] ret = new String[3];
        ret = ((ImageMapForm) inputF).getSelected();
        String whichmap = ret[0];
        String xcoord = ret[1];
        String ycoord = ret[2];
        int xint = Integer.parseInt(xcoord)-1;
        int yint = Integer.parseInt(ycoord)-1;
        String subid = (String) inputF.getSubid();
        
        /*
        imagemap = () request.getSession().getAttribute(Constants.MAP_FILE);
        lookuptable = () request.getSession().getAttribute(Constants.LOOKUP_TABLE);
        Use xint, yint, imagemap and lookuptable to get mapifname
        */
        
        ArrayList sblist = (ArrayList) inputDataB.getSubList();
        ResultsBean rb = new ResultsBean();
        HashMap<String, String> mapinfnames = new HashMap<String, String>();
        
        // ResultsBean of this submission
        for(int i=0;i < sblist.size();i++){
            rb = (ResultsBean) sblist.get(i);
            String submitid = rb.getSubmitID();
            if(submitid.equals(subid)){
            	HashMap<String, int[][]> mpArrayHash = (HashMap<String, int[][]>) rb.getMapArrayHash();
                int[][] mpArray = (int[][]) mpArrayHash.get(whichmap);
                HashMap<String, HashMap> lookupTabHash = (HashMap<String, HashMap>) rb.getLookupTableHash();
                HashMap<String, String> lookupTab = (HashMap<String, String>) lookupTabHash.get(whichmap);
                int mapcode = mpArray[yint][xint];
                if(lookupTab.containsKey(String.valueOf(mapcode))){
                    mapinfname = (String) lookupTab.get(String.valueOf(mapcode));
                }
                mapinfname = "mapinfo" + File.separator + mapinfname;
                
                if(fwd.equals("results")){
                    mapinfnames = (HashMap<String, String>) rb.getMapinfonamesR();
                    mapinfnames.put(whichmap, mapinfname);
                    rb.setMapinfonamesR(mapinfnames);
                }else{
                    mapinfnames = (HashMap<String, String>) rb.getMapinfonamesE();
                    mapinfnames.put(whichmap, mapinfname);
                	rb.setMapinfonamesE(mapinfnames);
                }
                sblist.set(i,rb);
                inputDataB.setSubList(sblist);
                break;
            }
        }
        
        request.getSession().setAttribute("inputdata", inputDataB);
        if(fwd.equals("results")){
            return mapping.findForward("Results"); 
        }
        return mapping.findForward("EnterData");   
        
    }
}