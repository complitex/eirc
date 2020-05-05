package ru.complitex.common.util;

/**
 * @author Anatoly Ivanov
 * 28.05.2020 22:17
 */
public class Exceptions {
    public static String getCauseMessage(Exception e, boolean initial){
        if (e.getCause() != null){
            if (initial || e.getCause().getMessage() == null){
                Throwable t = e;

                while (t.getCause() != null){
                    t = t.getCause();
                }

                return t.getMessage();
            }

            return e.getMessage() + " ->  " + e.getCause().getMessage();
        }

        return e.getMessage() + "";
    }

    public static String getCauseMessage(Exception e){
        return getCauseMessage(e, false) + "";
    }
}
