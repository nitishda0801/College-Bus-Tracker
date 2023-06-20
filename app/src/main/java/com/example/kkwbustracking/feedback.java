package com.example.kkwbustracking;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class feedback extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);
        EditText text1=findViewById(R.id.edit2);
        EditText text2=findViewById(R.id.edit2);
        Button bt=(Button)findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i=new Intent(Intent.ACTION_SEND);
                i.setType("message/html");
                i.putExtra(Intent.EXTRA_EMAIL,new String("srinivasabaji@gmail.com"));
                i.putExtra(Intent.EXTRA_SUBJECT,"Feedback from app");
                i.putExtra(Intent.EXTRA_TEXT,"name"+text1.getText()+"\nMessage"+text2.getText());
                try
                {
                    startActivity(Intent.createChooser(i, "please select email"));
                }
                catch(android.content.ActivityNotFoundException ex)
                {
                    Toast.makeText(feedback.this,"there are no email clints",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}