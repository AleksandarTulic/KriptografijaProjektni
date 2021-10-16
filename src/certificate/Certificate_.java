package certificate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Iterator;
import java.util.Scanner;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import cryptoAlgo.Sha;

public class Certificate_ {
	private String userName;
	private PrivateKey priKey;
	private PublicKey pubKey;
	private String resPath;
	private String homePath;
	private String sharePath;
	private X509Certificate cert;
	private SecretKey skD;
	private SecretKey skA;
	private String certPath;
	
	public Certificate_(String userName, String certPath) {
		this.userName = userName;
		this.certPath = certPath;
		resPath = System.getProperty("user.dir") + File.separator + "resources" + File.separator + "korisnici" + File.separator + userName;
		homePath = System.getProperty("user.dir") + File.separator + "home" + File.separator + userName;
		sharePath = System.getProperty("user.dir") + File.separator + "share";
	}
	
	public boolean checkCertificate() {
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			FileInputStream f = new FileInputStream(certPath);
			cert = (X509Certificate) cf.generateCertificate(f);
			
			cert.checkValidity();
			
			CertificateFactory cr = CertificateFactory.getInstance("X.509");
			FileInputStream fr = new FileInputStream(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "rootca" + File.separator + "rootca.crt");
			X509Certificate rc = (X509Certificate) cr.generateCertificate(fr);
			
			FileInputStream fList = new FileInputStream(System.getProperty("user.dir") + File.separator + "resources" + File.separator + "rootca" + File.separator + "list.pem");
			X509CRL crl = (X509CRL) cf.generateCRL(fList);
			Iterator <? extends X509CRLEntry> it = null;
			
			if (crl.getRevokedCertificates() != null ) {
				it = crl.getRevokedCertificates().iterator();
			}
			
			if ( it != null ) {
				while (it.hasNext()) {
					X509CRLEntry x509 = it.next();
					
					if ( x509.getSerialNumber().equals(cert.getSerialNumber()) ) {
						System.out.println("Certifikat je povucen.");
						return false;
					}
				}
			}
			
			/////////////////////////////////////////////////////////////////////
			// CN represents the server name protected by the SSL certificate.
			/////////////////////////////////////////////////////////////////////
			if ( cert.getIssuerDN().getName().equals(rc.getIssuerDN().getName()) ) {
				String look = cert.getSubjectDN().getName();
				String []sp = look.split(", ");
				
				int i = 0;
				for (;i<sp.length;i++) {
					boolean flagBreak = false;
					for (int j=0;j<sp[i].length()-1;j++) {
						if ( sp[i].charAt(j) == 'C' && sp[i].charAt(j+1) == 'N' ) {
							flagBreak = true;
							break;
						}
					}
					
					if ( flagBreak ) {
						break;
					}
				}
				
				String subName = sp[i].split("=")[1];
				
				if ( subName.equals(userName) ) {
					pubKey = cert.getPublicKey();
					getPrivateKey();
					getSecretKeyD();
					getSecretKeyA();
					return true;
				}
			}
		} catch (CertificateException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (CRLException e) {
			e.printStackTrace();
			return false;
		}
		
		return false;
	}
	
	public boolean checkLogIn(String password) {
		File f = new File(resPath + File.separator + "password.txt");

		if ( f.exists() ) {
			try {
				Scanner scan = new Scanner(f);
				
				String pass = "";
				if ( scan.hasNextLine() ) {
					pass = scan.nextLine();
				}
				
				scan.close();
				
				password = Sha.getSha256(password);
			
				return password.equals(pass) ? true : false;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	private void getPrivateKey() throws FileNotFoundException {
		File f = new File(resPath + File.separator + userName + ".pem");
		
		if ( !f.exists() ) {
			throw new FileNotFoundException();
		}
		
		try {
			KeyFactory factory = KeyFactory.getInstance("RSA");

		    try (FileReader keyReader = new FileReader(f);
		      PemReader pemReader = new PemReader(keyReader)) {

		        PemObject pemObject = pemReader.readPemObject();
		        byte[] content = pemObject.getContent();
		        PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content);
		        priKey = factory.generatePrivate(privKeySpec);
		    }catch (Exception e) {
		    	e.printStackTrace();
		    }
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getSecretKeyD() throws FileNotFoundException{
		File f = new File(resPath + File.separator + "enc_dec_keyD.txt");

		if ( !f.exists() ) {
			throw new FileNotFoundException();
		}
		
		try {
			Scanner scan = new Scanner(f);
			String res = "";
			if ( scan.hasNextLine() ) {
				res = scan.nextLine();
			}
			
			byte []re = Base64.getDecoder().decode(res);
			skD = new SecretKeySpec(re, 0, re.length, "DES");
			
			scan.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getSecretKeyA() throws FileNotFoundException{
		File f = new File(resPath + File.separator + "enc_dec_keyA.txt");

		if ( !f.exists() ) {
			throw new FileNotFoundException();
		}
		
		try {
			Scanner scan = new Scanner(f);
			String res = "";
			if ( scan.hasNextLine() ) {
				res = scan.nextLine();
			}
			
			byte []re = Base64.getDecoder().decode(res);
			skA = new SecretKeySpec(re, 0, re.length, "AES");
			
			scan.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public PrivateKey getPriKey() {
		return priKey;
	}

	public void setPriKey(PrivateKey priKey) {
		this.priKey = priKey;
	}

	public PublicKey getPubKey() {
		return pubKey;
	}

	public void setPubKey(PublicKey pubKey) {
		this.pubKey = pubKey;
	}

	public String getResPath() {
		return resPath;
	}

	public void setResPath(String resPath) {
		this.resPath = resPath;
	}

	public String getHomePath() {
		return homePath;
	}

	public void setHomePath(String homePath) {
		this.homePath = homePath;
	}

	public String getSharePath() {
		return sharePath;
	}

	public void setSharePath(String sharePath) {
		this.sharePath = sharePath;
	}

	public X509Certificate getCert() {
		return cert;
	}

	public void setCert(X509Certificate cert) {
		this.cert = cert;
	}

	public SecretKey getSkD() {
		return skD;
	}

	public void setSkD(SecretKey skD) {
		this.skD = skD;
	}

	public SecretKey getSkA() {
		return skA;
	}

	public void setSkA(SecretKey skA) {
		this.skA = skA;
	}
	
}
