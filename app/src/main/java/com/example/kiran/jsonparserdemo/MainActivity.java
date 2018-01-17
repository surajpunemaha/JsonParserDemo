package com.example.kiran.jsonparserdemo;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    ListView listv_frnds;
    ArrayList<String> al_frnds;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listv_frnds= (ListView) findViewById(R.id.listv_frnds);
        al_frnds = new ArrayList<>();

        getFriendsDetails();
    }

    public void getFriendsDetails()
    {
        al_frnds.clear();
        
        final ProgressDialog pd = new ProgressDialog(MainActivity.this);
        pd.setMessage("Getting details");
        pd.show();

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        String base = "http://192.168.43.240/demo/frnds_list.php";

        client.post(base, params, new AsyncHttpResponseHandler()
        {
            @Override
            public void onSuccess(String response)
            {
                pd.dismiss();
                try
                {
                    JSONObject root = new JSONObject(response);
                    System.out.println("root=> "+root);
                    JSONArray arr = root.getJSONArray("results");
                    System.out.println("array=> "+arr);
                    for(int i=0; i<arr.length(); i++)
                    {
                        JSONObject obj = arr.getJSONObject(i);
                        System.out.println("object"+i+" "+obj);
                        String name = obj.optString("frnd_name");
                        String age = obj.optString("frnd_age");

                        al_frnds.add(name+" "+age);
                    }

                    ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,
                            android.R.layout.simple_list_item_1,
                            al_frnds);
                    listv_frnds.setAdapter(adapter);

                }catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Throwable error, String content)
            {
                pd.dismiss();
                Toast.makeText(MainActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
