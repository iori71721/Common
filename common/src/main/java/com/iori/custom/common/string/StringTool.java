package com.iori.custom.common.string;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringTool {
    public static boolean isEmpty(String checkString){
        boolean isEmpty=true;
        if(checkString != null && !checkString.isEmpty()){
            isEmpty=false;
        }
        return isEmpty;
    }

    public static String replaceValidString(String input,String invalidString){
        Pattern pattern=Pattern.compile("["+invalidString+"]");
        Matcher matcher=pattern.matcher(input);
        return matcher.replaceAll("");
    }

    public static String replaceVolleyValidStrings(String input){
        List<String> invalidStrings=new ArrayList<>(10);
//        /r
        char invalidChar=0x0d;
        String invalidString=new String(new char[]{invalidChar});
        invalidStrings.add(invalidString);

        for(String scanString:invalidStrings){
            input=replaceValidString(input,scanString);
        }
        return input;
    }

    public static String generateDelimiterString(List<String> input,String delimiter){
        if(input == null){
            return "";
        }

        StringBuilder stringBuilder=new StringBuilder(500);

        String addString="";
        synchronized (input) {
            for (int i = 0; i < input.size(); i++) {
                addString = input.get(i);
                stringBuilder.append(addString);
                if (i != input.size() - 1) {
                    stringBuilder.append(delimiter);
                }
            }
        }
        return stringBuilder.toString();
    }
}
