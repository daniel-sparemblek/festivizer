package com.hfad.organizationofthefestival.defaultUser;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DefaultUserController {
    private DefaultUserActivity activity;
    private DefaultUserApi api;

    public DefaultUserController(DefaultUserActivity activity) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DefaultUserApi.class);

        this.activity = activity;
    }

    public void showUserProfile(int permission, String username, String accessToken){
        switch (permission){
            case 1:
                api.getLeader(username, "Bearer " + accessToken);
        }
    }
}
