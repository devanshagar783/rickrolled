package com.example.rickrolled;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "PhoneAuthActivity";

    private FirebaseAuth mAuth;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private EditText phoneNumber;
    private EditText smsCode;
    private TextInputLayout phonebox, otpbox;

    private Button getotp, verify;
    private FloatingActionButton resend;

    private Animation resendanim;

    @Override
    public void onStart() {
        super.onStart();
        if (mVerificationInProgress && validatePhoneNumber()) {
            phoneNumber = findViewById(R.id.phoneNumber);
            smsCode = findViewById(R.id.otp);
            startPhoneNumberVerification(phoneNumber.getText().toString());
        }
        Log.d(TAG, "in onStart");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        phoneNumber = findViewById(R.id.phoneNumber);
        smsCode = findViewById(R.id.otp);
        mAuth = FirebaseAuth.getInstance();
        phonebox = findViewById(R.id.phonebox);
        otpbox = findViewById(R.id.otpbox);
        resend = findViewById(R.id.btnResend);
        getotp = findViewById(R.id.btngetotp);
        verify = findViewById(R.id.btnVerify);
        setWatcher(phonebox, phoneNumber);
        setWatcher(otpbox, smsCode);

        resendanim = AnimationUtils.loadAnimation(SignupActivity.this, R.anim.resend_rot);
        signOut();

        getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Log.d(TAG, "onClick: btngetotp");
                    phonebox.setError(null);
                    otpbox.setError(null);
                    if (!validatePhoneNumber()) {
                        return;
                    }
                    startPhoneNumberVerification(phoneNumber.getText().toString());
                    //auto retrieval of verification code
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseAuthSettings firebaseAuthSettings = auth.getFirebaseAuthSettings();
                    firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNumber.getText().toString(), smsCode.getText().toString());
                } catch (Exception e) {
                    phonebox.setError("Enter the field");
                    otpbox.setError("Enter the field");
                }
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Button Otp");
                if (TextUtils.isEmpty(smsCode.getText().toString())) {
                    otpbox.setError("Cannot be empty.");
                    return;
                }
                otpbox.setError(null);
                verifyPhoneNumberWithCode(mVerificationId, smsCode.getText().toString());
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "buttonResend");
                resend.startAnimation(resendanim);
                resendVerificationCode(phoneNumber.getText().toString(), mResendToken);
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NotNull PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NotNull FirebaseException e) {
                Log.d(TAG, "onVerificationFailed" + e.getMessage().toString());
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    phonebox.setError("Please check the phone number");
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    phonebox.setError(null);
                    Toast.makeText(SignupActivity.this, "Server overload... could not verify", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
    }

    public void setWatcher(TextInputLayout layout, EditText editText) {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        editText.addTextChangedListener(watcher);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        try {
            Log.d(TAG, "startPhoneNumberverification");
            FirebaseAuth auth = FirebaseAuth.getInstance();
            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(phoneNumber)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)                 // Activity (for callback binding)
                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
            mVerificationInProgress = true;
            auth.setLanguageCode("en");
        } catch (Exception e) {
            Toast.makeText(SignupActivity.this, "Phone verify failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        Log.d(TAG, "signInwithPhone");
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            checkRegister();
                        } else {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(SignupActivity.this, "Verification failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void verifyPhoneNumberWithCode(String mVerificationId, String code) {
        Log.d(TAG, "verifyPhoneNumberWithcode");
        try {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            signInWithPhoneAuthCredential(credential);
        } catch (Exception e) {
            Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validatePhoneNumber() {
        Log.d(TAG, "validatePhoneNumber");
        phoneNumber = findViewById(R.id.phoneNumber);
        if (TextUtils.isEmpty(phoneNumber.getText().toString())) {
            phonebox.setError("Invalid phone number.");
            return false;
        }
        phonebox.setError(null);
        return true;
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        try {
            phonebox.setError(null);
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phoneNumber,        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks,         // OnVerificationStateChangedCallbacks
                    token);             // ForceResendingToken from callbacks
        } catch (IllegalArgumentException e) {
            phonebox.setError("Enter your phone number");
        }
    }

    private void signOut() {
        mAuth.signOut();
    }

    //checking previous login or not, return true if record exists else return false
    public void checkRegister() {
        TextView phone = findViewById(R.id.phoneNumber);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("number", phone.getText().toString())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG, "onSucessListner");
                        List<DocumentSnapshot> snapshotsList = queryDocumentSnapshots.getDocuments();
                        Intent intent;
                        if (!snapshotsList.isEmpty()) {
                            Log.d(TAG, "onSuccess: user exists");
                            Toast.makeText(SignupActivity.this, "Verified!.Loggin in", Toast.LENGTH_SHORT).show();
                            intent = new Intent(SignupActivity.this, MainActivity.class);
                            intent.putExtra("Number", phone.getText().toString());
                            startActivity(intent);
                        } else {
                            Log.d(TAG, "checkRegister: not found");
                            Toast.makeText(SignupActivity.this, "Verified! Create your account", Toast.LENGTH_SHORT).show();
                            saveDetails();
                            intent = new Intent(SignupActivity.this, MainActivity.class);
                            intent.putExtra("Number", phone.getText().toString());
                            startActivity(intent);
                        }
                    }
                });
    }

    private void saveDetails() {
        Log.d(TAG, "saveDeatils");
        HashMap<String, Object> map = new HashMap<>();
        map.put("number", phoneNumber.getText().toString());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .set(map, SetOptions.merge());
    }
}