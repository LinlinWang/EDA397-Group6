package com.EDA397.Navigator.NaviGitator.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

import com.EDA397.Navigator.NaviGitator.Adapters.AccountAdapter;
import com.EDA397.Navigator.NaviGitator.R;
import com.EDA397.Navigator.NaviGitator.SupportFunctions.Encrypter;
import com.EDA397.Navigator.NaviGitator.SupportFunctions.GitFunctionality;

/**
 * Activity for choosing which saved account to log in with (if any exist)
 */
public class AccountPickerActivity extends Activity implements AdapterView.OnItemClickListener {

    private SharedPreferences accounts;
    private SharedPreferences current;
    private SharedPreferences.Editor accEdit;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_picker);
        ArrayList<String> columns = new ArrayList<String>();
        accounts = getSharedPreferences("StoredAccounts", MODE_PRIVATE);
        accEdit = accounts.edit();
        Set<String> c = accounts.getAll().keySet();
        columns.addAll(c);
        listView = (ListView) findViewById(R.id.account_list);
        listView.setClickable(true);
        listView.setOnItemClickListener(this);
        listView.setAdapter(new AccountAdapter(getApplicationContext(),R.layout.account_item,
                R.id.account_text, columns));
    }

    /**
     * Overriden method used to try and log in with the selected account.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final String s = ((TextView)view.findViewById(R.id.account_text)).getText().toString();
        GitFunctionality git = GitFunctionality.getInstance();
        if (git.gitLogin(s, Encrypter.decrypt(accounts.getString(s, "")))) {
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
}
