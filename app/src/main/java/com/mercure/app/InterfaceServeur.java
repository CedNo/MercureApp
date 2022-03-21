package com.mercure.app;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface InterfaceServeur {
    @GET("/api/merc_user.php")
    Call<List<Utilisateur>> getUtilisateurs();
}
