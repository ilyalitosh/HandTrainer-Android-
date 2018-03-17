package com.litosh.ilya.handtrainer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.litosh.ilya.handtrainer.db.DBService;
import com.litosh.ilya.handtrainer.db.models.Activity;
import com.litosh.ilya.handtrainer.db.models.Person;

import java.util.List;

import io.realm.Realm;

/**
 * Created by ilya_ on 17.03.2018.
 */

public class AuthActivity extends AppCompatActivity {

    private LinearLayout authForm, registerForm;
    private Button registerButton, registerButtonSubmit, registerButtonCancelSubmit, enterButton;
    private EditText inputLoginAuth, inputPassAuth, inputLoginRegister, inputPassRegister;
    private ProgressDialog pDialog;
    private DBService dbService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_page);

        Realm.init(this);
        dbService = new DBService();
        initComponents();
        initListeners();

    }

    public void initComponents(){
        authForm = findViewById(R.id.authform_authpage);
        registerForm = findViewById(R.id.registerform_authpage);
        registerButton = findViewById(R.id.buttonauth_register_authform_authpage);
        registerButtonSubmit = findViewById(R.id.buttonregister_submit_registerform_authpage);
        registerButtonCancelSubmit = findViewById(R.id.buttonregister_cancel_registerform_authpage);
        enterButton = findViewById(R.id.buttonauth_enter_authform_authpage);
        inputLoginAuth = findViewById(R.id.inputauth_login_authform_authpage);
        inputPassAuth = findViewById(R.id.inputauth_pass_authform_authpage);
        inputLoginRegister = findViewById(R.id.inputregister_login_registerform_authpage);
        inputPassRegister = findViewById(R.id.inputregister_pass_registerform_authpage);
        pDialog = new ProgressDialog(this);
    }

    public void initListeners(){
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog.show();
                authForm.setVisibility(View.GONE);
                registerForm.setVisibility(View.VISIBLE);
                pDialog.dismiss();
            }
        });
        registerButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog.show();
                registerForm.setVisibility(View.GONE);
                authForm.setVisibility(View.VISIBLE);
                Person person = new Person();
                person.setLogin(inputLoginRegister.getText().toString());
                person.setPassword(inputPassRegister.getText().toString());
                dbService.addPerson(person);
                registerForm.setVisibility(View.GONE);
                authForm.setVisibility(View.VISIBLE);
                pDialog.dismiss();
                Toast.makeText(AuthActivity.this, "Успешная регистрация", Toast.LENGTH_SHORT).show();
            }
        });
        registerButtonCancelSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pDialog.show();
                registerForm.setVisibility(View.GONE);
                authForm.setVisibility(View.VISIBLE);
                pDialog.dismiss();
            }
        });
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!inputLoginAuth.getText().toString().equals("") && !inputPassAuth.getText().toString().equals("")){
                    pDialog.show();
                    List<Person> list = dbService.getPersons();
                    if(list.size() != 0){
                        for(int i = 0; i < list.size(); i++){
                            if(list.get(i).getLogin().equals(inputLoginAuth.getText().toString())){
                                if(list.get(i).getPassword().equals(inputPassAuth.getText().toString())){
                                    User.setId(0L);
                                    User.setActivity(1);
                                    User.setUserId(list.get(i).getId());
                                    User.setUserLogin(list.get(i).getLogin());
                                    Activity activity = new Activity();
                                    activity.setId(0L);
                                    activity.setActivity(1);
                                    activity.setUserId(list.get(i).getId());
                                    dbService.updateActivity(activity);
                                    pDialog.dismiss();
                                    Intent intent = new Intent(AuthActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    break;
                                }else{
                                    pDialog.dismiss();
                                    Toast.makeText(AuthActivity.this, "Неверный пароль", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                            if(i == list.size() - 1){
                                pDialog.dismiss();
                                Toast.makeText(AuthActivity.this, "Нет такого пользователя", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else{
                        pDialog.dismiss();
                        Toast.makeText(AuthActivity.this, "Нет пользователей, зарегистрируйтесь", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(AuthActivity.this, "Какое-то поле не заполнено", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
