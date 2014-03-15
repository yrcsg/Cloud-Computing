package com.servlet;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.ClasspathPropertiesFileCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.ListTablesRequest;
import com.amazonaws.services.dynamodbv2.model.ListTablesResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
public class TestDB {

	public static void main(String[] args) {
	
		final AmazonDynamoDBClient dynamoDB;
		dynamoDB = new AmazonDynamoDBClient(//在servlet中定义一个dynamo db
				new AWSCredentialsProviderChain(
						new InstanceProfileCredentialsProvider(),
						new ClasspathPropertiesFileCredentialsProvider()));

		int i=0;
		String[] tables = new String[100];
		String[] tablesfirstvedio =new String[100];
		String lastEvaluatedTableName = null;
		do {

			ListTablesRequest listTablesRequest = new ListTablesRequest()
			.withLimit(10)
			.withExclusiveStartTableName(lastEvaluatedTableName);

			ListTablesResult result = dynamoDB.listTables(listTablesRequest);
			lastEvaluatedTableName = result.getLastEvaluatedTableName();

			for (String name : result.getTableNames()) {

				tables[i]=name;
				i++;

			}
			i++;
		} while (lastEvaluatedTableName != null);
		i--;
	}

}
