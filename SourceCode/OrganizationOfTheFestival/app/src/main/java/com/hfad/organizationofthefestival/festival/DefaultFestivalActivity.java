package com.hfad.organizationofthefestival.festival;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.hfad.organizationofthefestival.R;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DefaultFestivalActivity extends AppCompatActivity {

    private String accessToken;
    private String refreshToken;
    private Long festivalId;

    private TextView tvName;
    private TextView tvDesc;
    private TextView tvStart;
    private TextView tvEnd;
    private ImageView ivLogo;

    private DefaultFestivalController controller;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.festival_profile);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        festivalId = intent.getLongExtra("id", 0);

        tvName = findViewById(R.id.festName);
        tvDesc = findViewById(R.id.desc);
        tvStart = findViewById(R.id.startTime);
        tvEnd = findViewById(R.id.endTime);
        ivLogo = findViewById(R.id.festivalLogo);

        controller = new DefaultFestivalController(this, accessToken, festivalId.toString(), refreshToken);


        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        controller.getFestivalData();

    }

    public void fillInActivity(Festival festival) {
        tvName.setText(festival.getName());
        tvDesc.setText(festival.getDesc());
        tvStart.setText(parseDateTime(festival.getStartTime())
                .truncatedTo(ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        tvEnd.setText(parseDateTime(festival.getEndTime())
                .truncatedTo(ChronoUnit.MINUTES)
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));

        setProfilePicture(festival.getLogo());

        dialog.dismiss();
    }

    public ZonedDateTime parseDateTime(String dateTime) {
        int year = Integer.parseInt(dateTime.substring(0, 4));
        int month = Integer.parseInt(dateTime.substring(5, 7));
        int day = Integer.parseInt(dateTime.substring(8, 10));
        int hour = Integer.parseInt(dateTime.substring(11, 13));
        int minute = Integer.parseInt(dateTime.substring(14, 16));
        int second = Integer.parseInt(dateTime.substring(17, 19));

        return ZonedDateTime.of(year, month, day, hour, minute, second, 0, ZoneId.systemDefault());
    }

    private void setProfilePicture(String picture) {
        byte[] pictureBytes = Base64.decode(picture, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(pictureBytes, 0, pictureBytes.length);
        ivLogo.setImageBitmap(bitmap);
    }
}
