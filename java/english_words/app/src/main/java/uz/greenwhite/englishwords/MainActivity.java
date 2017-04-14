package uz.greenwhite.englishwords;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    private Switch aSwitch;
    private TextView tvInfo;
    private Words words = new Words();
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.aSwitch = (Switch) findViewById(R.id.s_words);
        this.tvInfo = (TextView) findViewById(R.id.tv_info);

        findViewById(R.id.btn_yes).setOnClickListener(this);
        findViewById(R.id.btn_no).setOnClickListener(this);
        findViewById(R.id.btn_reload).setOnClickListener(this);
        findViewById(R.id.btn_answer).setOnClickListener(this);
        findViewById(R.id.btn_random).setOnClickListener(this);
        findViewById(R.id.btn_bookmark).setOnClickListener(this);

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                reloadListItems();
            }
        });
        reloadContent();
    }

    public void reloadContent() {
        onRequestPermissionsResult(0, new String[]{}, new int[]{});
    }

    private void reloadListItems() {
        findViewById(R.id.btn_reload).setVisibility(View.GONE);
        findViewById(R.id.ll_answer).setVisibility(View.GONE);
        findViewById(R.id.btn_reload).setVisibility(View.GONE);
        findViewById(R.id.btn_answer).setVisibility(View.VISIBLE);

        words.load();
        if (aSwitch.isChecked()) words.randomiseT();
        else words.randomiseE();
        position = 0;
        nextStep();
    }

    @Override
    public void onClick(View view) {
        Word word = words.getWords().isEmpty() ? null : words.getWords().get(position - 1);
        switch (view.getId()) {
            case R.id.btn_yes:
                if (word == null) return;
                if (aSwitch.isChecked()) {
                    words.setTStats(word.english, 1);
                } else {
                    words.setEStats(word.english, 1);
                }
                nextStep();
                break;
            case R.id.btn_no:
                if (word == null) return;
                if (aSwitch.isChecked()) {
                    words.setTStats(word.english, -1);
                } else {
                    words.setEStats(word.english, -1);
                }
                nextStep();
                break;
            case R.id.btn_answer:
                findViewById(R.id.btn_answer).setVisibility(View.GONE);
                findViewById(R.id.ll_answer).setVisibility(View.VISIBLE);
                break;
            case R.id.btn_reload:
                reloadListItems();
                break;
            case R.id.btn_random:
                if (aSwitch.isChecked()) words.randomiseT();
                else words.randomiseE();
                position = 0;
                nextStep();
                break;
            case R.id.btn_bookmark:
                if (word == null) return;
                words.saveBookmark(word.english);
                break;
        }
    }

    private void nextStep() {
        if (position < words.getWords().size()) {
            position++;

            Word word = words.getWords().get(position - 1);
            ((TextView) findViewById(R.id.tv_text)).setText(aSwitch.isChecked() ? word.translation : word.english);
            ((TextView) findViewById(R.id.tv_answer)).setText(!aSwitch.isChecked() ? word.translation : word.english);
            ((TextView) findViewById(R.id.tv_reader)).setText(word.reader);
            ((TextView) findViewById(R.id.tv_some_text)).setText(word.text);

            findViewById(R.id.tv_text).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_answer).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_answer).setVisibility(View.GONE);
            findViewById(R.id.btn_reload).setVisibility(View.GONE);
        } else {
            findViewById(R.id.btn_reload).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_answer).setVisibility(View.GONE);
            findViewById(R.id.btn_answer).setVisibility(View.GONE);
            findViewById(R.id.tv_text).setVisibility(View.GONE);
        }
        tvInfo.setText("" + position + "/" + words.getWords().size());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (Build.VERSION.SDK_INT < 23) {
            reloadListItems();
        } else {
            if (!permissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    !permissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }
            } else {
                reloadListItems();
            }
        }
    }

    public boolean permissionGranted(String permission) {
        return checkPermission(permission, android.os.Process.myPid(), android.os.Process.myUid())
                == PackageManager.PERMISSION_GRANTED;
    }
}

