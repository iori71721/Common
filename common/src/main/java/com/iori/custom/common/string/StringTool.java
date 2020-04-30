package com.iori.custom.common.string;

public class StringTool {
    public static boolean isEmpty(String checkString){
        boolean isEmpty=true;
        if(checkString != null && !checkString.isEmpty()){
            isEmpty=false;
        }
        return isEmpty;
    }
}
