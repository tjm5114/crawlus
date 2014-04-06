package sreg;

import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.util.zip.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.lang.SecurityException;
import org.apache.struts.upload.FormFile;

/**
 * Model Bean containing variables to contain all the information entered on the web pages
 * and methods for storing and processing that information. 
 *
 * @author Richard Newton
 */
public class InputDataBean {

	private String name = "";
	public String getName() {return this.name;}
	public void setName(String name) {this.name = name;}
	
	private String username = "";
	public String getUsername() {return this.username;}
	public void setUsername(String username) {this.username = username;}

	// Path to directory containing the shell script for running the R script - set in ApplicationSettings.properties
	private String controldir = "";
	public String getControldir() {return this.controldir;}
	public void setControldir(String controldir) {this.controldir = controldir;}

	//  Path to webapps directory - only needed if app contains an Oar entry box
	private String topdir = "";
	public String getTopdir() {return this.topdir;}
	public void setTopdir(String topdir) {this.topdir = topdir;}

	private String userdir = "";
	public String getUserdir() {return this.userdir;}
	public void setUserdir(String userdir) {this.userdir = userdir;}

	// Path to parent directory containing the data submission directories - set in ApplicationSettings.properties
	private String storedir = "";
	public String getStoredir() {return this.storedir;}
	public void setStoredir(String storedir) {this.storedir = storedir;}

	// Path to working directory for this data submission (named submitID see below)
	private String workdir = "";
	public String getWorkdir() {return this.workdir;}
	public void setWorkdir(String workdir) {this.workdir = workdir;}

	// Path  used in DisplayFileAction
	private String resultsdir = "";
	public String getResultsdir() {return this.resultsdir;}
	public void setResultsdir(String resultsdir) {this.resultsdir = resultsdir;}

	// Unique ID for this data submission - created in EnterDataAction.java
	private String submitID = "";
	public String getSubmitID() {return this.submitID;}
	public void setSubmitID(String submitID) {this.submitID = submitID;}

	// Flag set if analysis worked without an error
	private String success = "";
	public String getSuccess() {return this.success;}
	public void setSuccess(String success) {this.success = success;}

	// Flag set when process_info.txt ready to display
	private String pirdy = "";
	public String getPirdy() {return this.pirdy;}
	public void setPirdy(String pirdy) {this.pirdy = pirdy;}

	//  Flag set when process_graph.jpeg ready to display
	private String gpirdy = "";
	public String getGpirdy() {return this.gpirdy;}
	public void setGpirdy(String gpirdy) {this.gpirdy = gpirdy;}

	// List of files to be uploaded
	private ArrayList<Object> uploadList = new ArrayList<Object>();
	public ArrayList getUploadList() {return this.uploadList;}
	public void setUploadList(ArrayList<Object> uploadList) { this.uploadList = uploadList;}

	//  List of names of files to be uploaded  
	private ArrayList<String> uploadNameList = new ArrayList<String>();
	public ArrayList getUploadNameList() {return this.uploadNameList;}
	public void setUploadNameList(ArrayList<String> uploadNameList) { this.uploadNameList = uploadNameList;}

	// List of results files, used by 'Results.jsp' web page for displaying a list of results files
	private ArrayList<String> resultsList = new ArrayList<String>();
	public ArrayList getResultsList() {return this.resultsList;}
	public void setResultsList(ArrayList<String> resultsList) { this.resultsList = resultsList;}

	// Map of parameters and their current values
	private HashMap<String, String> paramList = new HashMap<String, String>();
	public HashMap getParamList() {return this.paramList;}
	public void setParamList(HashMap<String, String> paramList) {this.paramList = paramList;}

	//  Map of zip file variable name (key) and filenames in the zip archive (values)
	private HashMap<String, ArrayList> zipList = new HashMap<String, ArrayList>();
	public HashMap getZipList() {return this.zipList;}
	public void setZipList(HashMap<String, ArrayList> zipList) {this.zipList = zipList;}

	// Number of parameters
	private String numParams = "";
	public String getNumParams() {return this.numParams;}
	public void setNumParams(String numParams) {this.numParams = numParams;}

	// List of ResultsBeans, one RB for each submission
	private ArrayList<ResultsBean> subList = new ArrayList<ResultsBean>();
	public ArrayList getSubList() {return this.subList;}
	public void setSubList(ArrayList<ResultsBean> subList) { this.subList = subList;}   

