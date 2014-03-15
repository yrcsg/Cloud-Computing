<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<%@ page import="java.io.*,java.util.*, javax.servlet.*"%>
<%@ page import="com.servlet.List_object"%>    
<%@ page import="com.database.*"%>  
<%@ page import="java.sql.ResultSet"%>    

  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">


<link rel="stylesheet" href="styles/styles.css" type="text/css"
	media="screen">
<link href="http://vjs.zencdn.net/4.4/video-js.css" rel="stylesheet">
<script src="http://vjs.zencdn.net/4.4/video.js"></script>
<style type="text/css">
.vjs-default-skin .vjs-control-bar {
	font-size: 124%
}
</style>



<title>Upload your own reply</title>
<link rel="stylesheet" href="styles/styles.css" type="text/css" media="screen">
</head>

<body background="1234.jpg">

<img src="1.gif"/>
<% 		 request.getAttribute("name");
		// out.print(request.getAttribute("videoname"));
		 List_object prt = new List_object();
         String bucketName=prt.get_bucketName();
         
         String main_video_table="main_video1";%>
         


<%	 	 RDS rds=new RDS(); 
     	 ArrayList<String> request_set=rds.queryTable(main_video_table);
     	 
         
         for(int i=0;i<=request_set.size()-1;i++){
        	 %>
        	 <form>
        	 <tr><td>
        	 <video id="my_video_1" class="video-js vjs-default-skin" controls preload="auto" width="440" height="264" poster="my_video_poster.png" data-setup="{}">		
			 <source src=<%out.println(request_set.get(i));%> type='video/mp4'>
			 <source src=<%out.println(request_set.get(i));%> type='video/mp3'>  
        	 <source src=<%out.println(request_set.get(i));%> type='video/ogg'>
        	 <%
        	 out.println("</video>");
        	 out.print("</form>");
        	 out.print("<br></br>");
         }

%>
<form  ENCTYPE="multipart/form-data" ACTION="GoMain" METHOD=POST>
<b><input type="submit" value="back" style="font-size: 20px; width: 5em; height=90px; background-color: lightblue;  color: #3366FF; font-weight: bold; " size="180" />
</b>
</form>

<form  ENCTYPE="multipart/form-data" ACTION="FileUpload" METHOD=POST>
Please enter your email to get SNS:
<input name="submit_email" ID="submit_email" value="please enter">
		<br><br><br>
	  	<left>		<table border="0" >
	  	  <tr><td><b>Upload a video To Reply: </b>
  <input name="F1" type="file"/>
  <b><input type="submit" value="reply" style="font-size: 20px; width: 5em; height=90px; background-color: lightblue;  color: #3366FF; font-weight: bold; " size="180" />
</b></td></tr>
                  <!--   <tr><center><td colspan="2"><p align="center"><B>Upload Center</B><center></td></tr>
                    <tr><td><b>Choose the file To Upload:</b></td>
                    <td><INPUT NAME="F1" TYPE="file"></td></tr>
					<tr><td colspan="2"><p align="right"><INPUT TYPE="submit" VALUE="Upload Video" ></p></td></tr> -->
</table>
</left>
</form>
</body>
</html>




     
