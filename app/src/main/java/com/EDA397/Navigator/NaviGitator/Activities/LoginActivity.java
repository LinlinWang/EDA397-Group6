package com.EDA397.Navigator.NaviGitator.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.EDA397.Navigator.NaviGitator.R;

public class LoginActivity extends ActionBarActivity {

    private SharedPreferences accounts;
    private SharedPreferences.Editor accEdit;
    private boolean checked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        accounts = getSharedPreferences("StoredAccounts", MODE_PRIVATE);
        accEdit = accounts.edit();
        checked = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void login(View view) {
        final String name = ((EditText)findViewById(R.id.account)).getText().toString().trim().toLowerCase();
        final String pw = ((EditText)findViewById(R.id.password)).getText().toString().trim();
        final String value = accounts.getString(name, "");
        GitFunctionality git = GitFunctionality.getInstance();

        if(name.length() < 1 || pw.length() < 1){
            //Error handling too short/blank field.
            Toast toast = Toast.makeText(getApplicationContext(),
            "Field(s) too short/unfilled", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP| Gravity.LEFT, 0, 0);
            toast.show();
        }
        else if (value.equals("")) {
            // Logging in with New/Unsaved account.
            if (git.gitLogin(name,pw)) {
                if (checked) {
                    //Account remembered even if app is force stopped.
                    accEdit.putString(name, Encrypter.encrypt(pw));
                    accEdit.commit();
                    startActivity(new Intent("com.EDA397.Navigator.NaviGitator.Activities.MainActivity"));
                }
                else {
                    //Only stored while app is "alive".
                    Intent i = new Intent("com.EDA397.Navigator.NaviGitator.Activities.MainActivity");
                    i.putExtra("name", name);
                    i.putExtra("pw", Encrypter.encrypt(pw));
                    startActivity(i);
                }
            }
            else{
                //Invalid GitHub account.
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Not a valid GitHub account", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                toast.show();
            }
        }
        else if (Encrypter.decrypt(value).equals(pw)) {
             if (git.gitLogin(name,pw)) {
                 //Logging in with existing account. Should be linked to successful github login.
                 startActivity(new Intent("com.EDA397.Navigator.NaviGitator.Activities.MainActivity"));
             }
            else{
                 //Password has ben changed via github website.
                 Toast toast = Toast.makeText(getApplicationContext(),
                         "Password is no longer valid", Toast.LENGTH_SHORT);
                 toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
                 toast.show();
             }
        }
        else {
            //Error handling wrong password.
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Missmatch with stored password", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
            toast.show();
        }
    }
    public void remCheckbox(View view) {
        checked = ((CheckBox) view).isChecked();
    }
    public void pickAcc(View view) {
        startActivity(new Intent("com.EDA397.Navigator.NaviGitator.Activities.AccountPickerActivity"));
    }
}
