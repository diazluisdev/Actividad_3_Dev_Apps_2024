package com.example.actividad_3_dev_apps_luis_felipe_diaz_luiggi_salemi_soto_2024.Gastos;

import static androidx.constraintlayout.widget.Constraints.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.actividad_3_dev_apps_luis_felipe_diaz_luiggi_salemi_soto_2024.Principal.Login;
import com.example.actividad_3_dev_apps_luis_felipe_diaz_luiggi_salemi_soto_2024.Principal.MenuPrincipal;
import com.example.actividad_3_dev_apps_luis_felipe_diaz_luiggi_salemi_soto_2024.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class GestionarGastos extends AppCompatActivity {

    private EditText idGastos, fechaIngreso, nombreGasto, valorGasto, categoriaGasto, descripcionGasto;
    private Button botonGuardar, botonBuscar, botonEditar, botonEliminar;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gestionar_gastos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Inicializar elementos de la interfaz
        idGastos = findViewById(R.id.idGastos);
        fechaIngreso = findViewById(R.id.fechaIngreso);
        nombreGasto = findViewById(R.id.nombreGasto);
        valorGasto = findViewById(R.id.valorGasto);
        categoriaGasto = findViewById(R.id.categoriaGasto);
        descripcionGasto = findViewById(R.id.descripcionGasto);

        botonGuardar = findViewById(R.id.botonGuardar);
        botonBuscar = findViewById(R.id.botonBuscar);
        botonEditar = findViewById(R.id.botonEditar);
        botonEliminar = findViewById(R.id.botonEliminar);

        //eventos para los botones de la interfaz de usuario
        botonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarGasto();
            }
        });


        botonEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarGasto();
            }
        });


        botonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarGasto();
            }
        });


        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarGasto();
            }
        });


    }

    //funciones para los botones de la interfaz de usuario
    private void buscarGasto() {
        String id = idGastos.getText().toString().trim();
        if (id.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese el ID del gasto", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            db.collection("usuarios").document(userId).collection("gastos").document(id)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    fechaIngreso.setText(document.getString("fechaIngreso"));
                                    nombreGasto.setText(document.getString("nombreGasto"));
                                    valorGasto.setText(document.getString("valorGasto"));
                                    categoriaGasto.setText(document.getString("categoriaGasto"));
                                    descripcionGasto.setText(document.getString("descripcionGasto"));
                                } else {
                                    Toast.makeText(GestionarGastos.this, "No se encontró el gasto", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d(TAG, "Error al obtener el documento: ", task.getException());
                            }
                        }
                    });
        }
    }

    private void editarGasto() {
        String id = idGastos.getText().toString().trim();
        String fecha = fechaIngreso.getText().toString().trim();
        String nombre = nombreGasto.getText().toString().trim();
        String valor = valorGasto.getText().toString().trim();
        String categoria = categoriaGasto.getText().toString().trim();
        String descripcion = descripcionGasto.getText().toString().trim();

        if (id.isEmpty() || fecha.isEmpty() || nombre.isEmpty() || valor.isEmpty() || categoria.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            Map<String, Object> gasto = new HashMap<>();
            gasto.put("fechaIngreso", fecha);
            gasto.put("nombreGasto", nombre);
            gasto.put("valorGasto", valor);
            gasto.put("categoriaGasto", categoria);
            gasto.put("descripcionGasto", descripcion);

            db.collection("usuarios").document(userId).collection("gastos").document(id)
                    .update(gasto)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(GestionarGastos.this, "Gasto actualizado correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "Error al actualizar el documento: ", task.getException());
                            }
                        }
                    });
        }
    }

    private void eliminarGasto() {
        String id = idGastos.getText().toString().trim();
        if (id.isEmpty()) {
            Toast.makeText(this, "Por favor ingrese el ID del gasto", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            db.collection("usuarios").document(userId).collection("gastos").document(id)
                    .delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(GestionarGastos.this, "Gasto eliminado correctamente", Toast.LENGTH_SHORT).show();
                                limpiarCampos();
                            } else {
                                Log.d(TAG, "Error al eliminar el documento: ", task.getException());
                            }
                        }
                    });
        }
    }

    private void guardarGasto() {
        String id = idGastos.getText().toString().trim();
        String fecha = fechaIngreso.getText().toString().trim();
        String nombre = nombreGasto.getText().toString().trim();
        String valor = valorGasto.getText().toString().trim();
        String categoria = categoriaGasto.getText().toString().trim();
        String descripcion = descripcionGasto.getText().toString().trim();

        if (id.isEmpty() || fecha.isEmpty() || nombre.isEmpty() || valor.isEmpty() || categoria.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            Map<String, Object> gasto = new HashMap<>();
            gasto.put("idGasto", id);
            gasto.put("fechaIngreso", fecha);
            gasto.put("nombreGasto", nombre);
            gasto.put("valorGasto", valor);
            gasto.put("categoriaGasto", categoria);
            gasto.put("descripcionGasto", descripcion);

            db.collection("usuarios").document(userId).collection("gastos").document(id)
                    .set(gasto)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(GestionarGastos.this, "Gasto guardado correctamente", Toast.LENGTH_SHORT).show();
                                limpiarCampos();
                            } else {
                                Log.d(TAG, "Error al guardar el documento: ", task.getException());
                            }
                        }
                    });
        }
    }

    private void limpiarCampos() {
        idGastos.setText("");
        fechaIngreso.setText("");
        nombreGasto.setText("");
        valorGasto.setText("");
        categoriaGasto.setText("");
        descripcionGasto.setText("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MenuPrincipal.class);
        startActivity(intent);
        finish();
    }


}