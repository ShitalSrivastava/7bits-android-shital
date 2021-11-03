package in.sevenbits.shital.viewmodel;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.AndroidViewModel;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private Context context;
    public ObservableBoolean messageVisibility;
    public ObservableBoolean isLoading;

    public MainViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        messageVisibility = new ObservableBoolean(false);
        isLoading = new ObservableBoolean(true);
    }

}
