package com.nimrag.kevin.aweweico.lib;

/**
 * Created by kevin on 2017/3/16.
 * result接口， 无论是向服务器请求数据还是从cache取数据
 */

public interface IResult {
    /**
     * 是否失效
     */
    boolean outOfDate();
    /**
     * 数据是否来自cache
     */
    boolean fromCache();
}
