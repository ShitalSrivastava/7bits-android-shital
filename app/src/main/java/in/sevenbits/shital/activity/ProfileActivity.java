package in.sevenbits.shital.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import in.sevenbits.shital.R;
import in.sevenbits.shital.base.BaseActivity;
import in.sevenbits.shital.databinding.ActivityMainBinding;
import in.sevenbits.shital.databinding.ActivityProfileBinding;
import in.sevenbits.shital.viewmodel.MainViewModel;
import in.sevenbits.shital.viewmodel.ProfileViewModel;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    private ProfileViewModel viewModel;
    private ActivityProfileBinding binding;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        setToolbar(binding.toolbar, false, true);
        binding.toolbar.setTitle("Update Profile");
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        binding.setProfileViewModel(viewModel);
        binding.btnSave.setOnClickListener(this);
        currentUser = auth.getCurrentUser();
        viewModel.phone.set(currentUser.getPhoneNumber());

        viewModel.email.set(currentUser.getEmail());

        viewModel.name.set(currentUser.getDisplayName());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    @Override
    public void onClick(View v) {
        if(!TextUtils.isEmpty(viewModel.name.get())) {

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(viewModel.name.get())
                    .build();
            currentUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                            }
                        }
                    });
        }

        if(!TextUtils.isEmpty(viewModel.email.get())) {
            currentUser.updateEmail(viewModel.email.get()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                    }
                }
            });
        }
    }
}