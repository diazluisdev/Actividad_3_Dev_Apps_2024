package com.example.actividad_3_dev_apps_luis_felipe_diaz_luiggi_salemi_soto_2024.Principal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.actividad_3_dev_apps_luis_felipe_diaz_luiggi_salemi_soto_2024.R;
import com.example.actividad_3_dev_apps_luis_felipe_diaz_luiggi_salemi_soto_2024.Usuarios.RecuperarContrasena;
import com.example.actividad_3_dev_apps_luis_felipe_diaz_luiggi_salemi_soto_2024.Usuarios.RegistrarUsuarios;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    TextInputEditText campoCorreo, campoContraseña;
    TextView recuperarContraseña, acercaDe;
    private FirebaseAuth mAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        campoCorreo = findViewById(R.id.campoCorreoLogin);
        campoContraseña = findViewById(R.id.campoContraseñaLogin);
        recuperarContraseña = findViewById(R.id.recuperarContraseña);
        mAuth = FirebaseAuth.getInstance();
        Button registrarse = findViewById(R.id.BotonRegistrarse);
        Button BotonLogin = findViewById(R.id.BotonLogin);
        acercaDe = findViewById(R.id.acercaApp);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Iniciando sesión...");
        progressDialog.setCancelable(false);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Evento del TextView o Label "Registrarse" para que abra la actividad o pantalla de registrar usuarios

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, RegistrarUsuarios.class);
                startActivity(intent);
            }
        });

        //Evento del TextView o Label "Acerca de" para que abra la actividad o pantalla de acerca de la app
        acercaDe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, AcercaDe.class);
                startActivity(intent); }
        });


        //Evento del botón "Iniciar Sesión"
        BotonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarCamposLogin();

            }


        });

        //Evento del TextView o Label "Recuperar contraseña" para que abra la actividad o pantalla de recuperar contraseña
        recuperarContraseña.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Login.this, RecuperarContrasena.class);
                startActivity(intent);
                finish();
            }

        });
    }

    //Funcion para validar los datos ingresados en los campos de inicio de sesion
    public void validarCamposLogin() {

        String email = campoCorreo.getText().toString().trim();
        String password = campoContraseña.getText().toString().trim();


        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            campoCorreo.setError("Ingrese un correo electrónico válido");
            return;
        } else {

            campoCorreo.setError(null);

        }

        if (password.isEmpty() || password.length() < 12) {
            campoContraseña.setError("La contraseña debe contener al menos 12 caracteres");
            return;
        } else if (!Pattern.compile("[0-9]").matcher(password).find()) {
            campoContraseña.setError("La contraseña debe contener al menos un número");
            return;
        } else {

            campoContraseña.setError(null);


        }
        LogicaInciarSesion(email, password);

    }

    //Funcion para iniciar sesion
    public void LogicaInciarSesion(String correo, String contraseña) {

        progressDialog.show();

        mAuth.signInWithEmailAndPassword(correo, contraseña)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, MenuPrincipal.class);
                            startActivity(intent);
                            finish();
                        } else {
                                progressDialog.dismiss();

                            Toast.makeText(Login.this, "Error al iniciar sesión, credenciales incorrectas", Toast.LENGTH_SHORT).show();


                        }
                    }


                });
    }

}







