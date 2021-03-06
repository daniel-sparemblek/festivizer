package com.hfad.organizationofthefestival.organizer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.adapters.AuctionAdapter;
import com.hfad.organizationofthefestival.login.LoginActivity;
import com.hfad.organizationofthefestival.organizer.FragmentAdapters.JobsAdapter;
import com.hfad.organizationofthefestival.search.OrganizerSearchActivity;
import com.hfad.organizationofthefestival.utility.ApplicationAuction;
import com.hfad.organizationofthefestival.utility.Job;
import com.hfad.organizationofthefestival.utility.JobApply;
import com.hfad.organizationofthefestival.worker.JobProfileActivity;
import com.hfad.organizationofthefestival.worker.jobSearch.WorkerJobSearchActivity;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class JobsActivity extends AppCompatActivity {

    private JobsController jobsController;
    private String accessToken;
    private String refreshToken;
    private String username;
    private ListView lvJobs;
    private JobApply[] gotJobs = null;

    private List<ApplicationAuction> auctionedJobs;
    private List<Job> pendingJobs;
    private List<Job> activeJobs;
    private List<Job> completedJobs;
    private String permission;

    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_screen_my_jobs);

        Toolbar toolbar = findViewById(R.id.organizer_toolbar);
        toolbar.setTitle("My Jobs");
        setSupportActionBar(toolbar);

        JobsAdapter jobsAdapter = new JobsAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(jobsAdapter);
        viewPager.setOffscreenPageLimit(4);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("accessToken");
        refreshToken = intent.getStringExtra("refreshToken");
        username = intent.getStringExtra("username");
        permission = intent.getStringExtra("permission");

        jobsController = new JobsController(this, accessToken, username, refreshToken);

        dialog = new ProgressDialog(this);
        dialog.setMessage(Html.fromHtml("<big>Loading...</big>"));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        jobsController.getAuctionedJobs();

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    jobsController.getAuctionedJobs();
                }

                if (position == 1) {
                    jobsController.getActiveJobs();
                }

                if (position == 2) {
                    jobsController.getPendingJobs();
                }

                if (position == 3) {
                    jobsController.getCompletedJobs();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.organizer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.myProfile) {
            switchActivity(OrganizerActivity.class);
        } else if (id == R.id.applyForFest) {
            switchActivity(ApplyFestActivity.class);
        } else if (id == R.id.myEvents) {
            switchActivity(EventsActivity.class);
        } else if (id == R.id.myJobs) {
            // do nothing
        } else if (id == R.id.printPass) {
            switchActivity(OrganizerPrintPassActivity.class);
        } else if (id == R.id.search) {
            switchActivity(OrganizerSearchActivity.class);
        } else if (id == R.id.logout) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
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

    public void fillInAuctions(ApplicationAuction[] auctions) {
        List<ApplicationAuction> onAuctions = Arrays.stream(auctions)
                .filter(t -> parseDateTime(t.getEndTime()).isAfter(ZonedDateTime.now()))
                .collect(Collectors.toList());

        //auctionedJobs = auctionsToJobs(onAuctions.toArray(new ApplicationAuction[0]));
        lvJobs = findViewById(R.id.orgJobsList);

        //Job[] jobs = auctionedJobs.toArray(new Job[0]);

        AuctionAdapter myCustomAdapter = new AuctionAdapter(this,
                R.layout.organizer_screen_my_jobs_one_row, onAuctions, jobsController);
        lvJobs.setAdapter(myCustomAdapter);

        dialog.dismiss();
    }

    public void fillInActiveJobs(Job[] jobs) {
        activeJobs = Arrays.stream(jobs)
                .filter(t -> t.getWorkerId() != 0)
                .filter(t -> !t.isCompleted())
                .collect(Collectors.toList());

        jobs = activeJobs.toArray(new Job[0]);
        lvJobs = findViewById(R.id.orgActiveJobList);

        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, jobsToStrings(jobs));
        lvJobs.setAdapter(specializationArrayAdapter);

        lvJobs.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(JobsActivity.this, JobProfileActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            intent.putExtra("job_id", activeJobs.get(position).getId());
            startActivity(intent);
        });

        dialog.dismiss();
    }

    public void fillInPendingJobs(Job[] jobs) {
        pendingJobs = Arrays.stream(jobs)
                .filter(t -> t.getWorkerId() == 0)
                .filter(t -> !t.isCompleted())
                .collect(Collectors.toList());

        jobs = pendingJobs.toArray(new Job[0]);
        lvJobs = findViewById(R.id.orgPendingJobList);

        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, jobsToStrings(jobs));
        lvJobs.setAdapter(specializationArrayAdapter);

        lvJobs.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(JobsActivity.this, JobAuctionActivity.class);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("username", username);
            intent.putExtra("job_id", pendingJobs.get(position).getId());
            startActivity(intent);
        });

        dialog.dismiss();
    }

    public void fillInCompletedJobs(Job[] jobs) {
        completedJobs = Arrays.stream(jobs)
                .filter(t -> t.isCompleted())
                .collect(Collectors.toList());

        Job[] toArray = completedJobs.toArray(new Job[0]);
        lvJobs = findViewById(R.id.orgCompletedJobList);

        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, jobsToStrings(toArray));
        lvJobs.setAdapter(specializationArrayAdapter);

        lvJobs.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(JobsActivity.this, WorkerJobSearchActivity.class);
            Job job = completedJobs.get(position);
            intent.putExtra("accessToken", accessToken);
            intent.putExtra("refreshToken", refreshToken);
            intent.putExtra("job_id", job.getId());
            Gson gson = new Gson();
            intent.putExtra("job", gson.toJson(job));
            startActivity(intent);
        });

        dialog.dismiss();
    }

    public List<String> jobsToStrings(Job[] jobs) {
        return Arrays.asList(jobs).stream()
                .map(Job::getName)
                .collect(Collectors.toList());
    }

    public List<Job> auctionsToJobs(ApplicationAuction[] applicationAuctions) {
        return Arrays.asList(applicationAuctions).stream()
                .map(ApplicationAuction::getJob)
                .collect(Collectors.toList());
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

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    public void goToWaitingList(String jobId) {
        Intent intent = new Intent(this, WaitingListActivity.class);
        intent.putExtra("accessToken", accessToken);
        intent.putExtra("refreshToken", refreshToken);
        intent.putExtra("username", username);
        intent.putExtra("jobId", jobId);
        this.startActivity(intent);
    }
}
