package jobanbondi.jobanbondi.project.jobanbondi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
import jobanbondi.jobanbondi.project.jobanbondi.util.DividerItemDecoration;
import jobanbondi.jobanbondi.project.jobanbondi.util.SingletonClass;

public class ShowVideoComplainActivity extends AppCompatActivity {

    List<CommentClass> commentClassesList;
    RecyclerView.Adapter adapter;
    TextView txtComplain,txtDate;
    String complain,dateTime,url,userId,id_;
    Button btnVideoComment, btnVideoPost;
    String comment,user_name,final_url;
    EditText etComment;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video_complain);

        recyclerView = (RecyclerView) findViewById(R.id.list_of_comments);
        commentClassesList = new ArrayList<CommentClass>();
        adapter = new CommentAdapter(getApplicationContext(),commentClassesList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        btnVideoComment = (Button) findViewById(R.id.btnVideoComment);
        btnVideoPost = (Button) findViewById(R.id.btnVideoPost);
        txtComplain = (TextView) findViewById(R.id.txtVComplain);
        txtDate = (TextView) findViewById(R.id.txtDate);
        etComment = (EditText) findViewById(R.id.etVideoCmnt);

        SQLiteHandler handler = new SQLiteHandler(this);
        HashMap<String,String> db = handler.getUserDetails();
        user_name = db.get("name");

        Intent intent = getIntent();
        complain = intent.getStringExtra("complain");
        dateTime = intent.getStringExtra("date");
        url = intent.getStringExtra("url");
        userId = intent.getStringExtra("user_id");
        id_ = intent.getStringExtra("id_");

        //final_url = "http://www.dogmatt.com/Project21/getAllComment.php?user_id="+userId+"&video_id"+id_;

        txtComplain.setText(complain);
        txtDate.setText(dateTime);

        getTotalComment(userId,id_);
    }

    public void btnPlayVideo(View view) {
        Intent intent = new Intent(ShowVideoComplainActivity.this, TestActivity.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }

    public void btnVideoComments(View view) {

        btnVideoComment.setVisibility(View.GONE);
        etComment.setVisibility(View.VISIBLE);
        btnVideoPost.setVisibility(View.VISIBLE);

    }

    public void btnVideoPost(View view) {

        btnVideoPost.setVisibility(View.GONE);
        btnVideoComment.setVisibility(View.VISIBLE);

        comment = etComment.getText().toString();

        commentToServer(comment,id_,userId,user_name);
        etComment.setVisibility(View.GONE);
        btnVideoPost.setVisibility(View.GONE);
        getTotalComment(userId,id_);
        commentClassesList.clear();
    }

    private void commentToServer(final String comnt, final String id, final String Uid,final String userName) {


        StringRequest request = new StringRequest(Request.Method.POST, Config.POST_COMMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
                        commentClassesList.add(commentClass);

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
}
