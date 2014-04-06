package com.EDA397.Navigator.NaviGitator.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.EDA397.Navigator.NaviGitator.R;

import org.eclipse.egit.github.core.Repository;

import java.util.ArrayList;
import java.util.Set;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {

    private SharedPreferences current;
    private SharedPreferences.Editor currEdit;
    private SharedPreferences repoList;
    private SharedPreferences.Editor repoEdit;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        current = getSharedPreferences("CurrentAccount", MODE_PRIVATE);
        currEdit = current.edit();
        repoList = getSharedPreferences("Repositories", MODE_PRIVATE);
        repoEdit = repoList.edit();

        if (this.getIntent().getStringExtra("name") == null &&
           (current.getString("name", "").equals(""))){
            startActivity(new Intent("com.EDA397.Navigator.NaviGitator.Activities.LoginActivity"));
        }
        else{
            ArrayList<String> columns = new ArrayList<String>();
            Set<String> c = repoList.getAll().keySet();
            columns.addAll(c);
            listView = (ListView) findViewById(R.id.repo_list);
            listView.setClickable(true);
            listView.setOnItemClickListener(this);

            //setting adapter
            listView.setAdapter(new RepoAdapter(getApplicationContext(),R.layout.repo_item,
                    R.id.repo_text, columns));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final String s = ((TextView)view.findViewById(R.id.repo_text)).getText().toString();
        Toast toast = Toast.makeText(getApplicationContext(),
                s, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP | Gravity.LEFT, 0, 0);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            currEdit.clear();
            currEdit.commit();
            startActivity(new Intent("com.EDA397.Navigator.NaviGitator.Activities.LoginActivity"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        currEdit.clear();
        currEdit.commit();
        super.onBackPressed();
    }
}
