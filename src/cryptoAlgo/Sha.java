package cryptoAlgo;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import org.bouncycastle.util.encoders.Hex;

import forms.MainForm;

public class Sha {
	public static String getSha256(String a) {
		String res = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte []hash = md.digest(a.getBytes(StandardCharsets.UTF_8));
			res = new String(Hex.encode(hash));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	public static String getHashFileMD5(String file) {
		File f = new File(MainForm.user.getHomePath() + File.separator + file);
		String res = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			FileInputStream fi = new FileInputStream(f);
			
			byte []arr = new byte[1024];
			int br = 0;
			
			while ( (br = fi.read(arr)) != -1 ) {
				md.update(arr, 0, br);
			}
			
			fi.close();
			
			byte []re = md.digest();
			
			StringBuilder sb = new StringBuilder();
			
			for (int i=0;i<re.length;i++) {
				sb.append(Integer.toString((re[i] & 0xff) + 0x100, 16).substring(1));
			}
			
			res = sb.toString();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	public static void printHashFileMD5(String file) {
		File f = new File(MainForm.user.getHomePath() + File.separator + "hash" + File.separator + file);
		
		try {
			PrintWriter pw = new PrintWriter(f);
			String res = getHashFileMD5(file);
			pw.println(res);
			pw.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean checkSignatureMD5(String file) {
		File f = new File(MainForm.user.getHomePath() + File.separator + "hash" + File.separator + file);
		
		boolean flag = false;
		
		try {
			Scanner scan = new Scanner(f);
			String res1 = "";
			String res2 = getHashFileMD5(file);
			
			if ( scan.hasNextLine() ) {
				res1 = scan.nextLine();
			}
			
			scan.close();
			
			if ( res1.equals(res2) )
				flag = true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
	public static String getHashFile512(String file) {
		File f = new File(file);
		String res = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			
			FileInputStream fi = new FileInputStream(f);
			
			byte []arr = new byte[1024];
			int br = 0;
			
			while ( (br = fi.read(arr)) != -1 ) {
				md.update(arr, 0, br);
			}
			
			fi.close();
			
			byte []re = md.digest();
			
			StringBuilder sb = new StringBuilder();
			
			for (int i=0;i<re.length;i++) {
				sb.append(Integer.toString((re[i] & 0xff) + 0x100, 16).substring(1));
			}
			
			res = sb.toString();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}
	
	public static void printHashFile512(String file1, String file2) {
		File f = new File(file1);
		
		try {
			PrintWriter pw = new PrintWriter(f);
			String res = getHashFile512(file2);
			pw.println(res);
			pw.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean checkSignature512(String file) {
		File f = new File(file);
		
		boolean flag = false;
		
		try {
			Scanner scan = new Scanner(f);
			String res1 = "";
			String res2 = getHashFile512(file);
			
			if ( scan.hasNextLine() ) {
				res1 = scan.nextLine();
			}
			
			scan.close();
			
			if ( res1.equals(res2) )
				flag = true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}
}
