package com.hfad.organizationofthefestival.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.defaultUser.DefaultUserActivity;
import com.hfad.organizationofthefestival.utility.User;

import java.util.List;
import java.util.stream.Collectors;

public class SearchActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String searcherUsername;

    private SearchController searchController;

    private TextView tvSearch;
    private Button btnSearch;
    private ListView lvSearchResults;

    private List<User> userList;

    private int searcherPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        tvSearch = findViewById(R.id.tv_search);
        btnSearch = findViewById(R.id.btn_search);
        lvSearchResults = findViewById(R.id.lv_search);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        searcherUsername = intent.getStringExtra("searcherUsername");
        searcherPermission = intent.getIntExtra("searcherPermission", 0);
        searchController = new SearchController(this, accessToken, refreshToken);

        btnSearch.setOnClickListener(v -> {
            search();
        });

        lvSearchResults.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent1 = new Intent(SearchActivity.this, DefaultUserActivity.class);

            intent1.putExtra("searcherPermission", searcherPermission);
            intent1.putExtra("searcherUsername", searcherUsername);
            intent1.putExtra("permission", userList.get(position).getRole());
            intent1.putExtra("accessToken", accessToken);
            intent1.putExtra("username", lvSearchResults.getItemAtPosition(position).toString());
            startActivity(intent1);
        });
    }

    public void search() {
        String searched = tvSearch.getText().toString();
        searchController.search(searched);
    }

    public void fillResults(List<User> users) {
        userList = users;
        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, users.stream().map(User::getUsername).collect(Collectors.toList()));
        lvSearchResults.setAdapter(specializationArrayAdapter);

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
