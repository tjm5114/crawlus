package sreg;


import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;



/**
 *
 *
 * @author Richard Newton
 */

public class CancelAction extends Action {
    
    public ActionForward execute(ActionMapping  mapping,
            ActionForm          form,
            HttpServletRequest  request,
            HttpServletResponse response)
    throws Exception, IOException {
        ActionMessages warnings = new ActionMessages();
        if(request.getSession().getAttribute(Constants.ANALYSIS_FLAG) == null){
            //warnings.add("warning", new ActionMessage("sreg.cancel.error.message"));
            //saveErrors(request, warnings);
            request.getSession().setAttribute(Constants.ANALYSIS_FLAG,"");
            request.getSession().setAttribute( Constants.RSTARTED_FLAG, "");
            request.getSession().setAttribute( Constants.RFINISHED_FLAG, "");
            request.getSession().setAttribute( Constants.CANCELLED_FLAG, "");
            return mapping.getInputForward();
        }else{
            String anflag = (String) request.getSession().getAttribute(Constants.ANALYSIS_FLAG);
            if(!anflag.equals("running")){
                //warnings.add("warning", new ActionMessage("sreg.cancel.error.message"));
                //saveErrors(request, warnings);
                request.getSession().setAttribute(Constants.ANALYSIS_FLAG,"");
                return mapping.getInputForward();
            }else{
                InputDataBean inputDataB = new InputDataBean();
                if(request.getSession().getAttribute(Constants.INPUT_DATA) != null){
                    inputDataB = (InputDataBean) request.getSession().getAttribute(Constants.INPUT_DATA);
                }
                request.getSession().setAttribute(Constants.CANCEL_FLAG, "cancel");	// Set this session attribute to cancel so that InputAction will know not to proceed any further when it returns from Model
                String rsflag = "";
                String rfflag = "";
                String cdflag = "";
                Thread thr = new Thread();
                while(!rsflag.equals("started") && !rfflag.equals("finished") && !cdflag.equals("cancelled")){
                    rsflag = (String) request.getSession().getAttribute(Constants.RSTARTED_FLAG);
                    rfflag = (String) request.getSession().getAttribute(Constants.RFINISHED_FLAG);
                    cdflag = (String) request.getSession().getAttribute(Constants.CANCELLED_FLAG);
                    thr.sleep(100);
                }
                if(rsflag.equals("started")){
                    inputDataB.killR();
                }
                inputDataB.clearDir();
                String submitid = (String) inputDataB.getSubmitID();
                inputDataB.setSubmitID("");
                warnings.add("warning", new ActionMessage("sreg.cancelled.message", submitid));
                saveErrors(request, warnings);
                request.getSession().setAttribute( Constants.INPUT_DATA, inputDataB);
                request.getSession().setAttribute(Constants.CANCEL_FLAG, "");
                request.getSession().setAttribute( Constants.CANCELLED_FLAG, "");
                request.getSession().setAttribute( Constants.RSTARTED_FLAG, "");
                request.getSession().setAttribute( Constants.RFINISHED_FLAG, "");
                request.getSession().setAttribute(Constants.ANALYSIS_FLAG,"");
                return mapping.getInputForward();
            }
        }
    }
}
