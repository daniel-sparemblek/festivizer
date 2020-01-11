package com.hfad.organizationofthefestival.worker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.login.LoginActivity;
import com.hfad.organizationofthefestival.search.SearchActivity;

import java.io.IOException;

public class WorkerActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

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

    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_profile);

        Toolbar toolbar = findViewById(R.id.worker_toolbar);
        toolbar.setTitle("My Profile");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");


        workerController = new WorkerController(this, accessToken, username, refreshToken);

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        workerController.getWorker();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.worker_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.addSpecialization) {
            Intent intent = new Intent(this, SpecializationsActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            intent.putExtra("permission", worker.getPermission());
            this.startActivity(intent);
            finish();

        } else if (id == R.id.applyForJob) {
            Intent intent = new Intent(this, JobOffersActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            intent.putExtra("permission", worker.getPermission());
            this.startActivity(intent);
            finish();
        } else if (id == R.id.activeJobs) {
            Intent intent = new Intent(this, ActiveJobsActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            intent.putExtra("permission", worker.getPermission());
            this.startActivity(intent);
            finish();
        } else if (id == R.id.myApplications) {
            Intent intent = new Intent(this, MyApplicationsActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            intent.putExtra("permission", worker.getPermission());
            this.startActivity(intent);
            finish();
        } else if (id == R.id.printPass) {

        } else if (id == R.id.search) {
            Intent intent = new Intent(this, SearchActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            intent.putExtra("permission", worker.getPermission());
            this.startActivity(intent);
            finish();
        } else if (id == R.id.worker_profile) {
            dialog = new ProgressDialog(this);
            dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            workerController.getWorker();
        }
        else if (id == R.id.logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void fillInActivity(Worker worker) {
        this.worker = worker;

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
                android.R.layout.simple_list_item_1, worker.getJobNames());
        lvJobs.setAdapter(jobsArrayAdapter);

        lvSpecializations = findViewById(R.id.specializationList);
        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, worker.getSpecializations());
        lvSpecializations.setAdapter(specializationArrayAdapter);

        dialog.dismiss();
    }

    private void setProfilePicture(String picture) {
        byte[] pictureBytes = Base64.decode(picture, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(pictureBytes, 0, pictureBytes.length);
        ivProfilePicture.setImageBitmap(bitmap);
    }

    public void chooseImage(View view) {
        Intent intent1 = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        this.startActivityForResult(intent1, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ivProfilePicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
