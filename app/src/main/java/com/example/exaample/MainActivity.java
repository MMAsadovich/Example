/*
 * @MMAsadovich
 * 2020/8/28
 * Created by Murodov Murodjon
 */

package com.example.exaample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    DatabaseReference ref;
    TextView textView ;
    public static String notifyTitle = "This is notification title";
    public static boolean is_connect = false; // network status
    String[] addData = {"News","P2P","Payment"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAnalytics.getInstance(this);
        textView = findViewById(R.id.textView);
        getConnect();

        // set firstFragment
        MainFragment mainFragment = new MainFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.main_layout,mainFragment).commit();
        getData();
    }

    // Firebase change data listener fun()
    public void getData(){
        if (is_connect){
            for (final String s : addData) {
                ref = FirebaseDatabase.getInstance().getReference(s);
                ref.addValueEventListener(
                        new ValueEventListener() {
                            boolean initial = true;

                            // if change dataBase , this is working
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (initial) {
                                    // 1 marta database tekshirmaydi
                                    initial = false;
                                    return;
                                }
                                collectNewsData((Map<String, Object>) dataSnapshot.getValue(), s);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.i("Error", databaseError.toString());
                            }
                        });
            }
            }else {
                Toast.makeText(this,"Internet disconnect",Toast.LENGTH_SHORT).show();
            }
    }

    // database get last information
    private void collectNewsData(Map<String,Object> map, String s) {
        final ArrayList data = new ArrayList();
        try {
            for (Map.Entry<String, Object> entry : map.entrySet()){
                String value = (String) entry.getValue();
                data.add(value);
                Collections.sort(data);
            }
            String getValue = (String) data.get(data.size()-1);
            // only change database notification working
            notification(s,getValue);
        }catch (Exception e){
            Log.i("Error", "Sorry. Unhandled error!!!");
            Toast.makeText(MainActivity.this,"Sorry. Unhandled error (",Toast.LENGTH_SHORT).show();
        }
    }

    // Add firebase realtime database news
    public void newsButtonClick (View view ){
        getConnect();
        if (is_connect) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("News");
                    myRef.push().setValue("News " );
                }
            });
        }else {
            Toast.makeText(this,"Internet disconnect",Toast.LENGTH_SHORT).show();
        }
    }

    // Add firebase realtime database p2p
    public void p2pBtnClick(View view){
        getConnect();
        if (is_connect) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("P2P");
                    myRef.push().setValue("P2P ");
                }
            });
        }else {
            Toast.makeText(this,"Internet disconnect",Toast.LENGTH_SHORT).show();
        }
    }

    // Add firebase realtime database  payment
    public void paymentClick(View view){
        getConnect();
        if (is_connect) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Payment");
                    myRef.push().setValue("Payment ");
                }
            });
        }else {
            Toast.makeText(this,"Internet disconnect",Toast.LENGTH_SHORT).show();
        }
    }

    // add new data firebase show notification
    private void notification(String title, String message){
        // add new data firebase set secondFragment
        NotificationFragment fragment = new NotificationFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_layout,fragment).commit();
               Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                MainFragment fragment = new MainFragment();
                FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.main_layout,fragment).commit();
            }
        }, 2000); // two second again set firstFragment

        notifyTitle = title;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("a","a", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"a")
                    .setContentTitle(title)
                    .setSmallIcon(R.drawable.firebase1)
                    .setAutoCancel(true)
                    .setContentText(message + ". Success :)");
                   // .setContentIntent(pendingIntent);

            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
            managerCompat.notify(999,builder.build());
        }
    }

    //  Check network connection
    public void getConnect(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            is_connect = networkInfo != null;
    }
}

// Intent intent = new Intent(this, NotifyActivity.class);
//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);