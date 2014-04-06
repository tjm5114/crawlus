package sreg;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 *
 *
 * @author Richard Newton
 */

public class SessionListener implements HttpSessionListener {
    
    public synchronized void sessionCreated(HttpSessionEvent se){
    }
    public synchronized void sessionDestroyed(HttpSessionEvent sev){
        HttpSession ses = sev.getSession();
        InputDataBean inputDataB = new InputDataBean(); 
		if(ses.getAttribute("inputdata") != null){ 
			inputDataB = (InputDataBean) ses.getAttribute("inputdata"); 
			inputDataB.clearDirs();
		}
    }
}
