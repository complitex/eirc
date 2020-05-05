package ru.complitex.domain.util;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Anatoly A. Ivanov
 * 30.11.2017 15:48
 */
public class Locales {
    private Integer systemLocaleId;
    private Locale systemLocale;

    private Integer altLocaleId;

    private Map<Locale, Integer> map = new ConcurrentHashMap<>();
    private Map<Integer, Locale> mapId = new ConcurrentHashMap<>();

    public static final Locale RU = new Locale("ru");
    public static final Locale UA = new Locale("uk");

    public static final Integer RU_ID = 1;
    public static final Integer UA_ID = 2;

    private static Locales instance = new Locales();

    private Map<Class, Properties> propertiesMap = new ConcurrentHashMap<>();

    public Locales() {
        map.put(RU, RU_ID);
        map.put(UA, UA_ID);

        mapId.put(RU_ID, RU);
        mapId.put(UA_ID, UA);

        systemLocale = RU;
        systemLocaleId = RU_ID;

        altLocaleId = UA_ID;
    }

    public static Locale getSystemLocale() {
        return instance.systemLocale;
    }

    public static Integer getSystemLocaleId() {
        return instance.systemLocaleId;
    }

    public static Integer getAltLocaleId() {
        return instance.altLocaleId;
    }

    public static Integer getLocaleId(Locale locale){
        return instance.map.get(locale);
    }

    public static Collection<Integer> getLocaleIds(){
        return instance.map.values();
    }

    public static Locale getLocale(Integer localeId){
        return instance.mapId.get(localeId);
    }

    public static String getLanguage(Integer localeId){
        return instance.mapId.get(localeId).getLanguage();
    }

    private static Properties getProperties(Class _class){
        Properties properties = instance.propertiesMap.get(_class);

        if (properties == null){
            properties = new Properties();

            try {
                properties.load(_class.getResourceAsStream(_class.getSimpleName() + ".properties"));

                instance.propertiesMap.put(_class, properties);
            } catch (IOException e) {
                return null;
            }
        }

        return properties;
    }

    public static String getString(Class _class, String key){
        return Objects.requireNonNull(getProperties(_class)).getProperty(key);
    }
}
