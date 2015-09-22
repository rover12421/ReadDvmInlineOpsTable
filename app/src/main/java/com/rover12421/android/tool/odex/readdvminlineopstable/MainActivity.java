package com.rover12421.android.tool.odex.readdvminlineopstable;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Button btn;
    private TextView textView;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        btn = (Button) findViewById(R.id.getdvminfo);
        textView = (TextView) findViewById(R.id.dvminfo);
        btn.setMovementMethod(ScrollingMovementMethod.getInstance());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btn.getText().toString().equals("Copy")) {
                    ClipboardManager cManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData cData = ClipData.newPlainText("text", textView.getText());
                    cManager.setPrimaryClip(cData);
                    Toast.makeText(context, "已经复制到粘贴板!", Toast.LENGTH_LONG).show();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        textView.setText("不支持5.0及以上的系统!!");
                        return;
                    }
//                    btn.setEnabled(false);
//                    btn.setClickable(false);
//                textView.setText(ReadDvmInfo.ReadDvmInlineOpsTable());
                    textView.setText(ReadDvmInfo.GetInlineMethodResolver());
                    btn.setText("Copy");
                }

            }
        });
    }
}
