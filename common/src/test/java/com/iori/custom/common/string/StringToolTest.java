package com.iori.custom.common.string;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StringToolTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void isEmpty(){
        String checkString=null;
        boolean checkResult=StringTool.isEmpty(checkString);
        boolean correctResult=true;
        assertEquals(correctResult,checkResult);

        checkString="";
        checkResult=StringTool.isEmpty(checkString);
        correctResult=true;
        assertEquals(correctResult,checkResult);

        checkString="abcd";
        checkResult=StringTool.isEmpty(checkString);
        correctResult=false;
//        不相等便會出錯，前放預期，後放實際
        assertEquals(correctResult,checkResult);
    }

    @Test
    public void replaceValidString(){
        String checkData="aabcdaa";
        String waitCheckData=StringTool.replaceValidString(checkData,"a");
        String answer="bcd";
        boolean checkResult=waitCheckData.equals(answer);
        assertTrue(checkResult);
    }
}
