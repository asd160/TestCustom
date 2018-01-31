/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.example.bick.myapplication;

import android.app.Activity;
import android.os.Bundle;

import com.example.bick.myapplication.custom.LineGraphicView;

import java.util.ArrayList;

/*
 * MainActivity class that loads {@link MainFragment}.
 */
public class MainActivity extends Activity {

    LineGraphicView tu;
    ArrayList<Double> yListTall;//最高温度
    ArrayList<Double> yListLow;//最低温度
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_layout);
        tu = (LineGraphicView) findViewById(R.id.lgv);

        yListTall = new ArrayList<Double>();
        yListTall.add(16.103);
        yListTall.add(15.05);
        yListTall.add(19.60);
        yListTall.add(15.2);
        yListTall.add(20.32);
        yListTall.add(16.0);
        yListTall.add(17.0);

        yListLow = new ArrayList<Double>();
        yListLow.add((double) 2.103);
        yListLow.add(4.05);
        yListLow.add(6.60);
        yListLow.add(3.08);
        yListLow.add(4.32);
        yListLow.add(2.0);
        yListLow.add(5.0);



//        ArrayList<String> xRawDatas = new ArrayList<String>();
//        xRawDatas.add("05-19");
//        xRawDatas.add("05-20");
//        xRawDatas.add("05-21");
//        xRawDatas.add("05-22");
//        xRawDatas.add("05-23");
//        xRawDatas.add("05-24");
//        xRawDatas.add("05-25");
//        xRawDatas.add("05-26");

        tu.setData(yListTall,yListLow, null, 30 , 3);
    }
}
