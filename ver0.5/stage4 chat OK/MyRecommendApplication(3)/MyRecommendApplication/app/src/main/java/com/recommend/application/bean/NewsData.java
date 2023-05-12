package com.recommend.application.bean;

public class NewsData {


    private String title;
    private String digest;
    private String docid;
    private String pc_url;
    private String m_url;
    private String imgsrc;
    private String source;
    private String time;

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getDigest() {
        return digest;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getDocid() {
        return docid;
    }

    public void setPc_url(String pc_url) {
        this.pc_url = pc_url;
    }

    public String getPc_url() {
        return pc_url;
    }

    public void setM_url(String m_url) {
        this.m_url = m_url;
    }

    public String getM_url() {
        return m_url;
    }

    public void setImgsrc(String imgsrc) {
        this.imgsrc = imgsrc;
    }

    public String getImgsrc() {
        return imgsrc;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }


    @Override
    public String toString() {
        return "NewsData{" +
                "title='" + title + '\'' +
                ", digest='" + digest + '\'' +
                ", docid='" + docid + '\'' +
                ", pc_url='" + pc_url + '\'' +
                ", m_url='" + m_url + '\'' +
                ", imgsrc='" + imgsrc + '\'' +
                ", source='" + source + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
