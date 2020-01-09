package com.hfad.organizationofthefestival.organizer;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hfad.organizationofthefestival.R;
import com.hfad.organizationofthefestival.organizer.FragmentAdapters.JobsAdapter;
import com.hfad.organizationofthefestival.search.SearchActivity;
import com.hfad.organizationofthefestival.utility.JobApply;
import com.hfad.organizationofthefestival.utility.ApplicationAuction;

import java.util.LinkedList;

public class JobsActivity extends AppCompatActivity {

    private JobsController jobsController;
    private String accessToken;
    private String refreshToken;
    private String username;
    private ListView lvJobs;
    private JobApply[] gotJobs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_screen_my_jobs);

        Toolbar toolbar = findViewById(R.id.organizer_toolbar);
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

        jobsController = new JobsController(this, accessToken, username, refreshToken);

        jobsController.getAuctionedJobs();

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if(position == 0) { //Auction
                }

                if(position == 1) { //Active
                    if(gotJobs == null) {
                        jobsController.getActiveJobs();
                    } else {
                        fillInActiveJobs(gotJobs);
                    }

                }

                if(position == 2) { //Pending
                    if(gotJobs == null) {
                        jobsController.getPendingJobs();
                    } else {
                        fillInPendingJobs(gotJobs);
                    }
                }

                if(position == 3) { //Completed
                    if(gotJobs == null) {
                        jobsController.getCompletedJobs();
                    } else {
                        fillInCompletedJobs(gotJobs);
                    }
                }
            }
        });
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
            // do nothing
        } else if (id == R.id.printPass) {
            switchActivity(PrintPassActivity.class);
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

    public void fillInAuctions(ApplicationAuction[] jobs) {
        lvJobs = findViewById(R.id.orgJobsList);
        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, jobsController.formatAuctions(jobs));
        lvJobs.setAdapter(specializationArrayAdapter);
    }

    public void fillInActiveJobs(JobApply[] jobs) {
        lvJobs = findViewById(R.id.orgActiveJobList);
        JobApply[] newList = new JobApply[30];

        for(JobApply job : jobs) {
            System.out.println(job.getName() + " " + job.isCompleted());
            System.out.println("Worker: " + job.getWorker());
            if(!job.isCompleted() && job.getWorker() != null)
                System.out.println("I'll add this one");

        }

        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, jobsController.formatJobs(jobs));
        lvJobs.setAdapter(specializationArrayAdapter);
    }

    public void fillInPendingJobs(JobApply[] jobs) {
        lvJobs = findViewById(R.id.orgPendingJobList);

        JobApply[] newList = new JobApply[30];

        for(JobApply job : jobs) {
            System.out.println(job.getName() + " " + job.isCompleted());
            System.out.println("Worker: " + job.getWorker());
            if(!job.isCompleted() && job.getWorker() == null)
                System.out.println("I'll add this one");

        }

        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, jobsController.formatJobs(jobs));
        lvJobs.setAdapter(specializationArrayAdapter);
    }

    public void fillInCompletedJobs(JobApply[] jobs) {
        lvJobs = findViewById(R.id.orgCompletedJobList);

        LinkedList<JobApply> newList = new LinkedList<>();

        for(JobApply job : jobs) {
            if(job.isCompleted())
                newList.add(job);
        }

        JobApply[] filteredArray = new JobApply[newList.size()];
        filteredArray = newList.toArray(filteredArray);

        ArrayAdapter<String> specializationArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, jobsController.formatJobs(filteredArray));
        lvJobs.setAdapter(specializationArrayAdapter);
    }

    public void saveJobs(JobApply[] jobs) {
        gotJobs = jobs;
    }
}
