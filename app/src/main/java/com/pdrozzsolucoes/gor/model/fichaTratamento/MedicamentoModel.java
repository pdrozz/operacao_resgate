package com.pdrozzsolucoes.gor.model.fichaTratamento;

import android.os.Parcel;
import android.os.Parcelable;

public class MedicamentoModel implements Parcelable {


    private String medicamento, dose="", via="", duração="", período="", qnt="";

    protected MedicamentoModel(Parcel in) {
        medicamento = in.readString();
        dose = in.readString();
        via = in.readString();
        duração = in.readString();
        período = in.readString();
        qnt = in.readString();
    }

    public static final Creator<MedicamentoModel> CREATOR = new Creator<MedicamentoModel>() {
        @Override
        public MedicamentoModel createFromParcel(Parcel in) {
            return new MedicamentoModel(in);
        }

        @Override
        public MedicamentoModel[] newArray(int size) {
            return new MedicamentoModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(medicamento);
        parcel.writeString(dose);
        parcel.writeString(via);
        parcel.writeString(duração);
        parcel.writeString(período);
        parcel.writeString(qnt);
    }

    public MedicamentoModel() {
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getDuração() {
        return duração;
    }

    public void setDuração(String duração) {
        this.duração = duração;
    }

    public String getPeríodo() {
        return período;
    }

    public void setPeríodo(String período) {
        this.período = período;
    }

    public String getQnt() {
        return qnt;
    }

    public void setQnt(String qnt) {
        this.qnt = qnt;
    }
}
