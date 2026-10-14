/*
 * UserCreator
 *
 * Data-access layer for the "JWTUser" DynamoDB table, which is expected on a local
 * DynamoDB at http://localhost:8000. It can create and delete the table, insert
 * users, and check a userid/password pair. Running main() seeds and checks the
 * demo users user0..user9 with passwords password0..password9.
 *
 * NOTE: passwords are stored in plaintext. That is fine for a demo but not for real
 * use. Hash them with a slow KDF such as bcrypt, scrypt, or PBKDF2.
 */
package dynamodb;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class UserCreator {

    static final String TABLE_NAME = "JWTUser";

    private static final String ENDPOINT = "http://localhost:8000";
    private static final String REGION = "us-west-2";

    private final DynamoDBMapper mapper;

    /** Connects to the local DynamoDB endpoint. */
    public UserCreator() {
        mapper = new DynamoDBMapper(getHandle());
    }

    /** Builds a DynamoDB client for the local endpoint. */
    static AmazonDynamoDB getHandle() {
        return AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(ENDPOINT, REGION))
                .build();
    }

    /** Creates the JWTUser table, keyed by "userid". */
    public static void createTable() {
        CreateTableRequest request = new CreateTableRequest()
                .withAttributeDefinitions(new AttributeDefinition("userid", ScalarAttributeType.S))
                .withKeySchema(new KeySchemaElement("userid", KeyType.HASH))
                .withProvisionedThroughput(new ProvisionedThroughput(10L, 10L))
                .withTableName(TABLE_NAME);

        try {
            System.out.println(getHandle().createTable(request));
        } catch (AmazonServiceException e) {
            System.err.println(e.getErrorMessage());
            throw e;
        }
    }

    /** Drops the JWTUser table. */
    public static void deleteTable() {
        System.out.println(getHandle().deleteTable(TABLE_NAME));
    }

    /** Inserts or overwrites a user record. */
    public void insertUser(String userid, String password) {
        mapper.save(new UserRecord(userid, password));
    }

    /**
     * Returns true if {@code userid} exists and its stored password equals
     * {@code password}. The comparison takes constant time, so response timing does
     * not reveal how much of the password matched.
     */
    public boolean validate(String userid, String password) {
        if (userid == null || password == null) {
            return false;
        }
        UserRecord userRecord = mapper.load(UserRecord.class, userid);
        return userRecord != null && userRecord.getPassword() != null
                && MessageDigest.isEqual(userRecord.getPassword().getBytes(StandardCharsets.UTF_8),
                                         password.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Dev harness. Pass "create" to create the table, "seed" to insert the demo users,
     * or nothing to check user0..user10 (user10 should fail).
     */
    public static void main(String[] args) {
        String command = args.length > 0 ? args[0] : "validate";
        if (command.equals("create")) {
            createTable();
            return;
        }

        UserCreator creator = new UserCreator();
        if (command.equals("seed")) {
            for (int i = 0; i < 10; i++) {
                creator.insertUser("user" + i, "password" + i);
            }
            return;
        }
        for (int i = 0; i <= 10; i++) {
            System.out.println("user" + i + " valid=" + creator.validate("user" + i, "password" + i));
        }
    }
}
