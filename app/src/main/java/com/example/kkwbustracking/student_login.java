package com.example.kkwbustracking;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class student_login extends  AppCompatActivity
{
    EditText name_edittext,phone_edittext,email_edittext,rollno_edittext,bus_name_edittext;
    Button login;
    String roll_no,name,phone,email,bus_name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_login);

        rollno_edittext= findViewById(R.id.stud_rollno);
        phone_edittext = findViewById(R.id.stud_phone);
        name_edittext = findViewById(R.id.stud_name);
        email_edittext = findViewById(R.id.stud_email);
        bus_name_edittext=findViewById(R.id.stud_bus_name);

        login = findViewById(R.id.stud_login);

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                name = name_edittext.getText().toString();
                phone = phone_edittext.getText().toString();
                roll_no = rollno_edittext.getText().toString();
                email = email_edittext.getText().toString();
                bus_name=bus_name_edittext.getText().toString();
                boolean N=false,P=false,E=false,R=false;

                //validate username
                String name_regex = "[a-zA-Z]+\\.?";
                Pattern p = Pattern.compile(name_regex);
                Matcher m1 = p.matcher(name);

                if(TextUtils.isEmpty(name))
                {
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

            }
        });

    }


}
