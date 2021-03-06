package com.example.kurukurupapa.oauth03.accountmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.kurukurupapa.oauth03.R;

/**
 * Androidで、AccountManagerを使用して、OAuth認証を行います。
 *
 * 参考：
 * 琴線探査: AndroidのAccountManager経由でGoogleのOAuth2認証を行うには？（外部ライブラリ完全非依存版）
 * http://kinsentansa.blogspot.jp/2012/08/androidaccountmanagergoogleoauth2.html
 * AccountManager | Android Developers
 * http://developer.android.com/reference/android/accounts/AccountManager.html
 */
public class AccountManagerActivity extends Activity {
    public static final int REQUEST_CODE_ACCOUNT_PICKER = 10;
    public static final int REQUEST_CODE_AUTHORIZATION = 20;

    private static final String TAG = AccountManagerActivity.class.getSimpleName();

    private AccountManagerOAuthHelper mOAuthHelper;
    private Button mStartButton;
    private TextView mResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_account_manager);

        mStartButton = (Button) findViewById(R.id.start_button);
        mResultTextView = (TextView) findViewById(R.id.result_text_view);

        mOAuthHelper = new AccountManagerOAuthHelper(this, new Runnable() {
            @Override
            public void run() {
                onOAuthOk();
            }
        }, new Runnable() {
            @Override
            public void run() {
                onOAuthNg();
            }
        });
    }

    /**
     * スタートボタンクリック時の処理です。
     * @param v スタートボタン
     */
    public void onStartButtonClick(View v) {
        Log.v(TAG, "onStartButtonClick called");
        showWorkingState();
        mOAuthHelper.clear();
        mOAuthHelper.start();
    }

    /**
     * 他アクティビティからの戻り時の処理です。
     * @param requestCode リクエストコード
     * @param resultCode リザルトコード
     * @param data データ
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, "onActivityResult called");
        Log.v(TAG, "requestCode=" + requestCode + ",resultCode=" + resultCode);
        switch (requestCode) {
            // アカウント選択ダイアログ
            case REQUEST_CODE_ACCOUNT_PICKER:
                mOAuthHelper.onAccountPickerResult(requestCode, resultCode, data);
                break;
        }
    }

    /**
     * OAuth＆Googleサービスアクセス成功時に呼ばれます。
     */
    private void onOAuthOk() {
        Log.v(TAG, "onOAuthOk called");
        showResult(mOAuthHelper.getResult());
    }

    /**
     * OAuth＆Googleサービスアクセス中止/失敗時に呼ばれます。
     */
    private void onOAuthNg() {
        Log.v(TAG, "onOAuthNg called");
        showResult("未取得です。");
    }

    private void showWorkingState() {
        mStartButton.setEnabled(false);
        mResultTextView.setText("");
        setProgressBarIndeterminateVisibility(true);
    }

    private void showResult(String result) {
        mResultTextView.setText(result);
        mStartButton.setEnabled(true);
        setProgressBarIndeterminateVisibility(false);
    }

    /**
     * 戻るボタンクリック時の処理です。
     * @param v 戻るボタン
     */
    public void onBackButtonClick(View v) {
        Log.v(TAG, "onBackButtonClick called");
        finish();
    }
}
