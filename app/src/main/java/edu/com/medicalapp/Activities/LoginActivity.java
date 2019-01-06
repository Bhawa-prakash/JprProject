package edu.com.medicalapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.Login;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.com.medicalapp.Models.LoginRequest;
import edu.com.medicalapp.Models.LoginResponse;
import edu.com.medicalapp.R;
import edu.com.medicalapp.Retrofit.RestClient;
import edu.com.medicalapp.utils.Constants;
import edu.com.medicalapp.utils.LogPrefs;
import edu.com.medicalapp.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.edit_email)
    EditText editEmail;

    @BindView(R.id.edit_Passwword)
    EditText editPassword;

    @BindView(R.id.loginButton)
    Button btnLogin;

    @BindView(R.id.login_button)
    LoginButton loginBtn;


    CallbackManager callbackManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        callbackManager=CallbackManager.Factory.create();
        Intent intent=new Intent(LoginActivity.this,MainActivity.class);

        startActivity(intent);
        finish();

        loginFb();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validation();
            }
        });



    }


    //Login Validation
    private void Validation() {

        boolean check=true;
        String Email=editEmail.getText().toString().trim();
        String Password=editPassword.getText().toString().trim();

        if(Email.isEmpty())
        {
            editEmail.setError(getString(R.string.empty_field));
            check=false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
        {
            editEmail.setError(getString(R.string.invalid_email));
            check=false;

        }

        if(Password.isEmpty())
        {
            editPassword.setError(getString(R.string.empty_field));
            check=false;
        }

        if(check==false)
        {

            Toast.makeText(this,getString(R.string.invalid_data), Toast.LENGTH_SHORT).show();
        }
        else
        {
            final LoginRequest loginRequest=new LoginRequest();
            loginRequest.setUserName(Email);
            loginRequest.setPassword(Password);
            if(Utils.isInternetConnected(this))
            {
                  Utils.showProgressDialog(this);
                RestClient.loginUser(loginRequest, new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response)
                    {


                        Utils.dismissProgressDialog();
                        if(response.code()==200 && response.body()!=null)
                        {
                            LoginResponse loginResponse=response.body();
                            LogPrefs.putString(LoginActivity.this, Constants.ACCESS_TOKEN_EMAIL,loginResponse.getToken());
                           // LogPrefs.putString(LoginActivity.this,Constants.NAME,loginResponse.getName());
                          //  LogPrefs.putString(LoginActivity.this,Constants.EMAILID,loginResponse.getEmail());
                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            intent.putExtra(Constants.NAME,loginResponse.getName()!=null?loginResponse.getName():"");
                            intent.putExtra(Constants.EMAILID,loginResponse.getEmail()!=null?loginResponse.getEmail():"");
                            startActivity(intent);
                            finish();

                            }
                            else
                            {
                            Toast.makeText(LoginActivity.this, "Invalid Credential", Toast.LENGTH_SHORT).show();

                            }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {




                    }
                });
            }
            else
            {
                Toast.makeText(this,getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }
        }



    }




    private void loginFb() {


       loginBtn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
           @Override
           public void onSuccess(LoginResult loginResult) {

               Toast.makeText(LoginActivity.this, "login reesult " + loginResult.getAccessToken(), Toast.LENGTH_SHORT).show();

               startActivity(new Intent(LoginActivity.this, MainActivity.class));


           }

           @Override
           public void onCancel() {

               Toast.makeText(LoginActivity.this, "Login Cancel: " + getString(R.string.login_cancel), Toast.LENGTH_SHORT).show();

           }

           @Override
           public void onError(FacebookException error) {
               Toast.makeText(LoginActivity.this, "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();

           }
       });




    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id==R.id.home)
        {
            finish();

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }




}
