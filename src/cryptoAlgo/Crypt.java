package cryptoAlgo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;

import forms.MainForm;

public class Crypt {
	public static void encryptFile(String algo, InputStream is, OutputStream os) {
		try {
			Cipher c = Cipher.getInstance(algo);
			if ( algo.equals("DES") ) 
				c.init(Cipher.ENCRYPT_MODE, MainForm.user.getSkD());
			else
				c.init(Cipher.ENCRYPT_MODE, MainForm.user.getSkA());
			CipherInputStream cis = new CipherInputStream(is, c);
			doCopy(cis, os);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void decryptFile(String algo, InputStream is, OutputStream os) {
		try {
			Cipher c = Cipher.getInstance(algo);
			if ( algo.equals("DES") ) 
				c.init(Cipher.DECRYPT_MODE, MainForm.user.getSkD());
			else
				c.init(Cipher.DECRYPT_MODE, MainForm.user.getSkA());
			CipherOutputStream cos = new CipherOutputStream(os, c);
			doCopy(is, cos);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void decryptFile(String algo, InputStream is, OutputStream os, SecretKey sk) {
		try {
			Cipher c = Cipher.getInstance(algo);
			if ( algo.equals("DES") ) 
				c.init(Cipher.DECRYPT_MODE, sk);
			else
				c.init(Cipher.DECRYPT_MODE, sk);
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
	
	public static void addAlgo(String file, String algo) {
		File f = new File(MainForm.user.getHomePath() + File.separator + "hash" + File.separator + file);
	
		try {
			FileWriter fileWritter = new FileWriter(f.getAbsolutePath(), true);
	        BufferedWriter bw = new BufferedWriter(fileWritter);
	        bw.write(algo);
	        bw.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String readAlgo(String file) {
		String algo = "";
		int br = 0;
		
		File f = new File(MainForm.user.getHomePath() + File.separator + "hash" + File.separator + file);
		
		try {
			if ( f.exists() ) {
				Scanner scan = new Scanner(f);
				
				while ( scan.hasNextLine() ) {
					br++;
					String buff = scan.nextLine();
					
					if ( br == 2 ) {
						algo = buff;
					}
				}
				
				scan.close();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return algo;
	}
}
