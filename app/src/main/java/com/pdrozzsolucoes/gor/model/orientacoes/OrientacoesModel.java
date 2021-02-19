package com.pdrozzsolucoes.gor.model.orientacoes;

import android.os.Parcel;
import android.os.Parcelable;

public class OrientacoesModel implements Parcelable {

    private String nomeAnimal="",nomeProprietario="",data="",endereco="",hora="",tipoAnimal="",obs="";
    private String numeroOcorrencia="";
    private String nomeCompletoProprietario="",dataNascimento="",cpf="";
    private boolean nutricao1=false,nutricao2=false,nutricao3=false,nutricao4=false,nutricao5=false,nutricao6=false;
    private boolean sanidade1=false,sanidade2=false,sanidade3=false,sanidade4=false;
    private boolean conforto1=false,conforto2=false,conforto3=false,conforto4=false,conforto5=false,conforto6=false;
    private boolean comportamento1=false,comportamento2=false,comportamento3=false,comportamento4=false,comportamento5=false;
    private boolean proprietarioAssinou=true;
    private String assinaturaResponsavel="",assinaturaOperacao="";

    public static final String CAO="Cão";
    public static final String GATO="Gato";
    public static final String EQUINO="Equino";



    public OrientacoesModel() {
    }


    protected OrientacoesModel(Parcel in) {
        nomeAnimal = in.readString();
        nomeProprietario = in.readString();
        data = in.readString();
        endereco = in.readString();
        hora = in.readString();
        tipoAnimal = in.readString();
        obs = in.readString();
        numeroOcorrencia = in.readString();
        nomeCompletoProprietario = in.readString();
        dataNascimento = in.readString();
        cpf = in.readString();
        nutricao1 = in.readByte() != 0;
        nutricao2 = in.readByte() != 0;
        nutricao3 = in.readByte() != 0;
        nutricao4 = in.readByte() != 0;
        nutricao5 = in.readByte() != 0;
        nutricao6 = in.readByte() != 0;
        sanidade1 = in.readByte() != 0;
        sanidade2 = in.readByte() != 0;
        sanidade3 = in.readByte() != 0;
        sanidade4 = in.readByte() != 0;
        conforto1 = in.readByte() != 0;
        conforto2 = in.readByte() != 0;
        conforto3 = in.readByte() != 0;
        conforto4 = in.readByte() != 0;
        conforto5 = in.readByte() != 0;
        conforto6 = in.readByte() != 0;
        comportamento1 = in.readByte() != 0;
        comportamento2 = in.readByte() != 0;
        comportamento3 = in.readByte() != 0;
        comportamento4 = in.readByte() != 0;
        comportamento5 = in.readByte() != 0;
        proprietarioAssinou = in.readByte() != 0;
        assinaturaResponsavel = in.readString();
        assinaturaOperacao = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nomeAnimal);
        dest.writeString(nomeProprietario);
        dest.writeString(data);
        dest.writeString(endereco);
        dest.writeString(hora);
        dest.writeString(tipoAnimal);
        dest.writeString(obs);
        dest.writeString(numeroOcorrencia);
        dest.writeString(nomeCompletoProprietario);
        dest.writeString(dataNascimento);
        dest.writeString(cpf);
        dest.writeByte((byte) (nutricao1 ? 1 : 0));
        dest.writeByte((byte) (nutricao2 ? 1 : 0));
        dest.writeByte((byte) (nutricao3 ? 1 : 0));
        dest.writeByte((byte) (nutricao4 ? 1 : 0));
        dest.writeByte((byte) (nutricao5 ? 1 : 0));
        dest.writeByte((byte) (nutricao6 ? 1 : 0));
        dest.writeByte((byte) (sanidade1 ? 1 : 0));
        dest.writeByte((byte) (sanidade2 ? 1 : 0));
        dest.writeByte((byte) (sanidade3 ? 1 : 0));
        dest.writeByte((byte) (sanidade4 ? 1 : 0));
        dest.writeByte((byte) (conforto1 ? 1 : 0));
        dest.writeByte((byte) (conforto2 ? 1 : 0));
        dest.writeByte((byte) (conforto3 ? 1 : 0));
        dest.writeByte((byte) (conforto4 ? 1 : 0));
        dest.writeByte((byte) (conforto5 ? 1 : 0));
        dest.writeByte((byte) (conforto6 ? 1 : 0));
        dest.writeByte((byte) (comportamento1 ? 1 : 0));
        dest.writeByte((byte) (comportamento2 ? 1 : 0));
        dest.writeByte((byte) (comportamento3 ? 1 : 0));
        dest.writeByte((byte) (comportamento4 ? 1 : 0));
        dest.writeByte((byte) (comportamento5 ? 1 : 0));
        dest.writeByte((byte) (proprietarioAssinou ? 1 : 0));
        dest.writeString(assinaturaResponsavel);
        dest.writeString(assinaturaOperacao);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrientacoesModel> CREATOR = new Creator<OrientacoesModel>() {
        @Override
        public OrientacoesModel createFromParcel(Parcel in) {
            return new OrientacoesModel(in);
        }

        @Override
        public OrientacoesModel[] newArray(int size) {
            return new OrientacoesModel[size];
        }
    };

