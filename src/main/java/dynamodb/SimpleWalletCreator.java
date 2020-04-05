package dynamodb;



import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

/*
Single private key stored in db
 */
public class SimpleWalletCreator {

  /*  String rsaCertFile = "/home/manoj/IdeaProjects/KryptIdEcoSysDemo/src/main/resources/userrsacertificate.pem";
    String rsaPrivateFile = "/home/manoj/IdeaProjects/KryptIdEcoSysDemo/src/main/resources/userrsakey.der";



    public static void main(String[] args) {

      //  deleteTable();
        createTable();




    }



    public void function(final AmazonDynamoDB client, final String tableName, final String ttlField){

        //table created now enabling TTL
        final UpdateTimeToLiveRequest req = new UpdateTimeToLiveRequest();
        req.setTableName(tableName);

        final TimeToLiveSpecification ttlSpec = new TimeToLiveSpecification();
        ttlSpec.setAttributeName(ttlField);
        ttlSpec.setEnabled(true);

        req.withTimeToLiveSpecification(ttlSpec);

        client.updateTimeToLive(req);
    }


    public void printKeys()
    {


            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
            List<SimpleWalletRecord> scanResult = mapper.scan(SimpleWalletRecord.class, scanExpression);

            scanResult.stream().forEach(r-> System.out.println(r));



    }

    // create table SimpleWallet




    PublicKey rsaPublicKey;
    PrivateKey rsaPrivateKey;
    X509Certificate certificate;

    public SimpleWalletCreator()
    {


    }


    static    String table_name = "SimpleWallet";


    public void insertKeys()
    {

        rsaPublicKey = CryptUtil.getPublicKeyFromCertFile(rsaCertFile,"RSA");
        rsaPrivateKey = CryptUtil.getPrivateKeyDerFromFile(rsaPrivateFile,"RSA");
        certificate = CertUtil.getCertificate(rsaCertFile);

        String privateKey = CryptUtil.convertPrivateKeyToString(rsaPrivateKey);
        String publicKey = CryptUtil.convertPublicKeyToString(rsaPublicKey);

        String certStr = CertUtil.getCertAsString(certificate);



        SimpleWalletRecord simpleWalletRecord = new SimpleWalletRecord();


        simpleWalletRecord.setPrivateKey(privateKey);
        simpleWalletRecord.setPublicKey(publicKey);
        simpleWalletRecord.setCertificate(certStr);

        mapper.save(simpleWalletRecord);

    }


*/








}
