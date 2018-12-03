package com.example.administrator.myapplication;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import java.io.IOException;

import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;

public class AlertDialogActivity extends AppCompatActivity implements DialogInterface.OnClickListener {
    public static AlertDialogActivity context = null;
    private MediaPlayer player = new MediaPlayer();//定义播放类
    PowerManager.WakeLock mWakelock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);//定义开启屏幕唤醒的内容
        mWakelock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.FULL_WAKE_LOCK, "AlertDialog");
        mWakelock.acquire();

        KeyguardManager keyguardManager = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("AlertDialog");
        //keyguardLock.disableKeyguard();
        context = this;
        try{
            Uri localUri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM);
            if((player != null) && (localUri != null))
            {
                player.setDataSource(context,localUri);//播放文件
                player.prepare();
                player.setLooping(false);
                player.start();
            }

            AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
            //localBuilder.setTitle (R.string.app_name);
            //localBuilder.setTitle(R.string.alertName);
            localBuilder.setMessage(getIntent().getStringExtra("content"));
            //localBuilder.setPositiveButton(R.string.positiveButton,this);
            //localBuilder.setNegativeButton(R.string.negativeButton,this);
            localBuilder.show();//展示文件内容

        }catch (IllegalArgumentException localIllegalArgumentException)
        {
            localIllegalArgumentException.printStackTrace();
        }
        catch (SecurityException localSecurityException)
        {
            localSecurityException.printStackTrace();
        }
        catch (IllegalStateException localIllegalStateException)
        {
            localIllegalStateException.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        // TODO Auto-generated method stub
        switch(which){
            case DialogInterface.BUTTON1://查看按钮
            {
                Intent intent = new Intent(AlertDialogActivity.this, EditActivity.class);
                Bundle b = new Bundle();
                b.putString("datetime",getIntent().getStringExtra("datetime"));
                b.putString("content", getIntent().getStringExtra("content"));
                b.putString("alerttime",getIntent().getStringExtra("alerttime"));
                intent.putExtra("android.intent.extra.INTENT", b);
                startActivity(intent);
                finish();
            }
            case DialogInterface.BUTTON2://忽略按钮
            {
                player.stop();
                finish();
            }
        }
    }
}