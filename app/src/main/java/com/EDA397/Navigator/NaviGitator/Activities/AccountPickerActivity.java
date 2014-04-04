package com.EDA397.Navigator.NaviGitator.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

import com.EDA397.Navigator.NaviGitator.R;




public class AccountPickerActivity extends Activity implements AdapterView.OnItemClickListener {

    private SharedPreferences accounts;
    private SharedPreferences current;
    private SharedPreferences.Editor accEdit;
    private SharedPreferences.Editor currEdit;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_picker);
        ArrayList<String> columns = new ArrayList<String>();
        accounts = getSharedPreferences("StoredAccounts", MODE_PRIVATE);
        current = getSharedPreferences("CurrentAccount", MODE_PRIVATE);
        accEdit = accounts.edit();
        currEdit = current.edit();
        // For the cursor adapter, specify which columns go into which views
        Set<String> c = accounts.getAll().keySet();
        columns.addAll(c);
        listView = (ListView) findViewById(R.id.account_list);
        listView.setClickable(true);
        listView.setOnItemClickListener(this);

        //setting adapter
        listView.setAdapter(new AccountAdapter(getApplicationContext(),R.layout.account_item,
                R.id.account_text, columns));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final String s = ((TextView)view.findViewById(R.id.account_text)).getText().toString();
        //Check github login here as well?
        currEdit.clear();
        currEdit.putString("name", s);
        currEdit.putString("pw", accounts.getString(s, ""));
        currEdit.commit();
        startActivity(new Intent("com.EDA397.Navigator.NaviGitator.Activities.MainActivity"));
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
}
