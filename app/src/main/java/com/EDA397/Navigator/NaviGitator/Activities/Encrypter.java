package com.EDA397.Navigator.NaviGitator.Activities;
/**
import android.util.Base64;
import android.util.Log;
import java.security.SecureRandom;
import javax.crypto.*;
import javax.crypto.spec.*;
 **/

/**
 * Class for encrypting/decrypting user passwords. Uses AES encryption/decryption implementation
 * from http://www.developer.com/ws/android/encrypting-with-android-cryptography-api.html
 */
public class Encrypter {
    private static final String TAG = "SymmetricAlgorithmAES";

    /**
     * Encrypts the passed in String
     * @param s The password to encrypt
     * @return The encrypted password
     */
    public static String encrypt (String s){
        /**
        SecretKeySpec sks = null;
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("any data used as random seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, sr);
            sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
        } catch (Exception e) {
            Log.e(TAG, "AES secret key spec error");
        }
        // Encode the original data with AES
        byte[] encodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, sks);
            encodedBytes = c.doFinal(s.getBytes());
        } catch (Exception e) {
            Log.e(TAG, "AES encryption error");
        }
        return Base64.encodeToString(encodedBytes, Base64.DEFAULT);
         **/
        return s;
    }

    /**
     * Decrypts the passed in String
     * @param s The password to decrypt
     * @return The decrypted password
     */
    public static String decrypt (String s){
        /**
        SecretKeySpec sks = null;
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("any data used as random seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, sr);
            sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
        } catch (Exception e) {
            Log.e(TAG, "AES secret key spec error");
        }
        // Decode the encoded data with AES
        byte[] decodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, sks);
            decodedBytes = c.doFinal(Base64.decode(s, Base64.DEFAULT));
        } catch (Exception e) {
            Log.e(TAG, "AES decryption error");
        }
        return new String(decodedBytes);
        **/
        return s;
    }
}
