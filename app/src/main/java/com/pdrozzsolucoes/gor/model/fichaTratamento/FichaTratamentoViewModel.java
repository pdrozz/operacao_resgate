package com.pdrozzsolucoes.gor.model.fichaTratamento;

import androidx.lifecycle.ViewModel;

public class FichaTratamentoViewModel extends ViewModel {

    private FichaTratamentoModel model;
    public final static String CAO="Cão";
    public final static String GATO="Gato";
    public final static String EQUINO="Equino";
    public final static String FEMEA="Fêmea";
    public final static String MACHO="Macho";
    public final static String RETORNO="Retornado ao local do resgate";
    public final static String DOACAO="Doação";

    public FichaTratamentoViewModel() {
        model=new FichaTratamentoModel();
    }

    public FichaTratamentoModel getModel() {
        return model;
    }

    public void setModel(FichaTratamentoModel model) {
        this.model = model;
    }
}
