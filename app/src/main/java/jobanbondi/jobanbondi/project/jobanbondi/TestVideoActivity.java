package jobanbondi.jobanbondi.project.jobanbondi;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.devbrackets.android.exomedia.listener.OnPreparedListener;
import com.devbrackets.android.exomedia.ui.widget.EMVideoView;

public class TestVideoActivity extends AppCompatActivity implements OnPreparedListener{

    EMVideoView emVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_video);

        setupVideoView();
    }

    private void setupVideoView() {
        emVideoView = (EMVideoView)findViewById(R.id.video_view);
        emVideoView.setOnPreparedListener(this);

        //For now we just picked an arbitrary item to play.  More can be found at
        //https://archive.org/details/more_animation
        emVideoView.setVideoURI(Uri.parse("http://www.dogmatt.com/Project21/uploads/VID_20161124_192323.mp4"));

    }

    @Override
    public void onPrepared() {
        emVideoView.start();
    }
}
