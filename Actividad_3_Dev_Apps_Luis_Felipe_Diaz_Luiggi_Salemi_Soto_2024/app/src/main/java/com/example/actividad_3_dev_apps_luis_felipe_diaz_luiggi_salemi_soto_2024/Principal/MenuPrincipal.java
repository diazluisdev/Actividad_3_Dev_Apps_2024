package com.example.actividad_3_dev_apps_luis_felipe_diaz_luiggi_salemi_soto_2024.Principal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.actividad_3_dev_apps_luis_felipe_diaz_luiggi_salemi_soto_2024.Fuentes.GestionarFuentes;
import com.example.actividad_3_dev_apps_luis_felipe_diaz_luiggi_salemi_soto_2024.Gastos.GestionarGastos;
import com.example.actividad_3_dev_apps_luis_felipe_diaz_luiggi_salemi_soto_2024.Ingresos.GestionarIngresos;
import com.example.actividad_3_dev_apps_luis_felipe_diaz_luiggi_salemi_soto_2024.R;
import com.example.actividad_3_dev_apps_luis_felipe_diaz_luiggi_salemi_soto_2024.Usuarios.DatosUsuarios;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.view.View;

public class MenuPrincipal extends AppCompatActivity {

    TextView vistaNombreUsuario;
    private FirebaseAuth mAuth;
    Button datosUsuarios, gestionarIngresos, gestionarFuentes, gestionarGastos, Logout;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_principal);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialización de FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        vistaNombreUsuario = findViewById(R.id.nombreUsuario);
        Logout = findViewById(R.id.botonCerrarSesion);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cerrando sesión...");
        progressDialog.setCancelable(false);
        datosUsuarios = findViewById(R.id.datosPersonales);
        gestionarIngresos = findViewById(R.id.gestionarIngreso);
        gestionarFuentes = findViewById(R.id.GestionarFuentes);
        gestionarGastos = findViewById(R.id.GestionarGastos);

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("usuarios").document(userId).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String primerNombre = documentSnapshot.getString("primerNombre");
                                vistaNombreUsuario.setText(primerNombre);
                            }
                        }
                    });
        }

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });

        datosUsuarios.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, DatosUsuarios.class);
            startActivity(intent);
        });

        gestionarIngresos.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, GestionarIngresos.class);
            startActivity(intent);
        });

        gestionarFuentes.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, GestionarFuentes.class);
            startActivity(intent);
        });

        gestionarGastos.setOnClickListener(v -> {
            Intent intent = new Intent(MenuPrincipal.this, GestionarGastos.class);
            startActivity(intent);
        });
    }

    private void cerrarSesion() {
        progressDialog.show();

        mAuth.signOut();

        // Retrasar el inicio de la siguiente actividad
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                Toast.makeText(MenuPrincipal.this, "Sesión cerrada exitosamente", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MenuPrincipal.this, Login.class);
                startActivity(intent);
                finish();
            }
        }, 2000); // 2 segundos de retraso
    }
}
