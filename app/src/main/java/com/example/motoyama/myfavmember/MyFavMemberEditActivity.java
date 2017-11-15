package com.example.motoyama.myfavmember;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MyFavMemberEditActivity extends AppCompatActivity {

    private Realm mRealm;
    EditText mDateEdit;
    EditText mTitleEdit;
    EditText mDetailEdit;
    Button mDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_member_edit);

        mRealm = Realm.getDefaultInstance();
        mDateEdit = (EditText) findViewById(R.id.dateEdit);
        mTitleEdit = (EditText)findViewById(R.id.titleEdit);
        mDetailEdit = (EditText) findViewById(R.id.detailEdit);
        mDelete = (Button) findViewById(R.id.delete);

        long myfavmemberId = getIntent().getLongExtra("myfavmember_id",-1);
        if(myfavmemberId != -1){
            RealmResults<MyFavMember> results = mRealm.where(MyFavMember.class)
                    .equalTo("id",myfavmemberId).findAll();
            MyFavMember myfavmember = results.first();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String date = sdf.format(myfavmember.getDate());
            mDateEdit.setText(date);
            mTitleEdit.setText(myfavmember.getTitle());
            mDetailEdit.setText(myfavmember.getDetail());
            mDelete.setVisibility(View.VISIBLE);
        }else {
            mDelete.setVisibility(View.INVISIBLE);
        }

    }

    public void onSaveTapped(View view) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date dateParse = new Date();
        try {
            dateParse = sdf.parse(mDateEdit.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Date date = dateParse;

        long myfavmemberId = getIntent().getLongExtra("myfavmember_id", -1);
        if (myfavmemberId != -1) {
            final RealmResults<MyFavMember> results = mRealm.where(MyFavMember.class)
                    .equalTo("id", myfavmemberId).findAll();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    MyFavMember myfavmember = results.first();
                    myfavmember.setDate(date);
                    myfavmember.setTitle(mTitleEdit.getText().toString());
                    myfavmember.setDetail(mDetailEdit.getText().toString());
                }
            });
            Snackbar.make(findViewById(android.R.id.content),
                    "アップデートしました", Snackbar.LENGTH_LONG)
                    .setAction("戻る", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    })
                    .setActionTextColor(Color.YELLOW)
                    .show();
        } else {

            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm reaalm) {
                    Number maxId = reaalm.where(MyFavMember.class).max("id");
                    long nextId = 0;
                    if (maxId != null) nextId = maxId.longValue() + 1;
                    MyFavMember myfavmember
                            = reaalm.createObject(MyFavMember.class, new Long(nextId));
                    myfavmember.setDate(date);
                    myfavmember.setTitle(mTitleEdit.getText().toString());
                    myfavmember.setDetail(mDetailEdit.getText().toString());
                }
            });
            Toast.makeText(this, "追加しました", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    public void onDeleteTapped(View view){
        final long myfavmemberId = getIntent().getLongExtra("myfavmember_id",-1);
        if(myfavmemberId != -1){

            mRealm.executeTransaction(new Realm.Transaction(){
                @Override
                public void execute(Realm realm){
                    MyFavMember myfavmember = realm.where(MyFavMember.class)
                            .equalTo("id",myfavmemberId).findFirst();
                    myfavmember.deleteFromRealm();
                }
            });
            finish();
        }

    }
}
