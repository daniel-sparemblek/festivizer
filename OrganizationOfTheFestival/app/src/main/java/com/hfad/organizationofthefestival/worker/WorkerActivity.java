package com.hfad.organizationofthefestival.worker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;

public class WorkerActivity extends AppCompatActivity {

    private WorkerController workerController;
    private String accessToken;
    private String refreshToken;
    private String username;
    private Worker worker;

    private TextView tvName;
    private TextView tvEmail;
    private TextView tvPhone;
    private ListView lvSpecializations;
    private ListView lvJobs;
    private ImageView ivProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_profile);

        Toolbar toolbar = findViewById(R.id.worker_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");


        workerController = new WorkerController(this);

        workerController.getWorker(accessToken, username, worker);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.worker_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.myEvents) {
            System.out.println("Stisnuo sam evente");
        } else if(id == R.id.myProfile) {
            System.out.println("Stisnuo sam profil");
        }

        return super.onOptionsItemSelected(item);
    }

    public void fillInActivity(Worker worker) {

        ivProfilePicture = findViewById(R.id.profile_picture);
        tvName = findViewById(R.id.workerName);
        tvPhone = findViewById(R.id.workerPhone);
        tvEmail = findViewById(R.id.workerEmail);

        tvName.setText(worker.getUsername());
        tvPhone.setText(worker.getPhone());
        tvEmail.setText(worker.getEmail());

        setProfilePicture(worker.getPicture());
        lvJobs = findViewById(R.id.jobsList);
        ArrayAdapter<String> jobsArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, worker.getJobs());
        lvJobs.setAdapter(jobsArrayAdapter);

        lvSpecializations = findViewById(R.id.specializationList);
        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, worker.getSpecializations());
        lvSpecializations.setAdapter(specializationArrayAdapter);
    }

    private void setProfilePicture(String picture){
        byte[] pictureBytes = Base64.decode(picture, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(pictureBytes, 0, pictureBytes.length);
        ivProfilePicture.setImageBitmap(bitmap);
    }
}
