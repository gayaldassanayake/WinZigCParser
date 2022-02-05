package com.cs4542.compiler.util;

import java.util.ArrayList;

public class Util {
    public static String ConvertCharListToString(ArrayList<Character> charList) {
        StringBuilder builder = new StringBuilder(charList.size());
        for(Character ch: charList) {
            builder.append(ch);
        }
        return builder.toString();
    }

    public static ArrayList<Character> ConvertStringToCharList(String str) {
        ArrayList<Character> charList = new ArrayList<>();
        for (char ch : str.toCharArray()) {
            charList.add(ch);
        }
        return charList;
    }

    public static String GetCharFromString(String str, int index) {
        return index<str.length()? str.substring(index, index+1): "";
    }
}
