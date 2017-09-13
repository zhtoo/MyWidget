package com.hs.doubaobao.utils.log;

/**

 * Description:
 */
public enum DateFormatter {
    NORMAL("yyyy-MM-dd HH:mm"),
    DD("yyyy-MM-dd"),
    SS("yyyy-MM-dd HH:mm:ss");

    private String value;

    DateFormatter(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
