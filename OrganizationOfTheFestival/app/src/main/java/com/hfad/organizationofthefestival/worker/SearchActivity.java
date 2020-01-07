package com.hfad.organizationofthefestival.worker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.utility.User;

import java.util.Arrays;
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
        setContentView(R.layout.worker_search);

        tvSearch = findViewById(R.id.worker_tv_search);
        btnSearch = findViewById(R.id.worker_btn_search);
        searchResults = findViewById(R.id.worker_lv_search);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");

        searchController = new SearchController(this, accessToken, username, refreshToken);

        searchResults.setOnItemClickListener((parent, view, position, id) -> {
            //POSJETI PROFIL, NEZ DI JE TO
        });
    }

    public void search(View view) {
        String searched = tvSearch.getText().toString();
        searchController.search(searched);
    }

    public void fillResults(User[] body) {
        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, usersToStrings(body));
        searchResults.setAdapter(specializationArrayAdapter);

    }

    public List<String> usersToStrings(User[] jobs) {
        userList = Arrays.asList(jobs);

        List<String> stringList = userList.stream()
                .map(t -> t.getUsername())
                .collect(Collectors.toList());
        return stringList;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
