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

import static com.wlp.humidifier.MainActivity.hengshiguangbo;


public class Fragment3 extends Fragment {
    private byte[] commond_hshs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment3, null);
        LoopView loopView_3 = view.findViewById(R.id.loopView_3);
        Button button_sendhengshi = view.findViewById(R.id.quegding_3);
        ArrayList<String> hengshibiao = new ArrayList<>();
        hengshibiao.add("恒湿 45%");
        hengshibiao.add("恒湿 55%");
        hengshibiao.add("恒湿 65%");
        hengshibiao.add("恒湿 75%");
        hengshibiao.add("恒湿 85%");
        hengshibiao.add("恒湿 95%");
        hengshibiao.add("关闭 恒湿");
        //设置原始数据
        loopView_3.setItems(hengshibiao);
        //设置初始位置
        loopView_3.setInitPosition(1);
//滚动监听
        loopView_3.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                switch (index) {
                    case 0:
                        commond_hshs = new byte[]{(byte) 0xEB, 0x01, 0x02, 0x00, 0x2d, 0x00, (byte) 0x90};// 恒湿 45  0x2d
                        break;
                    case 1:
                        commond_hshs = new byte[]{(byte) 0xEB, 0x01, 0x03, 0x00, 0x37, 0x00, (byte) 0x90};// 恒湿 55  0x2d
                        break;
                    case 2:
                        commond_hshs = new byte[]{(byte) 0xEB, 0x01, 0x04, 0x00, 0x41, 0x00, (byte) 0x90};// 恒湿 65  0x2d
                        break;
                    case 3:
                        commond_hshs = new byte[]{(byte) 0xEB, 0x01, 0x05, 0x00, 0x4b, 0x00, (byte) 0x90};// 恒湿 75  0x2d
                        break;
                    case 4:
                        commond_hshs = new byte[]{(byte) 0xEB, 0x01, 0x06, 0x00, 0x55, 0x00, (byte) 0x90};// 恒湿 85  0x2d
                        break;
                    case 5:
                        commond_hshs = new byte[]{(byte) 0xEB, 0x01, 0x07, 0x00, 0x5f, 0x00, (byte) 0x90};// 恒湿 95  0x2d
                        break;
                    case 6:
                        commond_hshs = new byte[]{(byte) 0xEB, 0x01, 0x08, 0x00, 0x00, 0x00, (byte) 0x90};// 恒湿 关闭   0x2d
                        break;
                }
            }
        });

        button_sendhengshi.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                Intent hengshishezhi = new Intent(hengshiguangbo);
                hengshishezhi.putExtra("hengshishezhi", commond_hshs);
                Objects.requireNonNull(getActivity()).sendBroadcast(hengshishezhi);

            }
        });


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}
