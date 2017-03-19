package com.nimrag.kevin.aweweico.lib;

/**
 * Created by kevin on 2017/3/16.
 * result接口， 无论是向服务器请求数据还是从cache取数据
 */

public interface IResult {
    /**
     * 是否失效标志,写入数据时记录写入时间，取出时根据刷新间隔来判断是否有效
     */
    boolean outOfDate();
    /**
     * 数据是否来自cache
     */
    boolean fromCache();
}
