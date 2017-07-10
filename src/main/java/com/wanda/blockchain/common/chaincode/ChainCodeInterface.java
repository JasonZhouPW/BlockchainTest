package com.wanda.blockchain.common.chaincode;

import java.util.ArrayList;

/**
 * Created by Zhou peiwen on 2017/7/10.
 */
public interface ChainCodeInterface {

    public void setHandler(CCHandlerInterface handler);

    public CCHandlerInterface getHandler();

    public void init();

    public CCResponse invoke(String method,ArrayList<String> param);
}
