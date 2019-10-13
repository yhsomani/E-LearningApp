package androiddev.com.elearning;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Explore extends AppCompatActivity implements customAdapter.onIemClickListener{
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private int TotalSearchResults;

    public static final String SHARED_PREFS = "sharedPrefs";
    private ArrayList<Course> CoursesList=new ArrayList<Course>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//setting the layout manager, most important thing
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new customAdapter(CoursesList,this,this);
        recyclerView.setAdapter(adapter);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String a = sharedPreferences.getString("id", "");
        makeApiCall(a);
    }

    private void makeApiCall(String a) {
        //making progress bar
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        //ends here
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://apj-learning.herokuapp.com/myCourses";
        CoursesList.clear();
        Map<String,String> paramId=new HashMap<String,String>();
        paramId.put("StudentId",a);
        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
        Log.v("id",a);
        JSONObject paramJson=new JSONObject(paramId);

        JsonObjectRequest objectRequest =new JsonObjectRequest(Request.Method.POST, url, paramJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int noOfCourses=response.length();
                JSONObject temp = null;
//                Toast.makeText(Explore.this, response.toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(Explore.this, Integer.toString(noOfCourses), Toast.LENGTH_SHORT).show();
                for(int i=0;i<noOfCourses;i++){
                    try {
                        temp=response.getJSONObject("course"+Integer.toString(i+1));
                    } catch (JSONException e) {
                        Toast.makeText(Explore.this, "error in parsing", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
//                    Toast.makeText(Explore.this,temp.toString(), Toast.LENGTH_LONG).show();
                    String codeStr= null;
                    try {
                        codeStr = temp.getString("Code");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                    String name= null;
                    try {
                        name = temp.getString("Name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                    String html_url= null;
                    try {
                        html_url = temp.getString("Image");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                    String instructorId= null;
                    try {
                        instructorId = temp.getString("Instructor Id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                    String description= null;
                    try {
                        description = temp.getString("Description");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                    CoursesList.add(new Course(codeStr,name,html_url,instructorId,description));
                }
                adapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Explore.this, "Error from Volley", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(objectRequest);
        Toast.makeText(this, Integer.toString(CoursesList.size()), Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onItemClicked(int position) {
        // https://www.youtube.com/watch?v=69C1ljfDvl0
        // now this above position is given by the customAdapter class
        // what is happening is that mainActivity has implemented a clickListener, and passing the ......... see video
        //Toast.makeText(this,githubUsers.get(position).getScore(), Toast.LENGTH_SHORT).show();

        // passing the object from one activity to another, https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android
//        Intent intent= new Intent(this,eachUser.class);
//        intent.putExtra("profile",githubUsers.get(position));
//        startActivity(intent);
    }
}
