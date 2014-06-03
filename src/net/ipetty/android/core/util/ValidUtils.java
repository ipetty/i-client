package net.ipetty.android.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidUtils {
	
	public static boolean isEmail(String str) {
         String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
         Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
         Matcher matcher = pattern.matcher(str);
         return matcher.matches();
     }
	 
	public boolean isNumeric(String str) {
		 Pattern pattern = Pattern.compile("[0-9]*");
		 Matcher matcher = pattern.matcher(str);
		 return matcher.matches();
	}
	 
	public static boolean isMobileNO(String str) {  
        Pattern p = Pattern  
                .compile("^((13[0-9])|(15[^4,//D])|(18[0,5-9]))//d{8}$");  
        Matcher matcher = p.matcher(str);  
        return matcher.matches();
    }  
}
