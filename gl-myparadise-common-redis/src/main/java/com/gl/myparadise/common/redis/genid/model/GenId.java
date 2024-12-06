package com.gl.myparadise.common.redis.genid.model;


import java.io.Serializable;

public class GenId implements Serializable {

    private static final long serialVersionUID = -6989667274770634155L;


    private Long currNo;

    private String genKey;

    private String subKey;



    public String getGenKey() {
        return genKey;
    }

    public void setGenKey(String genKey) {
        this.genKey = genKey;
    }

    public String getSubKey() {
        return subKey;
    }

    public void setSubKey(String subKey) {
        this.subKey = subKey;
    }

    public Long getCurrNo() {
        return currNo;
    }

    public void setCurrNo(Long currNo) {
        this.currNo = currNo;
    }
}
