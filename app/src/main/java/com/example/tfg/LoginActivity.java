package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tfg.model.Pictograma;
import com.example.tfg.remote.APIUtils;
import com.example.tfg.remote.PictogramService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextView textViewLoginTitulo;
    private EditText editTextLoginCorreo,editTextLoginPassword;
    private Button buttonLoginIniciarSesion, buttonLoginRegistro;

    private FirebaseAuth mAuth;

    Button btnAddUser;
    Button btnGetUsersList;
    ListView listView;

    PictogramService userService;
    List<Pictograma> list = new ArrayList<Pictograma>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        inicializarComponentes();
    }


    private void inicializarComponentes() {
        textViewLoginTitulo = findViewById(R.id.textViewLoginTitulo);
        editTextLoginCorreo = findViewById(R.id.editTextLoginCorreo);
        buttonLoginRegistro = findViewById(R.id.buttonLoginRegistro);
        buttonLoginIniciarSesion = findViewById(R.id.buttonLoginIniciarSesion);
        SharedPreferences sharedPreferences = getSharedPreferences("Login", Context.MODE_PRIVATE);

        listView = (ListView) findViewById(R.id.listView);
        userService = APIUtils.getPictoService();




        buttonLoginIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String correo = editTextLoginCorreo.getText().toString();

                if (correo.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Ingrese un correo electrónico válido.", Toast.LENGTH_SHORT).show();
                    return; // Salir del método si el campo de correo está vacío
                }

                mAuth.fetchSignInMethodsForEmail(correo)
                        .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                                if (task.isSuccessful()) {
                                    SignInMethodQueryResult result = task.getResult();
                                    List<String> signInMethods = result.getSignInMethods();
                                    if (signInMethods != null && !signInMethods.isEmpty()) {
                                        // El correo tiene una cuenta asociada
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("correo", correo);
                                        editor.apply();
                                        realizarLogin(correo);
                                    } else {
                                        // El correo no tiene una cuenta asociada
                                        Toast.makeText(LoginActivity.this, "El correo no tiene una cuenta asociada.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // Hubo un error al verificar el correo
                                    Toast.makeText(LoginActivity.this, "Error al verificar el correo.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        buttonLoginRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextRegistroNombreUsuario, editTextRegistroContrasenna;
                Button buttonRegistroCompletarRegistro;

                Dialog dialog = new Dialog(LoginActivity.this);
                dialog.setContentView(R.layout.dialog_registro);
                editTextRegistroNombreUsuario = dialog.findViewById(R.id.editTextRegistroNombreUsuario);
                editTextRegistroContrasenna = dialog.findViewById(R.id.editTextRegistroContrasenna);
                buttonRegistroCompletarRegistro = dialog.findViewById(R.id.buttonRegistroCompletarRegistro);

                buttonRegistroCompletarRegistro.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                        mAuth.createUserWithEmailAndPassword(editTextRegistroNombreUsuario.getText().toString(), editTextRegistroContrasenna.getText().toString())
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                            if (task.isSuccessful()) {
                                                String user = editTextLoginCorreo.getText().toString();
                                                realizarLogin1(user);
                                            } else {
                                                Exception exception = task.getException();
                                                if (exception instanceof FirebaseAuthUserCollisionException) {
                                                    // Ya existe una cuenta con el mismo correo
                                                    Toast.makeText(LoginActivity.this, "Ya existe una cuenta con este correo.", Toast.LENGTH_SHORT).show();
                                                } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
                                                    // Correo o contraseña inválidos
                                                    Toast.makeText(LoginActivity.this, "Correo o contraseña inválidos.", Toast.LENGTH_SHORT).show();
                                                    return;
                                                } else {
                                                    // Otro error al crear la cuenta
                                                    Toast.makeText(LoginActivity.this, "Error, no se ha creado la cuenta.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            dialog.dismiss();
                                    }
                                });
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, "Error. Verifique las credenciales.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        SharedPreferences sharedPreferences = getSharedPreferences("mi_preferences", Context.MODE_PRIVATE);
        String currentUser = sharedPreferences.getString("correo", "");

        if (!currentUser.isEmpty()) {
            realizarLogin(currentUser);
        }
    }

    private void realizarLogin(String currentUser) {
        Intent intent = new Intent(getApplicationContext(), SelectCalendarActivity.class);
        intent.putExtra("email",currentUser);
        startActivity(intent);
        finish();
        //pasar los datos con un bundle

    }

    private void realizarLogin1(String currentUser) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        Toast.makeText(this, "Cuenta creada corectamente.", Toast.LENGTH_SHORT).show();
        intent.putExtra("email",currentUser);
        startActivity(intent);
        finish();
        //pasar los datos con un bundle

    }

}