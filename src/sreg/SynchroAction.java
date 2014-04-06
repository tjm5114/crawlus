package sreg;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * Abstract <strong>Action</strong> that enhances on the standard <code>
 * org.apache.struts.action.Action</code> by providing a means of executing an
 * action in a synchronized mode. Subclasses must implement the method
 * <code>executeSynchro</code>.
 *
 * <p>Synchronization is based on the synchronizer token pattern implemented
 * in Struts and is appropriate to actions that are transactional. It protects an
 * action against inadequate submits, either multiple submits from the same page
 * or submits from a page that was previously bookmarked and that does not respect
 * the normal flow of control.
 *
 * <p>The proposed implementation also provides a means to recover the results of
 * a synchronized action in case of multiple submits. In fact, the token that a page
 * contains gives it a right to execute only once a synchronized action; all subsequent
 * submits from the same page will lead to the results of the first action. The results
 * may be expressed in the form provided to the action, as validation errors or
 * as an exception.
 *
 * <p>If a request comes in with an <strong>invalid</strong> token a synchronization
 * error is detected, a validation error is saved and forward is given to
 * <code>FORWARD_ERROR</code>.
 *
 * <p>Prior to executing a synchronized action, a token must be saved in the
 * session and carried to the page that will submit to the action. A token is saved
 * by calling <code>saveToken(HttpServletRequest)</code> before leading to the page.
 *
 * @author Romain Guay
 * @version $Revision: 1.2 $ $Date: 2003/09/11 $
 * @since Struts 1.1-b1
 */

public abstract class SynchroAction extends Action  {

	// --------------------------------------------------------- Constantes
    

	public static String PACKAGE = "sreg";

	/**
     * The session attributes key under which the form that results from the current
     * synchronized action is stored.
	 */
	private static final String FORM_KEY = PACKAGE + ".SynchroAction.FORM";

	/**
     * The session attributes key under which the exception that results from the current
     * synchronized action is stored.
	 */
	private static final String EXCEPTION_KEY = PACKAGE + ".SynchroAction.EXCEPTION";

	/**
     * The session attributes key under which the forward that results from the current
     * synchronized action is stored.
	 */
	private static final String FORWARD_KEY = PACKAGE + ".SynchroAction.FORWARD";

	/**
     * The session attributes key under which the errors that results from the current
     * synchronized action is stored.
	 */
	private static final String ERRORS_KEY = PACKAGE + ".SynchroAction.ERRORS";

	/**
     * The request attributes key under which the synchronizer token is stored.
	 */
	protected static final String REQUEST_TOKEN_KEY =
		org.apache.struts.taglib.html.Constants.TOKEN_KEY;

	/**
     * The session attributes key under which the token that identifies the current
     * synchronized action is stored.
	 */
	private static final String CURRENT_TOKEN_KEY = PACKAGE + ".SynchroAction.CURRENT_TOKEN";


	// --------------------------------------------------------- Méthodes

    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
     * already been completed.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception Exception if an error occurs
     */
	public final ActionForward execute(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	throws Exception {

		HttpSession session = request.getSession();

		// Synchronized on the session id, instead of the session itself
		// because the session is a facade that is not guaranteed to be
		// the same object on each request. Its id is the same String
		// instance however.
		synchronized(session.getId()) {

			// Get the request token.
			String requestToken = request.getParameter(REQUEST_TOKEN_KEY);

			// Get the session token.  ?? Not used and not known in Struts 1.2.9
			// String sessionToken = (String) session.getAttribute(TRANSACTION_TOKEN_KEY);

			// Get the current token.
			String currentToken = (String) session.getAttribute(CURRENT_TOKEN_KEY);


			// If the request token is the current token then recover the
			// results from the previous synchronized action.
			if (currentToken != null && currentToken.equals(requestToken)) {
				return recover(mapping, form, request, response);
			}

			// If token is invalid, process the synchronization error.
			if (!isTokenValid(request, true)) {
				return executeSynchroError(mapping, form, request, response, currentToken);
			}

			// Reset session attributes.
			session.removeAttribute(FORM_KEY);
			session.removeAttribute(EXCEPTION_KEY);
			session.removeAttribute(FORWARD_KEY);
			session.removeAttribute(ERRORS_KEY);

			// Store the current token in the session.
			session.setAttribute(CURRENT_TOKEN_KEY, requestToken);

			ActionForward forward = null;
			try {

				// Execute synchronized action.
				forward = executeSynchro(mapping, form, request, response);

				// Keep the form resulting from the action.
				session.setAttribute(FORM_KEY, form);

				// Keep the forward resulting from the action.
				session.setAttribute(FORWARD_KEY, forward);

				// Keep the errors resulting from the action.
				session.setAttribute(ERRORS_KEY, request.getAttribute(Globals.ERROR_KEY));

			} catch (Exception e) {
				// Keep the exceptions resulting from the action and rethrow them.
				session.setAttribute(EXCEPTION_KEY, e);
				throw e;
			}

			return forward;
		}
	}

	/**
	 * Execute the action in synchronized mode.
	 *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
	 *
     * @exception Exception if an error occurs
	 */
	protected abstract ActionForward executeSynchro(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	throws Exception;


	/**
	 * Recover the results of the synchronized action.
	 *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
	 *
     * @exception Exception if an error occurs
	 */
	protected ActionForward recover(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
	throws Exception {


		HttpSession session = request.getSession();

		// Recover the exception if any and rethrow it.
		Exception e = (Exception) session.getAttribute(EXCEPTION_KEY);
		if (e != null) {
			throw e;
		}

		// Recover the form.
		form = (ActionForm) session.getAttribute(FORM_KEY);

		// Put back the form in the appropriate context.
		if ("request".equals(mapping.getScope())) {
			request.setAttribute(mapping.getAttribute(), form);
		} else {
			// Put back a copy to protect the form from being repopulated
			// by upcoming requests.
			// Note: This might be avoided in future releases of Struts if a flag
			// could be set to prevent auto-population under certain circumstances.
			if (form != null) {
				session.setAttribute(mapping.getAttribute(), BeanUtils.cloneBean(form));
			}
		}

		// Recover and save the errors.
		saveErrors(request, (ActionMessages) session.getAttribute(ERRORS_KEY));

		// Recover and return the forward.
		return (ActionForward) session.getAttribute(FORWARD_KEY);
	}

	/**
	 * Execute the action in case of a synchronization error.
	 *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
	 *
     * @exception Exception if an error occurs
	 */
	protected ActionForward executeSynchroError(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response,
		String currTok)
	throws Exception {

		// Save an error.
        // ActionMessages errors = new ActionMessages();
		// errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("sreg.synchronization.error"));
		// saveErrors(request, errors);

		// Forward to FORWARD_ERROR.
	    
	    if(currTok == null){
	        return mapping.findForward("TimedOut");
	    }else{
	        return mapping.findForward("Error");
	    }
	}

}
