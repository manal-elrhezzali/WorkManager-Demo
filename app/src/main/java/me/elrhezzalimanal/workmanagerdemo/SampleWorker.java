package me.elrhezzalimanal.workmanagerdemo;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SampleWorker  extends Worker {
    private static final String TAG = "SampleWorker";

    public static final String WORK_NUMBER_KEY ="number";

    public SampleWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


//    this method gets called whenever we enqueue ou work
//    it creates a worker thread
    @NonNull
    @Override
    public Result doWork() {
        Data inputData = getInputData();// to get the Data passed
        int number = inputData.getInt(WORK_NUMBER_KEY,-1);
        if (number != -1) {
            for (int i=0; i<number; i++){
                Log.d(TAG, "doWork: i was " + i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return Result.failure();//return it if the work failed
                }
            }
//            Result.retry(); use it if you want to retry your work
        }

        return Result.success();//return it if the work was successful
    }
}
