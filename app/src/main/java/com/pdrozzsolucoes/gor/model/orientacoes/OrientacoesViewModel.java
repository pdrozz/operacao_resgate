package com.pdrozzsolucoes.gor.model.orientacoes;

import androidx.lifecycle.ViewModel;

public class OrientacoesViewModel extends ViewModel {

    private OrientacoesModel model;

    public OrientacoesViewModel(OrientacoesModel model) {
        this.model = model;
    }

    public OrientacoesViewModel() {
        this.model=new OrientacoesModel();
    }

    public OrientacoesModel getModel() {
        return model;
    }

    public void setModel(OrientacoesModel model) {
        this.model = model;
    }
}
