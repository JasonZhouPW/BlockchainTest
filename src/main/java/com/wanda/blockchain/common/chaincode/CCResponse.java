package com.wanda.blockchain.common.chaincode;

import java.util.HashMap;

/**
 * Created by Zhou peiwen on 2017/7/7.
 */
public class CCResponse {
    private HashMap<String,Object> res;

    public HashMap<String, Object> getRes() {
        return res;
    }

    public void setRes(HashMap<String, Object> res) {
        this.res = res;
    }

    public CCResponse(){}
}
