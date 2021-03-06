package cryptoAlgo;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;

public class Rsa {
	public static String encrypt(String plainText, Key key) throws Exception {
        Cipher encryptCipher = Cipher.getInstance("RSA");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(cipherText);
    }
    
	public static String decrypt(String cipherText, Key key) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(cipherText);
        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
        return new String(decryptCipher.doFinal(bytes), StandardCharsets.UTF_8);
    }
}
