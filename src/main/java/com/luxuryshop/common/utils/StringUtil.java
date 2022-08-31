package com.luxuryshop.common.utils;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class StringUtil {


    public static String removeAccent(String s) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(s)) return "";
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("")
                .replaceAll("đ", "d")
                .replaceAll("Đ", "D")
                .replaceAll("`", "")
                .replaceAll("´", "")
                .replaceAll("\\^", "");
    }

    public static String textToUrl(String text) {
        text = removeAccent(text);
        text = text.toLowerCase();
        text = text.replaceAll("[^a-zA-Z0-9 ]", "");
        text = text.replaceAll(" ", "-");
        return text;
    }

    //decode unicode to character
    public static String decodeUnicode(String s) {
        return StringEscapeUtils.unescapeJava(s);
    }

    public static Double getDoubleFromString(String str) {
        if (str == null) return null;
        str = "0" + str; // solve issue: replace str -> empty string
        return Double.valueOf(str.replaceAll("[^0-9.]+", "").trim());
    }

    public static Integer getIntegerFromString(String str) {
        if (str == null) return null;
        str = "0" + str;
        return Integer.valueOf(str.replaceAll("[^0-9]+", "").trim());
    }
}
