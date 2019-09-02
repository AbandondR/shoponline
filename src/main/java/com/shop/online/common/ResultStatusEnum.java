package com.shop.online.common;

/**
 * @author YJH
 * @version V1.0 创建时间：2019/3/25 0025
 */
public enum ResultStatusEnum {
    ERROR("-1","内部错误"),
    SUCCESS("0","成功");

    private String status;
    private String msg;

    private ResultStatusEnum(String status, String msg)
    {
        this.status=status;
        this.msg=msg;
    }

    public String getMsg()
    {
        return this.msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
