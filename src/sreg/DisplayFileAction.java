package sreg;

import java.io.*;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DownloadAction;
import org.apache.struts.util.MessageResources;
import org.apache.struts.upload.FormFile;


/**
 * Action to display a file on Results.jsp.
 *
 * @author Richard Newton
 */

public class DisplayFileAction extends DownloadAction {
    
    protected StreamInfo getStreamInfo(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response)
    throws Exception {
        
        String subid = request.getParameter("subid");
        String fileName = request.getParameter("file");
        String cType = request.getParameter("ctype");
        
        InputDataBean inputDataB = (InputDataBean) request.getSession().getAttribute("inputdata");
        ArrayList sblist = (ArrayList) inputDataB.getSubList();
        ResultsBean rb = new ResultsBean();
        String resdir = "";
        String workdir = "";
        
        // ResultsBean of this submission
        for(int i=0;i < sblist.size();i++){
            rb = (ResultsBean) sblist.get(i);
            String submitid = rb.getSubmitID();
            if(submitid.equals(subid)){
                resdir = rb.getResultsdir();
                workdir = rb.getWorkdir();
                break;
            }
        }
        
        // Download the file
        String filePath = resdir + File.separator + fileName;
        String contentType = "text/plain";
        
        String fullFilePath = workdir + File.separator  +"results" + File.separator + fileName;
        File disFile = new File(fullFilePath);
        if(!disFile.exists()){
            if(cType.equals("txt")){filePath = "/file_not_found.txt";}
            if(cType.equals("png")){filePath = "/file_not_found.png";}
            if(cType.equals("jpg")){filePath = "/file_not_found.jpg";}
        }
        
        if(cType.equals("txt")){contentType = "text/plain";}
        if(cType.equals("png")){contentType = "image/png";}
        if(cType.equals("jpg")){contentType = "image/jpg";}
        
        ServletContext application = servlet.getServletContext();
        return new ResourceStreamInfo(contentType, application, filePath);
    }  
}
