package com.example.covid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
    EditText usuario, contraseña;
    Button login, cuenta;
    //Metodos de firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setTitle("Inicio De Sesión");

        usuario=(EditText)findViewById(R.id.userFragTxt);
        contraseña=(EditText)findViewById(R.id.passFragTxt);


        login = (Button) findViewById(R.id.loginBtn);
        cuenta=(Button) findViewById(R.id.crearbtn);
        //Obtener intancia de ambos metodos
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null)
                {
                    Log.d("TAG","Estas logeado: "+user.getUid());
                }else{
                    Log.d("TAG", "No esta logeado");
                }
            }
        };
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singIn();
            }
        });

        cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),crearcuenta.class);
                startActivityForResult(intent,0);
            }
        });
    }
    //Metodo para iniciar sesion
    private void singIn()
    {
        String email,password;
        if(!checkFields())
        {
        }else{
            email = usuario.getText().toString();
            password = contraseña.getText().toString();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast toast1 =
                                Toast.makeText(getApplicationContext(),
                                        "Inicio de Sesion Correcto", Toast.LENGTH_SHORT);
                        toast1.show();
                        startActivity(new Intent(login.this,Covid.class));
                        updateState("BIENVENIDO");
                    }else{
                        Toast toast1 =
                                Toast.makeText(getApplicationContext(),
                                        "No se pudo Iniciar Sesion", Toast.LENGTH_SHORT);
                        toast1.show();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if(e instanceof FirebaseAuthInvalidCredentialsException)
                    {
                        updateState("Contraseña Equivocada");
                    }
                    else if(e instanceof FirebaseAuthInvalidUserException)
                    {
                        updateState("Usuario Incorrecto");
                    }
                    else{
                        updateState(e.getLocalizedMessage());
                    }
                }
            });
        }
    }
    private void updateState(String mensaje)
    {
        Toast toast1 =
                Toast.makeText(getApplicationContext(),
                        mensaje, Toast.LENGTH_LONG);
        toast1.show();
    }

    private boolean checkFields()
    {
        String correocheck, contraseñacheck;
        correocheck = usuario.getText().toString();
        contraseñacheck = contraseña.getText().toString();
        if(correocheck.isEmpty())
        {
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "Escribe un correo valido", Toast.LENGTH_SHORT);
            toast1.show();
            return false;
        }
        if(contraseñacheck.isEmpty())
        {
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "Escribe una contraseña valida", Toast.LENGTH_SHORT);
            toast1.show();
            return  false;
        }
        return true;
    }
}
