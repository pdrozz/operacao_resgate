package com.pdrozzsolucoes.gor.model.fichaTratamento


data class FichaTratamentoModel(
        var numeroFicha: String ="",
        var especie: String ="",
        var sexo: String ="",
        var equino: Boolean = false,
        var obito: Boolean = false,
        var pelagem: String ="",
        var caract: String ="",
        var causaObito: String ="",
        var dataInicio: String ="",
        var suspeitaClinica: String ="",
        var procedimentos: String ="",
        var destino: String ="",
        var outroDestino: Boolean =false,
        var dataAlta: String ="",
        var dataDestino: String ="",
        var dataObito: String ="",
        var listItem: List<MedicamentoModel> = ArrayList()
){
}
