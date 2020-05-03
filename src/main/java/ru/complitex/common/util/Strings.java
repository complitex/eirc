package ru.complitex.common.util;

/**
 * @author Anatoly Ivanov
 * 22.04.2020 21:46
 */
public class Strings {
    public static boolean equalsIgnoreCase(String s1, String s2) {
        return s1 == null && s2 == null || !(s1 == null || s2 == null) && s1.equalsIgnoreCase(s2);
    }
}
