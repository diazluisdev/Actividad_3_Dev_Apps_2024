package com.example.actividad_3_dev_apps_luis_felipe_diaz_luiggi_salemi_soto_2024.Usuarios;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.actividad_3_dev_apps_luis_felipe_diaz_luiggi_salemi_soto_2024.Principal.Login;
import com.example.actividad_3_dev_apps_luis_felipe_diaz_luiggi_salemi_soto_2024.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarContrasena extends AppCompatActivity {

    Button botonRecuperarContra;
    TextInputEditText correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recuperar_contrasena);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        botonRecuperarContra = findViewById(R.id.BotonRecuperar);
        correo = findViewById(R.id.campoCorreo);

        //codigo para el boton de recuperar contraseña
        botonRecuperarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validar();

            }


        });


    }
    //codigo para validar el correo
    public void validar() {
        String correoValido = correo.getText().toString();
        if (correoValido.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(correoValido).matches()) {
            correo.setError("Ingrese un correo valido");
            return;

        }
    //si el correo es valido se llama al metodo para enviar el correo para actualizar la contraseña
        enviarCorreo();
    }

    //codigo para enviar el correo
    private void enviarCorreo() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String correoValido = correo.getText().toString();
        auth.sendPasswordResetEmail(correoValido)

                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(RecuperarContrasena.this, "Correo enviado, revisar bandeja de entrada", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RecuperarContrasena.this, Login.class);
                            startActivity(intent);
                            finish();
                        } else {

                            Toast.makeText(RecuperarContrasena.this, "Error al enviar el correo", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }
//codigo para regresar al login con el boton de atras del movil
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
    }
}
