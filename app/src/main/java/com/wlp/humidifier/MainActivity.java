package com.wlp.humidifier;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wlp.humidifier.Fragment.Fragment1;
import com.wlp.humidifier.Fragment.Fragment2;
import com.wlp.humidifier.Fragment.Fragment3;

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
    byte[] endcommond, opencommond, closecommond,commond_js;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    public static final String fragmentt = "com.wlp.fragment1";  //温度广播
    public static final String dingshiguangbo = "com.wlp.dingshisetting";  //定时广播
    public static final String hengshiguangbo = "com.wlp.hengshisetting";  //恒湿广播
    public static final String fragmenty = "com.wlp.fragment2";  // 湿度广播
    public static final String action = "com.example.broadcast";  //蓝牙握手广播
    public static final String actionshuju = "com.example.shuju.broadcast";// 蓝牙数据广播
    public static final String actionend = "com.example.end.broadcast";   //蓝牙结束广播
    private static final String TAG = "ble_tag";
    public String str = "-1";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCollector.addActivity(this);//每启动一个Activity，就将其加入到activity列表中
        EventBus.getDefault().register(this);
        inViewtab();
        intViewbar();
        broadcast();
        android.support.v7.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {  //侧拉菜单

                switch (menuItem.getItemId()) {
                    case R.id.nav_add:   //添加设备
                        Intent intent = new Intent(MainActivity.this, SearchBtActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.nav_exit:   //退出
                        ActivityCollector.finishAllActivity();
                        break;
                    case R.id.nav_open:  //打开加湿器
                        str = "e";
                        putShow();
                        break;
                    case R.id.nav_close:  //关闭加湿器
                        str = "d";
                        putShow();
                        break;
                }
                mDrawerLayout.closeDrawers();
                return false;
            }
        });
    }
/********************************恒湿/定时的广播注册********************************************/
    private void broadcast() {
        IntentFilter FGds = new IntentFilter(dingshiguangbo);
        registerReceiver(FOG13, FGds);
        IntentFilter FGhs = new IntentFilter(hengshiguangbo);
        registerReceiver(FOG14, FGhs);

    }
    // 定时   收到后执行的方法
    BroadcastReceiver  FOG13 =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            str="c";
            putShow();
            commond_ds = intent.getByteArrayExtra("dingshishezhi");

        }
    };
    //恒湿    收到后执行的方法
    BroadcastReceiver  FOG14 =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            str="a";
            putShow();
            commond_sd = intent.getByteArrayExtra("hengshishezhi");

        }
    };

    /*********************************下方左右滑动界面********************************************/

    private void inViewtab() {
        mTabLayout = (TabLayout) findViewById(R.id.mytab);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);


        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(mViewPager);
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentAdapter pagerAdapter = new FragmentAdapter(fm);
        pagerAdapter.addFragment(new Fragment1(), "状态");
        pagerAdapter.addFragment(new Fragment2(), "定时");
        pagerAdapter.addFragment(new Fragment3(), "恒湿");
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
    }

    /*******************雾量0-100档位控制*****************************************/
    private void intViewbar() {
        SeekBar klala = (SeekBar) findViewById(R.id.sing);
        final TextView JD = (TextView) findViewById(R.id.jindu);

        klala.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                JD.setText(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                putShow();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int p = seekBar.getProgress();
                str = "b";
                byte fog = (byte) (p & 0xFF);
                commond_wl = new byte[]{(byte) 0xEB, 0x01, 0x16, 0x00, fog, 0x00, (byte) 0x90};
                Toast.makeText(MainActivity.this, "雾量输出" + p + "%", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*******************握手**************************************/
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


    /*************************  加湿器发送指令部分  ****************************************/

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
                    Log.e(TAG, "广播发送数据雾量命令" + commond_wl);
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
                    Log.e(TAG, "广播发送数据定时命令" + commond_ds);
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
            case "d":           //关闭加湿器，结束 ///中断
                if (messageEvent.length == 5 && messageEvent[1] == 0x02 && messageEvent[0] == 0x03 && messageEvent[3] == 0x00) {

                    Intent open = new Intent(actionshuju);
                    closecommond = new byte[]{(byte) 0xEB, 0x21, 0x00, 0x00, 0x00, 0x00, (byte) 0x90};
                    open.putExtra("shujudata", closecommond);
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
            case "e":       //打开加湿器
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
        DashboardViewsw mDashboardView3 = findViewById(R.id.viewsw);
        DashboardViewwl mDashboardView2 = findViewById(R.id.viewwl);
        if (messageEvent.length == 7 && messageEvent[0] == (byte) 0xeb && messageEvent[6] == (byte) 0x90) {    //判断头尾数据
            Log.d(TAG, "检验");
            if (messageEvent[1] == 0x01 && messageEvent[2] == 0x11) { //控指令符合
                int progressfog = messageEvent[4];
                mDashboardView2.setCreditValueWithAnim(progressfog);  //雾量大小显示
            }
        } else if (messageEvent[1] == 0x12) {          //有无水传感器  会报警
            mDashboardView3.setCreditValueWithAnim(0);
        } else if (messageEvent[1] == 0x32) {          // 水位
            final int d = messageEvent[4];  //转换10进制
            mDashboardView3.setCreditValueWithAnim(d);  //                       水位仪表盘  识别1-10  显示无延迟  0有延迟
        } else if (messageEvent[1] == 0x11) {          //温度 /湿度
            switch (messageEvent[2]) {
                case 0x00:
                    int a = messageEvent[3];
                    int c = (((a * 256 & 0xFF00) | (messageEvent[4] & 0x00FF)) & 0xFFFC); //温度算法       温度精度14位
                    double d = ((c / 65536.0) * 175.72 - 46.85);
                    String cc = String.format("%.2f", d);
                    Intent wdwd = new Intent(fragmentt);  //广播 发送 温度数据
                    wdwd.putExtra("tpcanshu",cc);
                    sendBroadcast(wdwd);
                    break;
                case 0x01:
                    int b = messageEvent[3];
                    int shidu = (((b * 256 & 0xFF00) | (messageEvent[4] & 0x00FF)) & 0xFFF0);  //湿度精度只有12位
                    double g = (shidu / 65536.0) * 125 - 6;
                    String bb = String.format("%.2f", g);
                    Intent sdsd = new Intent(fragmenty);  //广播 发送 湿度数据
                    sdsd.putExtra("tpcanshu",bb);
                    sendBroadcast(sdsd);       //液态水的湿度
                    break;


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

    @Override        //注销EvevtBus
    protected void onDestroy() {
        super.onDestroy();
        //解除注册
        EventBus.getDefault().unregister(this);
        unregisterReceiver(FOG13);
        unregisterReceiver(FOG14);
    }
}
