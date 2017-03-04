package org.hyunbeom.owls_02;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Invited extends AppCompatActivity {
    private String email;
    private String partner;
    String option;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invited);
        email = SaveSharedPreference.getSharedPreferenceString(getApplicationContext(),"email","nothing");
        partner= SaveSharedPreference.getSharedPreferenceString(getApplicationContext(),"partner","nothing");

        TextView textView =(TextView)findViewById(R.id.partnerinvited);
        textView.setText("partner: "+partner);

        TextView agreeButton = (TextView)findViewById(R.id.agree);
        agreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option="agree";
                invoke_await();
            }
        });//instant end
        TextView disagreeButton= (TextView)findViewById(R.id.disagree);
        disagreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option ="disagree";
            invoke_await();
            }
        });//instant end

    }




    public  void  invoke_await(){

        find_partner(partner,email);
    }
    private void find_partner(final String partner, final String myEmail){
        Log.i("","in partner");

        class FindPartnerAsync extends AsyncTask<String,Void,String> {
            Dialog loadingDialog;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog= ProgressDialog.show(Invited.this,"now Cancelling...","....^^");
            }
            @Override
            protected String doInBackground(String... params) {
                String partner=params[0];
                String myEmail=params[1];


                InputStream is =null;
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("partner",partner));
                nameValuePairs.add(new BasicNameValuePair("email",myEmail));
                nameValuePairs.add(new BasicNameValuePair("status","invited"));
                nameValuePairs.add((new BasicNameValuePair("option",option)));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost =new HttpPost(Config.LOGIN_URL+"invited.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"),8);
                    StringBuilder sb =new StringBuilder();

                    String line = null;

                    while ((line = reader.readLine())!=null){
                        sb.append(line+"\n");
                    }
                    result = sb.toString();


                }catch (Exception e){
                    e.printStackTrace();
                }

                return result;
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                String result = s.trim();

                if(result.equalsIgnoreCase("cancel")){
                    startActivity(new Intent(Invited.this,LoginActivity.class));
                }else if(result.equalsIgnoreCase("agree")){
                    startActivity((new Intent(Invited.this,MainActivity.class)));
                }

                else  if(result.equalsIgnoreCase("connectionfailure")){
                    Toast.makeText(getApplicationContext(), "connectionfailure", Toast.LENGTH_LONG).show();

                }

                else {
                    //      SaveSharedPreference.clearShredPreference(getApplicationContext());
                    Toast.makeText(getApplicationContext(), "Wrong access...", Toast.LENGTH_LONG).show();
                }
                loadingDialog.dismiss();;
            }//post Execute end
        }// inner class end
        FindPartnerAsync fpa =new FindPartnerAsync();
        fpa.execute(partner,myEmail);
    }// method end

}
