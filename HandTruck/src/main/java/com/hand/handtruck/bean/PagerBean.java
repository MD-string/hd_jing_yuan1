package com.hand.handtruck.bean;

import com.hand.handtruck.ui.TransportThing.bean.TransportBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wcf on 2018-05-03.
 */

public class PagerBean implements Serializable {
    private String totalElement;
    private String totalPage;
    private String pageSize;
    private String nowPage;
    private String element;
    private List<TransportBean>  content;

    public String getTotalElement() {
        return totalElement;
    }

    public void setTotalElement(String totalElement) {
        this.totalElement = totalElement;
    }

    public String getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getNowPage() {
        return nowPage;
    }

    public void setNowPage(String nowPage) {
        this.nowPage = nowPage;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public List<TransportBean> getContent() {
        return content;
    }

    public void setContent(List<TransportBean> content) {
        this.content = content;
    }
}
