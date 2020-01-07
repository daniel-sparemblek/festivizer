package com.hfad.organizationofthefestival.leader;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.festival.Festival;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LeaderPrintPassActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private String username;
    private int leaderId;

    private Spinner spFestivalPicker;
    private Button btnGeneratePass;

    private LeaderPrintPassController leaderPrintPassController;

    private List<Festival> festivalList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leader_print_pass);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");
        leaderId = intent.getIntExtra("leader_id", 0);

        spFestivalPicker = findViewById(R.id.leader_sp_festival_picker);
        btnGeneratePass = findViewById(R.id.leader_btn_generate_pass);

        leaderPrintPassController = new LeaderPrintPassController(this, accessToken, username, refreshToken);
        leaderPrintPassController.getLeaderFestivals(leaderId);
    }


    public void fillInActivity(Festival[] body) {
        ArrayAdapter<String> festivalsArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, festivalsToStrings(body));

        spFestivalPicker.setAdapter(festivalsArrayAdapter);
    }

    public List<String> festivalsToStrings(Festival[] festivals) {
        festivalList = Arrays.asList(festivals);

        return festivalList.stream()
                .map(Festival::getName)
                .collect(Collectors.toList());
    }

    public void generatePass(View view) {
        int position = spFestivalPicker.getSelectedItemPosition();

        Festival festival = festivalList.get(position);
        createPdf("Marko", "Markić", festival);

    }

    private void createPdf(String leaderName, String leaderLastName, Festival festival) {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(40);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("PASS", 150, 70, paint);
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
}
