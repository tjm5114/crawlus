<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="sreg.ResultsBean" %>
<%@ page import="java.util.HashMap" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%
	response.setHeader("Cache-control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader ("Expires", 0);
%>

<html:html locale="true">
<head>

<meta http-equiv="expires" content="0"/>
<meta name="robots" content="noarchive"/>
<title><bean:message key="all.jsp.page.heading"/></title>
<SCRIPT LANGUAGE="JavaScript">
function disableFormNC() { 

if (document.all || document.getElementById) {
document.body.style.cursor="wait";
for (j = 0; j < document.forms.length; j++) {
for (i = 0; i < document.forms[j].length; i++) {
var tempobj = document.forms[j].elements[i];
if (tempobj.name == "analyse" || tempobj.name=="clear" )
tempobj.disabled = true;
}
}
return true;
}
else {
return false;
   }
}

function disableFormAll(){
if (document.all || document.getElementById) {
document.body.style.cursor="wait";
for (j = 0; j < document.forms.length; j++) {
for (i = 0; i < document.forms[j].length; i++) {
var tempobj = document.forms[j].elements[i];
if (tempobj.name == "analyse" || tempobj.name == "clear" || tempobj.name=="cancel")
tempobj.disabled = true;
}
}
return true;
}
else {
return false;
   }
}


function retFalse () {
return false;
}
//  End -->
</script>

</head>

<body bgcolor="white"><p>

	<table border="0" cellpadding="2" cellspacing="10"  width="100%">
    <tr><td text="white" bgcolor="#7DC623" colspan="3">
    <table border="0" cellpadding="2" cellspacing="0" width="100%">
    <tr>
    <td width="100%">  <h1><font color="white">
    <bean:message key="all.jsp.page.heading"/>
    </font></h1>
    </td>
    <td>&nbsp;</td>
    <td nowrap></td>
    <td></td>
    </tr>
    </table>
    </td></tr>
    <tr><td>
    <hr noshade="" size="1"/>
    </td></tr>
    </table>
    
	<table border="0" cellpadding="12" cellspacing="0" width="100%">
		<tr>
			<br/>
		</tr>
	 <tr> 
		 <td align="left" colspan ="2">This simple web app demonstrates proof of concept for the HACKPSU bartour app, Crawlr.

Seifu Chonde PhD(c), Angela Garza PhD(c),Ken Hutchison PhD(c), Trey Morris, Evan White</td> 
	 </tr>

		<tr>
			<br/>
		</tr>
	
		<html:form action="enterdata.do" method="POST" enctype="multipart/form-data"  onsubmit="return disableFormNC();">
		 <tr> 
			 <td align="left" width ="20%"><html:select property="startingbar"  > 
				 <html:option value="Allen.St..Grill">Allen.St..Grill </html:option> </br> 
				 <html:option value="Bar.Bleu">Bar.Bleu </html:option> </br> 
				 <html:option value="The.Brewery">The.Brewery </html:option> </br> 
				 <html:option value="Cafe.210">Cafe.210 </html:option> </br> 
				 <html:option value="Chilis">Chilis </html:option> </br> 
				 <html:option value="Chrome">Chrome </html:option> </br> 
				 <html:option value="Chumleys">Chumleys </html:option> </br> 
				 <html:option value="Darkhorse.Tavern">Darkhorse.Tavern </html:option> </br> 
				 <html:option value="Gingerbread.Man">Gingerbread.Man </html:option> </br> 
				 <html:option value="Indigo">Indigo </html:option> </br> 
				 <html:option value="Inferno">Inferno </html:option> </br> 
				 <html:option value="Kildares">Kildares </html:option> </br> 
				 <html:option value="Levels">Levels </html:option> </br> 
				 <html:option value="Lions.Den">Lions.Den </html:option> </br> 
				 <html:option value="Local.Whiskey">Local.Whiskey </html:option> </br> 
				 <html:option value="Mad.Mex">Mad.Mex </html:option> </br> 
				 <html:option value="The.Phyrst">The.Phyrst </html:option> </br> 
				 <html:option value="Bill.Pickles.Tap.Room">Bill.Pickles.Tap.Room </html:option> </br> 
				 <html:option value="The.Rathskeller">The.Rathskeller </html:option> </br> 
				 <html:option value="Rotellis">Rotellis </html:option> </br> 
				 <html:option value="Rumors.Lounge">Rumors.Lounge </html:option> </br> 
				 <html:option value="The.Saloon">The.Saloon </html:option> </br> 
				 <html:option value="The.Shandygaff">The.Shandygaff </html:option> </br> 
				 <html:option value="The.Tavern.Restaurant">The.Tavern.Restaurant </html:option> </br> 
				 <html:option value="Z.Bar...The.Deli">Z.Bar...The.Deli </html:option> </br> 
				 <html:option value="Zenos">Zenos </html:option> </br> 
			 </html:select> </td>
			 <td align="left" width ="75%"><font color="red"><strong><html:errors property="startingbar"/></strong></font>Please enter the bar where you would like to begin:</td> 
			 <td align="left" width ="5%"><br/><br/></td> 
		 </tr>
		 <tr> 
			 <td align="left" width ="20%"><html:select property="costpref"  > 
				 <html:option value="1">1 </html:option> </br> 
				 <html:option value="2">2 </html:option> </br> 
				 <html:option value="3">3 </html:option> </br> 
				 <html:option value="4">4 </html:option> </br> 
				 <html:option value="5">5 </html:option> </br> 
				 <html:option value="6">6 </html:option> </br> 
				 <html:option value="7">7 </html:option> </br> 
				 <html:option value="8">8 </html:option> </br> 
				 <html:option value="9">9 </html:option> </br> 
				 <html:option value="10">10 </html:option> </br> 
			 </html:select> </td>
			 <td align="left" width ="75%"><font color="red"><strong><html:errors property="costpref"/></strong></font>Please enter how much you care about the cost of bars (1-10). 
1:'Break out the black Amex.' 10:'I'm gonna need change for the bus." </td> 
			 <td align="left" width ="5%"><br/><br/></td> 
		 </tr>
		 <tr> 
			 <td align="left" width ="20%"><html:select property="crowdpref"  > 
				 <html:option value="1">1 </html:option> </br> 
				 <html:option value="2">2 </html:option> </br> 
				 <html:option value="3">3 </html:option> </br> 
				 <html:option value="4">4 </html:option> </br> 
				 <html:option value="5">5 </html:option> </br> 
				 <html:option value="6">6 </html:option> </br> 
				 <html:option value="7">7 </html:option> </br> 
				 <html:option value="8">8 </html:option> </br> 
				 <html:option value="9">9 </html:option> </br> 
				 <html:option value="10">10 </html:option> </br> 
			 </html:select> </td>
			 <td align="left" width ="75%"><font color="red"><strong><html:errors property="crowdpref"/></strong></font>Please enter how crowded of a bar you would like.
1: 'Me, Myself, and a Bartender' 10: 'Vertical Twister please'</td> 
			 <td align="left" width ="5%"><br/><br/></td> 
		 </tr>
		 <tr> 
			 <td align="left" width ="20%"><html:select property="alcpref"  > 
				 <html:option value="1">1 </html:option> </br> 
				 <html:option value="2">2 </html:option> </br> 
				 <html:option value="3">3 </html:option> </br> 
				 <html:option value="4">4 </html:option> </br> 
				 <html:option value="5">5 </html:option> </br> 
				 <html:option value="6">6 </html:option> </br> 
				 <html:option value="7">7 </html:option> </br> 
				 <html:option value="8">8 </html:option> </br> 
				 <html:option value="9">9 </html:option> </br> 
				 <html:option value="10">10 </html:option> </br> 
			 </html:select> </td>
			 <td align="left" width ="75%"><font color="red"><strong><html:errors property="alcpref"/></strong></font>Please enter your preference for boozeage.
1: 'Dolla Dolla Beers' 10: 'Liquor is quicker'</td> 
			 <td align="left" width ="5%"><br/><br/></td> 
		 </tr>
		 <tr> 
			 <td align="left" width ="20%"><html:select property="nobot"  > 
				 <html:option value="1">1 </html:option> </br> 
				 <html:option value="2">2 </html:option> </br> 
				 <html:option value="3">3 </html:option> </br> 
				 <html:option value="4">4 </html:option> </br> 
				 <html:option value="5">5 </html:option> </br> 
				 <html:option value="6">6 </html:option> </br> 
				 <html:option value="7">7 </html:option> </br> 
				 <html:option value="8">8 </html:option> </br> 
				 <html:option value="9">9 </html:option> </br> 
				 <html:option value="10">10 </html:option> </br> 
				 <html:option value="11">11 </html:option> </br> 
				 <html:option value="12">12 </html:option> </br> 
				 <html:option value="13">13 </html:option> </br> 
				 <html:option value="14">14 </html:option> </br> 
				 <html:option value="15">15 </html:option> </br> 
				 <html:option value="16">16 </html:option> </br> 
				 <html:option value="17">17 </html:option> </br> 
				 <html:option value="18">18 </html:option> </br> 
				 <html:option value="19">19 </html:option> </br> 
				 <html:option value="20">20 </html:option> </br> 
				 <html:option value="21">21 </html:option> </br> 
				 <html:option value="22">22 </html:option> </br> 
				 <html:option value="23">23 </html:option> </br> 
				 <html:option value="24">24 </html:option> </br> 
			 </html:select> </td>
			 <td align="left" width ="75%"><font color="red"><strong><html:errors property="nobot"/></strong></font>Please enter the number of bars you would like to visit:</td> 
			 <td align="left" width ="5%"><br/><br/></td> 
		 </tr>

	<tr><td valign="top" colspan = "1">
	  <table>
	    <tr>
	      <td>
		    <html:hidden property="procFlag" value="enter"/>
  		    <html:submit property="analyse">Analyse</html:submit><br/>
  		    </html:form>
  	      </td>
  	      <td>
             &nbsp;&nbsp;&nbsp;
          </td>
  	      <td>
  	        <html:form action="cancelanalysis.do"  onsubmit="return disableFormAll();">
  	        <html:cancel property="cancel">Cancel</html:cancel><br/>
  	        </html:form>
  	      </td>
  	    </tr>
      </table>
	</td></tr>
	

	<tr><td>
		<font color="red"><strong><html:errors property="org.apache.struts.action.GLOBAL_MESSAGE"/></strong></font>
		<font color="green"><strong><html:errors property="warning"/></strong></font>
	</td></tr>
		
	<logic:present name="inputdata" scope="session">
    	<logic:notEmpty name="inputdata" property="success" scope="session">
    		
    	<tr><td>
    		<hr noshade="" size="1"/>
    	</td></tr>
    	  	
    	
    	
	 <logic:notEmpty name="inputdata" property="submitID"> 
	 <tr><td> 
	<h2>Current Results Files</h2> 
	</td></tr> 
	 <bean:define id="currentSubmitID" name="inputdata" property="submitID" type="String"/> 
	 </table> 
	 <table border="0" cellpadding="12" cellspacing="0" width="100%"> 
	 <tr> 
	 <td colspan="1" align="left" valign="top"> <object type="text/plain"  data="displayfile.do?subid=<%= currentSubmitID %>&amp;ctype=txt&amp;file=bartour.txt" width="850" height="750"></object> </td> 

	 </tr>
	 <tr> 
	 <td colspan="1" align="left" valign="top"> <object type="image/png" data="displayfile.do?subid=<%= currentSubmitID %>&amp;ctype=png&amp;file=rplot.png"  width="960" height="960"></object> </td> 

	 </tr>
	 </table> 
	 </logic:notEmpty> 

<table border="0" cellpadding="12" cellspacing="0" width="100%">   	
    	    
    	<tr><td>
			<h2>Results Pages</h2>
		</td></tr>
		<logic:notEmpty name="inputdata" property="subList" scope="session">
		<logic:iterate id="sub"  name="inputdata"  indexId="ind" property="subList" type="ResultsBean">
		<bean:define id="listSubID" name="sub" property="submitID" type="String"/>
    	<tr>
    		<td align="left" width=30%>
             	<html:link forward="Results" paramId="resSubID" paramName="listSubID" target="_blank"> Results for submission: <bean:write name="sub" property="submitID"/> </html:link>
             	<br/>
        	</td>
        	<td align="left" width=70%>
        		Parameter values:&nbsp;
        		<logic:iterate id="params"  name="sub"  indexId="pind" property="parList">
  	           		<bean:write name="params" property="key"/>&nbsp;=&nbsp;
  	           		<logic:notEmpty name="params" property="value">
  	           		<bean:write name="params" property="value"/>
  	           		</logic:notEmpty>
  	           		<logic:empty name="params" property="value">
  	           		""
  	           		</logic:empty>
  	           		<%  int ip = pind.intValue();
            			String ipos = Integer.toString(ip);%>
  	           		<logic:notEqual name="inputdata" property="numParams" value="<%= ipos%>">,&nbsp;</logic:notEqual>
  	           		<logic:equal name="inputdata" property="numParams" value="<%= ipos%>">.&nbsp;</logic:equal>
  	        	</logic:iterate>
  	        	
  	        	<br/>
  	        	<logic:iterate id="zlist"  name="sub"  property="zpList">
  	        		<bean:write name="zlist" property="key"/>&nbsp;(files unzipped):<br/>
  	        		<bean:define id="fnames" name="zlist" property="value"/>
  	        		<logic:iterate id="fn"  name="fnames">
  	        			<bean:write name="fn"/>&nbsp;&nbsp;
  	        		</logic:iterate>
  	        		<br/>
  	        	</logic:iterate>
  	        	
  	    	</td>
        </tr>
        </logic:iterate>
        </logic:notEmpty>
        
        <tr><td colspan = "2">
            <br/>
            <html:form action="clearpage.do"  onsubmit="return disableFormAll();">
            <html:hidden property="procFlag" value="clear"/>
	        <html:submit property="clear">Clear Page</html:submit> 
            </html:form>
     	</td></tr>
        
        
    	</logic:notEmpty>
	</logic:present>
	
  
</table>

</body>
</html:html>
