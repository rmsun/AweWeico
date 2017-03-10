package com.nimrag.kevin.aweweico.lib.orm.extra;

/**
 * Created by sunkevin on 2017/3/10.
 * 增删改查需要的额外信息
 */

public class Extra {
    /**
     * 数据拥有者
     */
    private String owner;
    /**
     * 数据的key
     */
    private String key;

    public Extra(String owner, String key) {
        this.owner = owner;
        this.key = key;
    }

    public String getOwner() { return this.owner; }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getKey() { return this.key; }
    public void setKey(String key) {
        this.key = key;
    }
}
