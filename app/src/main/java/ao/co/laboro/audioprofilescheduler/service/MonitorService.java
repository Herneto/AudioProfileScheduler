package ao.co.laboro.audioprofilescheduler.service;

import android.accessibilityservice.AccessibilityService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.widget.Toast;

import java.util.Calendar;

import ao.co.laboro.audioprofilescheduler.*;
import ao.co.laboro.audioprofilescheduler.ProfileItem;

public class MonitorService extends Service {

    @Override
    public void onCreate() {


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {

                while(true){
                    Context context = MonitorService.this;

                    final Calendar c = Calendar.getInstance();
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minute = c.get(Calendar.MINUTE);

                    hour = Integer.parseInt(format(get12h(hour)));
                    minute = Integer.parseInt(format(minute));

                    Toast.makeText(context, hour+":"+minute, Toast.LENGTH_SHORT).show();
                    Db mDb = new Db(context);
                    ProfileItem profileItem = mDb.hasDayAndMinute(hour, minute);
                    if(profileItem != null){
                        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
                        if(profileItem.getStatus().equals("Vibrate")){
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                        }else if(profileItem.getStatus().equals("Normal")){
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        }else{
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        }
                    }
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

            public int get12h(int hourOfDay){
                return hourOfDay > 12? hourOfDay - 12:hourOfDay;
            }

            public String format(int number){
                if(number < 10)
                    return "0"+number;
                return ""+number;
            }
        });
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                while(true){
                    Context context = MonitorService.this;

                    final Calendar c = Calendar.getInstance();
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minute = c.get(Calendar.MINUTE);

                    hour = Integer.parseInt(format(get12h(hour)));
                    minute = Integer.parseInt(format(minute));
                    Toast.makeText(context, hour+":"+minute, Toast.LENGTH_SHORT).show();
                    Db mDb = new Db(context);
                    ProfileItem profileItem = mDb.hasDayAndMinute(hour, minute);
                    if(profileItem != null){
                        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
                        if(profileItem.getStatus().equals("Vibrate")){
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                        }else if(profileItem.getStatus().equals("Normal")){
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        }else{
                            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        }
                    }
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

            public int get12h(int hourOfDay){
                return hourOfDay > 12? hourOfDay - 12:hourOfDay;
            }

            public String format(int number){
                if(number < 10)
                    return "0"+number;
                return ""+number;
            }
        }).start();
        return null;
    }

    @Override
    public void onDestroy() {

    }
}
