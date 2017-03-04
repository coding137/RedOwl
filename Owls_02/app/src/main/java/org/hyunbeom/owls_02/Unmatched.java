package org.hyunbeom.owls_02;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.M;

public class Unmatched extends Activity {

    //Textview to show currently logged in user
    private TextView textView;
    private EditText partnerEmail;
    private String partner;
    private String myEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unmatched);
        SharedPreferences pref = getSharedPreferences("pref",MODE_PRIVATE);
        Intent intent = getIntent();
        String email = SaveSharedPreference.getSharedPreferenceString(getApplicationContext(),"email","nothing");

        TextView textView = (TextView) findViewById(R.id.textView);
        partnerEmail=(EditText) findViewById(R.id.partnerEmail);
        textView.setText("User :  "+email);
        myEmail= email;
//        Button goList =(Button) findViewById(R.id.goList);
//        goList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
////                Intent golist = new Intent(Unmatched.this, MainActivity.class);
////                Unmatched.this.startActivity(golist);
//            }
//        });

    }

    public void invoke_find_partner(View view){
        partner=partnerEmail.getText().toString();

        if(partner.equalsIgnoreCase(myEmail)){
            Toast.makeText(getApplicationContext(), "잘못된 형식입니다. ", Toast.LENGTH_LONG).show();

            return;
        }
        find_partner(partner,myEmail);
        Log.i("","in invoke");
    }

    private void find_partner(final String partner, final String myEmail){
        Log.i("","in partner");

        class FindPartnerAsync extends AsyncTask<String,Void,String>{
            Dialog loadingDialog;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog= ProgressDialog.show(Unmatched.this,"now Matching...","Match your partner..^^");
            }
            @Override
            protected String doInBackground(String... params) {
                String partner=params[0];
                String myEmail=params[1];

                InputStream is =null;
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("partner",partner));
                nameValuePairs.add(new BasicNameValuePair("email",myEmail));
                nameValuePairs.add(new BasicNameValuePair("status","unmatched"));
                String result = null;

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost =new HttpPost(Config.LOGIN_URL+"findpartner.php");
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

                if(result.equalsIgnoreCase("no partner")){

                }else if (result.equalsIgnoreCase("matched")){

                }else if(result.equalsIgnoreCase("await")){
                    SaveSharedPreference.setSharedPreferenceString(getApplicationContext(),"partner",partner);
                    startActivity(new Intent(Unmatched.this,Await.class));
                }else if(result.equalsIgnoreCase("invited")){

                }else {
              //      SaveSharedPreference.clearShredPreference(getApplicationContext());
                    Toast.makeText(getApplicationContext(), "Invalid User Name or Password", Toast.LENGTH_LONG).show();
                }
            }//post Execute end
        }// inner class end
      FindPartnerAsync fpa =new FindPartnerAsync();
        fpa.execute(partner,myEmail);
    }// method end


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //  getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.signupButton) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //Logout function
    private void logout(){
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to logout?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        //Getting out sharedpreferences
                        SharedPreferences preferences = getSharedPreferences(Config.SHARED_PREF_NAME,Context.MODE_PRIVATE);
                        //Getting editor
                        SharedPreferences.Editor editor = preferences.edit();

                        //Puting the value false for loggedin
                        editor.putBoolean(Config.LOGGEDIN_SHARED_PREF, false);

                        //Putting blank value to email
                        editor.putString(Config.EMAIL_SHARED_PREF, "");

                        //Saving the sharedpreferences
                        editor.commit();

                        //Starting login activity
                        Intent intent = new Intent(Unmatched.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


}
