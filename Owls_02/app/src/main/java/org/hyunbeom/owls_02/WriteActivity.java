package org.hyunbeom.owls_02;

import android.app.Activity;
import  android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WriteActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextDay;
    private EditText editTextCont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        //TextView registerButton = (TextView) findViewById(R.id.writeButton);

        editTextCont = (EditText) findViewById(R.id.writeContents);

    }

    public void insert(View view){
        String cont = editTextCont.getText().toString();
        String name = "aaa";
        String day = "2017-06-28";

        insertToDatabase(cont, name, day);
    }

    private void insertToDatabase(String cont, String name, String day){

        class InsertData extends AsyncTask<String, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(WriteActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

                String s = result.trim();
                if(s.equalsIgnoreCase("success")){
                    Intent intent = new Intent(WriteActivity.this, MainActivity.class);
                    finish();
                    startActivity(intent);
                }
            }

            @Override
            protected String doInBackground(String... params) {

                try{
                    String cont = (String)params[0];
                    String name = (String)params[1];
                    String day = (String)params[2];

                    String link="http://192.168.0.14/write.php";
                    String data  = URLEncoder.encode("contents", "UTF-8") + "=" + URLEncoder.encode(cont, "UTF-8");
                    data += "&" + URLEncoder.encode("writer", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8");
                    data += "&" + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(day, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while((line = reader.readLine()) != null)
                    {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }

            }
        }

        InsertData task = new InsertData();
        task.execute(cont,name,day);
    }
}
