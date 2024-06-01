package com.example.actividad_3_dev_apps_luis_felipe_diaz_luiggi_salemi_soto_2024.Usuarios;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegistrarUsuarios extends AppCompatActivity {

    TextInputEditText contraseñaRegistro, correoElectronico, primerNombre, segundoNombre, primerApellido, segundoApellido, preguntaContraseña, respuestaContraseña, cedula, telefono, pais;
    Button registrarse;
    TextView errorID;
    Spinner tipoIdentidad;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuarios);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtener los datos ingresados en los text input edit text
        // y demas elementos del formulario
        correoElectronico = findViewById(R.id.correoElectronico);
        contraseñaRegistro = findViewById(R.id.contraseñaRegistro);
        tipoIdentidad = findViewById(R.id.tipoDocumento);
        errorID = findViewById(R.id.errorTipoID);
        primerNombre = findViewById(R.id.primerNombre);
        segundoNombre = findViewById(R.id.segundoNombre);
        primerApellido = findViewById(R.id.primerApellido);
        segundoApellido = findViewById(R.id.segundoApellido);
        cedula = findViewById(R.id.registroCedula);
        preguntaContraseña = findViewById(R.id.preguntaContra);
        respuestaContraseña = findViewById(R.id.respuestaContra);
        registrarse = findViewById(R.id.registrarse);
        telefono = findViewById(R.id.telefonoUsuario);
        pais = findViewById(R.id.paisUsuario);

        registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validarCampos();

            }
        });

        mAuth = FirebaseAuth.getInstance();

        // Este es el código para el comportamiento del componente spinner para que el usuario pueda elegir una opción
        String[] opcionesTipoIdentificacion = {"CC", "TI", "CE", "LC", "PPT"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(RegistrarUsuarios.this, android.R.layout.simple_spinner_item, opcionesTipoIdentificacion);
        tipoIdentidad.setAdapter(adapter);
    }

    //Este codigo es para validar los datos ingresados en el formulario
    public void validarCampos() {

        String email = correoElectronico.getText().toString().trim();
        String password = contraseñaRegistro.getText().toString().trim();
        String tipoDocumentoText = tipoIdentidad.getSelectedItem().toString().trim();
        String primerNombreText = primerNombre.getText().toString().trim();
        String segundoNombreText = segundoNombre.getText().toString().trim();
        String primerApellidoText = primerApellido.getText().toString().trim();
        String segundoApellidoText = segundoApellido.getText().toString().trim();
        String preguntaContraseñaText = preguntaContraseña.getText().toString().trim();
        String respuestaContraseñaText = respuestaContraseña.getText().toString().trim();
        String cedulaText = cedula.getText().toString().trim();
        String telefonoText = telefono.getText().toString().trim();
        String paisText = pais.getText().toString().trim();

        if (primerNombreText.isEmpty()) {
            primerNombre.setError("Ingrese su primer nombre");
            return;
        } else {
            primerNombre.setError(null);
        }

        if (segundoNombreText.isEmpty()) {
            segundoNombre.setError("Ingrese su segundo nombre");
            return;
        } else {
            segundoNombre.setError(null);
        }

        if (tipoDocumentoText.isEmpty()) {
            errorID.setText("Seleccione su tipo de documento");
            return;
        } else {
            errorID.setText("");
        }

        if (primerApellidoText.isEmpty()) {
            primerApellido.setError("Ingrese su primer apellido");
            return;
        } else {
            primerApellido.setError(null);
        }
        if (segundoApellidoText.isEmpty()) {
            segundoApellido.setError("Ingrese su segundo apellido");
            return;
        } else {
            segundoApellido.setError(null);
        }

        if (cedulaText.isEmpty()) {
            cedula.setError("Ingrese su número de cédula");
            return;
        } else {
            cedula.setError(null);
        }
        if (preguntaContraseñaText.isEmpty()) {
            preguntaContraseña.setError("Ingrese su pregunta de seguridad");
            return;
        } else {
            preguntaContraseña.setError(null);
        }
        if (respuestaContraseñaText.isEmpty()) {
            respuestaContraseña.setError("Ingrese su respuesta de seguridad");
            return;
        } else {
            respuestaContraseña.setError(null);
        }

        if (telefonoText.isEmpty()) {
            telefono.setError("Ingrese su número de teléfono");
            return;
        } else {
            telefono.setError(null);

        }
        if (paisText.isEmpty()) {
            pais.setError("Ingrese su país");
            return;

        } else {
            pais.setError(null);
        }


        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            correoElectronico.setError("Ingrese un correo electrónico válido");
            return;
        } else {

            correoElectronico.setError(null);

        }

        if (password.isEmpty() || password.length() < 12) {
            contraseñaRegistro.setError("La contraseña debe contener al menos 12 caracteres");
            return;
        } else if (!Pattern.compile("[0-9]").matcher(password).find()) {
            contraseñaRegistro.setError("La contraseña debe contener al menos un número");
            return;
        } else {

            contraseñaRegistro.setError(null);


        }
        registrarUsuario(email, password);

    }

    //Este es el código para registrar un usuario en Firestore y Firebase Authentication una ves que le hayamos pasado los datos del metodo guardarInformacionUsuario
    public void registrarUsuario(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registro exitoso
                            FirebaseUser user = mAuth.getCurrentUser();
                            guardarInformacionUsuario(user.getUid(), primerNombre.getText().toString(), segundoNombre.getText().toString(), tipoIdentidad.getSelectedItem().toString().trim(), primerApellido.getText().toString(), segundoApellido.getText().toString(), preguntaContraseña.getText().toString(), respuestaContraseña.getText().toString(), cedula.getText().toString(), telefono.getText().toString(), pais.getText().toString());
                            Toast.makeText(RegistrarUsuarios.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistrarUsuarios.this, Login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Error en el registro
                            Toast.makeText(RegistrarUsuarios.this, "Error al registrar el usuario.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    //Este es el código para guardar la información adicional al correo y contraseña del usuario en Firestore antes de poder registrarlo en la base de datos de Firestore
    private void guardarInformacionUsuario(String userId, String primerNombre, String segundoNombre, String tipoId, String primerApellido, String segundoApellido, String pregunta, String respuesta, String cedula, String tel, String pais) {
        // Referencia a la colección de usuarios en Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("usuarios").document(userId);

        // Crear un objeto con la información del usuario
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("primerNombre", primerNombre);
        usuario.put("segundoNombre", segundoNombre);
        usuario.put("primerApellido", primerApellido);
        usuario.put("segundoApellido", segundoApellido);
        usuario.put("pregunta", pregunta);
        usuario.put("respuesta", respuesta);
        usuario.put("cedula", cedula);
        usuario.put("tipoIdentidad", tipoId);
        usuario.put("telefono", tel);
        usuario.put("pais", pais);

        // Guardar la información del usuario en Firestore
        userRef.set(usuario)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Informar al usuario que se ha registrado correctamente
                        Toast.makeText(RegistrarUsuarios.this, "Usuario registrado exitosamente.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Manejar el error al guardar la información del usuario en Firestore
                        Toast.makeText(RegistrarUsuarios.this, "Error al guardar la información del usuario.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
