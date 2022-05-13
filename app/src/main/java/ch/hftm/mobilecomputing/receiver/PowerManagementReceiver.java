package ch.hftm.mobilecomputing.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class PowerManagementReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = null;

        switch (intent.getAction()) {
            case Intent.ACTION_BATTERY_LOW:
                message = "batter low";
                break;
            case Intent.ACTION_POWER_CONNECTED:
                message = "power connected";
                break;
            case Intent.ACTION_POWER_DISCONNECTED:
                message = "power disconnected";
                break;
        }

        if (message != null) Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}