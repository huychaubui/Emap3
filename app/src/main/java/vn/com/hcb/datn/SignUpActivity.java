package vn.com.hcb.datn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import vn.com.hcb.model.User;

public class SignUpActivity extends AppCompatActivity {
    DatabaseReference mDataUser;
    EditText txtEmail, txtPassword, txtDisplay_Name;
    Button btnSignUp;
    TextView tvLogin;
    ProgressDialog progressDialog;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        addControls();
        addEvents();
    }
    private void addControls() {
        auth = FirebaseAuth.getInstance();
        txtEmail = (EditText) findViewById(R.id.txtEmail_signUp);
        txtPassword = (EditText) findViewById(R.id.txtPassword_signUp);
        txtDisplay_Name = (EditText) findViewById(R.id.txtDisplay_Name);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);
        tvLogin = (TextView) findViewById(R.id.tvLogin);
        progressDialog = new ProgressDialog(this);

    }



    private void addEvents() {
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                final String name = txtDisplay_Name.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    txtEmail.setError("Nhập Email");
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    txtEmail.setError("Nhập Tên");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    txtPassword.setError("Nhập mật khẩu");
                    return;
                }

                if (password.length() < 6) {
                    txtPassword.setError("Mật khẩu quá ngắn!");
                    return;
                }

                progressDialog.setMessage("Đang đăng ký");
                progressDialog.show();
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.

                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Đăng ký thất bại! Vui lòng kiểm tra lại.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    String Uid =  task.getResult().getUser().getUid().toString();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(name).build();
                                    task.getResult().getUser().updateProfile(profileUpdates);
                                    mDataUser = FirebaseDatabase.getInstance().getReference("User").child(Uid);

                                    User user = new User(email, name, null);
                                    mDataUser.setValue(user, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if(databaseError==null){
                                                progressDialog.dismiss();
                                                Toast.makeText(SignUpActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                                                i.putExtra("email", txtEmail.getText().toString().trim());
                                                startActivity(i);
                                                finish();
                                            }
                                        }
                                    });

                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
