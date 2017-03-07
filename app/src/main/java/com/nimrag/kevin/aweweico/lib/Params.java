package com.nimrag.kevin.aweweico.lib;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kevin on 2017/2/27.
 */

public class Params implements Serializable {

    private List<String> mKeys = new ArrayList<String>();
    private HashMap<String, String> mParams = new HashMap<String, String>();
    private boolean enCodeAble = true;

    public Params() {}

    public HashMap<String, String> addParam(String k, String v) {
        if (!mKeys.contains(k)) {
            mKeys.add(k);
        }
        mParams.put(k, v);
        return mParams;
    }

    public String getParamValue(String k) {
        return mParams.get(k);
    }

    public boolean getEncodeAble() { return enCodeAble; }
    public void setEnCodeAble(boolean enCodeAble) { this.enCodeAble = enCodeAble; }

    public List<String> getKeys() { return mKeys; }
}
