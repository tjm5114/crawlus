package sreg;

import java.io.*;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
 * Action to check a submission id still exists.
 *
 * @author Richard Newton
 */

public class CheckIDAction extends Action {
    
    public ActionForward execute(ActionMapping       mapping,
            ActionForm          form,
            HttpServletRequest  request,
            HttpServletResponse response)
    throws Exception, IOException {
        
        InputDataBean inputDataB = (InputDataBean) request.getSession().getAttribute(Constants.INPUT_DATA);
        ArrayList sblist = (ArrayList) inputDataB.getSubList();
        
        ResultsBean rb = new ResultsBean();
        String workdir = "";
        
        //  The submission id of this submission, in the list of all the submissions associated with this session
        String subid = request.getParameter("subid");
        String whichAction = request.getParameter("action");
        // String subType = request.getParameter("subtype");
       
        //  ResultsBean of this submission
        for(int i=0;i < sblist.size();i++){
            rb = (ResultsBean) sblist.get(i);
            String submitid = rb.getSubmitID();
            if(submitid.equals(subid)){
                workdir = rb.getWorkdir();
                break;
            }
        }
        // if(workdir.equals("") && !subType.equals("current")){
        if(workdir.equals("")){
            return mapping.findForward("Gone");
        }else{
            return mapping.findForward(whichAction);
        }
    }
}

