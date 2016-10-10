package travel.project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Railway extends AppCompatActivity implements View.OnClickListener {

    private TextView tvPNR;
    private LinearLayout pnrLayout;
    private TextView tvTBS;
    private LinearLayout TBSLayout;
    private EditText pnr,stn1,stn2;
    private Button getPnr,findTrains;
    private String url="http://api.railwayapi.com/pnr_status/pnr/";
    private String key="piofs2102";
    private String url1="http://api.railwayapi.com/suggest_station/name/";
    private String url2="http://api.railwayapi.com/between/source/";
    private long  pnrNO;
    private TextView pnr_result,tbsResult;
    String trains[];
    String final1;
    String final2;
    String final3;
    String station1;
    String station2;
    String code1,code2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_railway);

        pnr=(EditText) findViewById(R.id.pnr);
        getPnr= (Button) findViewById(R.id.btnPNR);
        getPnr.setOnClickListener(this);
        pnr_result= (TextView) findViewById(R.id.tvPNRresult);

        stn1= (EditText) findViewById(R.id.station1);
        stn2= (EditText) findViewById(R.id.station2);
        findTrains= (Button) findViewById(R.id.btnTBS);
        findTrains.setOnClickListener(this);
        tbsResult= (TextView) findViewById(R.id.tvTBSresult);


        tvPNR = (TextView) findViewById(R.id.tvPNR);
        pnrLayout = (LinearLayout) findViewById(R.id.layoutPNR);
        tvPNR.setOnClickListener(this);

        tvTBS = (TextView) findViewById(R.id.tvTBS);
        TBSLayout = (LinearLayout) findViewById(R.id.layoutTBS);
        tvTBS.setOnClickListener(this);
    }

    public void getPNRstatus() {

        pnrNO = Long.parseLong((pnr.getText()).toString());

        StringBuilder sb = new StringBuilder("");
        sb.append(url);
        sb.append(pnrNO);
        sb.append("/apikey/"+key+"/");

        JsonObjectRequest jsonobjectRequest=new JsonObjectRequest(Request.Method.GET, sb.toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String trainNo = response.getString("train_name");
                    String pnr_no = response.getString("pnr");
                    String cLass = response.getString("class");
                    String dateOfj = response.getString("doj");
                    String totalPass = response.getString("total_passengers");
                    String chart = response.getString("chart_prepared");
                    StringBuilder sbb = new StringBuilder("");
                    sbb.append("Train No:-" + trainNo + "\n");
                    sbb.append("PNR:-" + pnr_no + "\n");
                    sbb.append("Date of Journey:-" + dateOfj + "\n");
                    sbb.append("Class:-" + cLass + "\n");
                    sbb.append("Char Prepared:-" + chart + "\n");
                    sbb.append("No of Passenger:-" + totalPass + "\n");
                    pnr_result.setText(sbb.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Railway.this,error.toString(),Toast.LENGTH_LONG);
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonobjectRequest);
    }

    public void changeVisibility(View view) {

        if(view.getVisibility() == View.VISIBLE)
            view.setVisibility(View.GONE);
        else
            view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvPNR:
                changeVisibility(pnrLayout);
                break;
            case R.id.tvTBS:
                changeVisibility(TBSLayout);
                break;
            case R.id.btnPNR:
                getPNRstatus();
                break;
            case R.id.btnTBS:
                getTBS();
        }
    }
    public void finalUrl1_2()
    {
        String stationName1=(stn1.getText()).toString();
        String stationName2=(stn2.getText()).toString();
        StringBuilder sb=new StringBuilder("");
        sb.append(url1);
        sb.append(stationName1);
        sb.append("/apikey/");
        sb.append(key);
        sb.append("/");
        final1=sb.toString();

        StringBuilder sb1=new StringBuilder("");
        sb1.append(url1);
        sb1.append(stationName2);
        sb1.append("/apikey/");
        sb1.append(key);
        sb1.append("/");
        final2=sb1.toString();
    }
    public void  finalUrl_3()
    {
        StringBuilder sb3=new StringBuilder("");
        sb3.append(url2);
        sb3.append(code1);
        sb3.append("/dest/");
        sb3.append(code2);
        sb3.append("/date/");
        sb3.append("/apikey/");
        sb3.append(key);
        sb3.append("/");
        final3=sb3.toString();
    }

    public void getTBS()
    {
        finalUrl1_2();
        finalUrl_3();
        JsonObjectRequest jsobj1=new JsonObjectRequest(Request.Method.GET, final1, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsarray = response.getJSONArray("station");
                            station1 = (jsarray.getJSONObject(0)).getString("fullname");
                            code1=(jsarray.getJSONObject(0)).getString("code");
                            stn1.setText(station1);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsobj1);

        JsonObjectRequest jsobj2=new JsonObjectRequest(Request.Method.GET, final2, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsarray = response.getJSONArray("station");
                            station2 = (jsarray.getJSONObject(0)).getString("fullname");
                            code2= (jsarray.getJSONObject(0)).getString("code");
                            stn2.setText(station2);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsobj2);
        JsonObjectRequest jsobj3=new JsonObjectRequest(Request.Method.GET, final3, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsarray = response.getJSONArray("train");
                            trains=new String[jsarray.length()];
                            for (int i=0;i<jsarray.length();i++)
                            {
                                JSONObject train=jsarray.getJSONObject(i);
                                String trainName=train.getString("name");
                                trains[i]=trainName;
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsobj3);
    }

}
