package me.elrhezzalimanal.workmanagerdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static me.elrhezzalimanal.workmanagerdemo.SampleWorker.WORK_NUMBER_KEY;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWorker();
    }

    private void initWorker() {

        //Data and Constraints objects are not necessary
        // you can enqueue the work without using them


        // Data object that we will pass to our worker
        Data data = new Data.Builder()
                .putInt(WORK_NUMBER_KEY, 10)
                .build();
        //define constraint for your work, like needing network connection
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
//                .setRequiresBatteryNotLow(true)
//                .setRequiresCharging(true) //battery must be charging
                .build();

        OneTimeWorkRequest downloadRequest = new OneTimeWorkRequest.Builder(SampleWorker.class)
//                .setInitialDelay(5, TimeUnit.HOURS)
                .setInputData(data)
                .setConstraints(constraints)
                .addTag("download")
                .build();

        WorkManager.getInstance(this).enqueue(downloadRequest);//enqueues the work

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                SampleWorker.class,
                1,
                TimeUnit.DAYS
        ).setInputData(data)
                .setConstraints(constraints)
                .setInitialDelay(2, TimeUnit.HOURS) //waits 2 hours and then enques your work
                .addTag("daily notification")
                .build();

//        WorkManager.getInstance(this).enqueue(periodicWorkRequest);

//        observing the state of the works that have the tag "download"
//        WorkManager.getInstance(this).getWorkInfosByTagLiveData("download").observe(this, new Observer<List<WorkInfo>>() {
//            @Override
//            public void onChanged(List<WorkInfo> workInfos) {
//                for (WorkInfo w: workInfos){
//                    Log.d(TAG, "onChanged: Work status:  "+ w.getState());
//                }
//            }
//        });

//        observing the state of a specific work by using its ID
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(downloadRequest.getId()).observe(this, new Observer<WorkInfo>() {
            @Override
            public void onChanged(WorkInfo workInfo) {
                Log.d(TAG, "onChanged: Work status " + workInfo.getState());
            }
        });

        WorkManager.getInstance(this).cancelWorkById(downloadRequest.getId());


    }
}