package sreg;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 *
 *
 * @author Richard Newton
 */

public class OneAction extends Action {
    
    public ActionForward execute(ActionMapping       mapping,
            ActionForm          form,
            HttpServletRequest  request,
            HttpServletResponse response)
    throws Exception, IOException {
        
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        session = request.getSession(true);
        
        saveToken(request);
        request.removeAttribute(mapping.getAttribute());
        return mapping.findForward("Continue"); 
    }
}

