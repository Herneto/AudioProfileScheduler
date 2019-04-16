package ao.co.laboro.audioprofilescheduler.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.Toast;

import ao.co.laboro.audioprofilescheduler.MainActivity;

public class BootDeviceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(Intent.ACTION_BOOT_COMPLETED.equals(action) || Intent.ACTION_REBOOT.equals(action) )
        {
            context.startService(new Intent(context.getApplicationContext(), MonitorService.class));
        }
    }
}
