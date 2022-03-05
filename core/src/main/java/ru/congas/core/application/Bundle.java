package ru.congas.core.application;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr_Told
 */
public class Bundle {

    final Map<String, Object> extras;
    final Class<? extends Activity> senderClass;

    public Bundle() {
        this(null);
    }

    public Bundle(@Nullable Class<? extends Activity> senderClass) {
        this.senderClass = senderClass;
        extras = new HashMap<>();
    }

    public @Nullable Class<? extends Activity> getSenderClass() {
        return senderClass;
    }

    public boolean isEmpty() {
        return extras.isEmpty();
    }

    public Bundle addExtra(String id, Object object) {
        extras.put(id, object);
        return this;
    }

    public Bundle remove(String id) {
        extras.remove(id);
        return this;
    }

    public Object getUnsafeObject(String id) {
        return extras.get(id);
    }

    public Integer getInteger(String id, Integer def) {
        try {
            return (Integer) extras.get(id);
        } catch (Exception e) {
            return def;
        }
    }

    public String getString(String id, String def) {
        try {
            return (String) extras.get(id);
        } catch (Exception e) {
            return def;
        }
    }

    public Short getShort(String id, Short def) {
        try {
            return (Short) extras.get(id);
        } catch (Exception e) {
            return def;
        }
    }
    public Long getLong(String id, Long def) {
        try {
            return (Long) extras.get(id);
        } catch (Exception e) {
            return def;
        }
    }
    public Boolean getBoolean(String id, Boolean def) {
        try {
            return (Boolean) extras.get(id);
        } catch (Exception e) {
            return def;
        }
    }
    public Character getCharacter(String id, Character def) {
        try {
            return (Character) extras.get(id);
        } catch (Exception e) {
            return def;
        }
    }

    public Object getObject(String id, Class<?> clazz, Object def) {
        try {
            return clazz.cast(extras.get(id));
        } catch (Exception e) {
            return def;
        }
    }

}
