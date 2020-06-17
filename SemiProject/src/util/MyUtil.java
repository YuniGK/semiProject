package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyUtil {
	
	public static boolean isNumber(String str) {
		Pattern pattern = Pattern.compile("[0-9]{1,9}");
		
		Matcher matcher = pattern.matcher(str);
				
		return matcher.matches();
	}

}
