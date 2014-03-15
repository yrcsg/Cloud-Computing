package com.servlet;
import com.database.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.servlet.S3Bucket;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import javax.servlet.annotation.MultipartConfig;
//import javax.servlet.http.Part;
//import javax.servlet.annotation.WebServlet;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;


public class FileUpload extends HttpServlet {
	String buckName="ruichi";
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost( request,  response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AmazonS3 s3 = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());
		Region usWest2 = Region.getRegion(Regions.US_WEST_2);
		s3.setRegion(usWest2);
		boolean ca = (request.getParameter("info")!=null);
		InputStream in = request.getInputStream();
		String topic_name="Cloud1";

		HashMap<String, Object> params=getParams(request);
		File f2=(File)params.get("File");
		String  FileName=f2.getName();
		// the uploaded file can be accessed in the format of:"https://s3-us-west-2.amazonaws.com/"+"bucket+"/"+FileName;
		s3.putObject(new PutObjectRequest(buckName, FileName, f2).withCannedAcl(CannedAccessControlList.PublicRead));
		////////////////////////////////////////////////////////////////////////////////////////
		//////// RDS UPDATE ////////////////////////////////////////////////////////////////////////
		RDS rds=new RDS();
		String url="http://d3nwpay4uy54dj.cloudfront.net/"+FileName;
		String table="main_video1";
		try {
			rds.insertTable(table, FileName, url);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		////////////////////////////////////////////////////////////////////////////////////////
		//////// SNS FUNCTION ////////////////////////////////////////////////////////////////
		AWSCredentials credentials = new ClasspathPropertiesFileCredentialsProvider().getCredentials();
		AmazonSNSClient sns=new AmazonSNSClient(credentials);
		sns.setRegion(usWest2);
		String owner="310698506257";
		String arn="arn:aws:sns:us-west-2:310698506257:newtopic";


		String email_temp=(String)params.get("submit_email");
		String message="File "+FileName+" is uploaded in bucket "+buckName;
		String subject="Hello, there is new video uploaded";
		if(!email_temp.equals("please enter")){
		subscribe_user(sns, arn, "email", email_temp);}
		sns.publish(arn, message, subject);

		response.sendRedirect("subpage.jsp");
	}

	protected static void create_topic(AmazonSNSClient sns, String arn, String topic_name){
		CreateTopicRequest name=new CreateTopicRequest(topic_name);
		sns.createTopic(name).withTopicArn("arn:aws:sns:us-west-2:310698506257:Cloud1");
	}

	protected static void subscribe_user(AmazonSNSClient sns, String arn, String protocal, String email){

		sns.subscribe(arn, protocal, email);

	}
	protected static void subscribe_users(AmazonSNSClient sns, String arn, String protocal){
		ArrayList<String> emails=new ArrayList<String>();
		emails.add("sy2518@columbia.edu");
		emails.add("yrcbsg@gmail.com");

		for(String e:emails){
			sns.subscribe(arn, "email", e);
		}
		System.out.println("Subscribe confirmation sent");
	}

	protected static HashMap<String, Object> getParams(
			HttpServletRequest request) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		boolean isMulti = ServletFileUpload.isMultipartContent(request);
		// need these after the loop ends
		String fileName = "";
		String fileNameToLowerCase = "";
		String fieldName = null;
		String fieldValue = null;
		String fileExtension = "";
		InputStream inputStream = null;
		File tempfile = null;
		if (isMulti) {
			ServletFileUpload upload = new ServletFileUpload();
			try {
				FileItemIterator iter = upload.getItemIterator(request);
				System.out.println("File name iterator:" + iter.hasNext());
				while (iter.hasNext()) {

					FileItemStream item = iter.next();
					inputStream = item.openStream();
					System.out.println("File name INput stream:"
							+ inputStream.available());
					if (item.isFormField()) {
						// like the hidden field for the thread
						fieldName = item.getFieldName();
						fieldValue = Streams.asString(inputStream);

						System.out.println(fieldName + ":" + fieldValue);
						parameters.put(fieldName, fieldValue);

					} else {
						System.out.println("inside else");
						fileName = item.getName();
						System.out.println(fileName);
						fileNameToLowerCase = fileName.toLowerCase();
						// has the file extension
						fileExtension = fileNameToLowerCase.substring(
								fileNameToLowerCase.indexOf(".") + 1,
								fileNameToLowerCase.length());
						System.out.println("fileextension:" + fileExtension);
						System.out.println("Filename:" + fileName);
						tempfile = File.createTempFile(
								"tmp_"+fileNameToLowerCase.substring(0,
										fileNameToLowerCase.indexOf(".")),
										fileExtension);
						try (FileOutputStream out = new FileOutputStream(
								tempfile)) {
							IOUtils.copy(inputStream, out);
							System.out.println("Size:"
									+ tempfile.getTotalSpace());

						}
					}// is form field of

				}// while loop

			}// try
			catch (Exception e) {
				e.printStackTrace();
			}
		}// is multi
		parameters.put("File",tempfile);
		return parameters;
	}// methods
}
