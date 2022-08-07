package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

public class Translations {

    @SerializedName("en")
    private EnglishTrans mEnglishTrans;
    @SerializedName("ar")
    private ArabicTrans mArabicTrans;
    @SerializedName("ha")
    private HausaTrans mHausaTrans;

    public Translations() {
    }

    public EnglishTrans getEnglishTrans() {
        return mEnglishTrans;
    }

    public void setEnglishTrans(EnglishTrans englishTrans) {
        mEnglishTrans = englishTrans;
    }

    public ArabicTrans getArabicTrans() {
        return mArabicTrans;
    }

    public void setArabicTrans(ArabicTrans arabicTrans) {
        mArabicTrans = arabicTrans;
    }

    public HausaTrans getHausaTrans() {
        return mHausaTrans;
    }

    public void setHausaTrans(HausaTrans hausaTrans) {
        mHausaTrans = hausaTrans;
    }
}
