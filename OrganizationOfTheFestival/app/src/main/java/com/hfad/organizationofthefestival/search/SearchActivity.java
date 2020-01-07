package com.hfad.organizationofthefestival.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.User;

import java.util.List;
import java.util.stream.Collectors;

public class SearchActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String username;

    private SearchController searchController;

    private TextView tvSearch;
    private Button btnSearch;
    private ListView searchResults;

    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        tvSearch = findViewById(R.id.tv_search);
        btnSearch = findViewById(R.id.btn_search);
        searchResults = findViewById(R.id.lv_search);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");

        searchController = new SearchController(this, accessToken, username, refreshToken);

        searchResults.setOnItemClickListener((parent, view, position, id) -> {
            //POSJETI PROFIL, NEZ DI JE TO
        });

        btnSearch.setOnClickListener(v -> {
          search();
        });
    }

    public void search() {
        String searched = tvSearch.getText().toString();
        searchController.search(searched);
    }

    public void fillResults(List<User> users) {
        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, users.stream().map(User::getUsername).collect(Collectors.toList()));
        searchResults.setAdapter(specializationArrayAdapter);

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
