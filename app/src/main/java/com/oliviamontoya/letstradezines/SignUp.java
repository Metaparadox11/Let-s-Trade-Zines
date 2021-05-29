package com.oliviamontoya.letstradezines;

import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.view.View;
import android.widget.CheckBox;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import androidx.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.UserProfileChangeRequest;

import android.net.Uri;

public class SignUp extends AppCompatActivity {

    TextView signUpText;
    TextView usernameTitle;
    EditText usernameEnter;
    TextView emailTitle;
    EditText emailEnter;
    TextView passwordTitle;
    EditText enterPassword;
    Button checkUsernameButton;
    TextView spinnerTitle;
    Spinner yearSpinner;
    TextView genderTitle;
    EditText genderEnter;
    TextView locationTitle;
    EditText locationEnter;
    TextView profileTitle;
    EditText profileInfo;
    TextView zineLikesTitle;
    CheckBox chkPerzines;
    CheckBox chkFanzines;
    CheckBox chkArtZines;
    CheckBox chkCompZines;
    CheckBox chkChapbooks;
    CheckBox chkMinicomics;
    CheckBox chkQueerZines;
    CheckBox chkFeministZines;
    CheckBox chkPoliticalZines;
    CheckBox chkMusicZines;
    CheckBox chkFictionZines;
    CheckBox chkOther;
    TextView zinesMadeTitle;
    CheckBox chkPerzines2;
    CheckBox chkFanzines2;
    CheckBox chkArtZines2;
    CheckBox chkCompZines2;
    CheckBox chkChapbooks2;
    CheckBox chkMinicomics2;
    CheckBox chkQueerZines2;
    CheckBox chkFeministZines2;
    CheckBox chkPoliticalZines2;
    CheckBox chkMusicZines2;
    CheckBox chkFictionZines2;
    CheckBox chkOther2;
    Button submitSignUpButton;
    TextView test;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "SignUp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        signUpText = (TextView) findViewById(R.id.signUpText);
        usernameTitle = (TextView) findViewById(R.id.usernameTitle);
        usernameEnter = (EditText) findViewById(R.id.usernameEnter);
        emailTitle = (TextView) findViewById(R.id.emailTitle);
        emailEnter = (EditText) findViewById(R.id.emailEnter);
        passwordTitle = (TextView) findViewById(R.id.passwordTitle);
        enterPassword = (EditText) findViewById(R.id.enterPassword);
        checkUsernameButton = (Button) findViewById(R.id.checkUsernameButton);
        checkUsernameButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        checkUsernameButton.setTextColor(Color.WHITE);
        spinnerTitle = (TextView) findViewById(R.id.spinnerTitle);
        yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
        yearSpinner.setPrompt("Birth Year");
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        genderTitle = (TextView) findViewById(R.id.genderTitle);
        genderEnter = (EditText) findViewById(R.id.genderEnter);
        locationTitle = (TextView) findViewById(R.id.locationTitle);
        locationEnter = (EditText) findViewById(R.id.locationEnter);
        profileTitle = (TextView) findViewById(R.id.profileTitle);
        profileInfo = (EditText) findViewById(R.id.profileInfo);
        zineLikesTitle = (TextView) findViewById(R.id.zineLikesTitle);
        chkPerzines = (CheckBox) findViewById(R.id.chkPerzines);
        chkFanzines = (CheckBox) findViewById(R.id.chkFanzines);
        chkArtZines = (CheckBox) findViewById(R.id.chkArtZines);
        chkCompZines = (CheckBox) findViewById(R.id.chkCompZines);
        chkChapbooks = (CheckBox) findViewById(R.id.chkChapbooks);
        chkMinicomics = (CheckBox) findViewById(R.id.chkMinicomics);
        chkQueerZines = (CheckBox) findViewById(R.id.chkQueerZines);
        chkFeministZines = (CheckBox) findViewById(R.id.chkFeministZines);
        chkPoliticalZines = (CheckBox) findViewById(R.id.chkPoliticalZines);
        chkMusicZines = (CheckBox) findViewById(R.id.chkMusicZines);
        chkFictionZines = (CheckBox) findViewById(R.id.chkFictionZines);
        chkOther = (CheckBox) findViewById(R.id.chkOther);
        zinesMadeTitle = (TextView) findViewById(R.id.zinesMadeTitle);
        chkPerzines2 = (CheckBox) findViewById(R.id.chkPerzines2);
        chkFanzines2 = (CheckBox) findViewById(R.id.chkFanzines2);
        chkArtZines2 = (CheckBox) findViewById(R.id.chkArtZines2);
        chkCompZines2 = (CheckBox) findViewById(R.id.chkCompZines2);
        chkChapbooks2 = (CheckBox) findViewById(R.id.chkChapbooks2);
        chkMinicomics2 = (CheckBox) findViewById(R.id.chkMinicomics2);
        chkQueerZines2 = (CheckBox) findViewById(R.id.chkQueerZines2);
        chkFeministZines2 = (CheckBox) findViewById(R.id.chkFeministZines2);
        chkPoliticalZines2 = (CheckBox) findViewById(R.id.chkPoliticalZines2);
        chkMusicZines2 = (CheckBox) findViewById(R.id.chkMusicZines2);
        chkFictionZines2 = (CheckBox) findViewById(R.id.chkFictionZines2);
        chkOther2 = (CheckBox) findViewById(R.id.chkOther2);
        submitSignUpButton = (Button) findViewById(R.id.submitSignUpButton);
        submitSignUpButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        submitSignUpButton.setTextColor(Color.WHITE);

