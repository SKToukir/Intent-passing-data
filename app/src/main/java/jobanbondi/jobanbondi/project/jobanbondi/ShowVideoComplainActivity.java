package jobanbondi.jobanbondi.project.jobanbondi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ShowVideoComplainActivity extends AppCompatActivity {

    TextView txtComplain,txtDate;
    String complain,dateTime,url,userId,id_;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video_complain);

        txtComplain = (TextView) findViewById(R.id.txtVComplain);
        txtDate = (TextView) findViewById(R.id.txtDate);


        Intent intent = getIntent();
        complain = intent.getStringExtra("complain");
        dateTime = intent.getStringExtra("date");
        url = intent.getStringExtra("url");
        userId = intent.getStringExtra("user_id");
        id_ = intent.getStringExtra("id_");

        txtComplain.setText(complain);
        txtDate.setText(dateTime);
    }

    public void btnPlayVideo(View view) {
        Intent intent = new Intent(ShowVideoComplainActivity.this, TestActivity.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }
}
