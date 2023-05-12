package com.example.tfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;

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
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword);
        buttonLoginRegistro = findViewById(R.id.buttonLoginRegistro);
        buttonLoginIniciarSesion = findViewById(R.id.buttonLoginIniciarSesion);

        btnAddUser = (Button) findViewById(R.id.btnAddUser);
        btnGetUsersList = (Button) findViewById(R.id.btnGetUsersList);
        listView = (ListView) findViewById(R.id.listView);
        userService = APIUtils.getUserService();



        buttonLoginIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signInWithEmailAndPassword(editTextLoginCorreo.getText().toString(), editTextLoginPassword.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    realizarLogin(user);
                                } else {
                                    Toast.makeText(LoginActivity.this, "Login fallido", Toast.LENGTH_SHORT).show();
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

                        mAuth.createUserWithEmailAndPassword(editTextRegistroNombreUsuario.getText().toString(), editTextRegistroContrasenna.getText().toString())
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            realizarLogin(user);
                                        } else {
                                            Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                        dialog.dismiss();
                                    }
                                });
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
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            realizarLogin(currentUser);
        }
    }

    private void realizarLogin(FirebaseUser currentUser) {
        Intent intent = new Intent(getApplicationContext(), SelectCalendarActivity.class);
        intent.putExtra("email",currentUser.getEmail());
        startActivity(intent);
        finish();
        //pasar los datos con un bundle

    }

}