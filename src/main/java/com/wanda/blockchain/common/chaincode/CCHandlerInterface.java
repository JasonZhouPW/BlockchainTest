package com.wanda.blockchain.common.chaincode;

import com.wanda.blockchain.common.block.BlockTrans;

import java.util.ArrayList;

/**
 * Created by Zhou peiwen on 2017/7/7.
 */
public interface CCHandlerInterface {

    void init(String userID, String chaincodeName, String chaincodeVersion) ;

    <T> boolean save(T entity,Class<T> claszz) ;

    <T> T queryByPK(String key, Class<T> clazz) ;

    <T> T queryBySecondaryKey(String key, Class<T> clazz, String propertyName) ;

    <T> ArrayList<T> queryListBySecondaryKey(String key, Class<T> clazz, String propertyName) ;

    <T> boolean deleteByPK(String key, Class<T> clazz) ;


    void start(String methodName, ArrayList<String> params) ;

    void end() ;
}
