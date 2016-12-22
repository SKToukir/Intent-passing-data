package jobanbondi.jobanbondi.project.jobanbondi;

import jobanbondi.jobanbondi.project.jobanbondi.AndroidMultiPartEntity.ProgressListener;
import jobanbondi.jobanbondi.project.jobanbondi.util.GPSTracker;
import jobanbondi.jobanbondi.project.jobanbondi.util.GetAddress;
import jobanbondi.jobanbondi.project.jobanbondi.util.ScalingUtilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class UploadActivity extends AppCompatActivity {
    // LogCat tag
    private static final String TAG = MainActivity.class.getSimpleName();
    private String userId;
    private EditText etUserComplain;
    private ProgressBar progressBar;
    private String filePath = null;
    private TextView txtPercentage;
    private ImageView imgPreview;
    private VideoView vidPreview;
    private Button btnUpload;
    long totalSize = 0;
    boolean isImage;
    private String location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        etUserComplain = (EditText) findViewById(R.id.etUserComplain);
        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        vidPreview = (VideoView) findViewById(R.id.videoPreview);

        GPSTracker gpsTracker = new GPSTracker(getApplicationContext());
        GetAddress userLocation = new GetAddress();
        //location = userLocation.getCompleteAddressString(gpsTracker.getLatitude(), gpsTracker.getLongitude(),getApplicationContext());
        location = String.valueOf(gpsTracker.getLatitude()+" "+gpsTracker.getLongitude());
//        ActionBar actionBar = getActionBar();
//
//        // Changing action bar background color
//        actionBar.setBackgroundDrawable(
//                new ColorDrawable(Color.BLACK));

        // Receiving the data from previous activity
        Intent i = getIntent();

        // image or video path that is captured in previous activity
        filePath = i.getStringExtra("filePath");

        // boolean flag to identify the media type, image or video
        isImage = i.getBooleanExtra("isImage", true);

        userId = i.getStringExtra("user_id");

        if (filePath != null) {
            // Displaying the image or video on the screen
            previewMedia(isImage);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }

        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String userComplain = etUserComplain.getText().toString();
                // uploading the file to server
                new UploadFileToServer().execute(userComplain,String.valueOf(isImage),userId,location);
            }
        });

    }

    /**
     * Displaying captured image/video on the screen
     * */
    private void previewMedia(boolean isImage) {
        // Checking whether captured media is image or video
        if (isImage) {
            imgPreview.setVisibility(View.VISIBLE);
            vidPreview.setVisibility(View.GONE);
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

            imgPreview.setImageBitmap(bitmap);
        } else {
            imgPreview.setVisibility(View.GONE);
            vidPreview.setVisibility(View.VISIBLE);
            vidPreview.setVideoPath(filePath);
            // start playing
            vidPreview.start();
        }
    }

    /**
     * Uploading the file to server
     * */
    private class UploadFileToServer extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(String... params) {
            String userComplains = params[0];
            boolean isImage = Boolean.parseBoolean(params[1]);
            String userId = params[2];
            String uLoc = params[3];
            return uploadFile(userComplains,isImage,userId,uLoc);
        }

        @SuppressWarnings("deprecation")
        private String uploadFile(String complainText,boolean imageOrNot,String uId, String loc) {
            String responseString = null;


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Config.FILE_UPLOAD_URL);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                File sourceFile = new File(filePath);

                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                // Extra parameters if you want to pass to server
                entity.addPart("complain",new StringBody(complainText));
                entity.addPart("dateTime", new StringBody(dateTime()));
                entity.addPart("isImage",new StringBody(String.valueOf(imageOrNot)));
                entity.addPart("user_id", new StringBody(uId));
                entity.addPart("user_location", new StringBody(loc.trim()));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);

            // showing the server response in an alert dialog
            showAlert(result);

            super.onPostExecute(result);
        }

    }

    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setTitle("Response from Servers")
                .setCancelable(false)
                .setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public String dateTime(){

        Date dateTime = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");

        return dateFormat.format(dateTime);
    }

//    public String decodeFile(String path) {
//        String strMyImagePath = null;
//        Bitmap scaledBitmap = null;
//
//        try {
//            // Part 1: Decode image
//            Bitmap unscaledBitmap = ScalingUtilities.decodeFile(path, 80, 80, ScalingUtilities.ScalingLogic.FIT);
//
//            if (!(unscaledBitmap.getWidth() <= 800 && unscaledBitmap.getHeight() <= 800)) {
//                // Part 2: Scale image
//                scaledBitmap = ScalingUtilities.createScaledBitmap(unscaledBitmap, 80, 80, ScalingUtilities.ScalingLogic.FIT);
//            } else {
//                unscaledBitmap.recycle();
//                return path;
//            }
//
//            // Store to tmp file
//
//            String extr = Environment.getExternalStorageDirectory().toString();
//            File mFolder = new File(extr + "/myTmpDir");
//            if (!mFolder.exists()) {
//                mFolder.mkdir();
//            }
//
//            String s = "tmp.png";
//
//            File f = new File(mFolder.getAbsolutePath(), s);
//
//            strMyImagePath = f.getAbsolutePath();
//            FileOutputStream fos = null;
//            try {
//                fos = new FileOutputStream(f);
//                scaledBitmap.compress(Bitmap.CompressFormat.PNG, 5, fos);
//                fos.flush();
//                fos.close();
//            } catch (FileNotFoundException e) {
//
//                e.printStackTrace();
//            } catch (Exception e) {
//
//                e.printStackTrace();
//            }
//
//            scaledBitmap.recycle();
//        } catch (Throwable e) {
//        }
//
//        if (strMyImagePath == null) {
//            return path;
//        }
//        return strMyImagePath;
//
//    }

}