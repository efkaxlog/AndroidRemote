package com.bajera.xlog.rc.models;

public class Data {

    private String dataType;
    private byte[] data;

    public Data(String dataType, byte[] data) {
        this.dataType = dataType;
        this.data = data;
    }

    public String getDataType() {
        return dataType;
    }

    public byte[] getData() {
        return data;
    }

}
