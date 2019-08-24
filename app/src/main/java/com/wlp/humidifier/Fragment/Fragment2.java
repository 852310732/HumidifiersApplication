package com.wlp.humidifier.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;
import com.wlp.humidifier.R;

import java.util.ArrayList;
import java.util.Objects;

import static com.wlp.humidifier.MainActivity.dingshiguangbo;  //定时广播


public class Fragment2 extends Fragment {
    private byte[] commond_wlwl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment2, null);
        LoopView loopView = view.findViewById(R.id.loopView);
        Button button_sendtime = view.findViewById(R.id.quegding);
        ArrayList<String> dingshibiao = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            dingshibiao.add("定时  " + i);
        }
        dingshibiao.add("关闭定时");
        //设置原始数据
        loopView.setItems(dingshibiao);
        //设置初始位置
        loopView.setInitPosition(1);
//滚动监听
        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                if (index < 8) {
                    byte time = (byte) (index+1);
                    commond_wlwl = new byte[]{(byte) 0xEB, 0x41, 0x01, 0x00, time, 0x00, (byte) 0x90};// 定时1-8
                } else if (index == 8) {
                    commond_wlwl = new byte[]{(byte) 0xEB, 0x41, 0x00, 0x00, 0x00, 0x00, (byte) 0x90};  //关闭定时
                }
            }
        });

        button_sendtime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                Intent dingshishezhi = new Intent(dingshiguangbo);
                dingshishezhi.putExtra("dingshishezhi", commond_wlwl);
                Objects.requireNonNull(getActivity()).sendBroadcast(dingshishezhi);

            }
        });


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}

