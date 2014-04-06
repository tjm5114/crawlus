package sreg;

import java.io.*;

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
 * Action to display the process info.
 *
 * @author Richard Newton
 */

public class DisplayPinfoAction extends DownloadAction {
    
    protected StreamInfo getStreamInfo(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response)
    throws Exception {

        InputDataBean inputDataB = (InputDataBean) request.getSession().getAttribute("inputdata");
        String resdir = (String) inputDataB.getResultsdir();
        String workdir = (String) inputDataB.getWorkdir();
        // Download the file
        
        String filePath = resdir + File.separator + "process_info.txt"; 
        String contentType = "text/plain";
        
        String fullFilePath = workdir + File.separator  +"results" + File.separator + "process_info.txt";
        File disFile = new File(fullFilePath);
        if(!disFile.exists()){
            filePath = "/blank.txt";
        }
        ServletContext application = servlet.getServletContext();
        return new ResourceStreamInfo(contentType, application, filePath);
    }  
}
