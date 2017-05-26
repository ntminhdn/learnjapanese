package com.example.user.learnjapanesevocabulary;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.user.learnjapanesevocabulary.learn.LearnActivity;
import com.example.user.learnjapanesevocabulary.model.Lesson;
import com.example.user.learnjapanesevocabulary.model.Version;
import com.example.user.learnjapanesevocabulary.model.Vocabulary;
import com.example.user.learnjapanesevocabulary.model.VocabularyObject;
import com.example.user.learnjapanesevocabulary.util.RMS;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private ListView mLvLesson;
    private ArrayAdapter<String> mAdapterLesson;
    private Toolbar mToolbar;
    private int mCurrentLessonIndex = 0;
    private int mVersion = 1;
    private TextView mTvTitle;
    private TextView mTvCurrentLesson;
    private View mTempViewLesson;
    private NavigationView mNavigationView;
    private ArrayList<Lesson> data = new ArrayList<>();
    private Button btnLearn;
    private Realm realm;
    private RMS rms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControl();
        init();
        addEvent();
    }

    private void init() {
        Realm.init(this);
        realm = Realm.getDefaultInstance();

        //show menu
        mToggle = new ActionBarDrawerToggle(
                this
                , mDrawerLayout
                , mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        //load RMS, CurrentLesson, version
        rms = RMS.getInstance();
        rms.init(this);
        rms.load();

        //if is first run then download data else do nothing
        checkVersionFromDatabase();
        if (rms.isFirstLaunchApp()) {
            new DownloadTask(getApplicationContext()).execute();
        }
        rms.increaseNumberOfLaunchApp();
        mCurrentLessonIndex = rms.getCurrentLessonIndex();
        mTvCurrentLesson.setText("Lesson " + mCurrentLessonIndex);
        mTvTitle.setText("Lesson " + mCurrentLessonIndex);
    }

    private void checkVersionFromDatabase() {
        mDatabase.child("lesson").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Version version = dataSnapshot.getValue(Version.class);
                System.out.println(version.getVersion());
                System.out.println(RMS.getInstance().mSharePreference.getInt("version", 0));
                System.out.println(rms.mSharePreference.getInt("version", 0));
                if (Integer.parseInt(version.getVersion()) > rms.getVersionApp()) {
                    mVersion = Integer.parseInt(version.getVersion());
                    rms.saveVersion(mVersion);
                    new DownloadTask(getApplicationContext()).execute();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLearn:
                Intent intent = new Intent(MainActivity.this, LearnActivity.class);
                startActivity(intent);

//                int c = 0;
//                for (int i = 0; i < data.size(); i++) {
//                    for (int j = 0; j < data.get(i).getmVocabularyList().size(); j++) {
//                        System.out.println(data.get(i).getmVocabularyList().get(j).getKanji());
//                        c++;
//                    }
//                }
//                System.out.println(c);

                break;
        }
    }

    class DownloadTask extends AsyncTask<Void, Integer, Void> {
        private ProgressDialog progressDialog;
        private Context context;

        public DownloadTask(Context context) {
            this.context = context;
            progressDialog = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Đang tải data");
            progressDialog.setMax(100);
            progressDialog.setMessage("Vui lòng đợi");
            progressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            progressDialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(data.size() / 251);
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 1; i < 6; i++) {
                final Lesson lesson = new Lesson(i, new ArrayList<Vocabulary>());
                final ArrayList<Vocabulary> voca = new ArrayList<>();
                mDatabase.child("lesson").child("lesson" + i).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Vocabulary vocabulary = postSnapshot.getValue(Vocabulary.class);
                            voca.add(vocabulary);
                        }
                        lesson.setmVocabularyList(voca);
                        data.add(lesson);

                        addToDatabase();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void strings) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    private void addToDatabase() {
        realm.beginTransaction();
        realm.deleteAll();
        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).getmVocabularyList().size(); j++) {

                VocabularyObject vocabulary = realm.createObject(VocabularyObject.class);
                vocabulary.setLesson(data.get(i).getLesson());
                vocabulary.setKanji(data.get(i).getmVocabularyList().get(j).getKanji());
                vocabulary.setMean(data.get(i).getmVocabularyList().get(j).getMean());
                vocabulary.setSound(data.get(i).getmVocabularyList().get(j).getSound());
                vocabulary.setWord(data.get(i).getmVocabularyList().get(j).getWord());

            }
        }
        realm.commitTransaction();
    }

    private void addEvent() {
        mLvLesson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentLessonIndex = position + 1;
                mTvTitle.setText("Lesson " + (position + 1));
                mTvCurrentLesson.setText("Lesson " + (position + 1));

                rms.saveCurrentLessonIndex(mCurrentLessonIndex);

                //set màu cho item được chọn
                if (mTempViewLesson != null) {
                    mTempViewLesson.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                }
                mTempViewLesson = view;
                mTempViewLesson.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        });
    }

    private void addControl() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.containerDrawerMain);
        mLvLesson = (ListView) findViewById(R.id.lvLesson);
        mAdapterLesson = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.lesson_version));
        mLvLesson.setAdapter(mAdapterLesson);
        mTvCurrentLesson = (TextView) findViewById(R.id.tvCurrentLesson);
        mTvTitle = (TextView) findViewById(R.id.tvTitle);
        mNavigationView = (NavigationView) findViewById(R.id.navView);
        btnLearn = (Button) findViewById(R.id.btnLearn);
        btnLearn.setOnClickListener(this);
    }

    @Override
    protected void onStop() {
        rms.saveCurrentLessonIndex(mCurrentLessonIndex);
        super.onStop();
    }
}