        test = (TextView) findViewById(R.id.test);
    }

    public void signUpSubmit(View view) {
        final String un = usernameEnter.getText().toString();
        final String em = emailEnter.getText().toString();
        final String pw = enterPassword.getText().toString();
        final String by = yearSpinner.getSelectedItem().toString();
        final String gen = genderEnter.getText().toString();
        final String loc = locationEnter.getText().toString();
        final String pro = profileInfo.getText().toString();


        //String hash = "";
        //try {
        //    hash = PasswordStorage.createHash(pw);
        //} catch (Exception e) {
        //    test.setText("Error");
        //}
        //String sha256hex = new String(Hex.encodeHex(DigestUtils.sha256(pw)));
        //test.setText(hash);

        mAuth.createUserWithEmailAndPassword(em, pw).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Toast.makeText(SignUp.this, "Authorization failed.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(un)
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User profile updated.");

                                        mAuth.signInWithEmailAndPassword(em, pw)
                                                .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                                        if (!task.isSuccessful()) {
                                                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                                                            Toast.makeText(SignUp.this, "Authorization failed.",
                                                                    Toast.LENGTH_SHORT).show();
                                                        } else {

                                                            Toast.makeText(SignUp.this, "Success!",
                                                                    Toast.LENGTH_SHORT).show();

                                                            Intent intent = new Intent(SignUp.this, Main2Activity.class);
                                                            startActivity(intent);
                                                        }

                                                    }
                                                });


                                        final Boolean per = chkPerzines.isChecked();
                                        final Boolean per2 = chkPerzines2.isChecked();
                                        final Boolean fan = chkFanzines.isChecked();
                                        final Boolean fan2 = chkFanzines2.isChecked();
                                        final Boolean art = chkArtZines.isChecked();
                                        final Boolean art2 = chkArtZines2.isChecked();
                                        final Boolean comp = chkCompZines.isChecked();
                                        final Boolean comp2 = chkCompZines2.isChecked();
                                        final Boolean chap = chkChapbooks.isChecked();
                                        final Boolean chap2 = chkChapbooks2.isChecked();
                                        final Boolean mini = chkMinicomics.isChecked();
                                        final Boolean mini2 = chkMinicomics2.isChecked();
                                        final Boolean queer = chkQueerZines.isChecked();
                                        final Boolean queer2 = chkQueerZines2.isChecked();
                                        final Boolean fem = chkFeministZines.isChecked();
                                        final Boolean fem2 = chkFeministZines2.isChecked();
                                        final Boolean pol = chkPoliticalZines.isChecked();
                                        final Boolean pol2 = chkPoliticalZines2.isChecked();
                                        final Boolean mus = chkMusicZines.isChecked();
                                        final Boolean mus2 = chkMusicZines2.isChecked();
                                        final Boolean fic = chkFictionZines.isChecked();
                                        final Boolean fic2 = chkFictionZines2.isChecked();
                                        final Boolean other = chkOther.isChecked();
                                        final Boolean other2 = chkOther2.isChecked();

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        DatabaseReference myRef = database.getReference(user.getUid());
                                        myRef.setValue(new User(un, em, by, gen, loc, pro, per, fan, art, comp,
                                                chap, mini, queer, fem, pol, mus, fic, other, per2, fan2, art2,
                                                comp2, chap2, mini2, queer2, fem2, pol2, mus2, fic2, other2, Uri.EMPTY.toString()));


                                        //Handler handler = new Handler();
                                        //handler.postDelayed(new Runnable() {

                                        //    @Override
                                        //    public void run() {
                                        //        Intent intent = new Intent(SignUp.this, Main2Activity.class);
                                        //        startActivity(intent);
                                        //    }

                                        //}, 5000); // 5000ms delay

                                    } else {
                                        Toast.makeText(SignUp.this, "Something awful went wrong. Email me to reset your account.",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
