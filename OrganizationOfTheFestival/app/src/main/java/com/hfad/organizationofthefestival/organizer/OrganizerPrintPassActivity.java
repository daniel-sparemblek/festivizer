package com.hfad.organizationofthefestival.organizer;

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
import com.hfad.organizationofthefestival.search.SearchActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class OrganizerPrintPassActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private int organizerId;
    private String username;
    private Organizer organizer;

    private Spinner spFestivalPicker;
    private Button btnGeneratePass;

    private List<Festival> festivalList;
    private OrganizerPrintPassController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_screen_prt_pass);

        Toolbar toolbar = findViewById(R.id.organizer_toolbar);
        toolbar.setTitle("Print Pass");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");
        spFestivalPicker = findViewById(R.id.org_pickFestival);
        btnGeneratePass = findViewById(R.id.org_genPass);

        controller = new OrganizerPrintPassController(this, accessToken, username, refreshToken);
        controller.getOrganizer();

        btnGeneratePass.setOnClickListener(v -> generatePass(spFestivalPicker.getSelectedView()));
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
            switchActivity(OrganizerActivity.class);
        } else if (id == R.id.applyForFest) {
            switchActivity(ApplyFestActivity.class);
        } else if (id == R.id.myEvents) {
            switchActivity(EventsActivity.class);
        } else if (id == R.id.myJobs) {
            switchActivity(JobsActivity.class);
        } else if (id == R.id.printPass) {
            // do nothing
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

    public void fillInActivity(Organizer organizer, List<Festival> festivalList) {
        this.organizer = organizer;
        this.festivalList = festivalList;

        ArrayAdapter<String> festivalsArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, festivalsToStrings(festivalList));

        spFestivalPicker.setAdapter(festivalsArrayAdapter);
    }

    public void fillInOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    public List<String> festivalsToStrings(List<Festival> festivals) {
        return festivals.stream()
                .map(Festival::getName)
                .collect(Collectors.toList());
    }

    public void generatePass(View view) {
        System.out.println("jebosebe");

        int position = spFestivalPicker.getSelectedItemPosition();

        Festival festival = festivalList.get(position);
        createPdf(organizer.getFirstName(), organizer.getLastName(), festival);

    }

    private void createPdf(String leaderFirstName, String leaderLastName, Festival festival) {
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
        canvas.drawText("VIP PASS", 99.5F, 40, titlePaint);

        Paint infoPaint = new Paint();
        infoPaint.setColor(Color.parseColor("#dfb23d"));
        infoPaint.setTextSize(15);
        infoPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(leaderFirstName, 99.5F, 80, infoPaint);
        canvas.drawText(leaderLastName, 99.5F, 100, infoPaint);
        canvas.drawText(festival.getName(), 99.5F, 120, infoPaint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(pictureStringToBitmap(organizer.getPicture()), 100, 100, false),
                49.5f, 140, titlePaint);
        canvas.drawBitmap(Bitmap.createScaledBitmap(pictureStringToBitmap(festival.getLogo()), 30, 30, false),
                10, 244, titlePaint);
        canvas.drawBitmap(createQRCodeImage(organizer.getUsername(), festival.getName()), 159, 244, titlePaint);


        document.finishPage(page);

        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path + "test-2.pdf";
        File filePath = new File(targetPdf);
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

    public Bitmap createQRCodeImage(String username, String festival) {
        String usernameHash = getMd5Hash(username);
        String festivalHash = getMd5Hash(festival);

        QRGEncoder qrgEncoder = new QRGEncoder(usernameHash + " " + festivalHash,
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
}
