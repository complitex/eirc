package ru.complitex.common.util;

import java.util.UUID;

/**
 * @author Anatoly Ivanov
 * 13.07.2020 22:50
 */
public class Ids {
    public static String randomUUID(){
        return UUID.randomUUID().toString();
    }
}
