package sreg;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import org.apache.struts.upload.FormFile;

/**
 * 
 *
 * @author Richard Newton
 */
public class ResultsBean {
    
    // Path to working directory for this data submission (named submitID see below)
    private String workdir = "";
    public String getWorkdir() {return this.workdir;}
    public void setWorkdir(String workdir) {this.workdir = workdir;}
    
    //  Path  used in DisplayFileAction
    private String resultsdir = "";
    public String getResultsdir() {return this.resultsdir;}
    public void setResultsdir(String resultsdir) {this.resultsdir = resultsdir;}
    
    // Unique ID for this data submission - created in EnterDataAction.java
    private String submitID = "";
    public String getSubmitID() {return this.submitID;}
    public void setSubmitID(String submitID) {this.submitID = submitID;}
    
    // Map of parameters and their current values
    private HashMap parList = new HashMap();
    public HashMap getParList() {return this.parList;}
    public void setParList(HashMap parList) {this.parList = parList;}
    
    //  List of results files, used by 'Results.jsp' web page for displaying a list of results files
    private ArrayList resList = new ArrayList();
    public ArrayList getResList() {return this.resList;}
    public void setResList(ArrayList resList) {this.resList = resList;}
    
    //  List of uploaded data filenames for this submission      
    private ArrayList upList = new ArrayList();
    public ArrayList getUpList() {return this.upList;}
    public void setUpList(ArrayList upList) {this.upList = upList;}
    
    //  Map of zip file variable name (key) and filenames in the zip archive (values) for this submission
    private HashMap<String, ArrayList> zpList = new HashMap<String, ArrayList>();
    public HashMap getZpList() {return this.zpList;}
    public void setZpList(HashMap<String, ArrayList> zpList) {this.zpList = zpList;}
    
    // Map where a key is the name of file containing lists of results files and the values are ArrayLists
    // of the names of these files 
    private HashMap<String, ArrayList> displayFileList = new HashMap<String, ArrayList>();
    public HashMap getDisplayFileList() {return this.displayFileList;}
    public void setDisplayFileList(HashMap<String, ArrayList> displayFileList) { this.displayFileList = displayFileList;}
    
    //  Warning message
    private String warningMessage = "";
    public String getWarningMessage () {return this.warningMessage;}
    public void setWarningMessage (String warningMessage ) {this.warningMessage  = warningMessage;}
    
    private HashMap<String, String> mapinfonamesE = new HashMap<String, String>();
    public HashMap getMapinfonamesE() {return this.mapinfonamesE;}
    public void setMapinfonamesE(HashMap<String, String> mapinfonamesE) {this.mapinfonamesE = mapinfonamesE;}
    
    private HashMap<String, String> mapinfonamesR = new HashMap<String, String>();
    public HashMap getMapinfonamesR() {return this.mapinfonamesR;}
    public void setMapinfonamesR(HashMap<String, String> mapinfonamesR) {this.mapinfonamesR = mapinfonamesR;}
    
    private HashMap<String, int[][]> mapArrayHash = new HashMap<String, int[][]>();
    public HashMap getMapArrayHash() {return this.mapArrayHash;}
    public void setMapArrayHash(HashMap<String, int[][]> mapArrayHash) {this.mapArrayHash = mapArrayHash;}
    
    private HashMap<String, HashMap> lookupTableHash= new HashMap<String, HashMap>();
    public HashMap<String, HashMap> getLookupTableHash() {return this.lookupTableHash;}
    public void setLookupTableHash(HashMap<String, HashMap> lookupTableHash) {this.lookupTableHash = lookupTableHash;}
    
}



