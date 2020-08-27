package com.example.exaample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exaample.fcm.FirebaseMessageReceiver;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    DatabaseReference ref;
    TextView textView ;
    public static String notifyTitle ;
    public static boolean is_connect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        textView = findViewById(R.id.textView);
        getConnect();

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getConnect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        getConnect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getConnect();
    }

    public void getData(final String s){
        getConnect();
        if (is_connect){
        ref = FirebaseDatabase.getInstance().getReference(s);
        ref.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        collectNewsData((Map<String,Object>) dataSnapshot.getValue(),s);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.i("Error", databaseError.toString());
                    }
                });
        }else {
            Toast.makeText(this,"Internet disconnect",Toast.LENGTH_SHORT).show();
        }
    }
    private void collectNewsData(Map<String,Object> map, String s) {
        final ArrayList data = new ArrayList();
        for (Map.Entry<String, Object> entry : map.entrySet()){
            String value = (String) entry.getValue();
            data.add(value);
            Collections.sort(data);
        }
        String getValue = (String) data.get(data.size()-1);
        notification(s,getValue);
    }

    public void okButtonClick (View view ){
        getConnect();
        if (is_connect) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("News");
                    myRef.push().setValue("News " );
                    getData("News");
                }
            });
        }else {
            Toast.makeText(this,"Internet disconnect",Toast.LENGTH_SHORT).show();
        }
    }

    public void pBtnClick(View view){
        getConnect();
        if (is_connect) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    getConnect();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("P2P");
                    myRef.push().setValue("P2P ");
                    getData("P2P");
                }
            });
        }else {
            Toast.makeText(this,"Internet disconnect",Toast.LENGTH_SHORT).show();
        }
    }

    public void paymentClick(View view){
        getConnect();
        if (is_connect) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Payment");
                   // currentTime = Calendar.getInstance().getTime();
                    myRef.push().setValue("Payment ");
                    getData("Payment");
                }
            });
        }else {
            Toast.makeText(this,"Internet disconnect",Toast.LENGTH_SHORT).show();
        }
    }

    private void notification(String title, String message){
        Intent intent = new Intent(this, NotifyActivity.class);
        notifyTitle = title;
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("a","a", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"a")
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.firebase1)
                    .setAutoCancel(true)
                    .setContentText(message + ". Click me! :)")
                    .setContentIntent(pendingIntent);

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            managerCompat.notify(999,builder.build());
        }
    }
    public void getConnect(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null){
                is_connect = true;
            } else {
                is_connect = false;
            }

    }
}