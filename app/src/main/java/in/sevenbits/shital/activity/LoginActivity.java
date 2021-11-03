package in.sevenbits.shital.activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;
import in.sevenbits.shital.base.BaseActivity;
import in.sevenbits.shital.databinding.ActivityLoginBinding;
import in.sevenbits.shital.viewmodel.LoginViewModel;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;
    private boolean isFirstAction = true;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        binding.setLoginViewModel(viewModel);

        binding.btnLogin.setOnClickListener(this);
        binding.btnResendOtp.setOnClickListener(this);

    }

    private void setUpNextAction(int command){
        if(command == 0){
            isFirstAction = false;
            binding.llPhone.setVisibility(View.GONE);
            binding.edtPassword.setVisibility(View.VISIBLE);
            binding.tvActionMessage.setText("Please enter the verification code sent to " + binding.edtPhone.getText().toString());
            binding.btnResendOtp.setVisibility(View.VISIBLE);
            binding.btnResendOtp.setEnabled(false);
            binding.btnResendOtp.setAlpha(0.2f);
        } else  {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == binding.btnResendOtp.getId()){
            setUpNextAction(0);
        } else {
            if (isFirstAction) {
                validateInputsAndSendOtp();
            } else {
                validateInputsAndVerifyOtp();
            }
        }
    }

    public void validateInputsAndSendOtp() {
        viewModel.inputEnabled.set(false);
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(binding.ccp.getSelectedCountryCodeWithPlus() + viewModel.phone.get())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this) // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                if(phoneAuthCredential.getSmsCode() != null) {
                                    viewModel.verificationCode.set(phoneAuthCredential.getSmsCode());
                                }
                                viewModel.inputEnabled.set(false);
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                e.printStackTrace();
                                viewModel.inputEnabled.set(true);

                            }

                            @Override
                            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                                super.onCodeAutoRetrievalTimeOut(s);
                                binding.btnResendOtp.setEnabled(true);
                                binding.btnResendOtp.setAlpha(1.0f);
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                viewModel.inputEnabled.set(true);
                                verificationId = s;
                                resendToken = forceResendingToken;
                                setUpNextAction(0);
                            }

                        }).setForceResendingToken(resendToken)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }


    public void validateInputsAndVerifyOtp() {
        viewModel.inputEnabled.set(false);
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, viewModel.verificationCode.get());
        signInWithPhoneAuthCredential(credential);
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();

                            // Update UI

                            setUpNextAction(1);
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
}