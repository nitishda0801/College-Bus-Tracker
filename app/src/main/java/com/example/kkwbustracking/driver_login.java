package com.example.kkwbustracking;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class driver_login extends AppCompatActivity
{
    EditText licence_no_edittext,name_edittext,phone_edittext,email_edittext;
    Button login;
    String licence_no,name,phone,email;
    DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_login);

        licence_no_edittext = findViewById(R.id.licence_no);
        phone_edittext = findViewById(R.id.phone);
        name_edittext = findViewById(R.id.name);
        email_edittext = findViewById(R.id.email);

        login = findViewById(R.id.login);
        databaseHelper = new DatabaseHelper(this);

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(driver_login.this, driver_tracklocation.class);
                Intent intent1 = new Intent(driver_login.this, driver_login.class);
                int flag=0;
                name = name_edittext.getText().toString();
                phone = phone_edittext.getText().toString();
                licence_no = licence_no_edittext.getText().toString();
                email = email_edittext.getText().toString();
                boolean L=false,P=false,N=false,E=false;
                //NAME -- validation
                String name_regex = "^[A-Za-z]\\w{5,29}$";
                Pattern p = Pattern.compile(name_regex);
                Matcher m1 = p.matcher(name);

                if(TextUtils.isEmpty(name)) {
                    name_edittext.setError("Name is Mandatory");
                }
                else if(m1.matches())
                    {
                        N=true;
                    }
                else {
                    name_edittext.setError("Enter Proper Name ");
                }
                //PHONE-validation
                String phone_regex = "(0/91)?[7-9][0-9]{9}";
                Pattern p1 = Pattern.compile(phone_regex);
                Matcher m2 = p1.matcher(phone);

                if(TextUtils.isEmpty(phone))
                {
                    phone_edittext.setError("Phone No. is Mandatory");
                }
                else if(m2.matches())
                {
                    P=true;
                }
                else {
                    phone_edittext.setError("Enter 10-digit phone number");
                }

                //EMAIL - validation
                String email_regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
                Pattern p2 = Pattern.compile(email_regex);
                Matcher m3 = p2.matcher(email);
                if(TextUtils.isEmpty(email))
                {
                    email_edittext.setError("Email is mandatory");
                }
                else
                    if(m3.matches())
                    {
                        E = true;
                    }
                    else
                    {
                        email_edittext.setError("Enter proper Email");
                    }
                    //LICENSE NO VALIDATION
                String license_regex ="^(([A-Z]{2}[0-9]{2})"
                        + "( )|([A-Z]{2}-[0-9]"
                        + "{2}))((19|20)[0-9]"
                        + "[0-9])[0-9]{7}$";
                Pattern p3 = Pattern.compile(license_regex);
                Matcher m4 = p3.matcher(licence_no);

                if(TextUtils.isEmpty(licence_no)) {
                    licence_no_edittext.setError("Licence No. is Mandatory");
                }
                else if(m4.matches())
                {
                    L=true;
                }
                else {
                    licence_no_edittext.setError("Enter proper Licence number");
                }
        //  !(TextUtils.isEmpty(name) && TextUtils.isEmpty(phone) && TextUtils.isEmpty(licence_no)

                if(N && L && P && E)
                {
                    databaseHelper.insertData(name, phone, licence_no, email);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(driver_login.this,"Enter proper details to Register ",Toast.LENGTH_LONG).show();
                }
            }

        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Driver Register");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white, getTheme()));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getWindow().setStatusBarColor(getResources().getColor(R.color.darkblue2, this.getTheme()));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_withoutroute, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.share)
        {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Here is the share content body";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
        else
        {
            Intent intent = new Intent(driver_login.this,about.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}