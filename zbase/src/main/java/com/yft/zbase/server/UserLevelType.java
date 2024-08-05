package com.yft.zbase.server;

import com.yft.zbase.R;

public enum UserLevelType {
    LEVE0(0, 0, "等级0"),
    LEVE1(R.mipmap.user_vip_a, 1 , "等级1"),
    LEVE2(R.mipmap.user_vip_b, 2 , "等级2"),
    LEVE3(R.mipmap.user_vip_c, 3 , "等级3"),
    LEVE4(R.mipmap.user_vip_d, 4 , "等级4"),
    LEVE5(R.mipmap.user_vip_e, 5 , "等级5");

    private int imageId;
    private int level;
    private String levelDel;
    UserLevelType(int imageId, int level, String levelDel) {
        this.imageId = imageId;
        this.level = level;
        this.levelDel = levelDel;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLevelDel() {
        return levelDel;
    }

    public void setLevelDel(String levelDel) {
        this.levelDel = levelDel;
    }
}
