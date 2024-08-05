package com.yft.zbase.bean;

import java.util.ArrayList;
import java.util.List;

public class KVBean {
    public static final String FAIL = "fail";
    public boolean isSel;
    public String key = "";
    public Object value;
    public Object value2;

    public static List<KVBean> getTestData3() {
        ArrayList<KVBean> arrayList = new ArrayList<>();
        KVBean kvBean = new KVBean();
        kvBean.key = "https://bbs.qt99.cc/upload/banner01_img.png";
        KVBean kvBean1 = new KVBean();
        kvBean1.key = "https://bbs.qt99.cc/upload/banner01_img.png";
        KVBean kvBean2 = new KVBean();
        kvBean2.key = "https://bbs.qt99.cc/upload/banner01_img.png";
        arrayList.add(kvBean);
        arrayList.add(kvBean1);
        arrayList.add(kvBean2);
        return arrayList;
    }

    public static List<KVBean> getTestData4() {
        /**
         * https://bbs.qt99.cc/upload/special01.png
         * https://bbs.qt99.cc/upload/special02.png
         * https://bbs.qt99.cc/upload/special03.png
         */
        ArrayList<KVBean> arrayList = new ArrayList<>();
        KVBean kvBean = new KVBean();
        kvBean.key = "https://bbs.qt99.cc/upload/special01.png";
        KVBean kvBean1 = new KVBean();
        kvBean1.key = "https://img.zcool.cn/community/01616a5ba0a7b7a8012099c8213eb9.jpg@1280w_1l_2o_100sh.jpg";
        KVBean kvBean2 = new KVBean();
        kvBean2.key = "https://img2.baidu.com/it/u=370611461,775275673&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=889";
        arrayList.add(kvBean);
        arrayList.add(kvBean1);
        arrayList.add(kvBean2);
        return arrayList;
    }

    public static List<KVBean> getTestData5() {
        ArrayList<KVBean> arrayList = new ArrayList<>();
        KVBean kvBean = new KVBean();
        kvBean.key = "https://bbs.qt99.cc/upload/special01.png";
        KVBean kvBean1 = new KVBean();
        kvBean1.key = "https://bbs.qt99.cc/upload/special02.png";
        KVBean kvBean2 = new KVBean();
        kvBean2.key = "https://bbs.qt99.cc/upload/special03.png";
        arrayList.add(kvBean);
        arrayList.add(kvBean1);
        arrayList.add(kvBean2);
        return arrayList;
    }

    public static List<KVBean> getTestData6() {
        ArrayList<KVBean> arrayList = new ArrayList<>();
        KVBean kvBean = new KVBean();
        kvBean.key = "https://bbs.qt99.cc/upload/special04.jpg";
        KVBean kvBean1 = new KVBean();
        kvBean1.key = "https://bbs.qt99.cc/upload/special05.jpg";
        KVBean kvBean2 = new KVBean();
        kvBean2.key = "https://bbs.qt99.cc/upload/special06.jpg";
        arrayList.add(kvBean);
        arrayList.add(kvBean1);
        arrayList.add(kvBean2);
        return arrayList;
    }
}
