package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import certificate.Certificate_;
import cryptoAlgo.Crypt;
import cryptoAlgo.Rsa;
import cryptoAlgo.Sha;
import forms.MainForm;
import helpFunctions.TreeFileSystemController;

public class MainController {
	public static boolean createFile(String fileName, String text, String algo) {
		String pathTemp = MainForm.user.getHomePath() + File.separator + "temporary" + File.separator + fileName + ".txt";
		
		File f = new File(pathTemp);
		
		try {
			PrintWriter pw = new PrintWriter(f);
			
			pw.println(text);
			
			pw.close();
			
			Crypt.encryptFile(algo, new FileInputStream(pathTemp), new FileOutputStream(MainForm.user.getHomePath() + File.separator + fileName + ".txt"));
			
			Sha.printHashFileMD5(fileName + ".txt");
			Crypt.addAlgo(fileName + ".txt", algo);
			
			if ( f.exists() )
				f.delete();
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static boolean deleteFile(String file) {
		String path = MainForm.user.getHomePath() + File.separator + file;
		File f = new File(path);
		
		boolean flag = false;
		if ( f.exists() ) {
			f.delete();
			flag = true;
		}
		
		return flag;
	}
	
	public static boolean uploadFile(String pathUpload, String algo) {
		File f = new File(pathUpload);
		
		try {
			if ( f.exists() ) {
				ArrayList<String> sp = TreeFileSystemController.splitPath(pathUpload);
				String fileName = sp.get(sp.size()-1);
				
				Crypt.encryptFile(algo, new FileInputStream(pathUpload), new FileOutputStream(MainForm.user.getHomePath() + File.separator + fileName));
				
				Sha.printHashFileMD5(fileName);
				Crypt.addAlgo(fileName, algo);
			}else {
				return false;
			}
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public static boolean downloadFile(String file, String pathDown) {
		File f1 = new File(MainForm.user.getHomePath() + File.separator + file);
		File f2 = new File(pathDown);
		
		try {
			if ( !f1.exists() || !f2.exists() )
				return false;
			
			if ( Sha.checkSignatureMD5(file) ) {
				String algo = "";
				algo = Crypt.readAlgo(file);
				
				Crypt.decryptFile(algo, new FileInputStream(MainForm.user.getHomePath() + File.separator + file), new FileOutputStream(pathDown + File.separator + file));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	public static boolean shareFile(String userName, String fileName) {
		Certificate_ a = new Certificate_(userName, "resources" + File.separator + "korisnici" + File.separator + userName + File.separator + userName + ".crt");
		
		if ( a.checkCertificate() ) {
			if ( copyFile(MainForm.user.getHomePath() + File.separator + fileName, "share" + File.separator + a.getUserName() + "_" + MainForm.user.getUserName() + File.separator + fileName) ) {
				Sha.printHashFile512("share" + File.separator + a.getUserName() + "_" + MainForm.user.getUserName() + File.separator + "res_" + fileName,
						"share" + File.separator + a.getUserName() + "_" + MainForm.user.getUserName() + File.separator + fileName);
				
				String alg = Crypt.readAlgo(fileName);
				String key = "";
				
				if ( alg.equals("DES") ) {
					key = Base64.getEncoder().encodeToString(MainForm.user.getSkD().getEncoded());
				}else {
					key = Base64.getEncoder().encodeToString(MainForm.user.getSkA().getEncoded());
				}
				
				try {
					File f = new File("share" + File.separator + a.getUserName() + "_" + MainForm.user.getUserName() + File.separator + "res_" + fileName);
					String RsaKey = Rsa.encrypt(key, a.getPubKey()).toString();
					FileWriter fileWritter = new FileWriter(f.getAbsolutePath(), true);
			        BufferedWriter bw = new BufferedWriter(fileWritter);
			        bw.write(alg + "\n");
			        bw.write(RsaKey);
			        bw.close();
			        
			        return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		return false;
	}
	
	private static boolean copyFile(String src, String dest) {
		File f1 = new File(src);
		File f2 = new File(dest);
		
		if ( !f2.exists() ) {
			ArrayList <String> arr = TreeFileSystemController.splitPath(dest);
			
			try {
				File f = new File("share" + File.separator + arr.get(1));
				
				f.mkdir();
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if ( f1.exists() ) {
			InputStream is = null;
			OutputStream os = null;
			
			try {
				is = new FileInputStream(f1);
				os = new FileOutputStream(f2);
				
				byte []buffer = new byte[1024];
				int length;
				
				while ( (length = is.read(buffer)) > 0 ) {
					os.write(buffer, 0, length);
				}
				
				is.close();
				os.close();
				
				return true;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	@SuppressWarnings("resource")
	public static boolean uploadSent(String fileName, String sharePath) {
		try {
			String algo = "";
			SecretKey key = null;
			String hash = "";
			
			File f = new File(sharePath + File.separator + "res_" + fileName);
			
			if ( f.exists() ) {
				Scanner scan = new Scanner(f);
				
				int br = 0;
				while ( scan.hasNextLine() ) {
					if ( br == 0 ) {
						hash = scan.nextLine();
					}else if ( br == 1 ) {
						algo = scan.nextLine();
					}else if ( br == 2 ) {
						String k = Rsa.decrypt(scan.nextLine(), MainForm.user.getPriKey());
						byte []b = Base64.getDecoder().decode(k);
						key = new SecretKeySpec(b, 0, b.length, algo);
					}
					
					br++;
				}
				
				scan.close();
				
				String hashAgain = Sha.getHashFile512(sharePath + File.separator + fileName);
				
				if ( hashAgain.equals(hash) ) {
					Crypt.decryptFile(algo, new FileInputStream(sharePath + File.separator + fileName), 
							new FileOutputStream(MainForm.user.getHomePath() + File.separator + "temporary" + File.separator + fileName), key);
					
					boolean flag = uploadFile(MainForm.user.getHomePath() + File.separator + "temporary" + File.separator + fileName, algo);
				
					deleteFile("temporary" + File.separator + fileName);
					return flag;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
