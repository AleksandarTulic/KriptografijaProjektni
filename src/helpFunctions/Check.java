package helpFunctions;

import java.io.File;

import forms.MainForm;

public class Check {
	public static boolean checkNameFile(String nameFile) {
		File f = new File(MainForm.user.getHomePath() + File.separator + nameFile + ".txt");
		if ( f.exists() ) 
			return false;
		if ( nameFile.length() == 0  )
			return false;
		if ( !Character.isAlphabetic(nameFile.charAt(0)) )
			return false;
		
		for (int i=1;i<nameFile.length();i++) {
			char slo = nameFile.charAt(i);
			
			if ( !(Character.isAlphabetic(slo) || Character.isDigit(slo)) )
				return false;
		}
		
		return true;
	}
}
