package com.hfad.organizationofthefestival.worker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.festival.Festival;
import com.hfad.organizationofthefestival.login.LoginActivity;
import com.hfad.organizationofthefestival.search.WorkerSearchActivity;
import com.hfad.organizationofthefestival.utility.ApplicationResponse;
import com.hfad.organizationofthefestival.utility.EventApply;
import com.hfad.organizationofthefestival.utility.JobApply;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class WorkerPrintPassActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private int workerId;
    private String username;
    private Worker worker;
    private int counter;

    private Spinner spJobPicker;
    private Button btnGeneratePass;

    private ApplicationResponse application;

    private WorkerPrintPassController workerPrintPassController;

    private WorkerClient workerClient;
    private List<JobApply> jobList;

    private ProgressDialog dialog;
    private int permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("tuuu");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.worker_print_pass);

        Toolbar toolbar = findViewById(R.id.worker_toolbar);
        toolbar.setTitle("Print Pass");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        workerId = intent.getIntExtra("worker_id", 0);
        username = intent.getStringExtra("username");
        permission = intent.getIntExtra("permission", 1);

        spJobPicker = findViewById(R.id.pick_job);
        btnGeneratePass = findViewById(R.id.genPass);

        workerPrintPassController = new WorkerPrintPassController(this, accessToken, username, refreshToken);

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        workerPrintPassController.getWorkerJobs(username);
    }


    public void fillInActivity(JobApply[] body) {
        ArrayAdapter<String> jobsArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, jobsToString(body));

        spJobPicker.setAdapter(jobsArrayAdapter);

        dialog.dismiss();
    }

    public void fillInWorker(Worker worker) {
        this.worker = worker;
    }

    public List<String> jobsToString(JobApply[] jobs) {
        jobList = Arrays.asList(jobs);

        return jobList.stream()
                .map(JobApply::getName)
                .collect(Collectors.toList());
    }

    public void fillInApplication(ApplicationResponse application, JobApply job){
        this.application = application;
        counter = application.getPeopleNumber();

        for (int i = 1; i <= counter; i++){
            System.out.println("JA SAM OVO PRINTO");
            System.out.println(worker.getFirstName());
            System.out.println(worker.getLastName());
            System.out.println(job);
            System.out.println(i);
            createPdf(worker.getFirstName(), worker.getLastName(), job, i);
        }
    }

    public void generatePass(View view) {
        System.out.println("GENERATE PASS");
        int position = spJobPicker.getSelectedItemPosition();

        JobApply job = jobList.get(position);
        System.out.println(job.getJobId() + "TUUUUUUUUUUUUUU");

        workerPrintPassController.getApplication(job);



    }

    private void createPdf(String workerFirstName, String workerLastName, JobApply job, int numOfPeople) {
        System.out.println("NULA tuuu");

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(199, 284, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        canvas.drawColor(Color.BLACK);
        Paint titlePaint = new Paint();
        titlePaint.setColor(Color.parseColor("#dfb23d"));
        titlePaint.setTextSize(25);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setFakeBoldText(true);
        canvas.drawText("WORKER PASS", 99.5F, 40, titlePaint);

        Paint infoPaint = new Paint();
        infoPaint.setColor(Color.parseColor("#dfb23d"));
        infoPaint.setTextSize(15);
        infoPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(workerFirstName, 99.5F, 80, infoPaint);
        canvas.drawText(workerLastName, 99.5F, 100, infoPaint);
        canvas.drawText(job.getName(), 99.5F, 120, infoPaint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(pictureStringToBitmap(worker.getPicture()), 100, 100, false),
                49.5f, 140, titlePaint);
        EventApply event = job.getEvent();
        Festival festival = event.getFestival();
        System.out.println("festival" + festival.getLogo());
        canvas.drawBitmap(Bitmap.createScaledBitmap(pictureStringToBitmap(festival.getLogo()), 30, 30, false),
                10, 244, titlePaint);
        canvas.drawBitmap(createQRCodeImage(worker.getUsername(), festival.getName(), job.getOrderNumber(), String.valueOf(numOfPeople)), 159, 244, titlePaint);


        document.finishPage(page);
        System.out.println("JEDAN TUUU");
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path + event.getName() + "(" + numOfPeople + ").pdf";
        File filePath = new File(targetPdf);
        System.out.println("DVA tuuu");
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error " + e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }

    public Bitmap createQRCodeImage(String username, String festival, String jobOrderNumber, String peopleOrder) {
        String usernameHash = getMd5Hash(username);
        String festivalHash = getMd5Hash(festival);
        String jobOrderNumberHash = getMd5Hash(jobOrderNumber);
        String peopleOrderHash = getMd5Hash(peopleOrder);

        QRGEncoder qrgEncoder = new QRGEncoder(usernameHash + " " + festivalHash + " " + jobOrderNumberHash + " " + peopleOrderHash,
                null, QRGContents.Type.TEXT, 30);
        try {
            return qrgEncoder.encodeAsBitmap();
        } catch (WriterException e) {
            return null;
        }
    }

    public String getMd5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String md5 = number.toString(16);

            while (md5.length() < 32)
                md5 = "0" + md5;

            return md5;
        } catch (NoSuchAlgorithmException e) {
            Log.e("MD5", e.getLocalizedMessage());
            return null;
        }
    }

    private Bitmap pictureStringToBitmap(String picture) {
        byte[] pictureBytes = Base64.decode(picture, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(pictureBytes, 0, pictureBytes.length);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
            Intent intent = new Intent(this, WorkerSearchActivity.class);
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
            workerPrintPassController.getWorker();
        }
        else if (id == R.id.logout) {
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchActivity(Class<?> destination) {
        Intent intent = new Intent(this, destination);
        intent.putExtra("worker_id", workerId);
        intent.putExtra("accessToken", accessToken);
        intent.putExtra("refreshToken", refreshToken);
        intent.putExtra("username", username);
        intent.putExtra("permission", permission);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
