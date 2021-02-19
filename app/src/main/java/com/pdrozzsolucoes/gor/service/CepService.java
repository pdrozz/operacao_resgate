package com.pdrozzsolucoes.gor.service;

import com.pdrozzsolucoes.gor.model.cep.CepModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CepService {

    @GET("{cep}/json/")
    Call<CepModel> getEndereco(@Path("cep") String cep);

    @GET("{uf}/{cidade}/{rua}/json/")
    Call<List<CepModel>> getEndereco(@Path("uf") String uf,
                                     @Path("cidade") String cidade,
                                     @Path("rua") String rua);

}
