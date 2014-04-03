package com.EDA397.Navigator.Navigator;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SimpleCursorAdapter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;


public class AccountPickerActivity extends ListActivity implements AdapterView.OnItemClickListener {

    private SharedPreferences accounts;
    private SharedPreferences current;
    private SharedPreferences.Editor accEdit;
    private SharedPreferences.Editor currEdit;
    Set<String> columns;
    String[] sc;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_account_picker);
        accounts = getSharedPreferences("StoredAccounts", MODE_PRIVATE);
        current = getSharedPreferences("CurrentAccount", MODE_PRIVATE);
        accEdit = accounts.edit();
        currEdit = current.edit();
        // For the cursor adapter, specify which columns go into which views
        columns = accounts.getAll().keySet();
        sc = new String[columns.size()];
        int i =0;
        for (String s : columns){
            sc[i] = s;
            i++;
        }
        listView = getListView();

        //setting adapter
        listView.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.account_item,
                R.id.account_text, sc));
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String s = ((TextView)view.findViewById(R.id.account_text)).getText().toString();
        //Check github login here as well?
        currEdit.clear();
        currEdit.putString("name", s);
        currEdit.putString("pw", accounts.getString(s, ""));
        currEdit.commit();
        startActivity(new Intent("com.EDA397.Navigator.Navigator.MainActivity"));
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
