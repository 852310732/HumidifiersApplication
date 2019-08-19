package com.wlp.humidifier;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    byte[] commond_sd;
    byte[] commind_ws;
    byte[] commond_wl;
    byte[] commond_ds;
    byte[] endcommond, opencommond, commond_js;

    public static final String action = "com.example.broadcast";
    public static final String actionshuju = "com.example.shuju.broadcast";
    public static final String actionend = "com.example.end.broadcast";
    private static final String TAG = "ble_tag";
    private String[] strs = null;
    public String str = "-1";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.addActivity(this);//每启动一个Activity，就将其加入到activity列表中
        EventBus.getDefault().register(this);
        android.support.v7.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Button sd = (Button) findViewById(R.id.sd);
        Button wl = (Button) findViewById(R.id.wl);
        Button dingshi = (Button) findViewById(R.id.dingshi);
        TextView shidu = findViewById(R.id.shidu);
        TextView wendu = findViewById(R.id.wendu);
        Spinner spinner_shidu = (Spinner) findViewById(R.id.spinner_shidu);
        Spinner spinner_wuliang = (Spinner) findViewById(R.id.spinner_wuliang);
        Spinner spinner_dingshi = (Spinner) findViewById(R.id.spinner_dingshi);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.nav_add:   //添加设备
                        Intent intent = new Intent(MainActivity.this, SearchBtActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_exit:   //退出
                        ActivityCollector.finishAllActivity();
                        break;
                    case R.id.nav_open:  //打开加湿器
                        str = "k";
                        putShow();
                        break;
                    case R.id.nav_close:  //关闭加湿器
                        str = "o";
                        putShow();
                        break;
                }
                mDrawerLayout.closeDrawers();
                return false;
            }
        });


        /*************************  下拉菜单选项 湿度  ****************************************/

        //原始string数组
        final String[] spinnerItems = {"湿度 45%", "湿度 55%", "湿度 65%", "湿度 75%", "湿度 85%", "湿度 95%"};
        //简单的string数组适配器：样式res，数组
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_spinner_item, spinnerItems);
        //下拉的样式res
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner_shidu.setAdapter(spinnerAdapter);
        //选择监听
        spinner_shidu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ("湿度 45%".equals(parent.getItemAtPosition(position).toString())) {
                    commond_sd = new byte[]{(byte) 0xEb, 0x01, 0x02, 0x00, 0x2d, 0x00, (byte) 0x90,};
                }
                if ("湿度 55%".equals(parent.getItemAtPosition(position).toString())) {
                    commond_sd = new byte[]{(byte) 0xEB, 0x01, 0x03, 0x00, 0x37, 0x00, (byte) 0x90};
                }
                if ("湿度 65%".equals(parent.getItemAtPosition(position).toString())) {
                    commond_sd = new byte[]{(byte) 0xEB, 0x01, 0x04, 0x00, 0x41, 0x00, (byte) 0x90};
                }
                if ("湿度 75%".equals(parent.getItemAtPosition(position).toString())) {
                    commond_sd = new byte[]{(byte) 0xEB, 0x01, 0x05, 0x00, 0x4b, 0x00, (byte) 0x90};
                }
                if ("湿度 85%".equals(parent.getItemAtPosition(position).toString())) {
                    commond_sd = new byte[]{(byte) 0xEB, 0x01, 0x06, 0x00, 0x55, 0x00, (byte) 0x90};
                }
                if ("湿度 95%".equals(parent.getItemAtPosition(position).toString())) {
                    commond_sd = new byte[]{(byte) 0xEB, 0x01, 0x07, 0x00, 0x5f, 0x00, (byte) 0x90};
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /**************************  下拉菜单选项 雾量  ****************************************/

        //原始string数组
        final String[] spinnerItems_wl = {"雾量 1", "雾量 2", "雾量 3", "雾量 4", "雾量 5"};
        //简单的string数组适配器：样式res，数组
        ArrayAdapter<String> spinnerAdapter_wl = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_spinner_item, spinnerItems_wl);
        spinnerAdapter_wl.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //下拉的样式res
        //绑定 Adapter到控件
        spinner_wuliang.setAdapter(spinnerAdapter_wl);
        //选择监听
        spinner_wuliang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ("雾量 1".equals(parent.getItemAtPosition(position).toString())) {
                    commond_wl = new byte[]{(byte) 0xEB, 0x01, 0x11, 0x00, 0x00, 0x00, (byte) 0x90};
                }
                if ("雾量 2".equals(parent.getItemAtPosition(position).toString())) {
                    commond_wl = new byte[]{(byte) 0xEB, 0x01, 0x12, 0x00, 0x00, 0x00, (byte) 0x90};
                }
                if ("雾量 3".equals(parent.getItemAtPosition(position).toString())) {
                    commond_wl = new byte[]{(byte) 0xEB, 0x01, 0x13, 0x00, 0x00, 0x00, (byte) 0x90};
                }
                if ("雾量 4".equals(parent.getItemAtPosition(position).toString())) {
                    commond_wl = new byte[]{(byte) 0xEB, 0x01, 0x14, 0x00, 0x00, 0x00, (byte) 0x90};
                }
                if ("雾量 5".equals(parent.getItemAtPosition(position).toString())) {
                    commond_wl = new byte[]{(byte) 0xEB, 0x01, 0x15, 0x00, 0x00, 0x00, (byte) 0x90};
                }
            }

            @Override

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
/**************************  下拉菜单选项 定时  ****************************************/

        //原始string数组
        final String[] spinnerItems_ds = {"关闭", "0.5小时", "1.0小时", "1.5小时", "2.0小时", "3.0小时", "5.0小时"};
        //简单的string数组适配器：样式res，数组
        ArrayAdapter<String> spinnerAdapter_ds = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_spinner_item, spinnerItems_ds);
        //下拉的样式res
        spinnerAdapter_ds.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //绑定 Adapter到控件
        spinner_dingshi.setAdapter(spinnerAdapter_ds);
        //选择监听
        spinner_dingshi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ("关闭".equals(parent.getItemAtPosition(position).toString())) {
                    commond_ds = new byte[]{(byte) 0xEB, 0x41, 0x00, 0x00, 0x00, 0x00, (byte) 0x90};
                }
                if ("0.5小时".equals(parent.getItemAtPosition(position).toString())) {
                    commond_ds = new byte[]{(byte) 0xEB, 0x41, 0x01, 0x00, 0x2D, 0x00, (byte) 0x90};
                }
                if ("1.0小时".equals(parent.getItemAtPosition(position).toString())) {
                    commond_ds = new byte[]{(byte) 0xEB, 0x41, 0x01, 0x00, 0x2D, 0x00, (byte) 0x90};
                }
                if ("1.5小时".equals(parent.getItemAtPosition(position).toString())) {
                    commond_ds = new byte[]{(byte) 0xEB, 0x41, 0x01, 0x00, 0x2D, 0x00, (byte) 0x90};
                }
                if ("2.0小时".equals(parent.getItemAtPosition(position).toString())) {
                    commond_ds = new byte[]{(byte) 0xEB, 0x41, 0x01, 0x00, 0x2D, 0x00, (byte) 0x90};
                }
                if ("3.0小时".equals(parent.getItemAtPosition(position).toString())) {
                    commond_ds = new byte[]{(byte) 0xEB, 0x41, 0x01, 0x00, 0x2D, 0x00, (byte) 0x90};
                }
                if ("5.0小时".equals(parent.getItemAtPosition(position).toString())) {
                    commond_ds = new byte[]{(byte) 0xEB, 0x41, 0x01, 0x00, 0x2D, 0x00, (byte) 0x90};
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        /******************* 湿度  **************************
         @Override********************/

        sd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                str = "a";
                putShow();

            }
        });
        /******************* 雾量  **********************************************/

        wl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = "b";
                putShow();
            }
        });
        /******************* 定时  **********************************************/

        dingshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                str = "c";
                putShow();

            }
        });
    }

    public void putShow() {
        commind_ws = new byte[]{0x03, 0x01, 0x00, 0x07, 0x24};
        Intent intent = new Intent(action);
        intent.putExtra("data", commind_ws);
        sendBroadcast(intent);
        Log.e(TAG, "握手" + commind_ws);
    }

    /******************************************************************************************/
    public boolean onOptionsItemSelected(MenuItem item) {  //标题栏左边打开侧拉菜单button
        // TODO Auto-generated method stub

        //android.R.id.home对应应用程序图标的id
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(Gravity.START);
            return true;
        }
         return super.onOptionsItemSelected(item);
    }


    /*************************  加湿器显示部分  ****************************************/

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(byte[] messageEvent) {
        Log.e(TAG, "EventBus拿到Change回调的数据" + messageEvent);
        switch (str) {
            case "a":          //湿度button
                if (messageEvent.length == 5 && messageEvent[0] == 0x03 && messageEvent[1] == 0x02 && messageEvent[3] == 0x00) {
                    Intent intent = new Intent(actionshuju);
                    intent.putExtra("shujudata", commond_sd);
                    sendBroadcast(intent);
                    Log.e(TAG, "广播发送数据湿度命令" + commond_sd);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent end = new Intent(actionend);
                                    endcommond = new byte[]{0x05, 0x03, 0x00, 0x01, 0x02, 0x03, 0x7E}; //中断命令
                                    end.putExtra("actionend", endcommond);
                                    sendBroadcast(end);
                                }
                            });
                        }
                    }, 500);
                }
                break;
            case "b":     //   雾量button
                if (messageEvent.length == 5 && messageEvent[0] == 0x03 && messageEvent[1] == 0x02 && messageEvent[3] == 0x00) {
                    Intent intenta = new Intent(actionshuju);
                    intenta.putExtra("shujudata", commond_wl);
                    sendBroadcast(intenta);
                    Log.e(TAG, "广播发送数据湿度命令" + commond_wl);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent end = new Intent(actionend);
                                    endcommond = new byte[]{0x05, 0x03, 0x00, 0x01, 0x02, 0x03, 0x7E}; //中断命令
                                    end.putExtra("actionend", endcommond);
                                    sendBroadcast(end);
                                }
                            });
                        }
                    }, 500);
                }

                break;
            case "c":         //定时button
                if (messageEvent.length == 5 && messageEvent[0] == 0x03 && messageEvent[1] == 0x02 && messageEvent[3] == 0x00) {
                    Intent intentb = new Intent(actionshuju);
                    intentb.putExtra("shujudata", commond_ds);
                    sendBroadcast(intentb);
                    Log.e(TAG, "广播发送数据湿度命令" + commond_ds);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent end = new Intent(actionend);
                                    endcommond = new byte[]{0x05, 0x03, 0x00, 0x01, 0x02, 0x03, 0x7E}; //中断命令
                                    end.putExtra("actionend", endcommond);
                                    sendBroadcast(end);
                                }
                            });
                        }
                    }, 500);
                }


                break;
            case "o":           //关闭加湿器，结束 ///中断
                if (messageEvent.length == 5 && messageEvent[1] == 0x02 && messageEvent[0] == 0x03 && messageEvent[3] == 0x00) {

                    Intent open = new Intent(actionshuju);
                    opencommond = new byte[]{(byte) 0xEB, 0x21, 0x00, 0x00, 0x00, 0x00, (byte) 0x90};
                    open.putExtra("shujudata", opencommond);
                    sendBroadcast(open);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent end = new Intent(actionend);
                                    endcommond = new byte[]{0x05, 0x03, 0x00, 0x01, 0x02, 0x03, 0x7E}; //中断命令
                                    end.putExtra("actionend", endcommond);
                                    sendBroadcast(end);
                                }
                            });
                        }
                    }, 500);

                }
                break;
            case "k":       //加湿器
                if (messageEvent.length == 5 && messageEvent[1] == 0x02 && messageEvent[0] == 0x03 && messageEvent[3] == 0x00) {

                    Intent open = new Intent(actionshuju);
                    opencommond = new byte[]{(byte) 0xEB, 0x21, 0x01, 0x00, 0x00, 0x00, (byte) 0x90}; //关闭雾化机命令
                    open.putExtra("shujudata", opencommond);
                    sendBroadcast(open);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Intent end = new Intent(actionend);
                                    endcommond = new byte[]{0x05, 0x03, 0x00, 0x01, 0x02, 0x03, 0x7E}; //中断命令
                                    end.putExtra("actionend", endcommond);
                                    sendBroadcast(end);
                                }
                            });
                        }
                    }, 500);

                }

                break;
        }
