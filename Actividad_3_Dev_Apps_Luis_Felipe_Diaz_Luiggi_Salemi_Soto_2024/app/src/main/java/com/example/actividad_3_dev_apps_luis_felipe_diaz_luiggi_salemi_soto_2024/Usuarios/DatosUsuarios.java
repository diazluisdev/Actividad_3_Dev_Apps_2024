package com.example.actividad_3_dev_apps_luis_felipe_diaz_luiggi_salemi_soto_2024.Usuarios;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.actividad_3_dev_apps_luis_felipe_diaz_luiggi_salemi_soto_2024.Principal.Login;
import com.example.actividad_3_dev_apps_luis_felipe_diaz_luiggi_salemi_soto_2024.Principal.MenuPrincipal;
import com.example.actividad_3_dev_apps_luis_felipe_diaz_luiggi_salemi_soto_2024.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class DatosUsuarios extends AppCompatActivity {

    TextInputEditText password, email, pregunta, respuesta, telefono, pais, currentPasswordInput;
    Button guardar, eliminarUser;

    private FirebaseAuth mAuth;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_datos_usuarios);

        // Set up the system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        password = findViewById(R.id.updateContraseña);
        email = findViewById(R.id.updateCorreo);
        pregunta = findViewById(R.id.updatePregunta);
        respuesta = findViewById(R.id.updateRespuesta);
        telefono = findViewById(R.id.updateTelefono);
        pais = findViewById(R.id.updatePais);
        currentPasswordInput = findViewById(R.id.currentPasswordInput);
        guardar = findViewById(R.id.actualizarDatos);
        eliminarUser = findViewById(R.id.eliminarUsuario);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        cargarDatosUsuario();

        guardar.setOnClickListener(v -> {
            validarCampos();
        });

        eliminarUser.setOnClickListener(v -> {
            eliminarCuentaUsuario();
        });
    }

    private void cargarDatosUsuario() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "No hay usuario autenticado.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();
        DocumentReference userRef = db.collection("usuarios").document(userId);

        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        telefono.setText(documentSnapshot.getString("telefono"));
                        email.setText(user.getEmail());
                        pregunta.setText(documentSnapshot.getString("pregunta"));
                        respuesta.setText(documentSnapshot.getString("respuesta"));
                        pais.setText(documentSnapshot.getString("pais"));
                    } else {
                        Toast.makeText(this, "No se encontraron datos del usuario.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar los datos del usuario.", Toast.LENGTH_SHORT).show();
                });
    }


    public void validarCampos() {
        String contraseña = password.getText().toString().trim();
        String correo = email.getText().toString().trim();
        String preguntaContraseña = pregunta.getText().toString().trim();
        String respuestaContraseña = respuesta.getText().toString().trim();
        String telefonoUsuario = telefono.getText().toString().trim();
        String paisUsuario = pais.getText().toString().trim();
        String currentPassword = currentPasswordInput.getText().toString().trim();


        if (correo.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            email.setError("Ingrese un correo electrónico válido");
            return;
        } else {
            email.setError(null);
        }

        if (preguntaContraseña.isEmpty()) {
            pregunta.setError("Ingrese su pregunta de seguridad");
            return;
        } else {
            pregunta.setError(null);
        }

        if (respuestaContraseña.isEmpty()) {
            respuesta.setError("Ingrese su respuesta de seguridad");
            return;
        } else {
            respuesta.setError(null);
        }

        if (telefonoUsuario.isEmpty()) {
            telefono.setError("Ingrese su teléfono");
            return;
        } else {
            telefono.setError(null);
        }

        if (paisUsuario.isEmpty()) {
            pais.setError("Ingrese su país");
            return;
        } else {
            pais.setError(null);
        }

        if (currentPassword.isEmpty()) {
            currentPasswordInput.setError("Ingrese su contraseña actual");
            return;
        } else {
            currentPasswordInput.setError(null);
        }

        actualizarDatosUsuario(telefonoUsuario, correo, contraseña, preguntaContraseña, respuestaContraseña, paisUsuario, currentPassword);
    }

    public void actualizarDatosUsuario(String telefono, String email, String password, String pregunta, String respuesta, String pais, String currentPassword) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "No hay usuario autenticado.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();
        DocumentReference userRef = db.collection("usuarios").document(userId);

        // Datos a actualizar en Firestore
        Map<String, Object> updates = new HashMap<>();
        updates.put("telefono", telefono);
        updates.put("email", email);
        updates.put("pregunta", pregunta);
        updates.put("respuesta", respuesta);
        updates.put("pais", pais);

        // Reautenticar al usuario antes de actualizar su email y contraseña
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
        user.reauthenticate(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Actualizar datos en Firestore
                        userRef.update(updates)
                                .addOnSuccessListener(aVoid -> {
                                    // Actualizar el correo electrónico
                                    user.updateEmail(email)
                                            .addOnCompleteListener(task1 -> {
                                                if (task1.isSuccessful()) {
                                                    // Actualizar la contraseña
                                                    user.updatePassword(password)
                                                            .addOnCompleteListener(task2 -> {
                                                                if (task2.isSuccessful()) {
                                                                    Toast.makeText(this, "Datos actualizados exitosamente.", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(this, "Error al actualizar la contraseña.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                } else {
                                                    Toast.makeText(this, "Error al actualizar el correo.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error al actualizar los datos en Firestore.", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(this, "Error en la reautenticación. Por favor, verifica tu contraseña actual.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void eliminarCuentaUsuario() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "No hay usuario autenticado.", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentPassword = currentPasswordInput.getText().toString().trim();
        if (currentPassword.isEmpty()) {
            Toast.makeText(this, "Por favor, ingrese su contraseña actual.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();
        DocumentReference userRef = db.collection("usuarios").document(userId);

        // Eliminar el usuario de Firebase Authentication
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
        user.reauthenticate(credential)
                .addOnSuccessListener(aVoid -> {
                    user.delete()
                            .addOnSuccessListener(aVoid1 -> {
                                // Eliminar los datos del usuario de Firestore
                                userRef.delete()
                                        .addOnSuccessListener(aVoid2 -> {
                                            Toast.makeText(this, "Cuenta y datos eliminados exitosamente.", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(DatosUsuarios.this, Login.class));
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(this, "Error al eliminar los datos del usuario de Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Error al eliminar la cuenta de autenticación: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al reautenticar al usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DatosUsuarios.this, MenuPrincipal.class);
        startActivity(intent);
        finish();
    }

}





