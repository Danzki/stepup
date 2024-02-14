package com.danzki;

import java.util.Date;

public class ObjectCache {
    final private Object value;
    final private Date createDateTime;

    public ObjectCache(Object value, Date createDateTime) {
        this.value = value;
        this.createDateTime = createDateTime;
    }

    public Object getValue() {
        return value;
    }

    public Boolean isLive(Long liveTime) {
        Date now = new Date();
        Date expireTime = new Date(this.createDateTime.getTime() + liveTime);
        if (now.compareTo(expireTime) > 0)  {
            return false;
        }
        return true;
    }
}
