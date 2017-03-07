package com.faladinojames.andela;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Falade James on 3/6/2017 All Rights Reserved.
 */

public class AndelaActivity extends AppCompatActivity {


    protected void shortToast(String t)
    {
        Toast.makeText(this,t,Toast.LENGTH_SHORT).show();
    }

    protected void log(String l)
    {
        Log.d("Andela",l);
    }
}
