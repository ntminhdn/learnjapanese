package com.example.user.learnjapanesevocabulary;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.learnjapanesevocabulary.model.Lesson;
import com.example.user.learnjapanesevocabulary.model.Version;
import com.example.user.learnjapanesevocabulary.model.VersionData;
import com.example.user.learnjapanesevocabulary.model.Vocabulary;
import com.example.user.learnjapanesevocabulary.util.RMS;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private MediaPlayer mMediaPlayer = new MediaPlayer();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private ListView mLvLesson;
    private ArrayAdapter<String> mAdapterLesson;
    private Toolbar mToolbar;
    private int mCurrentLessonIndex = 0;
    private int mVersion = 1;
    private TextView mTvCurrentLesson, mTvTitle;
    private View mTempViewLesson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.activity_main);
        addControl();
        init();
        addEvent();

//        đoạn code lấy list từ vựng
//        final List<Vocabulary> list = new ArrayList<Vocabulary>();
//        for (int i = 1; i < 6; i++) {
//
//            mDatabase.child("lesson").child("lesson" + 1).addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    list.clear();
//                    for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//                        Vocabulary vocabulary = postSnapshot.getValue(Vocabulary.class);
//                        list.add(vocabulary);
//                        System.out.println(vocabulary.getMean());
//                        System.out.println(vocabulary.getKanji());
//                        System.out.println(vocabulary.getWord());
//                        System.out.println(vocabulary.getSound());
////                        playMusic(vocabulary.getSound());
//                    }
//                }
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//        }


//        code lấy version

    }

    private void init() {

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
        RMS rms = RMS.getInstance();
        rms.init(this);
        rms.load();

        //if is first run then download data else do nothing
        checkVersionFromDatabase();
        if(rms.isFirstLaunchApp()){
            downloadData();
        } else {
            Toast.makeText(MainActivity.this, "Nothing", Toast.LENGTH_LONG).show();
        }
        rms.increaseNumberOfLaunchApp();
        mCurrentLessonIndex = rms.getCurrentLessonIndex();
        mTvCurrentLesson.setText("Lesson " + mCurrentLessonIndex);
        mTvTitle.setText("Lesson " + mCurrentLessonIndex);
    }

    private void checkVersionFromDatabase(){
        mDatabase.child("lesson").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Version version = dataSnapshot.getValue(Version.class);
                System.out.println(version.getVersion() + "====================");
                if(Integer.parseInt(version.getVersion()) != RMS.getInstance().getVersionApp()){
                    mVersion = Integer.parseInt(version.getVersion());
                    RMS.getInstance().saveVersion(mVersion);
                    Toast.makeText(MainActivity.this,"Đã đổi version",Toast.LENGTH_LONG).show();
                    downloadData();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void downloadData() {
        final List<Vocabulary> list = new ArrayList<Vocabulary>();
        for (int i = 1; i < 6; i++) {
            mDatabase.child("lesson").child("lesson" + 1).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    list.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        Vocabulary vocabulary = postSnapshot.getValue(Vocabulary.class);
                        list.add(vocabulary);
                        System.out.println(vocabulary.getMean());
                        System.out.println(vocabulary.getKanji());
                        System.out.println(vocabulary.getWord());
                        System.out.println(vocabulary.getSound());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void addEvent() {
        mLvLesson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentLessonIndex = position + 1;
                mTvTitle.setText("Lesson " + (position + 1));
                mTvCurrentLesson.setText("Lesson " + (position + 1));

                //set màu cho item được chọn
                if (mTempViewLesson != null) {
                    mTempViewLesson.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                }
                mTempViewLesson = view;
                mTempViewLesson.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
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
    }

//    public void playMusic(String url) {
//        try {
//            mMediaPlayer.setDataSource(url);
//            mMediaPlayer.prepare();
//            mMediaPlayer.start();
//        } catch (Exception e) {
////            Toast.makeText(getApplicationContext(), "Lỗi", Toast.LENGTH_LONG).show();
//        }
//    }

    @Override
    protected void onStop() {
        RMS.getInstance().saveCurrentLessonIndex(mCurrentLessonIndex);
        RMS.getInstance().saveVersion(mVersion);
        super.onStop();
    }
}