/********************************************数据显示解析******************************************************************/

        TextView shiducanshu = findViewById(R.id.shiducanshu);
        TextView wenducanshu = findViewById(R.id.wenducanshu);
        DashboardViewsw mDashboardView3 =  findViewById(R.id.viewsw);
        DashboardViewwl mDashboardView2 =  findViewById(R.id.viewwl);
        if (messageEvent.length == 7 && messageEvent[0] == (byte) 0xeb && messageEvent[6] == (byte) 0x90) {    //判断头尾数据
            Log.d(TAG, "检验");
            if (messageEvent[1] == 0x01) { //控指令符合
                switch (messageEvent[2]) {//雾量
                    case 0x11:  //1档
                        mDashboardView2.setCreditValueWithAnim(2);
                        break;
                    case 0x12:    //2档
                        mDashboardView2.setCreditValueWithAnim(4);
                        break;
                    case 0x13:   //3档
                        mDashboardView2.setCreditValueWithAnim(6);
                        break;
                    case 0x14:   //4档
                        mDashboardView2.setCreditValueWithAnim(8);
                        break;
                    case 0x15:   //5档
                        mDashboardView2.setCreditValueWithAnim(10);
                        break;
                }
            } else if (messageEvent[1] == 0x12) {          //有无水传感器  会报警
                int a = messageEvent[4];  //转换10进制

            } else if (messageEvent[1] == 0x32) {          // 水位
                final int d = messageEvent[4];  //转换10进制
                mDashboardView3.setCreditValueWithAnim(d);  //                       水位仪表盘  识别1-10  显示无延迟  0有延迟
            } else if (messageEvent[1] == 0x11) {          //温度 /湿度
                switch (messageEvent[2]) {
                    case 0x00:
                        int a = messageEvent[3];
                        int c = (((a * 256 & 0xFF00) | (messageEvent[4] & 0x00FF)) & 0xFFFC); //温度算法       温度精度14位
                        Log.e(TAG, String.valueOf(c));
                        double d = ((c / 65536.0) * 175.72 - 46.85);
                        String cc = String.format("%.2f", d);
                        wenducanshu.setText(cc + "℃");
                        break;
                    case 0x01:
                        int b = messageEvent[3];
                        int shidu = (((b * 256 & 0xFF00) | (messageEvent[4] & 0x00FF)) & 0xFFF0);  //湿度精度只有12位
                        double g = (shidu / 65536.0) * 125 - 6;
                        String bb = String.format("%.2f", g);
                        shiducanshu.setText(bb + "%");           //液态水的湿度
                        break;


                }
            }


        }
        /********************************************接收的握手协议**************************************************/
        if (messageEvent.length == 5 && messageEvent[0] == 3 && messageEvent[1] == 1 && messageEvent[3] == 7) {
            commond_js = new byte[]{0x03, 0x02, 0x00, 0x00, 0x15};
            Intent intent = new Intent(action);
            intent.putExtra("data", commond_js);
            sendBroadcast(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //  putShow();
    }

    @Override        //注销EvevtBus
    protected void onDestroy() {
        super.onDestroy();
        //解除注册
        EventBus.getDefault().unregister(this);
    }
}
