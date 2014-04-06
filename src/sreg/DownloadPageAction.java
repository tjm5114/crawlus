package sreg;

import java.io.*;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DownloadAction;
import org.apache.struts.util.MessageResources;

import java.util.zip.*;
import org.apache.struts.upload.FormFile;


/**
 * Action to construct the file Results.html, zip it and the results files, and download them to client.
 *
 * @author Richard Newton
 */

public class DownloadPageAction extends DownloadAction {
    
    protected StreamInfo getStreamInfo(ActionMapping mapping,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response)
    throws Exception {
        
        InputDataBean inputDataB = (InputDataBean) request.getSession().getAttribute(Constants.INPUT_DATA);
        String controldir = (String) inputDataB.getControldir();
        ArrayList sblist = (ArrayList) inputDataB.getSubList();
        ResultsBean rb = new ResultsBean();
        String workdir = null;
            
        MessageResources message_resources = getResources(request);
        MessageResources message_settings = getResources(request, "settings");
        String appName = message_settings.getMessage("name");
 
        //  The index of this submission, in the list of all the submissions associated with this session
        String subid = request.getParameter("subid");
       
        
        //  ResultsBean of this submission 
        for(int i=0;i < sblist.size();i++){
            rb = (ResultsBean) sblist.get(i);
            String submitid = rb.getSubmitID();
            if(submitid.equals(subid)){
                workdir = rb.getWorkdir();
                break;
            }
        }
            
        // Compose the Results.html file
        PrintWriter out = new PrintWriter(new FileWriter(workdir + File.separator + "Results.html"));
        out.println("<!DOCTYPE HTML PUBLIC \"-//W?C//DTD HTML4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\"><html><head><title>");
        out.println((String) message_resources.getMessage("all.jsp.page.heading"));
        out.println("</title></head><body bgcolor=\"white\"><p><table border=\"0\" cellpadding=\"2\" cellspacing=\"10\"  width=\"100%\"><tr><td text=\"white\" bgcolor=\"#7DC623\" colspan=\"3\">");
        out.println("<table border=\"0\" cellpadding=\"2\" cellspacing=\"0\" width=\"100%\"><tr><td width=\"100%\">  <h1><font color=\"white\">");
        out.println((String) message_resources.getMessage("all.jsp.page.heading"));
        out.println("</font></h1></td><td>&nbsp;</td><td nowrap></td><td></td></tr></table></td></tr><tr><td>");
        out.println("<hr noshade=\"\" size=\"1\"/></td></tr></table><table border=\"0\" cellpadding=\"12\" cellspacing=\"10\" width=\"100%\">");
        out.println("<tr><td><h2>Results for submission:"); 
        out.println((String) rb.getSubmitID());
        out.println("</h2></td></tr>");
        out.println("<tr><td align=\"left\" width=20%>");
        
        // Include the list of results files
        ArrayList resultsFiles = (ArrayList) rb.getResList();
        ArrayList resFiles = new ArrayList();
        
        // But if 'Results Only' selected first remove the data filenames 
        // from the list of files contained in the results subdirectory
        String which = request.getParameter("file");
        if(which.equals("results")){
            resFiles = (ArrayList) resultsFiles.clone();
            ArrayList dataFiles = (ArrayList) rb.getUpList();
            String dataFileName;
            for(int j=0;j < dataFiles.size();j++){
                dataFileName = (String) dataFiles.get(j);
                resFiles.remove(dataFileName);
            }
            HashMap zpList = (HashMap) rb.getZpList(); 
            String zfname;
            if(zpList.size() > 0){
                Iterator it = zpList.keySet().iterator();
                while(it.hasNext()){
                    String vkey = (String) it.next();
                    ArrayList zfnames = (ArrayList) zpList.get(vkey);
                    for(int i=0;i < zfnames.size();i++){
                        zfname = (String) zfnames.get(i);
                        resFiles.remove(zfname);
                    }
                }   
            }    
        }else{
            // resFiles = resultsFiles;
            resFiles = (ArrayList) resultsFiles.clone();
            HashMap zpList = (HashMap) rb.getZpList(); 
            String zfname;
            if(zpList.size() > 0){
                Iterator it = zpList.keySet().iterator();
                while(it.hasNext()){
                    String vkey = (String) it.next();
                    ArrayList zfnames = (ArrayList) zpList.get(vkey);
                    for(int i=0;i < zfnames.size();i++){
                        zfname = (String) zfnames.get(i);
                        resFiles.remove(zfname);
                    }
                }   
            }     
        }
        
        for(int i=0;i < resFiles.size();i++){
            String rFile = (String) resFiles.get(i);
            out.println("<a href=\"" +  rFile +   "\" target=\"_blank\">" + rFile + "</a><br/>");
        }
        out.print("</td><td align=\"left\" width=80%>Parameter values:&nbsp;");
        
        // Include the list of parameters and their values used in this submission
        HashMap paramList = rb.getParList();
        Set paramNames = paramList.keySet();
        Iterator it = paramNames.iterator();
        String pName;
        String pValue;
        while(it.hasNext()){
            pName = (String) it.next();
            pValue = (String) paramList.get(pName);
            out.print(pName + "&nbsp;=&nbsp;" + pValue + "&nbsp;&nbsp;");
        }      
        out.println("</td></tr>");
        
        // Include the results files which have been selected to be displayed on the page
        ArrayList<String> displayArray = new ArrayList<String>();
        BufferedReader in = new BufferedReader(new FileReader(controldir + File.separator +"toDisplay.txt"));
        String str;
        while ((str = in.readLine()) != null) {
            displayArray.add(str);
        }
        in.close();
        String dname;
        String ditem = "";
        for(int i=0;i < displayArray.size();i++){
            dname = (String) displayArray.get(i);
             if(dname.endsWith("png")){
                 ditem = "\t <tr> \n" +
  	    		"\t <td colspan=\"2\" align=\"left\" > "+  "<object type=\"image/png\" data=\""+ dname +"\" width=\"850\" height=\"750\"></object>"  +" </td> \n" +
  	    		"\t </tr>"; 
                 out.println(ditem);
             }
             if(dname.endsWith("jpg")){
             	   ditem = 	"\t <tr> \n" +
             	   			"\t <td colspan=\"2\" align=\"left\" > "+  "<object type=\"image/jpeg\" data=\""+ dname +"\" width=\"850\" height=\"750\"></object>"  +" </td> \n" +
             	   			"\t </tr>";
             	  out.println(ditem);
             }
        }
        for(int i=0;i < displayArray.size();i++){
            dname = (String) displayArray.get(i);
             if(dname.endsWith("txt")){
                 ditem = 	"\t <tr> \n" +
  	   			"\t <td colspan=\"2\" align=\"left\" > "+  "<object type=\"text/html\" data=\""+ dname +"\" width=\"850\" height=\"750\"></object>"  +" </td> \n" +
  	   			"\t </tr>";
                 out.println(ditem);
             }
        }
        out.println("</table></body></html:html>");
        out.close();
             
        
        // Zip all the files in the results subdirectory plus the Results.html file, unless 'Results Only'
        // selected in which case the data files are not included.

        FileOutputStream outz = new FileOutputStream(workdir + File.separator + appName + "_" + ((String) rb.getSubmitID()) + ".zip");
        ZipOutputStream zos = new ZipOutputStream(outz);
        ZipEntry zipe;

        // Zip the Results.html file
        FileInputStream zin = null;
        zin = new FileInputStream(workdir + File.separator  +"Results.html");
        byte[] buf = new byte[1024];
        int len;
        zipe = new ZipEntry((String) rb.getSubmitID() + File.separator + "Results.html");
        zos.putNextEntry(zipe);
        while ((len = zin.read(buf)) > 0) {
            zos.write(buf, 0, len);
        }
        zos.closeEntry();
        zin.close();
        
        // Zip the files
        for(int i=0;i < resFiles.size();i++){
            String rFile = (String) resFiles.get(i);
            zipe = new ZipEntry((String) rb.getSubmitID() + File.separator + rFile);
            zos.putNextEntry(zipe);
            zin = new FileInputStream(workdir + File.separator +"results" + File.separator + rFile);
            while ((len = zin.read(buf)) > 0) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            zin.close();
        }
        zos.close();
        
        // Download the zip file

        String filePath = workdir + File.separator + appName + "_" + ((String) rb.getSubmitID()) + ".zip"; 
        String contentType = "application/octet-stream";
        String fileName = appName + "_" + ((String) rb.getSubmitID()) + ".zip";
        response.setHeader("Content-disposition", 
                "attachment; filename=" + fileName);
        return new FileStreamInfo(contentType, new File(filePath));
    }
    
}

