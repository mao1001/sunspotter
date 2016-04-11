package edu.uw.mao1001.sunspotter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewStub stub = (ViewStub)findViewById(R.id.stub);

        if(stub != null){
            stub.inflate();
        }


//        LayoutInflater inflator = getLayoutInflater();
//        View view = inflator.inflate(R.layout.search_bundle, null);

    }
}
