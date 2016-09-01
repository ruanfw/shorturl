package com.yunbei.shorturl.core.cache.parse;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yunbei.shorturl.core.cache.annotation.Hash;
import com.yunbei.shorturl.core.cache.exception.InvalidParameterException;
import com.yunbei.shorturl.core.cache.exception.MissAnnotationException;
import com.yunbei.shorturl.core.entity.VisitorLog;

public final class EveryParser {

    public static final Logger LOG = LoggerFactory.getLogger("redis-hashkey-parser");

    /**
     * key参数前缀
     */
    public static final String KEY_PARAM_PREFIX = "{";

    public static final String KEY_PARAM_PREFIX_REPLACE = "\\{";

    /**
     * key参数后缀
     */
    public static final String KEY_PARAM_SUFFIX = "}";

    public static final String KEY_PARAM_SUFFIX_REPLACE = "\\}";

    /**
     * 根据对象的Hash注解生成key
     * 
     * @param obj
     * @return
     * @throws MissAnnotationException
     * @throws SecurityException
     * @throws NoSuchFieldException
     */
    public static String genKey(Object obj) throws MissAnnotationException, NoSuchFieldException, SecurityException {

        if (null == obj) {
            return null;
        }

        Class<?> clazz = obj.getClass();

        // hashMap annotation
        Hash hashAnnotation = clazz.getAnnotation(Hash.class);
        if (null == hashAnnotation) {
            throw new MissAnnotationException(clazz.getCanonicalName(), Hash.class.getCanonicalName());
        }

        String hashKey = hashAnnotation.key();
        if (StringUtils.isBlank(hashKey)) {
            throw new InvalidParameterException("hash key can not blank");
        }
        List<String> keys = parseHashKey(hashKey);

        for (String key : keys) {
            Field field = clazz.getDeclaredField(key);
            String val = getFieldStr(obj, field);
            hashKey = hashKey.replaceAll(KEY_PARAM_PREFIX_REPLACE + key + KEY_PARAM_SUFFIX_REPLACE, val);
        }

        return hashKey;
    }

    /**
     * 根据class的hash注解以及自己传的参数来构建hashKey
     * 
     * @param <T>
     * 
     * @param obj
     * @return
     * @throws MissAnnotationException
     * @throws NoSuchFieldException
     * @throws SecurityException
     */
    public static <T> String genKey(Class<T> clazz, String... params)
            throws MissAnnotationException, NoSuchFieldException, SecurityException {

        if (null == clazz) {
            return null;
        }

        Hash hashAnnotation = clazz.getAnnotation(Hash.class);
        if (null == hashAnnotation) {
            throw new MissAnnotationException(clazz.getCanonicalName(), Hash.class.getCanonicalName());
        }

        String hashKey = hashAnnotation.key();
        if (StringUtils.isBlank(hashKey)) {
            throw new InvalidParameterException("hash key can not blank");
        }
        List<String> keys = parseHashKey(hashKey);

        for (int i = 0; i < keys.size(); i++) {
            String val = StringUtils.EMPTY;
            if (i < params.length) {
                val = params[i];
            }
            hashKey = hashKey.replaceAll(KEY_PARAM_PREFIX_REPLACE + keys.get(i) + KEY_PARAM_SUFFIX_REPLACE, val);
        }

        return hashKey;
    }

    /**
     * 根据class的hash注解生成无参数的key
     * 
     * @param <T>
     * 
     * @param obj
     * @return
     * @throws MissAnnotationException
     * @throws NoSuchFieldException
     * @throws SecurityException
     */
    public static <T> String genKey(Class<T> clazz)
            throws MissAnnotationException, NoSuchFieldException, SecurityException {

        if (null == clazz) {
            return null;
        }

        Hash hashAnnotation = clazz.getAnnotation(Hash.class);
        if (null == hashAnnotation) {
            throw new MissAnnotationException(clazz.getCanonicalName(), Hash.class.getCanonicalName());
        }

        String hashKey = hashAnnotation.key();
        if (StringUtils.isBlank(hashKey)) {
            throw new InvalidParameterException("hash key can not blank");
        }
        return hashKey;
    }

    /**
     * 正则匹配花括号里面的值 hashKey user:{}:{}
     * 
     * @param hashKey
     * @return
     */
    protected static List<String> parseHashKey(String hashKey) {

        List<String> keys = new ArrayList<String>();

        Pattern p = Pattern.compile("\\{(.| )+?\\}");
        Matcher m = p.matcher(hashKey);
        while (m.find()) {
            keys.add(m.group().replace(KEY_PARAM_PREFIX, "").replace(KEY_PARAM_SUFFIX, ""));
        }
        return keys;

    }

    /**
     * 获取属性字符串
     *
     * @param obj
     * @param field
     * @param <T>
     * @return
     */
    protected static <T> String getFieldStr(T obj, Field field) {
        String str = null;
        try {
            Object o = obj.getClass().getMethod("get" + StringUtils.capitalize(field.getName())).invoke(obj);

            if (o != null) {
                // 属性类型
                Class<?> fieldClazz = field.getType();

                if (String.class == fieldClazz) {
                    str = String.valueOf(o);
                } else if (long.class == fieldClazz || Long.class == fieldClazz) {
                    str = String.valueOf(o);
                } else if (int.class == fieldClazz || Integer.class == fieldClazz) {
                    str = String.valueOf(o);
                } else if (boolean.class == fieldClazz || Boolean.class == fieldClazz) {
                    str = String.valueOf(o);
                } else if (Date.class == fieldClazz) {
                    // 转为时间戳
                    Date d = (Date) o;
                    str = String.valueOf(d.getTime());
                } else if (double.class == fieldClazz || Double.class == fieldClazz) {
                    str = String.valueOf(o);
                } else if (float.class == fieldClazz || Float.class == fieldClazz) {
                    str = String.valueOf(o);
                }
            } else {
                str = StringUtils.EMPTY;
            }
        } catch (IllegalAccessException e) {
            LOG.error(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            LOG.error(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            LOG.error(e.getMessage(), e);
        }
        return str;
    }

    public static <T> T map2Object(Map<String, Object> params, Class<T> clazz) {

        try {

            T t = clazz.newInstance();

            for (Entry<String, Object> param : params.entrySet()) {
                try {
                    t.getClass().getMethod("set" + StringUtils.capitalize(param.getKey()), param.getValue().getClass())
                            .invoke(t, param.getValue());
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                    continue;
                }
            }

            return t;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    public static void main(String[] args) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("visitorTime", System.currentTimeMillis());
        map.put("ip", "127.0.0.1");
        map.put("shortUrlIndex", "rfw");
        map.put("realUrl", "www.baidu.com");
        VisitorLog visitorLog = (VisitorLog) EveryParser.map2Object(map, VisitorLog.class);

        System.out.println(visitorLog.toString());

        // ShortUrl shortUrl = new ShortUrl("18768184438", 1, "www.baidu.com");

        // Event event = new Event(EventType.VISITOR_LOG);
        //
        // try {
        // String hashKey = HashKeyParser.genKey(event);
        // System.out.println(hashKey);
        // } catch (Exception e) {
        // LOG.warn(e.getMessage(), e);
        // }
    }

}
