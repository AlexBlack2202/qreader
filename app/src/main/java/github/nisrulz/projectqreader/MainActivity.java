/*
 * Copyright (C) 2016 Nishant Srivastava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github.nisrulz.projectqreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import github.nisrulz.qreader.QRDataListener;
import github.nisrulz.qreader.QREader;

public class MainActivity extends AppCompatActivity {

  private SurfaceView surfaceView;
  private TextView textView_qrcode_info;
  QREader qrEader;

  private boolean active = true;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    surfaceView = (SurfaceView) findViewById(R.id.camera_view);
    textView_qrcode_info = (TextView) findViewById(R.id.code_info);
    Button btn_startstop = (Button) findViewById(R.id.btn_startstop);

    qrEader = new QREader.Builder(MainActivity.this, surfaceView, new QRDataListener() {
      @Override public void onDetected(final String data) {
        Log.d("QREader", "Value : " + data);
        textView_qrcode_info.post(new Runnable() {
          @Override public void run() {
            textView_qrcode_info.setText(data);
          }
        });
      }
    }).facing(QREader.BACK_CAM).enableAutofocus(true).height(800).width(800).build();

    qrEader.init();

    findViewById(R.id.new_activity).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        startActivity(new Intent(MainActivity.this, MainActivity.class));
      }
    });
    btn_startstop.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {

        if (!active) {
          active = true;
          // Implement QRReader activate code

          qrEader = new QREader.Builder(MainActivity.this, surfaceView, new QRDataListener() {
            @Override public void onDetected(final String data) {
              Log.d("QREader", "Value : " + data);
              textView_qrcode_info.post(new Runnable() {
                @Override public void run() {
                  textView_qrcode_info.setText(data);
                }
              });
            }
          }).facing(QREader.BACK_CAM).enableAutofocus(true).height(800).width(800).build();

          qrEader.init();
          qrEader.start();
        } else {
          active = false;
          // Implement QRReader deactivate code

          qrEader.stop();
          qrEader.releaseAndCleanup();
        }
      }
    });
  }

  @Override protected void onStart() {
    super.onStart();

    // Call in onStart
    qrEader.start();
  }

  @Override protected void onStop() {
    super.onStop();
    // Call in onDestroy
    qrEader.stop();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    qrEader.releaseAndCleanup();
  }
}
