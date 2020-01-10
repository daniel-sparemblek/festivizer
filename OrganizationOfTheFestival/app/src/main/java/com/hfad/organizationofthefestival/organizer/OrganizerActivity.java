package com.hfad.organizationofthefestival.organizer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.search.SearchActivity;

public class OrganizerActivity extends AppCompatActivity {

    private OrganizerController organizerController;
    private String accessToken;
    private String refreshToken;
    private String username;

    private TextView tvName;
    private TextView tvEmail;
    private TextView tvPhone;
    private ListView lvFestivals;
    private ImageView ivProfilePicture;

    private Organizer organizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_profile);

        Toolbar toolbar = findViewById(R.id.organizer_toolbar);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");


        organizerController = new OrganizerController(this, accessToken, username, refreshToken);

        organizerController.getOrganizer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.organizer_menu, menu);
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
        } else if (id == R.id.applyForFest) {
            switchActivity(ApplyFestActivity.class);
        } else if (id == R.id.myEvents) {
            switchActivity(EventsActivity.class);
        } else if (id == R.id.myJobs) {
            switchActivity(JobsActivity.class);
        } else if (id == R.id.printPass) {
            Intent intent = new Intent(this, OrganizerPrintPassActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("leader_id", organizer.getId());
            intent.putExtra("username", organizer.getUsername());
            startActivity(intent);
        } else if (id == R.id.search) {
            switchActivity(SearchActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchActivity(Class<?> destination) {
        Intent intent = new Intent(this, destination);
        intent.putExtra("accessToken", accessToken);
        intent.putExtra("refreshToken", refreshToken);
        intent.putExtra("username", username);
        this.startActivity(intent);
    }

    public void fillInActivity(Organizer organizer) {
        this.organizer = organizer;
        tvName = findViewById(R.id.orgName);
        tvPhone = findViewById(R.id.orgPhone);
        tvEmail = findViewById(R.id.orgEmail);
        ivProfilePicture = findViewById(R.id.profile_picture);

        setProfilePicture(organizer.getPicture());
        tvName.setText(organizer.getUsername());
        tvPhone.setText(organizer.getPhone());
        tvEmail.setText(organizer.getEmail());

        lvFestivals = findViewById(R.id.orgFestivalList);
        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, organizer.getFestivals());
        lvFestivals.setAdapter(specializationArrayAdapter);
    }

    private void setProfilePicture(String picture) {
        byte[] pictureBytes = Base64.decode(picture, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(pictureBytes, 0, pictureBytes.length);
        ivProfilePicture.setImageBitmap(bitmap);
    }
}
