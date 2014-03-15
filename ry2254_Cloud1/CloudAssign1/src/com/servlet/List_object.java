package com.servlet;

import java.util.ArrayList;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
public class List_object{
	String bucketName = "liguifan_video";
	
	public List_object() {
	}
	
	
	public ArrayList<String> list_objects_names(){
		ArrayList<String> temp_keys=new ArrayList<String>();
		AmazonS3 s3 = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());
		ObjectListing objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(bucketName));
		for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
			temp_keys.add(objectSummary.getKey());
		}
		return temp_keys;
	}
	
	
	public String get_bucketName(){
		return this.bucketName;
	}
	
	
	public ArrayList<String> list_belonged_objects(String host){
		
		AmazonS3 s3 = new AmazonS3Client(new ClasspathPropertiesFileCredentialsProvider());
		
		ArrayList<String> list_sons=read_from_DB(host);
		
		ObjectListing objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(bucketName));
		for (S3ObjectSummary objectSummary : objectListing.getObjectSummaries()) {
			list_sons.add(objectSummary.getKey());
		}
		return list_sons;
	}
	
	public ArrayList<String> read_from_DB(String video_host){
		
		
		ArrayList<String> host_sons=new ArrayList<String>();
		// enquire RDS with "video_host"
		
		
		//host_sons is a list of string with names of video belonged to the host video
		return host_sons;
	}
}
