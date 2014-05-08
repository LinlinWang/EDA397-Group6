package com.EDA397.Navigator.NaviGitator.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.EDA397.Navigator.NaviGitator.R;
import com.EDA397.Navigator.NaviGitator.SupportFunctions.Encrypter;
import com.EDA397.Navigator.NaviGitator.SupportFunctions.GitFunctionality;

/**
 * Activity representing the application's login screen.
 */
public class LoginActivity extends Activity {

    private SharedPreferences accounts;
    private SharedPreferences.Editor accEdit;
    private boolean checked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        accounts = getSharedPreferences("StoredAccounts", MODE_PRIVATE);
        accEdit = accounts.edit();
        checked = false;
    }

    /**
     * Function reacting to the screen's login button, leading to evaluation of whatever the user
     * has input into the username/password fields (or lack f said input). Either results in a
     * transition to the main part of the application, or a toast notifying the user of what went
     * wrong.
     */
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
                    //Only logged in while app is "alive".
                    startActivity(new Intent("com.EDA397.Navigator.NaviGitator.Activities.MainActivity"));
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

    /**
     * Used to track the state of the "remember me!" checkbox, so that the app will know if it needs
     * to store the current account info.
     */
    public void remCheckbox(View view) {
        checked = ((CheckBox) view).isChecked();
    }

    /**
     * User is redirected if the account picking button is pressed.
     */
    public void pickAcc(View view) {
        startActivity(new Intent("com.EDA397.Navigator.NaviGitator.Activities.AccountPickerActivity"));
    }

    /**
     * We disable the back button while the user is on the login screen, in order to prevent
     * certain possible issues.
     */
    @Override
    public void onBackPressed() {
    }
}
