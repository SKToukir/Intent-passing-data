package jobanbondi.jobanbondi.project.jobanbondi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.github.rtoshiro.view.video.FullscreenVideoLayout;

import java.io.IOException;

public class TestActivity extends AppCompatActivity {

    // Declare variables
    ProgressDialog pDialog;
    VideoView videoview;
    TextView txtComplain, txtDateTime;
    String VideoURL,complain,dateTime;
    FullscreenVideoLayout videoLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


 //       videoview = (VideoView) findViewById(R.id.VideoView);
        // Execute StreamVideo AsyncTask

        Intent intent = getIntent();
        VideoURL = intent.getStringExtra("url");
//        complain = intent.getStringExtra("complain");
//        dateTime = intent.getStringExtra("date");
//
//        txtComplain.setText(complain);
//        txtDateTime.setText(dateTime);




//        // Create a progressbar
//        pDialog = new ProgressDialog(this);
//        // Set progressbar title
//        pDialog.setTitle("Video Streaming");
//        // Set progressbar message
//        pDialog.setMessage("Buffering...");
//        pDialog.setIndeterminate(false);
//        //pDialog.setCancelable(false);
//        pDialog.setCanceledOnTouchOutside(false);
//        // Show progressbar
//
//        pDialog.show();
//
//        try {
//            // Start the MediaController
//            MediaController mediacontroller = new MediaController(
//                    this);
//            mediacontroller.setAnchorView(videoview);
//            // Get the URL from String VideoURL
//            Uri video = Uri.parse(VideoURL);
//            videoview.setMediaController(mediacontroller);
//            videoview.setVideoURI(video);
////            txtComplain.setText(complain);
////            txtDateTime.setText(dateTime);
//
//        } catch (Exception e) {
//            hidePDialog();
//            Log.e("Error", e.getMessage());
//            e.printStackTrace();
//        }
//
//        videoview.requestFocus();
//        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            // Close the progress bar and play the video
//            public void onPrepared(MediaPlayer mp) {
//                hidePDialog();
//                videoview.start();
//            }
//        });


        videoLayout = (FullscreenVideoLayout) findViewById(R.id.videoview);
        videoLayout.setActivity(this);

        Uri videoUri = Uri.parse(VideoURL);
        try {
            videoLayout.setVideoURI(videoUri);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


//}
//    }


    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hidePDialog();
        finish();
    }
}