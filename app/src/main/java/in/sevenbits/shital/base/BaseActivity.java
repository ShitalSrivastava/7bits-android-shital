package in.sevenbits.shital.base;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import in.sevenbits.shital.R;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP & Build.VERSION_CODES.LOLLIPOP_MR1)
    private void setStatusBarColor(){
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP || Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary));
        }
    }

    public void setStatusBarColor(int color){
            getWindow().setStatusBarColor(color);
    }

    public void setToolbar(Toolbar toolbar, boolean titleEnabled, boolean backEnabled){
        this.toolbar = toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(titleEnabled);
        getSupportActionBar().setDisplayHomeAsUpEnabled(backEnabled);
    }

    public Toolbar getToolbar(){
        return this.toolbar;
    }

    @Override
    public void onBackPressed() {

    }


}