	//  Map where a key is the name of file containing lists of results files and the values are ArrayLists
	// of the names of these files 
	private HashMap<String, ArrayList> displayFileList = new HashMap<String, ArrayList>();
	public HashMap getDisplayFileList() {return this.displayFileList;}
	public void setDisplayFileList(HashMap<String, ArrayList> displayFileList) { this.displayFileList = displayFileList;}

	//  Warning message
	private String warningMessage = "";
	public String getWarningMessage () {return this.warningMessage;}
	public void setWarningMessage (String warningMessage ) {this.warningMessage = warningMessage;}

	/* private int[][] mapArray;
    public int[][] getMapArray() {return this.mapArray;}
    public void setMapArray(int[][] mapArray) {this.mapArray = mapArray;}

    private HashMap<String, String> lookupTable= new HashMap<String, String>();
    public HashMap<String, String> getLookupTable() {return this.lookupTable;}
    public void setLookupTable(HashMap<String, String> lookupTable) {this.lookupTable = lookupTable;}*/

	private HashMap<String, int[][]> mapArrayHash = new HashMap<String, int[][]>();
	public HashMap getMapArrayHash() {return this.mapArrayHash;}
	public void setMapArrayHash(HashMap<String, int[][]> mapArrayHash) {this.mapArrayHash = mapArrayHash;}

	private HashMap<String, HashMap> lookupTableHash= new HashMap<String, HashMap>();
	public HashMap<String, HashMap> getLookupTableHash() {return this.lookupTableHash;}
	public void setLookupTableHash(HashMap<String, HashMap> lookupTableHash) {this.lookupTableHash = lookupTableHash;}


	public  void addToUploadList(Object input){
		uploadList.add(input);
	}

	public  void addToUploadNameList(String val){ 
		uploadNameList.add(val);
	}

	public  void addToParamList(String iname, String val){
		paramList.put(iname, val);
	}

	public  void clearParamList(){
		paramList.clear();
	}

	public  void clearResultsList(){
		resultsList.clear();
	}

	public  void clearDisplayFileList(){
		displayFileList.clear();
	}

	public  void clearUploadList(){
		uploadList.clear();
	}

	public  void clearUploadNameList(){  
		uploadNameList.clear();
	}

	public  void clearSubList(){
		subList.clear();
	}

	public  void clearZipList(){
		zipList.clear();
	}

	public void setParamListSize(){
		int pint = paramList.size()-1;
		this.numParams = Integer.toString(pint);
	}

	public void addToSubList(){
		ResultsBean rb = new ResultsBean();
		rb.setSubmitID(submitID);
		rb.setResultsdir(resultsdir);
		rb.setWorkdir(workdir);
		rb.setParList((HashMap) paramList.clone());
		rb.setResList((ArrayList) resultsList.clone());
		rb.setUpList((ArrayList) uploadNameList.clone()); 
		rb.setZpList((HashMap) zipList.clone());
		rb.setDisplayFileList((HashMap) displayFileList.clone());
		rb.setWarningMessage(warningMessage);
		rb.setMapArrayHash(ArrayHashCopy(mapArrayHash));
		rb.setLookupTableHash((HashMap) lookupTableHash.clone()); 
		subList.add(rb);
	}

	public int[][] ArrayCopy(int[][] source){
		int[][] copy = source.clone();
		for(int i=0;i<source.length;i++){
			System.arraycopy(source[i],0,copy[i],0, source[i].length);
		}
		return copy;
	}

	public HashMap ArrayHashCopy(HashMap source){
		HashMap copy = new HashMap();
		Iterator itkeys = source.keySet().iterator();
		String mkey;
		while(itkeys.hasNext()){
			mkey = (String) itkeys.next();
			copy.put(mkey, ArrayCopy((int[][]) source.get(mkey)));
		}
		return copy;
	}

