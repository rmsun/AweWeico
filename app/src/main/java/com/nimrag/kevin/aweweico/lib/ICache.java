package com.nimrag.kevin.aweweico.lib;

/**
 * Created by kevin on 2017/3/16.
 * cache接口
 */

public interface ICache {
    IResult getData(Params urlParams);
    void setData(IResult result, Params urlParams);
}
