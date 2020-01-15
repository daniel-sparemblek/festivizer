package com.hfad.organizationofthefestival.search;

import android.widget.Toast;

import com.hfad.organizationofthefestival.search.SearchActivity;
import com.hfad.organizationofthefestival.utility.User;
import com.hfad.organizationofthefestival.worker.WorkerClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchController {
    private SearchActivity searchActivity;
    private SearchApi api;
    private String accessToken;
    private String refreshToken;

    public SearchController(SearchActivity searchActivity, String accessToken, String refreshToken) {
        api = new Retrofit.Builder()
                .baseUrl("https://kaogrupa.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SearchApi.class);

        this.accessToken = accessToken;
        this.searchActivity = searchActivity;
        this.refreshToken = refreshToken;
    }

    public void search(String searched) {
        HashMap<String, String> body = new HashMap<>();
        body.put("search", searched);
        Call<List<User>> call = api.searchUsers(body, "Bearer " + accessToken);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    searchActivity.fillResults(response.body());
                } else {
                    try {
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        Toast.makeText(searchActivity, errorObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(searchActivity, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(searchActivity, "Server-side or internet error on fetching user data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