	public void readMapArray(String mpfile, String which) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(mpfile));
		int rocount = 0;
		int cocount = 0;
		String line = null;
		String temp = null;
		while(in.ready()){
			line = in.readLine();
			rocount++;
		}
		StringTokenizer st = new StringTokenizer(line,",");
		while(st.hasMoreTokens()){
			temp = st.nextToken();
			cocount++;
		}
		in.close();
		int [][] mpa = new int [rocount][cocount];
		BufferedReader in2 = new BufferedReader(new FileReader(mpfile));
		int ro = 0;
		int co = 0;
		while(in2.ready()){
			line = in2.readLine();
			StringTokenizer st2 = new StringTokenizer(line,",");
			while(st2.hasMoreTokens()){
				mpa[ro][co] = Integer.parseInt(st2.nextToken());
				co++;
			}
			co = 0;
			ro++;
		}
		in2.close();
		this.mapArrayHash.put(which, mpa);
	}

	public void readLookupTable(String lutfile, String which) throws IOException{
		HashMap<String, String> lt = new HashMap<String, String>();
		BufferedReader in = new BufferedReader(new FileReader(lutfile));
		int ro = 0;
		int co = 0;
		String ky;
		String val;
		String line;
		while(in.ready()){
			line = in.readLine();
			StringTokenizer st = new StringTokenizer(line,",");
			ky = st.nextToken();
			val = st.nextToken();
			lt.put(ky, val);
		}
		in.close();
		this.lookupTableHash.put(which, lt);
	}

	// Method to delete old submission directories
	public  void clearDirs(){
		String wdir;
		ResultsBean rb;
		for(int i=0;i < subList.size();i++){
			rb = (ResultsBean) subList.get(i);
			wdir = (String) rb.getWorkdir();
			File wrkdir = new File(wdir);
			deleteDirectory(wrkdir);
		}
	}

	//  Method to remove current submission directory
	public  void clearDir(){
		File wrkdir = new File(workdir);
		deleteDirectory(wrkdir);
	}

	// Method to delete a directory and its contents, including subdirectories
	public static void deleteDirectory(File toDel) {         
		File[] children = toDel.listFiles();
		int num = Array.getLength(children);
		for(int i=0;i < num;i++){
			File tdChild = children[i]; 
			if(tdChild.isDirectory()) {
				deleteDirectory(tdChild);
			}
			tdChild.delete();
		}
		toDel.delete();
	}

	//  Method to copy a file
	public static void copyFile(File source, File dest) throws IOException {  
		if(!dest.exists()) {
			dest.createNewFile();
		}
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(source);
			out = new FileOutputStream(dest);
			// Transfer bytes from in to out
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
		}
		finally {
			in.close();
			out.close();
		}    
	}

	//  Method to append oar parameters
	public static void appendOar(String sfilename, String dfilename, String oarappname) throws IOException {  
		File source = new File(sfilename);
		File dest = new File(dfilename);
		if(!dest.exists()) {
			dest.createNewFile();
		}
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(source);
			out = new FileOutputStream(dest, true);
			String hding = "\nParameter values from " + oarappname + ":\n";
			out.write(hding.getBytes());
			// Transfer bytes from in to out
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
		}
		finally {
			in.close();
			out.close();
		}    
	}


	//  Method to unzip oar file 
	public boolean unzipOar(String oarzipfilename, String os){
		final int BUFFER = 2048;
		try {
			BufferedOutputStream dest = null;
			BufferedInputStream is = null;
			ZipEntry entry;
			ZipFile zipfile;
			File dir;
			FileOutputStream fos;
			int index;
			zipfile = new ZipFile(workdir + File.separator + "results" + File.separator + oarzipfilename);
			Enumeration e = zipfile.entries();
			while(e.hasMoreElements()) {
				entry = (ZipEntry) e.nextElement();
				index = entry.getName().lastIndexOf("/") ;
				if(index == -1){
					index = entry.getName().lastIndexOf("\\");
				}
				if(index != -1) { 
					dir = new File(workdir + File.separator + "results" + File.separator + entry.getName().substring(0, index));                
					if (!(dir.exists() && dir.isDirectory())) {
						dir.mkdirs();
					}
				}   
				is = new BufferedInputStream(zipfile.getInputStream(entry));
				int len;
				byte data[] = new byte[BUFFER];
				fos = new FileOutputStream(workdir + File.separator + "results" + File.separator + entry);
				dest = new BufferedOutputStream(fos, BUFFER);
				while ((len = is.read(data, 0, BUFFER)) 
						!= -1) {
					dest.write(data, 0, len);
				}
				dest.flush();
				dest.close();
				is.close();
			}
		} catch(Exception e) {
			System.out.println("Problems unzipping file: "  + e.toString());
			return false;
		}
		return true;
	}


	//  Method to unzip a zip file and store the names of the files unzipped
	//  Removes any directory structure in zip archive
	public boolean unzipFile(String zipfilename, String os, String vname){
		final int BUFFER = 2048;
		try {
			BufferedOutputStream dest = null;
			BufferedInputStream is = null;
			ZipEntry entry;
			ZipFile zipfile;
			File dir;
			File topDir = null;
			FileOutputStream fos;
			int index;
			int findex;
			String fname;
			String entryName;
			File fileSource;
			File fileDest;
			zipfile = new ZipFile(workdir + File.separator + "results" + File.separator + zipfilename);
			Enumeration e = zipfile.entries();
			ArrayList<String> unZippedFiles = new ArrayList<String>();
			while(e.hasMoreElements()) {
				entry = (ZipEntry) e.nextElement();
				entryName = (String) entry.getName();
				index = entryName.lastIndexOf("/");
				if(index == -1){
					index = entryName.lastIndexOf("\\");
				}
				findex = entryName.indexOf("/", 1);
				if(findex == -1){
					findex = entryName.indexOf("\\", 1);
				}
				if (index != -1) { 
					fname = entryName.substring(index+1, entryName.length());
					dir = new File(workdir + File.separator + "results" + File.separator + entryName.substring(0, index));  
					if (!(dir.exists() && dir.isDirectory())) {
						dir.mkdirs();
					}
				}else{
					fname = entryName;
				}
				if(findex != -1){
					topDir = new File(workdir + File.separator + "results" + File.separator + entryName.substring(0, findex));
				}else{
					topDir = null;
				}
				unZippedFiles.add(fname);
				is = new BufferedInputStream(zipfile.getInputStream(entry));
				int len;
				byte data[] = new byte[BUFFER];
				fos = new FileOutputStream(workdir + File.separator + "results" + File.separator + entry);
				dest = new BufferedOutputStream(fos, BUFFER);
				while ((len = is.read(data, 0, BUFFER)) 
						!= -1) {
					dest.write(data, 0, len);
				}
				dest.flush();
				dest.close();
				is.close();
				if(index != -1){
					fileSource = new File(workdir + File.separator + "results" + File.separator + entry);
					fileDest = new File(workdir + File.separator + "results" + File.separator + fname);
					copyFile(fileSource, fileDest);
				}
			}
			zipList.put(vname, unZippedFiles);
			if(topDir != null){
				deleteDirectory(topDir);
			}
		} catch(Exception e) {
			System.out.println("Problems unzipping file: "  + e.toString());
			return false;
		}
		return true;
	}



	// Method to upload the data files entered on the web page
	public  boolean uploadData() throws ServletException, IOException{
		for(int i=0;i < uploadList.size();i++){
			FormFile dataFile = (FormFile) uploadList.get(i);
			String fname = "";
			int fname_length = 0;
			// If validation is turned off there may not be a FormFile to upload
			// If validation is on its absence will be picked up before this and a validation error returned.
			if(dataFile != null){
				fname = dataFile.getFileName(); 
			}
			if(fname != null){
				fname_length = fname.length();
			} 

			if((dataFile != null) && fname != null && (fname_length > 0)){ 
				try {
					InputStream streamIn = dataFile.getInputStream();
					OutputStream streamOut = new FileOutputStream(workdir + File.separator + "results" + File.separator + dataFile.getFileName());
					int bytesRead = 0;
					byte[] buffer = new byte[8192];

					// Read the selected file and write it to the unique directory
					while ((bytesRead = streamIn.read(buffer, 0, 8192)) != -1) {
						streamOut.write(buffer, 0, bytesRead);
					}

					streamOut.close();
					streamIn.close();
				} catch (IOException e) {
					System.out.println("Problems reading uploaded file: " + dataFile.getFileName() + e.toString());
					return false;
				}
			}
		}
		return true;
	}


	// Method to make a unique working directory for this submission of data, the parent path being storedir, 
	// the directory name being the submission ID
	public  boolean makeDir() throws ServletException, SecurityException{
		try {
			File datadir = new File(workdir + File.separator + "results");
			datadir.mkdirs();	    
		} catch (SecurityException e) {
			System.out.println("Problems creating a directory" + e.toString());
			return false;
		}
		return true;
	}


	// Method to run the Shell script
	public static void runProg(String cmd) throws  IOException{

		// Get the applications instance of the Runtime class 
		Runtime r = Runtime.getRuntime();
		Process p = null;
		String load;
		try{
			// Use instance of Runtime class to run the command as a java Process
			p = r.exec(cmd);

			// Need to be careful with Process input and output to prevent it seizing up
			// May not be needed (up to **) in most cases but included just in case
			InputStream pin = p.getInputStream();
			InputStreamReader cin = new InputStreamReader(pin);
			BufferedReader in = new BufferedReader(cin);
			try{
				while((load = in.readLine()) !=null){}
			}
			catch (IOException e){ }

			InputStream epin = p.getErrorStream();
			InputStreamReader ecin = new InputStreamReader(epin);
			BufferedReader ein = new BufferedReader(ecin);
			try{
				while((load = ein.readLine()) !=null){}
			}
			catch (IOException e){ }

			ein.close();
			in.close();
			// **
		}

		catch (IOException e){
			System.out.println("Problems running: " + e.toString());
		} 

		// Wait for the process to finish
		try{p.waitFor();}
		catch(InterruptedException e){ System.out.println("Problems waiting: "+ e.toString());} 

	}

	// Method to run the Shell script
	public static void runProgWin(String[] cmd) throws  IOException{

		// Get the applications instance of the Runtime class 
		Runtime r = Runtime.getRuntime();
		Process p = null;
		String load;
		try{
			// Use instance of Runtime class to run the command as a java Process
			p = r.exec(cmd);

			// Need to be careful with Process input and output to prevent it seizing up
			// May not be needed (up to **) in most cases but included just in case
			InputStream pin = p.getInputStream();
			InputStreamReader cin = new InputStreamReader(pin);
			BufferedReader in = new BufferedReader(cin);
			try{
				while((load = in.readLine()) !=null){}
			}
			catch (IOException e){ }

			InputStream epin = p.getErrorStream();
			InputStreamReader ecin = new InputStreamReader(epin);
			BufferedReader ein = new BufferedReader(ecin);
			try{
				while((load = ein.readLine()) !=null){}
			}
			catch (IOException e){ }

			ein.close();
			in.close();
			// **
		}

		catch (IOException e){
			System.out.println("Problems running: " + e.toString());
		} 

		// Wait for the process to finish
		try{p.waitFor();}
		catch(InterruptedException e){ System.out.println("Problems waiting: "+ e.toString());} 

	}


	// Method to read the error file produced by the R script
	public  String readErrorFile(String errorfile) throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(errorfile));
		String error = "";
		while(in.ready()){
			error += in.readLine();}
		in.close();
		return error;
	}

	//  Method to read the warning file, if any, produced by the R script
	public String readWarning() throws IOException{
		File wfile = new File(workdir + File.separator +"results" + File.separator +"warning.txt");
		String warning = "";
		if(wfile.exists()){
			BufferedReader in = new BufferedReader(new FileReader(wfile));
			while(in.ready()){
				warning += in.readLine();
			}
			in.close();
		}
		return warning;
	}

	//  Method to make the list of results files produced by the R script
	public void makeResultsList() throws IOException{
		File datadir = new File(workdir + File.separator +"results");
		File[] results = datadir.listFiles();
		String filename;
		for(int i=0; i< Array.getLength(results);i++){
			if(!results[i].isDirectory()){
				filename = results[i].getName();   
				if(!displayFileList.containsKey(filename)){
					resultsList.add(filename);
				}
			}
		}
	}


	// Method to delete a file if it exists
	public  void deleteFile(String afilename) throws IOException{
		File afile = new File(afilename);
		if(afile.exists()){afile.delete();}
	}

	//  Method to run the R analysis
	public  String runAnalysis() throws ServletException, IOException{
		String os = System.getProperty("os.name").toLowerCase();
		// File to contain the complete R script
		PrintWriter out = new PrintWriter(new FileWriter(workdir + File.separator +"completeRscript.R"));
		// File to contain any errors from the R script (hopefully will be blank)
		PrintWriter out2 = new PrintWriter(new FileWriter(workdir + File.separator +"results"+ File.separator +"error.txt"));
		// File to contain parameter list
		PrintWriter out3 = new PrintWriter(new FileWriter(workdir + File.separator +"results"+ File.separator +"params_"+ submitID +".txt"));
		out3.println(submitID);
		out3.println("\nParameter values:");
		String plist = paramList.toString();
		plist = plist.replaceAll("\\{", " ");
		plist = plist.replaceAll("\\}", "");
		plist = plist.replaceAll(",", "\n");
		plist = plist.replaceAll("=", " = ");
		out3.println(plist);
		if(zipList.size() > 0){
			Iterator it = zipList.keySet().iterator();
			while(it.hasNext()){
				String vkey = (String) it.next();
				out3.println("\n"+ vkey +" (unzipped files):");
				ArrayList fnames = (ArrayList) zipList.get(vkey);
				for(int i=0;i < fnames.size();i++){
					out3.println(" "+ fnames.get(i));
				}
			}   
		}

		// Concatenate the user entered information as R assignments with the main R script
		// to create the complete R script and write it to the file "completeRscript.R"
		String rinfo = makeRinfo();
		if(os.indexOf("windows") > -1){ 
			String workdir_win = workdir.replaceAll("\\\\", "\\\\\\\\");
			out.println(" options(error=quote({ write(geterrmessage(),\""+ workdir_win + File.separator + File.separator +"results"+ File.separator + File.separator +"error.txt\")  ; q()})) \n"+
					"setwd(\""+ workdir_win + File.separator + File.separator +"results"+ File.separator + File.separator +"\") \n" +
					"username <-\""+ username +"\"\n" +
					rinfo);
		}else{
			out.println(" options(error=quote({ write(geterrmessage(),\""+ workdir + File.separator +"results"+ File.separator +"error.txt\")  ; q()})) \n"+
					"setwd(\""+ workdir + File.separator +"results"+ File.separator +"\") \n" +
					"username <-\""+ username +"\"\n" +
					rinfo);
		}
		out.close();
		out2.close();
		out3.close();


		String[] cmdwin = new String[3];
		if((os.indexOf("windows") > -1) || (os.indexOf("nt") > -1)){ 
			if(os.indexOf("windows 9") > -1){  
				// ?? need start command.com /c  
				cmdwin[0] = "command.com";
				cmdwin[1] = "/C";
				cmdwin[2] = "Rterm.exe --slave --no-restore --no-save <"+ workdir +"\\completeRscript.R >"+ workdir +"\\completeRscript.Rout" ;
				//cmdwin[2] = "Rcmd BATCH  --slave --no-restore --no-save <"+ workdir +"\\completeRscript.R" ;
				runProgWin(cmdwin);
			}else{
				cmdwin[0] = "cmd.exe";
				cmdwin[1] = "/C";
				cmdwin[2] = "Rterm.exe --slave --no-restore --no-save <"+ workdir +"\\completeRscript.R >"+ workdir +"\\completeRscript.Rout 2>&1" ;
				//cmdwin[2] = "Rcmd BATCH  --slave --no-restore --no-save "+ workdir +"\\completeRscript.R";
				runProgWin(cmdwin);
			}
		}else{
			//  Command for method runProg to run in Unix - the shell script "srcmd.sh" with "completeRscript.R" as the argument

			String cmdu = controldir +"srcmd.sh " + workdir +"/completeRscript.R " + workdir +"/completeRscript.Rout "; // + "\\\"--args " + submitID + "_proc\\\"";
			runProg(cmdu);
		}

		// Check the error file once the R script has finished and return the contents (hopefully blank)
		String error = readErrorFile(workdir + File.separator +"results"+ File.separator +"error.txt");
		return error;
	}

	//  Method to make a list of zip archive file names i.e. c("...","...","..." etc)
	public String makeZipNameString(String vkey){
		ArrayList znames = new ArrayList();
		znames = (ArrayList) zipList.get(vkey);
		String nString="c("; 
		for(int i=0;i<znames.size();i++){
			nString = nString +"\""+(String) znames.get(i)+"\""+",";
		}    
		nString = nString.substring(0,nString.lastIndexOf(","));
		nString = nString + ")";
		return nString;    
	}

	//  Method to make a list of multi-select items i.e. c("...","...","..." etc)
	public String makeTextMultListString(String[] lname){
		String str = "";
		int len = Array.getLength(lname);
		String nString="c(";
		if(len > 0){
			for(int j=0;j < len;j++){
				str = lname[j];
				nString = nString +"\""+ str +"\"" +",";
			} 
			nString = nString.substring(0,nString.lastIndexOf(","));
		}
		nString = nString + ")";
		return nString;    
	}

	//  Method to make a list of multi-select items i.e. c(...,...,... etc)
	public String makeNumericMultListString(String[] lname){
		String str = "";
		int len = Array.getLength(lname);
		String nString="c(";
		if(len > 0){
			for(int j=0;j < len;j++){
				str = lname[j];
				nString = nString + str +",";
			} 
			nString = nString.substring(0,nString.lastIndexOf(","));
		}
		nString = nString + ")";
		return nString;    
	}

	public void killR() {
		String cmd = controldir +"killR.sh " + name +"_store"+ File.separator + submitID;
		Runtime r = Runtime.getRuntime();
		Process p = null;
		String load;
		try {
			p = r.exec(cmd);

			InputStream pin = p.getInputStream();
			InputStreamReader cin = new InputStreamReader(pin);
			BufferedReader in = new BufferedReader(cin);
			try{
				while((load = in.readLine()) !=null){}
			}
			catch (IOException e){ System.out.println(e);}

			InputStream epin = p.getErrorStream();
			InputStreamReader ecin = new InputStreamReader(epin);
			BufferedReader ein = new BufferedReader(ecin);
			try{
				while((load = ein.readLine()) !=null){}
			}
			catch (IOException e){ System.out.println(e);}

			ein.close();
			in.close();
		}
		catch (IOException e){ System.out.println(e);}

		try{p.waitFor();}
		catch(InterruptedException e){System.out.println(e);}
	}



	public String makeRinfo() {
		String info = "";

	 String os = System.getProperty("os.name").toLowerCase(); 
	 String zipNameString = ""; 
	 String multListString = ""; 
	 String fname = ""; 
	 int fname_length = 0; 
	 info = info +  "startingbar <- \""+startingbar+"\"\n" ;
	 info = info +  "costpref <- "+costpref+"\n" ;
	 info = info +  "crowdpref <- "+crowdpref+"\n" ;
	 info = info +  "alcpref <- "+alcpref+"\n" ;
	 info = info +  "nobot <- "+nobot+"\n" ;
	 if(os.indexOf("windows") > -1){ 

	 }else{ 

	 }; 

	 if(os.indexOf("windows") > -1){ 
		 String controldir_win = controldir.replaceAll("\\\\", "\\\\\\\\"); 
		 info = info + "source(\""+ controldir_win + "branchandboundnewtest2.R\") \n" ;
	 }else{ 

		 info = info + "source(\""+ controldir + "branchandboundnewtest2.R\") \n" ;
	 }; 

	 return info; 
    } 


    public void makePinfo() throws IOException { 
    } 

    public void makeGpinfo() throws IOException { 
    } 

    public boolean unzipOars() throws IOException { 
        boolean success = false; 
	 String os = System.getProperty("os.name").toLowerCase(); 
	 success = true; 
	 return success; 
} 

    public void appendOarParams() throws IOException { 
	 String oarparamsfilename = ""; 
	 String paramsfilename = ""; 
    } 

    public boolean unzipFiles() throws IOException { 
        boolean success = false; 
	 String os = System.getProperty("os.name").toLowerCase(); 
	 String fname = ""; 
	 int fname_length = 0; 
	 success = true; 
	 return success; 
} 

    public void makeDisplayFileList() throws IOException { 
     BufferedReader in;
     String str;
     ArrayList filenames = new ArrayList();
     int numCols =  0;
     int count = 0;
     int numEmptyCols = 0;
} 

    public void readMapinfo() throws IOException { 
    } 

 public  boolean makeMapinfo() throws ServletException, SecurityException, IOException{ 
return true;
    } 

    public void makeMapinfoJsp() throws IOException { 
    } 

	 private String startingbar; 
	 public String getStartingbar() {return (this.startingbar);} 
	 public void setStartingbar(String startingbar) {this.startingbar = startingbar;}
	 private String costpref; 
	 public String getCostpref() {return (this.costpref);} 
	 public void setCostpref(String costpref) {this.costpref = costpref;}
	 private String crowdpref; 
	 public String getCrowdpref() {return (this.crowdpref);} 
	 public void setCrowdpref(String crowdpref) {this.crowdpref = crowdpref;}
	 private String alcpref; 
	 public String getAlcpref() {return (this.alcpref);} 
	 public void setAlcpref(String alcpref) {this.alcpref = alcpref;}
	 private String nobot; 
	 public String getNobot() {return (this.nobot);} 
	 public void setNobot(String nobot) {this.nobot = nobot;}
}
