package com.example.user.learnjapanesevocabulary.listening;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.learnjapanesevocabulary.R;
import com.example.user.learnjapanesevocabulary.model.ListeningQuestion;
import com.example.user.learnjapanesevocabulary.model.VocabularyObject;
import com.example.user.learnjapanesevocabulary.util.AudioService;
import com.example.user.learnjapanesevocabulary.util.RMS;
import com.example.user.learnjapanesevocabulary.util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by minh.nt on 5/29/2017.
 */

public class ListeningDetailActivity extends AppCompatActivity implements AudioService.PlayAudioCallback {
    private TextView tvQuestionNumber, tvCountDown;
    private Button btnAnswer1, btnAnswer2, btnAnswer3;
    private int position = 0;
    private List<ListeningQuestion> listQuestion = new ArrayList<>();
    private CountDownTimer timer;
    private AudioService mAudioService;
    private boolean isBound = false;
    private static final int MSG = 1;
    private Handler mHandlerAutoPlayAudio = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            synchronized (ListeningDetailActivity.this) {
                if (msg.what == MSG) {
                    int position = msg.arg1;
                    playAudioWithPath(position);
                }
            }
        }
    };

    private ServiceConnection mAudioServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            AudioService.AudioBinder binder = (AudioService.AudioBinder) iBinder;
            mAudioService = binder.getService();
            isBound = true;
            mAudioService.setPlayAudioCallback(ListeningDetailActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Intent bindServiceIntent = new Intent(this, AudioService.class);
        bindService(bindServiceIntent, mAudioServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void playAudioWithPath(int position) {
        if (mAudioService != null && isBound) {
            mAudioService.playOnline(listQuestion.get(position).getSound());
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening_detail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        get30Question();
        initView();
        loadQuestion(listQuestion.get(0));
    }

    private void get30Question() {
        RealmResults<VocabularyObject> list = Realm.getDefaultInstance().where(VocabularyObject.class).equalTo("lesson", RMS.getInstance().getCurrentLessonIndex()).findAll();
        List<Integer> questionIds = new ArrayList<>();
        Random random = new Random();
        int randomNumber;

        while (listQuestion.size() <= 10) {
//            randomNumber = Util.getRandom(1, list.size() - 1);
//            if (!questionIds.contains(randomNumber)) {
//                questionIds.add(randomNumber);
//                VocabularyObject vocabulary = list.get(randomNumber);
//                VocabularyObject firstWrongVocabulary = list.get(randomNumber + 1);
//                VocabularyObject secondWrongVocabulary = list.get(randomNumber - 1);
//                listQuestion.add(new ListeningQuestion(vocabulary.getSound(), vocabulary.getWord(), firstWrongVocabulary.getWord(), secondWrongVocabulary.getWord()));
//            }

            VocabularyObject vocabulary = list.get(position);
            VocabularyObject firstWrongVocabulary = list.get(position + 1);
            VocabularyObject secondWrongVocabulary = list.get(position + 2);
            listQuestion.add(new ListeningQuestion(vocabulary.getSound(), vocabulary.getWord(), firstWrongVocabulary.getWord(), secondWrongVocabulary.getWord()));
        }
    }

    private void loadQuestion(ListeningQuestion listeningQuestion) {
        // Chạy file âm thanh.
        Message msg = new Message();
        msg.what = MSG;
        msg.arg1 = position;

        mHandlerAutoPlayAudio.sendMessageDelayed(msg, 50);

        position++;
        tvQuestionNumber.setText(position + " / 30");
        btnAnswer1.setText(listeningQuestion.getFirstWrongWord());
        btnAnswer2.setText(listeningQuestion.getSecondWrongWord());
        btnAnswer3.setText(listeningQuestion.getCorrectWord());
    }

    private void initView() {
        tvQuestionNumber = (TextView) findViewById(R.id.tvQuestionNumber);
        tvCountDown = (TextView) findViewById(R.id.tvCountDown);
        btnAnswer1 = (Button) findViewById(R.id.btnAnswer1);
        btnAnswer2 = (Button) findViewById(R.id.btnAnswer2);
        btnAnswer3 = (Button) findViewById(R.id.btnAnswer3);

        timer = new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvCountDown.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                loadQuestion(listQuestion.get(position + 1));
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPlayAudioFinish() {
        timer.start();
    }

    @Override
    public void onPlayAudioCompletion() {

    }
}
