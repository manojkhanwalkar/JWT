package dynamodb;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.ArrayList;
import java.util.List;

public class UserCreator {

    // create table , add a couple of records , validate userid and password .


    static String table_name = "JWTUser";

    public static void createTable()
    {



        List<AttributeDefinition> definitions = new ArrayList<>();
        definitions.add(new AttributeDefinition(
                "userid", ScalarAttributeType.S));
       /* definitions.add(new AttributeDefinition(
                "Identity", ScalarAttributeType.S)); */

        CreateTableRequest request = new CreateTableRequest()
                .withAttributeDefinitions(definitions)
                .withKeySchema(new KeySchemaElement("userid", KeyType.HASH))
                .withProvisionedThroughput(new ProvisionedThroughput(
                        new Long(10), new Long(10)))
                .withTableName(table_name);

        final AmazonDynamoDB ddb = getHandle();




        try {
            CreateTableResult result = ddb.createTable(request);
            System.out.println(result);
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            System.exit(1);
        }
        System.out.println("Done!");
    }



    public static void deleteTable() {

        final AmazonDynamoDB ddb = getHandle();


        DeleteTableResult result = ddb.deleteTable(table_name);

        System.out.println(result);
    }

    public UserCreator() {

        ddb = getHandle();
        mapper = new DynamoDBMapper(ddb);
    }

    DynamoDBMapper mapper;

    AmazonDynamoDB ddb;

    static AmazonDynamoDB getHandle()
    {
        AmazonDynamoDB ddb = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "us-west-2"))
                .build();

        return ddb;

    }


    public void insertUser(String userid,String password)
    {


        UserRecord userRecord = new UserRecord();

        userRecord.setUserid(userid);
        userRecord.setPassword(password);



        mapper.save(userRecord);

    }


    public static void main(String[] args) {

      //  createTable();

        UserCreator creator = new UserCreator();

    /*    for (int i=0;i<10;i++)
        {
            creator.insertUser("user"+i, "password"+i);
        }*/

        for (int i=0;i<=10;i++)
        {
           System.out.println( "================>>>" + creator.validate("user"+i, "password"+i));
        }

    }



    public  boolean validate(String userid,String password)
    {


        UserRecord userRecord = mapper.load(UserRecord.class,userid);

        if (userRecord!=null)
        {
            if (userRecord.getPassword().equals(password))
                return true;
        }


        return false;



    }



}
