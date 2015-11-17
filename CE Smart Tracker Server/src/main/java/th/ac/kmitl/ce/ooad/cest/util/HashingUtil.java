/**
 *
 * modified from https://www.owasp.org/index.php/Hashing_Java
 *
 */

package th.ac.kmitl.ce.ooad.cest.util;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingUtil
{
    public static final int ITERATION_NUMBER = 1000;
    /**
     * From a password, a number of iterations and a salt,
     * returns the corresponding digest
     * @param iterationNb int The number of iterations of the algorithm
     * @param password String The password to encrypt
     * @param salt byte[] The salt
     * @return byte[] The digested password
     * @throws NoSuchAlgorithmException If the algorithm doesn't exist
     */
    public static byte[] getHash(int iterationNb, String password, byte[] salt) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        digest.update(salt);
        byte[] input = digest.digest(password.getBytes("UTF-8"));
        for (int i = 0; i < iterationNb; i++)
        {
            digest.reset();
            input = digest.digest(input);
        }
        return input;
    }

    /**
     * From a base 64 representation, returns the corresponding byte[]
     * @param data String The base64 representation
     * @return byte[]
     * @throws IOException
     */
    public static byte[] base64ToByte(String data)
    {
        //Base64.Decoder decoder = new Base64.Decoder().
        return Base64.getDecoder().decode(data);
    }

    /**
     * From a byte[] returns a base 64 representation
     * @param data byte[]
     * @return String
     * @throws IOException
     */
    public static String byteToBase64(byte[] data)
    {
        //BASE64Encoder encoder = new BASE64Encoder();
        //return encoder.encode(data);
        return Base64.getEncoder().encodeToString(data);
    }

    public static String[] hashPassword(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        // Uses a secure Random not a simple Random
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        // Salt generation 64 bits long
        byte[] bSalt = new byte[8];
        random.nextBytes(bSalt);
        // Digest computation
        byte[] bDigest = getHash(ITERATION_NUMBER, password, bSalt);
        String sDigest = HashingUtil.byteToBase64(bDigest);
        String sSalt = HashingUtil.byteToBase64(bSalt);
        String[] results = new String[2];
        results[0] = sDigest;
        results[1] = sSalt;
        return results;
    }

    public static boolean validate(String proposedPassword, String dbPassword, String salt) throws IOException, NoSuchAlgorithmException {
        byte[] bDigest = HashingUtil.base64ToByte(dbPassword);
        byte[] bSalt = HashingUtil.base64ToByte(salt);
        byte[] proposedDigest = getHash(ITERATION_NUMBER, proposedPassword, bSalt);
        return Arrays.equals(proposedDigest, bDigest);
    }
}