    public String getNomeAnimal() {
        return nomeAnimal;
    }


    public String getNumeroOcorrencia() {
        return numeroOcorrencia;
    }

    public void setNumeroOcorrencia(String numeroOcorrencia) {
        this.numeroOcorrencia = numeroOcorrencia;
    }

    public void setNomeAnimal(String nomeAnimal) {
        this.nomeAnimal = nomeAnimal;
    }

    public String getNomeProprietario() {
        return nomeProprietario;
    }

    public void setNomeProprietario(String nomeProprietario) {
        this.nomeProprietario = nomeProprietario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTipoAnimal() {
        return tipoAnimal;
    }

    public void setTipoAnimal(String tipoAnimal) {
        this.tipoAnimal = tipoAnimal;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public boolean isNutricao1() {
        return nutricao1;
    }

    public void setNutricao1(boolean nutricao1) {
        this.nutricao1 = nutricao1;
    }

    public boolean isNutricao2() {
        return nutricao2;
    }

    public void setNutricao2(boolean nutricao2) {
        this.nutricao2 = nutricao2;
    }

    public boolean isNutricao3() {
        return nutricao3;
    }

    public String getAssinaturaResponsavel() {
        return assinaturaResponsavel;
    }

    public void setAssinaturaResponsavel(String assinaturaResponsavel) {
        this.assinaturaResponsavel = assinaturaResponsavel;
    }

    public String getAssinaturaOperacao() {
        return assinaturaOperacao;
    }

    public void setAssinaturaOperacao(String assinaturaOperacao) {
        this.assinaturaOperacao = assinaturaOperacao;
    }

    public void setNutricao3(boolean nutricao3) {
        this.nutricao3 = nutricao3;
    }

    public boolean isNutricao4() {
        return nutricao4;
    }

    public void setNutricao4(boolean nutricao4) {
        this.nutricao4 = nutricao4;
    }

    public boolean isNutricao5() {
        return nutricao5;
    }

    public void setNutricao5(boolean nutricao5) {
        this.nutricao5 = nutricao5;
    }

    public boolean isNutricao6() {
        return nutricao6;
    }

    public void setNutricao6(boolean nutricao6) {
        this.nutricao6 = nutricao6;
    }

    public boolean isSanidade1() {
        return sanidade1;
    }

    public void setSanidade1(boolean sanidade1) {
        this.sanidade1 = sanidade1;
    }

    public boolean isSanidade2() {
        return sanidade2;
    }

    public void setSanidade2(boolean sanidade2) {
        this.sanidade2 = sanidade2;
    }

    public boolean isSanidade3() {
        return sanidade3;
    }

    public void setSanidade3(boolean sanidade3) {
        this.sanidade3 = sanidade3;
    }

    public boolean isSanidade4() {
        return sanidade4;
    }

    public void setSanidade4(boolean sanidade4) {
        this.sanidade4 = sanidade4;
    }

    public boolean isConforto1() {
        return conforto1;
    }

    public String getNomeCompletoProprietario() {
        return nomeCompletoProprietario;
    }

    public void setNomeCompletoProprietario(String nomeCompletoProprietario) {
        this.nomeCompletoProprietario = nomeCompletoProprietario;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public boolean isProprietarioAssinou() {
        return proprietarioAssinou;
    }

    public void setProprietarioAssinou(boolean proprietarioAssinou) {
        this.proprietarioAssinou = proprietarioAssinou;
    }

    public void setConforto1(boolean conforto1) {
        this.conforto1 = conforto1;
    }

    public boolean isConforto2() {
        return conforto2;
    }

    public void setConforto2(boolean conforto2) {
        this.conforto2 = conforto2;
    }

    public boolean isConforto3() {
        return conforto3;
    }

    public void setConforto3(boolean conforto3) {
        this.conforto3 = conforto3;
    }

    public boolean isConforto4() {
        return conforto4;
    }

    public void setConforto4(boolean conforto4) {
        this.conforto4 = conforto4;
    }

    public boolean isConforto5() {
        return conforto5;
    }

    public void setConforto5(boolean conforto5) {
        this.conforto5 = conforto5;
    }

    public boolean isConforto6() {
        return conforto6;
    }

    public void setConforto6(boolean conforto6) {
        this.conforto6 = conforto6;
    }

    public boolean isComportamento1() {
        return comportamento1;
    }

    public void setComportamento1(boolean comportamento1) {
        this.comportamento1 = comportamento1;
    }

    public boolean isComportamento2() {
        return comportamento2;
    }

    public void setComportamento2(boolean comportamento2) {
        this.comportamento2 = comportamento2;
    }

    public boolean isComportamento3() {
        return comportamento3;
    }

    public void setComportamento3(boolean comportamento3) {
        this.comportamento3 = comportamento3;
    }

    public boolean isComportamento4() {
        return comportamento4;
    }

    public void setComportamento4(boolean comportamento4) {
        this.comportamento4 = comportamento4;
    }

    public boolean isComportamento5() {
        return comportamento5;
    }

    public void setComportamento5(boolean comportamento5) {
        this.comportamento5 = comportamento5;
    }
}
