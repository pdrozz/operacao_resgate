package com.pdrozzsolucoes.gor.model.novaOcorrencia;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.pdrozzsolucoes.gor.utils.pdf.PdfUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NovaOcorrenciaModel implements Parcelable {

    private String responsavel, vonluntaio, tipoOcorrencia, origemOcorrencia, endereco,
    descricao,tipoAnimal, qualAnimal,numeroOcorrencia,gerada,finalizada,telefone;

    private String id;
    private String data;
    private String urlPdf="";

    private List<String> imagens;


    protected NovaOcorrenciaModel(Parcel in) {
        responsavel = in.readString();
        vonluntaio = in.readString();
        tipoOcorrencia = in.readString();
        origemOcorrencia = in.readString();
        endereco = in.readString();
        descricao = in.readString();
        tipoAnimal = in.readString();
        qualAnimal = in.readString();
        numeroOcorrencia = in.readString();
        gerada = in.readString();
        finalizada = in.readString();
        telefone = in.readString();
        id = in.readString();
        data = in.readString();
        urlPdf = in.readString();
        imagens = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(responsavel);
        dest.writeString(vonluntaio);
        dest.writeString(tipoOcorrencia);
        dest.writeString(origemOcorrencia);
        dest.writeString(endereco);
        dest.writeString(descricao);
        dest.writeString(tipoAnimal);
        dest.writeString(qualAnimal);
        dest.writeString(numeroOcorrencia);
        dest.writeString(gerada);
        dest.writeString(finalizada);
        dest.writeString(telefone);
        dest.writeString(id);
        dest.writeString(data);
        dest.writeString(urlPdf);
        dest.writeStringList(imagens);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NovaOcorrenciaModel> CREATOR = new Creator<NovaOcorrenciaModel>() {
        @Override
        public NovaOcorrenciaModel createFromParcel(Parcel in) {
            return new NovaOcorrenciaModel(in);
        }

        @Override
        public NovaOcorrenciaModel[] newArray(int size) {
            return new NovaOcorrenciaModel[size];
        }
    };

    public String getUrlPdf() {
        return urlPdf;
    }

    public void setUrlPdf(String urlPdf) {
        this.urlPdf = urlPdf;
    }

    public NovaOcorrenciaModel() {
        imagens=new ArrayList<>();
        id= UUID.randomUUID().toString().replace("-","");
    }

    public String getData() {
        return PdfUtils.getData();
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getVonluntaio() {
        return vonluntaio;
    }

    public void setVonluntaio(String vonluntaio) {
        this.vonluntaio = vonluntaio;
    }

    public String getTipoOcorrencia() {
        return tipoOcorrencia;
    }

    public void setTipoOcorrencia(String tipoOcorrencia) {
        this.tipoOcorrencia = tipoOcorrencia;
    }

    public String getOrigemOcorrencia() {
        return origemOcorrencia;
    }

    public void setOrigemOcorrencia(String origemOcorrencia) {
        this.origemOcorrencia = origemOcorrencia;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipoAnimal() {
        return tipoAnimal;
    }

    public void setTipoAnimal(String tipoAnimal) {
        this.tipoAnimal = tipoAnimal;
    }

    public String getQualAnimal() {
        return qualAnimal;
    }

    public void setQualAnimal(String qualAnimal) {
        this.qualAnimal = qualAnimal;
    }

    public String getNumeroOcorrencia() {
        return numeroOcorrencia;
    }

    public void setNumeroOcorrencia(String numeroOcorrencia) {
        this.numeroOcorrencia = numeroOcorrencia;
    }

    public String getGerada() {
        return gerada;
    }

    public void setGerada(String gerada) {
        this.gerada = gerada;
    }

    public String getFinalizada() {
        return finalizada;
    }

    public void setFinalizada(String finalizada) {
        this.finalizada = finalizada;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
