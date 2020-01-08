package com.hfad.organizationofthefestival.leader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.festival.creation.CreateFestivalActivity;
import com.hfad.organizationofthefestival.organizer.PendingOrganizer;
import com.hfad.organizationofthefestival.organizer.PrintPassActivity;
import com.hfad.organizationofthefestival.search.SearchActivity;

import java.util.ArrayList;

public class LeaderActivity extends AppCompatActivity {

    private TextView tvLeaderName;
    private TextView tvLeaderEmail;
    private TextView tvPhone;
    private ImageView ivProfilePicture;
    private ListView lvFestivalList;

    ArrayList<PendingOrganizer> pendingOrganizers = new ArrayList<>();
    private ListView approvalList;

    private String username;
    private String accessToken;
    private String refreshToken;

    private LeaderController controller;

    private Leader leader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_profile);

        Toolbar toolbar = findViewById(R.id.leader_toolbar);
        setSupportActionBar(toolbar);

        tvLeaderEmail = findViewById(R.id.leaderEmail);
        tvLeaderName = findViewById(R.id.leaderName);
        // change festival name to phone when activity is changed
        tvPhone = findViewById(R.id.festivalName);
        // change festival logo to profile picture when activity is changed
        ivProfilePicture = findViewById(R.id.profile_picture);

        approvalList = findViewById(R.id.festivalList);
        username = getIntent().getStringExtra("username");
        accessToken = getIntent().getStringExtra("accessToken");
        refreshToken = getIntent().getStringExtra("refreshToken");

        controller = new LeaderController(this, accessToken, username, refreshToken);

        controller.getData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.leader_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.myProfile) {
            finish();
            startActivity(getIntent());
        } else if (id == R.id.myFests) {
            Intent intent = new Intent(this, MyFestivalsActivity.class);
            intent.putExtra("leader_id", Integer.toString(leader.getId()));
            System.out.println("++++ " + leader.getId());
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            startActivity(intent);
        } else if (id == R.id.createNewFest) {
            Intent intent = new Intent(this, CreateFestivalActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            startActivity(intent);
        } else if (id == R.id.jobAuctns){
            Intent intent = new Intent(this, LeaderJobAuctionsActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            startActivity(intent);
        } else if (id == R.id.search) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            startActivity(intent);
        }
        else if(id == R.id.printPass) {
            Intent intent = new Intent(this, LeaderPrintPassActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("leader_id", leader.getId());
            intent.putExtra("username", leader.getUsername());
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void fillInActivity(Leader leader) {
        this.leader = leader;
        tvLeaderName.setText(leader.getUsername());
        tvLeaderEmail.setText(leader.getEmail());
        setProfilePicture(leader.getPicture());
        tvPhone.setText(leader.getPhone());
        lvFestivalList = findViewById(R.id.festivalList);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, leader.getFestivalNames());
        lvFestivalList.setAdapter(arrayAdapter);

    }

    // Decisions need to be fixed when JSONs get here
    public void onClickAccept(View view) {
        final int position = approvalList.getPositionForView((LinearLayout) view.getParent());

        String organizerUsername = pendingOrganizers.get(position).getUsername();
        int festivalId = pendingOrganizers.get(position).getFestivalId();

        // LeaderController.sendDecision(username, password, organizerUsername, Integer.toString(festivalId), Decision.ACCEPT, this);
    }

    public void onClickDecline(View view) {
        final int position = approvalList.getPositionForView((LinearLayout) view.getParent());

        String organizerUsername = pendingOrganizers.get(position).getUsername();
        int festivalId = pendingOrganizers.get(position).getFestivalId();

        //  LeaderController.sendDecision(username, password,organizerUsername, Integer.toString(festivalId), Decision.DECLINE, this);

    }

    private void setProfilePicture(String picture) {
        byte[] pictureBytes = Base64.decode(picture, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(pictureBytes, 0, pictureBytes.length);
        ivProfilePicture.setImageBitmap(bitmap);
    }
}
