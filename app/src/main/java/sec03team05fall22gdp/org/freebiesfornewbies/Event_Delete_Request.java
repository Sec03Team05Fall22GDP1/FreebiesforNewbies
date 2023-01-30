package sec03team05fall22gdp.org.freebiesfornewbies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Event_Delete_Request extends AppCompatActivity {
    Button Cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_delete_page);

        Cancel=findViewById(R.id.btncancel_event);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                canclemethod();
            }
        });
    }

    public void canclemethod(){
        startActivity(new Intent(Event_Delete_Request.this,HomeActivity.class));
    }
}