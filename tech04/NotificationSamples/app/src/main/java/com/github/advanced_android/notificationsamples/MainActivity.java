package com.github.advanced_android.notificationsamples;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    public static final int NOT_SET = -100;
    private Spinner visibilitySpinner;
    private Spinner prioritiesSpinner;

    final String[] VISIBILITIES =
            {
                    "設定しない",
                    "VISIBILITY_PUBLIC",
                    "VISIBILITY_PRIVATE",
                    "VISIBILITY_SECRET",
            };
    final int[] VISIBILITIES_INT =
            {
                    NOT_SET,
                    NotificationCompat.VISIBILITY_PUBLIC,
                    NotificationCompat.VISIBILITY_PRIVATE,
                    NotificationCompat.VISIBILITY_SECRET,
            };

    final String[] PRIORITIES =
            {
                    "設定しない",
                    "PRIORITY_MAX",
                    "PRIORITY_HIGH",
                    "PRIORITY_DEFAULT",
                    "PRIORITY_LOW",
                    "PRIORITY_MIN",
            };

    final int[] PRIORITIES_INT =
            {
                    NOT_SET,
                    NotificationCompat.PRIORITY_MAX,
                    NotificationCompat.PRIORITY_HIGH,
                    NotificationCompat.PRIORITY_DEFAULT,
                    NotificationCompat.PRIORITY_LOW,
                    NotificationCompat.PRIORITY_MIN,
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        visibilitySpinner = (Spinner) findViewById(R.id.visibility);
        prioritiesSpinner = (Spinner) findViewById(R.id.priority);

        visibilitySpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, VISIBILITIES));
        prioritiesSpinner.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, PRIORITIES));

    }

    public void build(View view) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_stat_name);

        // タイトル表示
        final CheckBox contentTitle = (CheckBox) findViewById(R.id.contentTitle);
        if (contentTitle.isChecked()) {
            builder.setContentTitle("ContentTitle");
        }

        // 内容表示
        final CheckBox contentText = (CheckBox) findViewById(R.id.contentText);
        if (contentText.isChecked()) {
            builder.setContentText("ContentText");
        }

        // クリックした時のインテントの設定
        final CheckBox contentIntentCheckBox = (CheckBox) findViewById(R.id.content_intent);
        if (contentIntentCheckBox.isChecked()) {
            final Intent launchIntent = new Intent(this, MainActivity.class);
            final PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
        }

        // 通知を横スライドでキャンセルできないようにする
        final CheckBox onGoingCheckBox = (CheckBox) findViewById(R.id.on_going);
        if (onGoingCheckBox.isChecked()) {
            builder.setOngoing(true);
        }

        // Action
        final CheckBox action1CheckBox = (CheckBox) findViewById(R.id.action1);
        if (action1CheckBox.isChecked()) {
            final PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            builder.addAction(new NotificationCompat.Action(R.drawable.ic_stat_name, "ActionText1", pendingIntent));
        }
        final CheckBox action2CheckBox = (CheckBox) findViewById(R.id.action2);
        if (action2CheckBox.isChecked()) {
            final PendingIntent pendingIntent = PendingIntent.getActivity(this, 2, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            builder.addAction(R.drawable.ic_stat_name, "ActionText2", pendingIntent);
        }
        final CheckBox action3CheckBox = (CheckBox) findViewById(R.id.action3);
        if (action3CheckBox.isChecked()) {
            final PendingIntent pendingIntent = PendingIntent.getActivity(this, 3, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            builder.addAction(R.drawable.ic_stat_name, "ActionText3", pendingIntent);
        }

        // Android WearでActionを表示する
        final CheckBox extendCheckBox = (CheckBox) findViewById(R.id.extend);
        if (extendCheckBox.isChecked()) {
            final PendingIntent pendingIntent = PendingIntent.getActivity(this, 3, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            builder.extend(new NotificationCompat.WearableExtender().addAction(new NotificationCompat.Action(R.drawable.ic_stat_name, action3CheckBox.getText(), pendingIntent)));
        }

        // AutoCancelを設定する(クリックした時に自動で非表示になる)
        final RadioGroup autoCancelRadioGroup = (RadioGroup) findViewById(R.id.autocancel);
        if (autoCancelRadioGroup.getCheckedRadioButtonId() == R.id.autocancel_true) {
            builder.setAutoCancel(true);
        }

        // Tickerを設定する
        final CheckBox tickerCheckBox = (CheckBox) findViewById(R.id.ticker);
        if (tickerCheckBox.isChecked()) {
            builder.setTicker("ticker");
        }

        // Progressを設定する
        final CheckBox progressCheckBox = (CheckBox) findViewById(R.id.progress);
        if (progressCheckBox.isChecked()) {
            builder.setProgress(100, 60, false);
        }

        // BigPictureStyleを設定する
        final CheckBox bigPictureStyleCheckBox = (CheckBox) findViewById(R.id.big_picture_style);
        if (bigPictureStyleCheckBox.isChecked()) {
            builder.setStyle(
                    new NotificationCompat.BigPictureStyle()
                            .bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.ic_stat_name))
                            .setBigContentTitle("BigContentTitle")
                            .setSummaryText("SummaryText"));
        }

        // InboxStyleを設定する
        final CheckBox inboxStyleCheckBox = (CheckBox) findViewById(R.id.inbox_style);
        if (inboxStyleCheckBox.isChecked()) {
            builder.setStyle(
                    new NotificationCompat.InboxStyle()
                            .addLine("line1")
                            .addLine("line2")
                            .addLine("line3")
                            .addLine("line4")
                            .addLine("line5")
                            .addLine("line6")
                            .addLine("line7")
                            .setBigContentTitle("BigContentTitle")
                            .setSummaryText("SummaryText"));
        }

        // BigTextStyleを設定する
        final CheckBox bigTextCheckBox = (CheckBox) findViewById(R.id.big_text_style);
        if (bigTextCheckBox.isChecked()) {
            builder.setStyle(
                    new NotificationCompat.BigTextStyle()
                            .bigText("BigText")
                            .setBigContentTitle("BigContentTitle")
                            .setSummaryText("SummaryText"));
        }

        // Colorを設定する
        final CheckBox colorCheckBox = (CheckBox) findViewById(R.id.color);
        if (colorCheckBox.isChecked()) {
            builder.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        // VISIBILITIESを設定(ロック画面で表示されるかどうか)
        final int selectedVisibilitiesPosition = visibilitySpinner.getSelectedItemPosition();
        final int visibilityInt = VISIBILITIES_INT[selectedVisibilitiesPosition];
        if (visibilityInt != NOT_SET) {
            builder.setVisibility(visibilityInt);
        }

        // 通知の優先度を設定 (上に表示されるかどうか)
        final int selectedPrioritySelectedPosition = prioritiesSpinner.getSelectedItemPosition();
        final int priorityInt = PRIORITIES_INT[selectedPrioritySelectedPosition];
        if (priorityInt != NOT_SET) {
            builder.setPriority(priorityInt);
        }

        // 端末を振動させるかどうか
        final CheckBox vibrationCheckBox = (CheckBox) findViewById(R.id.vibration);
        if (vibrationCheckBox.isChecked()) {
            builder.setVibrate(new long[]{0, 1000, 250, 1000});
        }

        // fullScreenIntentを設定するかどうか
        final CheckBox fullScreenIntentCheckBox = (CheckBox) findViewById(R.id.full_screen_intent);
        if (fullScreenIntentCheckBox.isChecked()) {
            final PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setFullScreenIntent(pendingIntent, true);
        }

        NotificationManagerCompat.from(this).notify(1, builder.build());

    }
}
