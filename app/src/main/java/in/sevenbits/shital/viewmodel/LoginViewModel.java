package in.sevenbits.shital.viewmodel;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;

public class LoginViewModel extends AndroidViewModel {

    private static final String TAG = LoginViewModel.class.getSimpleName();
    private Context context;
    public ObservableField<String> phone = new ObservableField<>();
    public ObservableField<String> verificationCode = new ObservableField<>();
    public ObservableField<String> errorMessage = new ObservableField<>();
    public ObservableBoolean inputEnabled = new ObservableBoolean(true);


    public LoginViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
    }




}
