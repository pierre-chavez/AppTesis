package com.example.uees2.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.onesignal.OneSignal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    ProgressBar progressBar;
    EditText editTextEmail, editTextPassword;
    String playerId = "";
    private FirebaseAuth mAuth;

    DatabaseReference databaseUsuario;

    public SignUpActivity() {
    }

    public SignUpActivity(ProgressBar progressBar, EditText editTextEmail, EditText editTextPassword, FirebaseAuth mAuth) {
        this.progressBar = progressBar;
        this.editTextEmail = editTextEmail;
        this.editTextPassword = editTextPassword;
        this.mAuth = mAuth;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        databaseUsuario = FirebaseDatabase.getInstance().getReference("Usuario");
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .setNotificationOpenedHandler(new NotificationOpenedHandler())
                .init();
        OneSignal.enableVibrate(true);
        OneSignal.enableSound(true);
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                Log.d("SignUp", "User:" + userId);
                playerId = userId;
                if (registrationId != null)
                    Log.d("SignUp", "registrationId:" + registrationId);

            }
        });
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.buttonSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        findViewById(R.id.textViewLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(v.getContext(), LoginActivity.class));
            }
        });
    }

    private void registerUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError("Email es requerido");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Por favor, ingresa un email válido");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Contraseña es requerida");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("El mínimo de caracteres para la contraseña debe ser de 6");
            editTextPassword.requestFocus();
            return;
        }
        if (!isValidPassword(password.trim())) {
            editTextPassword.setError("La contraseña debe tener al menos una letra mayúscula, una letra minúscula y un número");
            editTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {

                    String correo = editTextEmail.getText().toString().trim();
                    String contrasenia = editTextPassword.getText().toString().trim();
                    String userId = mAuth.getUid();
                    registrarUsuario(userId,correo, contrasenia, "Familiar", playerId);
                    final FirebaseUser user = mAuth.getCurrentUser();
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Verificación de Email enviada. Por favor verificar su correo.", Toast.LENGTH_LONG).show();
                        }
                    });
                    mAuth = null;
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                    finish();
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "Usuario ya ha sido registrado", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Por favor, verifica el correo o la contraseña e intantalo de nuevo.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    private void registrarUsuario(String userdId,String email, String password, String rol, String playerId) {
        Usuario usuario = new Usuario(userdId, email, password, rol, playerId);
        //String id = databaseUsuario.push().getKey();
        String id = userdId;
        databaseUsuario.child(id).setValue(usuario);
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$)\\S{6,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}
