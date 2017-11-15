package com.example.motoyama.myfavmember;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private Realm mRealm;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(MainActivity.this,MyFavMemberEditActivity.class));
            }
        });
        mRealm = Realm.getDefaultInstance();

        mListView = (ListView) findViewById(R.id.listView);
        RealmResults<MyFavMember> myfavmembers
                =mRealm.where(MyFavMember.class).findAll();
        MyFavMemberAdapter adapter = new MyFavMemberAdapter(myfavmembers);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent,View view,
                                            int position,long id){
                        MyFavMember myfavmember =
                                (MyFavMember) parent.getItemAtPosition(position);
                        startActivity(new Intent(MainActivity.this,
                                MyFavMemberEditActivity.class)
                                .putExtra("myfavmember_id",myfavmember.getId()));
                    }
                });
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mRealm.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
