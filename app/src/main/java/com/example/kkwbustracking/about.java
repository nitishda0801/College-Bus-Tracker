package com.example.kkwbustracking;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
public class about extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        Toolbar toolbar = findViewById(R.id.stud_toolbar);
        toolbar.setTitle("About");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white,getTheme()));
        setSupportActionBar(toolbar);
        TextView textView = (TextView) findViewById(R.id.my_website);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        getWindow().setStatusBarColor(getResources().getColor(R.color.darkblue2, this.getTheme()));

    }
    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }

}

