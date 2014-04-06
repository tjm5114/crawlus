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

public class EnterDataAction extends SynchroAction {
    
    public ActionForward executeSynchro(ActionMapping       mapping,
            ActionForm          form,
            HttpServletRequest  request,
            HttpServletResponse response)
    throws Exception, IOException {
        saveToken(request);
        ActionMessages errors = new ActionMessages();
        ActionMessages warnings = new ActionMessages();
        request.getSession().setAttribute( Constants.RSTARTED_FLAG, "");
        request.getSession().setAttribute( Constants.RFINISHED_FLAG, "");
        request.getSession().setAttribute( Constants.CANCELLED_FLAG, "");
        
        // Bean to hold info
        InputDataBean inputDataB = new InputDataBean();
        if(request.getSession().getAttribute(Constants.INPUT_DATA) != null){
            inputDataB = (InputDataBean) request.getSession().getAttribute(Constants.INPUT_DATA);
        }
        
        // Set process_info.txt not ready to display yet
        inputDataB.setPirdy("");
        request.getSession().setAttribute( Constants.INPUT_DATA, inputDataB);
        
        // Getting data file from Form
        EnterDataForm inputF = (EnterDataForm) form;
        String pflag = inputF.getProcFlag();
                
        PropertyDescriptor[] inputs = PropertyUtils.getPropertyDescriptors(form);
        Class inputtype;
        String iname;
        String val ="";
        FormFile ff;
        
        if(pflag.equals("clear")){
            for(int i=0;i< Array.getLength(inputs);i++){
                inputtype = inputs[i].getPropertyType();
                iname = inputs[i].getName();
                if(inputtype.getName().equals("java.lang.String") && !iname.equals("procFlag")){
                    PropertyUtils.setProperty(form, iname, ""); 
                }
                if(inputtype.getName().equals("boolean")){
                    Boolean tempbool = new Boolean(false);
                    BeanUtils.setProperty(form, iname, tempbool); 
                }
                if(inputtype.getName().equals("org.apache.struts.upload.FormFile")){
                    PropertyUtils.setProperty(form, iname, null);   
                }  
            }    
            PropertyUtils.copyProperties(inputDataB, inputF);
            inputDataB.clearDirs();
            inputDataB.clearSubList();
            inputDataB.setSubmitID("");
            inputDataB.setWarningMessage("");
            request.getSession().setAttribute(Constants.INPUT_DATA, inputDataB);
            form.reset(mapping, request);
            request.getSession().setAttribute(Constants.ANALYSIS_FLAG, "");
            return mapping.findForward("EnterData");
        }else{
            PropertyUtils.copyProperties(inputDataB, inputF);           
            inputDataB.clearParamList();
            inputDataB.clearResultsList();
            inputDataB.clearDisplayFileList();
            inputDataB.clearUploadList();
            inputDataB.clearUploadNameList(); 
            inputDataB.clearZipList();
            inputDataB.setWarningMessage("");
            
            for(int i=0;i< Array.getLength(inputs);i++){
                inputtype = inputs[i].getPropertyType();
                iname = inputs[i].getName();
                if(inputtype.getName().equals("java.lang.String") && !iname.equals("procFlag")){
                    val = (String) PropertyUtils.getProperty(form, iname);
                    inputDataB.addToParamList(iname, val);
                }
                if(inputtype.getName().equals("boolean")){
                    val = ((Boolean) PropertyUtils.getProperty(form, iname)).toString();
                    inputDataB.addToParamList(iname, val);
                }
                if(inputtype.getName().equals("org.apache.struts.upload.FormFile")){
                    ff = (FormFile) PropertyUtils.getProperty(form, iname);
                    val = ff.getFileName();
                    inputDataB.addToParamList(iname, val);
                    inputDataB.addToUploadNameList(val);   
                    inputDataB.addToUploadList(PropertyUtils.getProperty(inputF, iname));
                }  
            }
            
            inputDataB.setParamListSize();
            
            // Get directory paths held in file 'ApplicationSettings.properties'
            MessageResources messages = getResources(request, "settings");
            String name = messages.getMessage("name");	
            String os = System.getProperty("os.name").toLowerCase();
            String userdir;
            if(os.indexOf("windows") > -1){  
                userdir = request.getSession().getServletContext().getRealPath("\\");
                
            }else{
                userdir = request.getSession().getServletContext().getRealPath("/");
            }
            if(!userdir.endsWith(File.separator)){										
                userdir += File.separator;
            }
            int indx = userdir.lastIndexOf(File.separator, userdir.length()-2);
            String topdir = userdir.substring(0, indx);
            if(os.indexOf("windows") > -1){ 
                topdir = topdir.replaceAll("\\\\", "\\\\\\\\");
            }
            String controldir = userdir + "WEB-INF" + File.separator;														
            String storedir = userdir + "WEB-INF" + File.separator + name +"_store"+ File.separator; 
            
            // Create a unique ID for this data submission
            Random ran = new Random();
            String submitid = Long.toHexString(Math.abs(ran.nextLong())).toUpperCase();
            File datdir = new File(storedir + submitid);
            while(datdir.exists()){
                submitid = Long.toHexString(Math.abs(ran.nextLong())).toUpperCase();
                datdir = new File(storedir + submitid);
            }
            
            // Create name of unique working directory (and a shorter path version for use on Results.jsp)  
            String workdir = storedir + submitid;
            String resultsdir = "/WEB-INF"+ File.separator + name +"_store"+ File.separator + submitid + File.separator +"results";
            
            // Store information on directories' names in bean
            inputDataB.setName(name);
            inputDataB.setSubmitID(submitid);
            inputDataB.setControldir(controldir);
            inputDataB.setStoredir(storedir);
            inputDataB.setResultsdir(resultsdir);
            inputDataB.setWorkdir(workdir);
            inputDataB.setTopdir(topdir);
            inputDataB.setUserdir(userdir);
            
            // Make a directory with name of working directory and if successful, upload the data file
            boolean uploaded = false;
            boolean directorymade = false;
            boolean oarunzipped = false;
            boolean unzipped = false;
            
            directorymade = inputDataB.makeDir();
            if(directorymade){
                // Upload the data files
                uploaded = inputDataB.uploadData();
            }
            else{
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("sreg.directorymade.error"));
                saveErrors(request, errors);
                request.getSession().setAttribute(Constants.ANALYSIS_FLAG, "");
                return mapping.findForward("EnterData");
            }
            if(!uploaded){
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("sreg.uploaded.error"));
                saveErrors(request, errors);
                request.getSession().setAttribute(Constants.ANALYSIS_FLAG, "");
                return mapping.findForward("EnterData");
            }
            
