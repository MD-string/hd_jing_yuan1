package com.hand.handtruck.ui.form.bean;

import java.io.Serializable;
import java.util.List;

public class WeiLanList implements Serializable {
    private List<WeiLan> mlist;

    public List<WeiLan> getMlist() {
        return mlist;
    }

    public void setMlist(List<WeiLan> mlist) {
        this.mlist = mlist;
    }
}
