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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DownloadAction;
import org.apache.struts.util.MessageResources;

import org.apache.struts.upload.FormFile;


/**
 * Action to download a file from Results.jsp.
 *
 * @author Richard Newton
 */

public class DownloadFileAction extends DownloadAction {
 
    protected StreamInfo getStreamInfo(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response)
    throws Exception {
        
        InputDataBean inputDataB = (InputDataBean) request.getSession().getAttribute(Constants.INPUT_DATA);
        ArrayList sblist = (ArrayList) inputDataB.getSubList();
        ResultsBean rb = new ResultsBean();
        String workdir = null;
        
        //  The submission id of this submission, in the list of all the submissions associated with this session
        String subid = request.getParameter("subid");
        
        // Name of the file to download
        String fileName = request.getParameter("file");
        
        //  ResultsBean of this submission
        for(int i=0;i < sblist.size();i++){
            rb = (ResultsBean) sblist.get(i);
            String submitid = rb.getSubmitID();
            if(submitid.equals(subid)){
                workdir = rb.getWorkdir();
                break;
            }
        }
        
        // Download the file
        String filePath = workdir + File.separator + "results" + File.separator + fileName;
        File downloadFile = new File(filePath);

        ServletContext application = servlet.getServletContext();
        String contentType = application.getMimeType(downloadFile.getName());
        if(contentType == null){
            contentType = "application/octet-stream";
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);
        }
        response.setHeader("Content-disposition", "filename=" + fileName);
        return new FileStreamInfo(contentType, downloadFile);   
    }
}