            oarunzipped = inputDataB.unzipOars();
            if(!oarunzipped){
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("sreg.oarunzipped.error"));
                saveErrors(request, errors);
                request.getSession().setAttribute(Constants.ANALYSIS_FLAG, "");
                return mapping.findForward("EnterData");
            }
            
            unzipped = inputDataB.unzipFiles();
            if(!unzipped){
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("sreg.unzipped.error"));
                saveErrors(request, errors);
                request.getSession().setAttribute(Constants.ANALYSIS_FLAG, "");
                return mapping.findForward("EnterData");
            }
            
            // Create a file that the R script will periodically append information too
            // during the course of its execution - to provide pseudo real-time process information
            // to the web application, which displays this file in a javascript popup window
            // which is refreshed at regular intervals 
            
            inputDataB.makePinfo();
            inputDataB.makeGpinfo();
            
            directorymade = inputDataB.makeMapinfo();
            if(!directorymade){
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("sreg.directorymade.error"));
                saveErrors(request, errors);
                request.getSession().setAttribute(Constants.ANALYSIS_FLAG, "");
                return mapping.findForward("EnterData");
            }
            
            request.getSession().setAttribute( Constants.INPUT_DATA, inputDataB);
            
            if(!(os.indexOf("windows") > -1)){
                String fpth = controldir +"srcmd.sh";
                String[] chcmd = new String[]{"chmod", "755", fpth};           
                inputDataB.runProgWin(chcmd);
                fpth = controldir +"killR.sh";
                chcmd = new String[]{"chmod", "755", fpth};           
                inputDataB.runProgWin(chcmd);
            }
            
            String cflag = (String) request.getSession().getAttribute( Constants.CANCEL_FLAG);
            if(cflag.equals("cancel")){
                request.getSession().setAttribute( Constants.CANCELLED_FLAG, "cancelled");
                request.getSession().setAttribute(Constants.ANALYSIS_FLAG, "");
                return mapping.getInputForward();
            }else{
                //  Run the analysis, returning any error message
                request.getSession().setAttribute( Constants.RSTARTED_FLAG, "started");
                String errorR = (String) inputDataB.runAnalysis();
                request.getSession().setAttribute( Constants.RFINISHED_FLAG, "finished");
                request.getSession().setAttribute( Constants.RSTARTED_FLAG, "");
                cflag = (String) request.getSession().getAttribute( Constants.CANCEL_FLAG);
                if(cflag.equals("cancel")){
                    request.getSession().setAttribute( Constants.CANCELLED_FLAG, "cancelled");
                    request.getSession().setAttribute( Constants.RFINISHED_FLAG, "");
                    request.getSession().setAttribute(Constants.ANALYSIS_FLAG, "");
                    return mapping.getInputForward();
                }else{
                    if(errorR.equals("")){
                        // If no error set success flag and make results file list
                        inputDataB.setSuccess("success");
                        inputDataB.deleteFile(workdir + File.separator +"results"+ File.separator +"error.txt");
                        String warningR = (String) inputDataB.readWarning();
                        if(!warningR.equals("")){
                            warnings.add("warning", new ActionMessage("sreg.process.warning", warningR));
                            saveErrors(request, warnings);
                            inputDataB.setWarningMessage(warningR);
                        }
                        inputDataB.makeMapinfoJsp();
                        inputDataB.readMapinfo();
                        inputDataB.makeDisplayFileList();
                        inputDataB.makeResultsList();
                        inputDataB.addToSubList();
                        inputDataB.appendOarParams();
                        
                    }else{
                        // If an error, return the error to the web page 
                        errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("sreg.process.error", errorR));
                        saveErrors(request, errors);
                    }
                    
                    //	 store InputFileBean as session attribute INPUT_DATA
                    request.getSession().setAttribute( Constants.INPUT_DATA, inputDataB);
                    
                    // Return to 'EnterData' page
                    request.getSession().setAttribute( Constants.RFINISHED_FLAG, "");
                    request.getSession().setAttribute(Constants.ANALYSIS_FLAG, "");
                    return mapping.getInputForward();
                }
            }
        }
        
    }
}
