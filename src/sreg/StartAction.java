package sreg;


import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 *
 *
 * @author Richard Newton
 */

public class StartAction extends Action {
    
    public ActionForward execute(ActionMapping  mapping,
            ActionForm          form,
            HttpServletRequest  request,
            HttpServletResponse response)
    throws Exception, IOException {
        
        if(request.isRequestedSessionIdValid()){
            request.removeAttribute(mapping.getAttribute());
            return mapping.findForward("OneOnly"); 
        }else{
            saveToken(request);            
            request.removeAttribute(mapping.getAttribute());
            return mapping.findForward("Start"); 
        }
    }
}
