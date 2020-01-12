package com.hfad.organizationofthefestival.leader;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.festival.creation.CreateFestivalActivity;
import com.hfad.organizationofthefestival.search.LeaderSearchActivity;

public class LeaderActivity extends AppCompatActivity {

    private TextView tvLeaderName;
    private TextView tvLeaderEmail;
    private TextView tvPhone;
    private ImageView ivProfilePicture;
    private ListView lvFestivalList;

    private ListView approvalList;

    private String username;
    private String accessToken;
    private String refreshToken;
    private int permission;
    private int leaderId;

    private LeaderController controller;

    private Leader leader;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_profile);

        Toolbar toolbar = findViewById(R.id.leader_toolbar);
        toolbar.setTitle("My profile");
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

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

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

        if(leader.getIsPending() == 1) {
            Toast.makeText(this, "You have not yet been accepted as leader!", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(leader.getIsPending() == 2) {
            Toast.makeText(this, "Sorry, you have been revoked as leader...", Toast.LENGTH_SHORT).show();
            return true;
        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.myProfile) {
            finish();
            startActivity(getIntent());
        } else if (id == R.id.myFests) {
            switchActivity(MyFestivalsActivity.class);
        } else if (id == R.id.createNewFest) {
            switchActivity(CreateFestivalActivity.class);
        } else if (id == R.id.jobAuctns) {
            switchActivity(LeaderJobAuctionsActivity.class);
        } else if (id == R.id.search) {
            switchActivity(LeaderSearchActivity.class);
        } else if (id == R.id.printPass) {
            switchActivity(LeaderPrintPassActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchActivity(Class<?> destination) {
        Intent intent = new Intent(this, destination);
        intent.putExtra("leader_id", leaderId);
        intent.putExtra("accessToken", accessToken);
        intent.putExtra("refreshToken", refreshToken);
        intent.putExtra("username", username);
        intent.putExtra("permission", permission);
        startActivity(intent);
    }

    public void fillInActivity(Leader leader) {
        this.leader = leader;
        permission = leader.getPermission();
        leaderId = leader.getId();
        tvLeaderName.setText(leader.getUsername());
        tvLeaderEmail.setText(leader.getEmail());
        setProfilePicture(leader.getPicture());
        tvPhone.setText(leader.getPhone());
        lvFestivalList = findViewById(R.id.festivalList);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, leader.getFestivalNames());
        lvFestivalList.setAdapter(arrayAdapter);

        dialog.dismiss();
    }

    private void setProfilePicture(String picture) {
        byte[] pictureBytes = Base64.decode(picture, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(pictureBytes, 0, pictureBytes.length);
        ivProfilePicture.setImageBitmap(bitmap);
    }
}
