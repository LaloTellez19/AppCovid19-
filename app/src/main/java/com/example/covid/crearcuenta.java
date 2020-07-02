package com.example.covid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class crearcuenta extends AppCompatActivity {
    Button guardar, login;
    EditText email, contraseña, confir;
    //Metodos de firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Registro");
        setContentView(R.layout.activity_crearcuenta);
        guardar = (Button) findViewById(R.id.savebtn);
        login = (Button) findViewById(R.id.crearbtn);
        email = (EditText) findViewById(R.id.emailTxt);
        contraseña = (EditText) findViewById(R.id.passTxt);
        confir = (EditText) findViewById(R.id.conpassTxt);

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
                Intent intent = new Intent(v.getContext(),login.class);
                startActivityForResult(intent,0);
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearUsuario();
            }
        });
    }

    //Metodo para verificar campos a llenar
    private boolean checkFields()
    {
        String correocheck, contraseñacheck;
        correocheck = email.getText().toString();
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
    private boolean checkPassEquals()
    {
        String password1, password2;
        password1 = contraseña.getText().toString();
        password2 = confir.getText().toString();
        if(!password1.equals(password2))
        {
            Toast toast1 =
                    Toast.makeText(getApplicationContext(),
                            "Las contraseñas no coinciden", Toast.LENGTH_SHORT);
            toast1.show();
            return  false;
        }
        return true;
    }

    //Metodo crear cuenta
    private void crearUsuario()
    {
        String emailcreate,passwordcreate;
        //Validr que no exista cuenta
        if(!checkFields())
        {
        }else if(!checkPassEquals())
        {
        } else{
            //Asignar valores de los txt
            emailcreate = email.getText().toString();
            passwordcreate = contraseña.getText().toString();
            //Metodo para validar creacion de usuario
            mAuth.createUserWithEmailAndPassword(emailcreate,passwordcreate).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Toast toast1 =
                                Toast.makeText(getApplicationContext(),
                                        "Registro Exitoso Inice Sesion", Toast.LENGTH_LONG);
                        toast1.show();
                        startActivity(new Intent(crearcuenta.this,login.class));
                    }else{
                        Toast toast1 =
                                Toast.makeText(getApplicationContext(),
                                        "No se pudo crear el usuario", Toast.LENGTH_LONG);
                        toast1.show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if(e instanceof FirebaseAuthUserCollisionException)
                    {
                        updateState("Correo existente, intente con uno nuevo");
                    }else
                    {
                        updateState(e.getLocalizedMessage());
                    }
                }
            })

            ;
        }
    }
    private void updateState(String mensaje)
    {
        Toast toast1 =
                Toast.makeText(getApplicationContext(),
                        mensaje, Toast.LENGTH_SHORT);
        toast1.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthStateListener != null)
        {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
}
