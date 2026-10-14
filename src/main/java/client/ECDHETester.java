/*
 * ECDHETester
 *
 * Manual end-to-end test of the encrypted-message flow. It runs an ECDH key exchange
 * with the app service (/keyexchange), sends "Hello World" AES-encrypted to
 * /eservice, and prints the decrypted reply, which should be "HELLO WORLD".
 * Needs AppApplication running on :8380.
 */
package client;

import util.Connection;

public class ECDHETester {

    /** Runs one encrypted round trip and prints the decrypted result. */
    public static void main(String[] args) throws Exception {
        Connection app = new Connection("https://localhost:8380/");
        System.out.println(app.esend("Hello World", "eservice"));
    }
}
