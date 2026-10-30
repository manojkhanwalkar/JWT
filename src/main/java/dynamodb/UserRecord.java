/*
 * UserRecord
 *
 * DynamoDBMapper entity for a row in the "JWTUser" table: a userid hash key and a
 * password attribute.
 */
package dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "JWTUser")
public class UserRecord {

    private String userid;

    private String password;

    /** Creates a populated record. */
    public UserRecord(String userid, String password) {
        this.userid = userid;
        this.password = password;
    }

    /** No-arg constructor required by DynamoDBMapper. */
    public UserRecord() {
    }

    /** Partition key. */
    @DynamoDBHashKey(attributeName = "userid")
    public String getUserid() {
        return userid;
    }

    /** Sets the partition key. */
    public void setUserid(String userid) {
        this.userid = userid;
    }

    /** Stored password hash (see PasswordHasher). */
    @DynamoDBAttribute(attributeName = "password")
    public String getPassword() {
        return password;
    }

    /** Sets the stored password hash. */
    public void setPassword(String password) {
        this.password = password;
    }
}
