package com.example.bureauworks.core.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtil {

    public static boolean isValidEmail(String email) {
        if (IsNullUtil.isNullOrEmpty(email)) 
            return false;
        
        String emailPattern = "\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+(\\.[A-Za-z0-9-]+)*((\\.[A-Za-z0-9]{2,})|(\\.[A-Za-z0-9]{2,}\\.[A-Za-z0-9]{2,}))$)\\b";
        Pattern pattern = Pattern.compile(emailPattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }   
}
