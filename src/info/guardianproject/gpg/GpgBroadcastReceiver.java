
package info.guardianproject.gpg;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.File;

/**
 * @author abel
 */
public class GpgBroadcastReceiver extends BroadcastReceiver {

    /**
     * We launch SharedDaemonsService at boot and register the PATH var in
     * android terminal emulator
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        // we must call setup here to app_opt below isn't null
        NativeHelper.setup(context);

        String packageName = context.getPackageName();
        String action = intent.getAction();

        if (action.equals("jackpal.androidterm.broadcast.APPEND_TO_PATH")) {
            /* The directory we want appended goes into the result extras */
            Bundle result = getResultExtras(true);

            /**
             * By convention, entries are indexed by package name.
             * <p>
             * If you need to impose an ordering constraint for some reason, you
             * may prepend a number to your package name -- for example,
             * 50-com.example.awesomebin or 00-net.busybox.android.
             */
            File aliases = new File(NativeHelper.app_opt.getAbsolutePath(), "aliases");
            String pathToAppend = aliases.getAbsolutePath();
            result.putString(packageName, pathToAppend);

            setResultCode(Activity.RESULT_OK);
        } else if (action.equals("android.intent.action.BOOT_COMPLETED")) {
            if (Preferences.startOnBoot(context)) {
                GpgApplication.startGpgAgent(context);
                GpgApplication.startSharedDaemons(context);
            }
        }
    }
}
