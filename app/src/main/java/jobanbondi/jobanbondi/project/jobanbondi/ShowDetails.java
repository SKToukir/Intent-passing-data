package jobanbondi.jobanbondi.project.jobanbondi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jobanbondi.jobanbondi.project.jobanbondi.adapter.CommentAdapter;
import jobanbondi.jobanbondi.project.jobanbondi.util.AppController;
import jobanbondi.jobanbondi.project.jobanbondi.util.CommentClass;
import jobanbondi.jobanbondi.project.jobanbondi.util.LoadImageTask;
import jobanbondi.jobanbondi.project.jobanbondi.util.ModelClass;
import jobanbondi.jobanbondi.project.jobanbondi.util.RecyclerTouchListener;
import jobanbondi.jobanbondi.project.jobanbondi.util.SingletonClass;

public class ShowDetails extends AppCompatActivity implements LoadImageTask.Listener{

    String userID;
    String final_url;
    List<CommentClass> comment_list = new ArrayList<CommentClass>();
    private ProgressDialog pDialog;
    TextView txtComplain;
    TextView txtDate,txtLikeCount;
    ImageView iamge;
    String url,complain,date,userId,id_,comment,user_name;
    EditText etCommnt;
    Button btnPost, btnComment,btnLike;
    int i;
    CommentAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

//        int totalLikes = getTotalLike();
//        txtLikeCount.setText(totalLikes);

        recyclerView = (RecyclerView) findViewById(R.id.list_of_comments);
        adapter = new CommentAdapter(this,comment_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        txtLikeCount = (TextView) findViewById(R.id.txtLikeCount);
        btnLike = (Button) findViewById(R.id.btnLike);
        etCommnt = (EditText) findViewById(R.id.etComment);
        btnPost = (Button) findViewById(R.id.btnPost);
        btnComment = (Button) findViewById(R.id.btnComment);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading..");
        pDialog.show();

        txtComplain = (TextView) findViewById(R.id.txtShowComplain);
        txtDate = (TextView) findViewById(R.id.txtShowDate);
        iamge = (ImageView) findViewById(R.id.images);

        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> user = db.getUserDetails();

        user_name = user.get("name");
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        complain = intent.getStringExtra("complain");
        date = intent.getStringExtra("date");
        userId = intent.getStringExtra("user_id");
        id_ = intent.getStringExtra("id_");
        Toast.makeText(this,"Image ID is "+id_,Toast.LENGTH_LONG).show();
        userID = intent.getStringExtra("user_id");
        String video_id = intent.getStringExtra("video_id");

        final_url = "http://www.dogmatt.com/Project21/getAllComment.php?user_id="+userID+"&video_id"+id_;

        // get all comments
        getTotalComment(userID,id_);

        Toast.makeText(getApplicationContext(),userId+" "+id_,Toast.LENGTH_LONG).show();

        new LoadImageTask(this).execute(url);

        i = 0;
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // get total like from server
                //getTotalLike();

            }
        });

    }

    private void getTotalComment(String u_id, String v_id) {

        final_url = "http://www.dogmatt.com/Project21/getAllComment.php?user_id="+u_id+"&video_id="+v_id;

        final JsonArrayRequest request = new JsonArrayRequest(final_url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i<=response.length(); i++){
                    try {

                        JSONObject object = response.getJSONObject(i);
                        CommentClass commentClass = new CommentClass();
                        commentClass.setUserComment(object.getString(Config.USER_COMMENT));
                        commentClass.setUserName(object.getString(Config.USER_NAME));
                        comment_list.add(commentClass);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        AppController.getInstance().addToRequestQueue(request);

    }

    @Override
    public void onImageLoaded(Bitmap bitmap) {
        iamge.setImageBitmap(bitmap);
        txtComplain.setText(complain);
        txtDate.setText(date);
        hidePDialog();

    }

    @Override
    public void onError() {
        hidePDialog();
        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
    }

    public void hidePDialog() {
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

    public void btnComment(View view) {


            btnComment.setVisibility(View.INVISIBLE);
            btnPost.setVisibility(View.VISIBLE);
            etCommnt.setVisibility(View.VISIBLE);



    }

    private void commentToServer(final String comnt, final String id, final String Uid,final String userName) {


        StringRequest request = new StringRequest(Request.Method.POST, Config.POST_COMMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidePDialog();
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidePDialog();
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("video_id",id);
                params.put("user_id", Uid);
                params.put("user_comment",comnt);
                params.put("user_name",userName);
                return params;
            }
        };

        SingletonClass.getInstance(getApplicationContext()).addToRequestQueue(request);

    }

    private void likeToServer(final String comnt, final String id, final String Uid,final String userName) {


        StringRequest request = new StringRequest(Request.Method.POST, Config.POST_COMMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidePDialog();
                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidePDialog();
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String,String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("video_id",id);
                params.put("user_id", Uid);
                params.put("user_comment",comnt);
                params.put("user_name",userName);
                return params;
            }
        };

        SingletonClass.getInstance(getApplicationContext()).addToRequestQueue(request);

    }

    public void btnPost(View view) {


        getTotalComment(userID,id_);

        comment = etCommnt.getText().toString();

        if (comment != null){

            btnPost.setVisibility(View.GONE);
            btnComment.setVisibility(View.VISIBLE);
            etCommnt.setVisibility(View.GONE);
            commentToServer(comment,id_,userId,user_name);
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Loading..");
            pDialog.show();


        }else {
            Toast.makeText(getApplicationContext(),"Fill up first",Toast.LENGTH_LONG).show();
        }

    }
}
