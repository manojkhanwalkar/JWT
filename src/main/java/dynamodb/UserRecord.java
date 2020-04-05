package dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName="JWTUser")
public class UserRecord {

    @DynamoDBHashKey(attributeName = "userid")
    String userid;


    @DynamoDBAttribute(attributeName = "password")
    String password;


    public UserRecord(String userid, String password) {
        this.userid = userid;
        this.password = password;
    }

    public UserRecord() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


