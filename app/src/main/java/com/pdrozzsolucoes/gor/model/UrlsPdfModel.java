package com.pdrozzsolucoes.gor.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.pdrozzsolucoes.gor.utils.pdf.PdfUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UrlsPdfModel implements Parcelable {

    private String dt;
    private String end;
    private String u;
    private String uOco;
    private String uOri;
    private String n;

    public String getU() {
        return u;
    }

    public void setU(String u) {
        this.u = u;
    }

    protected UrlsPdfModel(Parcel in) {
        dt = in.readString();
        end = in.readString();
        u = in.readString();
        uOco = in.readString();
        uOri = in.readString();
        n = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dt);
        dest.writeString(end);
        dest.writeString(u);
        dest.writeString(uOco);
        dest.writeString(uOri);
        dest.writeString(n);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UrlsPdfModel> CREATOR = new Creator<UrlsPdfModel>() {
        @Override
        public UrlsPdfModel createFromParcel(Parcel in) {
            return new UrlsPdfModel(in);
        }

        @Override
        public UrlsPdfModel[] newArray(int size) {
            return new UrlsPdfModel[size];
        }
    };

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public UrlsPdfModel() {
    }


    public String getDt() {
        return PdfUtils.getData();
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getuFic() {
        return u;
    }

    public void setuFic(String uFic) {
        this.u = uFic;
    }

    public String getuOco() {
        return uOco;
    }

    public void setuOco(String uOco) {
        this.uOco = uOco;
    }

    public String getuOri() {
        return uOri;
    }

    public void setuOri(String uOri) {
        this.uOri = uOri;
    }
}
