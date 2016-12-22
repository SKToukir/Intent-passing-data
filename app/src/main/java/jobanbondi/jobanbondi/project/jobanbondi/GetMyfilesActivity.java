package jobanbondi.jobanbondi.project.jobanbondi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jobanbondi.jobanbondi.project.jobanbondi.adapter.ItemsAdapter;
import jobanbondi.jobanbondi.project.jobanbondi.util.ModelClass;
import jobanbondi.jobanbondi.project.jobanbondi.util.RecyclerTouchListener;
import jobanbondi.jobanbondi.project.jobanbondi.util.SingletonClass;

public class GetMyfilesActivity extends AppCompatActivity {

    String URL;
    //String URL = "http://www.dogmatt.com/Project21/json_image.php";
    private List<ModelClass> modelClasses = new ArrayList<ModelClass>();
    private ListView listView;
    private RecyclerView recyclerView;
    private ItemsAdapter adapter;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_myfiles);
        Intent intent = getIntent();
        String check = intent.getStringExtra("one");
        final String userId = intent.getStringExtra("user_id");
        String anotherCheck = intent.getStringExtra("two");


        URL = Config.GET_MY_FILES_URL+userId;


        recyclerView = (RecyclerView) findViewById(R.id.list_of_filess);
        adapter = new ItemsAdapter(this,modelClasses);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ModelClass item = modelClasses.get(position);
                Toast.makeText(getApplicationContext(), userId + " is selected!", Toast.LENGTH_SHORT).show();
                if (item.getIsImage().contains("true")){

                    Intent intent = new Intent(GetMyfilesActivity.this, ShowDetails.class);
                    intent.putExtra("url", item.getFiles());
                    intent.putExtra("complain", item.getComplain());
                    intent.putExtra("date", item.getDateTime());
                    intent.putExtra("id_",item.getId());
                    intent.putExtra("user_id",userId);
                    startActivity(intent);

                }else{

                    Intent intent = new Intent(GetMyfilesActivity.this, ShowVideoComplainActivity.class);
                    intent.putExtra("url", item.getFiles());
                    intent.putExtra("complain", item.getComplain());
                    intent.putExtra("date", item.getDateTime());
                    intent.putExtra("id_",item.getId());
                    intent.putExtra("user_id",userId);
                    startActivity(intent);
                }
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        pDialog = new ProgressDialog(this);

        pDialog.setMessage("Loading..");
        pDialog.show();

        JsonArrayRequest request = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                hidePDialog();
                for (int i = 0; i<=response.length(); i++){

                    try {
                        JSONObject object = response.getJSONObject(i);
                        ModelClass modelClass = new ModelClass();
                        modelClass.setId(object.getString("id_"));
                        modelClass.setFiles(object.getString("files"));
                        modelClass.setDateTime(object.getString("Datetime"));
                        modelClass.setComplain(object.getString("complain"));
                        modelClass.setIsImage(object.getString("isImage"));



                        modelClasses.add(modelClass);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                adapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidePDialog();
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();

            }
        });

        SingletonClass.getInstance(this).addToRequestQueue(request);
        //AppController.getInstance().addToRequestQueue(request);
    }

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
//
//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//        ModelClass name = (ModelClass) adapter.getItem(i);
//
//
//        if (name.getIsImage().contains("true")){
//
//            Intent intent = new Intent(GetAllDataFromServerActivity.this, ShowDetails.class);
//            intent.putExtra("url", name.getFiles());
//            intent.putExtra("complain", name.getComplain());
//            intent.putExtra("date", name.getDateTime());
//            startActivity(intent);
//
//        }else{
//
//            Intent intent = new Intent(GetAllDataFromServerActivity.this, ShowVideoComplainActivity.class);
//            intent.putExtra("url", name.getFiles());
//            intent.putExtra("complain", name.getComplain());
//            intent.putExtra("date", name.getDateTime());
//            startActivity(intent);
//        }
//    }
}