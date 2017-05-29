package com.example.user.learnjapanesevocabulary.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.user.learnjapanesevocabulary.R;
import com.example.user.learnjapanesevocabulary.util.RMS;


/**
 * Class show dialog help for user.
 *
 * @author BinhDT.
 */
public class HelperDialog extends Dialog {
    private static final String LOG_TAG = HelperDialog.class.getName();

    private AppCompatTextView mTvCancel;
    private AppCompatTextView mTvContent;
    private AppCompatTextView mTvTitle;
    private CheckBox cbShowDialog;

    private String mMessage = "";
    private String mTitle = "";

    /**
     * Constructor of class.
     *
     * @param context    Value context of Screen current.
     * @param themeResId Value theme id.
     */
    public HelperDialog(Context context, int themeResId, String title, String message) {
        super(context, themeResId);

        mTitle = title;
        mMessage = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Set layout for window.
         */
        setContentView(R.layout.view_dialog_helper);

        /**
         * Method init.
         */
        init();

        /**
         * Call method set event.
         */
        setEvent();
    }

    /**
     * Method init dialog help.
     */
    private void init() {
        /**
         * Set Attributes.
         */
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(this.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        this.getWindow().setAttributes(layoutParams);

        // Init View.
        mTvContent = (AppCompatTextView) findViewById(R.id.mTvContent);
        mTvTitle = (AppCompatTextView) findViewById(R.id.mTvTitle);
        mTvCancel = (AppCompatTextView) findViewById(R.id.mTvCancel);
        cbShowDialog = (CheckBox) findViewById(R.id.cbShowDialog);

        mTvTitle.setText(mTitle);
        mTvContent.setText(mMessage);

        cbShowDialog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                RMS.getInstance().saveDialogStatus(isChecked);
            }
        });
    }

    /**
     * Set event for view.
     */
    private void setEvent() {
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
