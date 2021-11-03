package in.sevenbits.shital.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

public class ProfileViewModel extends AndroidViewModel {

    private static final String TAG = ProfileViewModel.class.getSimpleName();
    private Context context;
    public ObservableField<String> phone = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> email = new ObservableField<>();
    public ObservableBoolean inputEnabled = new ObservableBoolean(true);


    public ProfileViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
    }




}
