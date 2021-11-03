package in.sevenbits.shital;

import android.app.Application;
import android.content.Context;


public class SevenBitsApplication extends Application {

    private static final String TAG = SevenBitsApplication.class.getSimpleName();

    private static SevenBitsApplication get(Context context) {
        return (SevenBitsApplication) context.getApplicationContext();
    }

    public static SevenBitsApplication create(Context context) {
        return SevenBitsApplication.get(context);
    }

}
