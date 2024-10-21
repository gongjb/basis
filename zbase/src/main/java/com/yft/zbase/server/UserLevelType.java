package com.yft.zbase.server;

import com.yft.zbase.R;

public enum UserLevelType {
    LEVE0(0, 0, "等级0");

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
