package com.wlp.humidifier.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wlp.humidifier.R;

import java.util.Objects;


public class Fragment1 extends Fragment {
    public static final String fragmentt = "com.wlp.fragment1";
    public static final String fragmenty = "com.wlp.fragment2";

    private TextView tpshuju;
    private TextView whshuju;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment1, null);
        tpshuju =view.findViewById(R.id.wenducanshu);
        whshuju = view.findViewById(R.id.shiducanshu);
        IntentFilter FGwd = new IntentFilter(fragmentt);
        Objects.requireNonNull(getActivity()).getApplicationContext().registerReceiver(FOG11, FGwd);


        IntentFilter FGsd = new IntentFilter(fragmenty);
        Objects.requireNonNull(getActivity()).getApplicationContext().registerReceiver(FOG12, FGsd);

        return view;
    }
    private BroadcastReceiver FOG11 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            double bb = intent.getDoubleExtra("tpcanshu",0);
            tpshuju.setText(""+bb+"℃");
        }
    };
    private BroadcastReceiver FOG12 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            double cc = intent.getDoubleExtra("whcanshu",0);
            whshuju.setText(""+cc+"%");
        }
    };


    @Override      //注销广播
    public void onDestroy() {
        super.onDestroy();
        Objects.requireNonNull(getActivity()).unregisterReceiver(FOG11);
        Objects.requireNonNull(getActivity()).unregisterReceiver(FOG12);
        }
    }







