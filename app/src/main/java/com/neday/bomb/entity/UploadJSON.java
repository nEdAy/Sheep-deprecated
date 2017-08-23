package com.neday.bomb.entity;

/**
 * 上传图片成功返回信息
 */
public class UploadJSON {
    private String filename;
    private String url;
    private String cdnname;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCdnname() {
        return cdnname;
    }

    public void setCdnname(String cdnname) {
        this.cdnname = cdnname;
    }
}
