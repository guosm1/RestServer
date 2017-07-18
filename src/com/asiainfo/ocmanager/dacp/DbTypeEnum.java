package com.asiainfo.ocmanager.dacp;

/**
 * Created by YANLSH
 * Created on 2017/7/3
 */
public enum DbTypeEnum {
    HIVE("true","hive"),
    HDFS("true","hdfs"),
    SPARK("true","spark"),
    HBASE("true","hbase"),
    KAFKA("true","kafka"),
    MAPREDUCE("true","mapreduce");

    String flag;

    String dbType;

    DbTypeEnum(String flag, String dbType) {
        this.flag = flag;
        this.dbType = dbType;
    }



    public static String getDbFlagEnum(String dbType){
        for (DbTypeEnum c : DbTypeEnum.values()) {
            if (c.getDbType().equals(dbType)) {
                return c.flag;
            }
        }
        return "";
    }

    public String getDbType() {
        return dbType;
    }
}
