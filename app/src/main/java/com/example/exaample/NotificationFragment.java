/*
 * @MMAsadovich
 * 2020/8/28
 * Created by Murodov Murodjon
 */

package com.example.exaample;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import static com.example.exaample.MainActivity.notifyTitle;

public class NotificationFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        TextView textView = view.findViewById(R.id.fragment_textView);
        textView.setText(notifyTitle);
        return view;
    }
}