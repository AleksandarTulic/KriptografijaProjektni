package cryptoAlgo;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;

import forms.MainForm;

public class Des {
	public static String encryptText(String text, SecretKey sk) {
		String res="";
		
		try {
			Cipher cipher; 
		    cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, sk);
			byte[] encrypted=cipher.doFinal(text.getBytes(StandardCharsets.ISO_8859_1));
			res=new String(encrypted, StandardCharsets.ISO_8859_1);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return res;
	}
	
	public static String decryptText(String text, SecretKey sk) {
		String res="";
		
		try {
			Cipher cipher; 
		    cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, sk);
			byte[] encrypted=cipher.doFinal(text.getBytes(StandardCharsets.ISO_8859_1));
			res = new String(encrypted, StandardCharsets.ISO_8859_1);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return res;
	}
	
	public static void encryptFile(InputStream is, OutputStream os) {
		try {
			Cipher c = Cipher.getInstance("DES");
			c.init(Cipher.ENCRYPT_MODE, MainForm.user.getSkD());
			CipherInputStream cis = new CipherInputStream(is, c);
			doCopy(cis, os);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void decryptFile(InputStream is, OutputStream os) {
		try {
			Cipher c = Cipher.getInstance("DES");
			c.init(Cipher.DECRYPT_MODE, MainForm.user.getSkD());
			CipherOutputStream cos = new CipherOutputStream(os, c);
			doCopy(is, cos);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void doCopy(InputStream is, OutputStream os) {
		byte []b = new byte[64];
		int numBytes;
		
		try {
			while ((numBytes = is.read(b)) != -1) {
				os.write(b, 0, numBytes);
			}
			
			os.flush();
			os.close();
			is.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}
