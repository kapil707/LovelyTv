package com.my.lovelytv;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Home_page extends AppCompatActivity {
    ProgressBar menu_loading1;
    final Context context = this;
    String page_url="http://skychannelnetwork.in/newapi/Api_1_1/index";
    String page_url1="http://skychannelnetwork.in/newapi/Api_1_1/index";
    String page_url2="http://skychannelnetwork.in/newapi/Api_1_1/index";
    String page_url3="http://skychannelnetwork.in/newapi/Api_1_1/index";
    String page_url4="http://skychannelnetwork.in/newapi/getlivechannel.php";
    String android_id = "";

    String result ="";

    GridView listview;
    Data_Adapter adapter;
    List<Data_get_or_set> livetvItems = new ArrayList<Data_get_or_set>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#211600"));
        }

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.menu);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#211600")));
        View view = getSupportActionBar().getCustomView();

        TextView action_bar_title1 = findViewById(R.id.action_bar_title);
        action_bar_title1.setText("Lovely Tv");
        ImageButton action_bar_back = view.findViewById(R.id.action_bar_back);
        action_bar_back.setVisibility(View.GONE);
        menu_loading1 = (ProgressBar) findViewById(R.id.menu_loading1);

        LinearLayout cast_LinearLayout = findViewById(R.id.cast_LinearLayout);
        cast_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.screen_cast_model);
                dialog.setTitle("Select Screen Type");

                LinearLayout ScreenMirroringbtn = (LinearLayout) dialog.findViewById(R.id.ScreenMirroringbtn);
                LinearLayout ScreenCastbtn = (LinearLayout) dialog.findViewById(R.id.ScreenCastbtn);

                ScreenMirroringbtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        try {
                            startActivity(new Intent("android.settings.WIFI_DISPLAY_SETTINGS"));
                            return;
                        } catch (ActivityNotFoundException activitynotfoundexception) {
                            activitynotfoundexception.printStackTrace();
                        }
                    }
                });

                ScreenCastbtn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        try {
                            startActivity(new Intent("android.settings.CAST_SETTINGS"));
                            return;
                        } catch (Exception exception1) {
                            Toast.makeText(getApplicationContext(), "Device not supported", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        Intent in = getIntent();
        String id = in.getStringExtra("id");
        android_id = in.getStringExtra("android_id");
        /**page_url = page_url1 = "http://"+id+".ngrok.io/kapil/rkslivetv/api_1_1/livetv";
        page_url2 = "http://"+id+".ngrok.io/kapil/rkslivetv/api_1_1/movies";
        page_url3 = "http://"+id+".ngrok.io/kapil/rkslivetv/api_1_1/webseries";*/

        page_url = page_url4;

        //Toast.makeText(getApplicationContext(), page_url1,Toast.LENGTH_LONG).show();

        listview = (GridView) findViewById(R.id.simpleGridView);
        adapter = new Data_Adapter(Home_page.this, livetvItems);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                // TODO Auto-generated method stub
                Data_get_or_set clickedCategory = livetvItems.get(arg2);
                final String id = clickedCategory.id();
                final String name = clickedCategory.name();
                final String image = clickedCategory.image();
                final String description = clickedCategory.description();
                final String url = clickedCategory.url();
                final String mytype = clickedCategory.mytype();

                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.select_player);
                dialog.setTitle("Select Player --- "+name);

                LinearLayout ply1 =  dialog.findViewById(R.id.ply1);
                LinearLayout ply2 =  dialog.findViewById(R.id.ply2);
                ply1.requestFocus();

                ply1.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        try {
                            Intent in = new Intent();
                            if(mytype.equals("livetv")) {
                                in.setClass(Home_page.this, Player_m3u8.class);
                            }
                            else {
                                in.setClass(Home_page.this, Player_mp4_mkv.class);
                            }
                            in.putExtra("id",id);
                            in.putExtra("name",name);
                            in.putExtra("image",image);
                            in.putExtra("description",description);
                            in.putExtra("url",url);
                            startActivity(in);
                            dialog.dismiss();
                            return;
                        } catch (ActivityNotFoundException activitynotfoundexception) {
                            activitynotfoundexception.printStackTrace();
                        }
                    }
                });

                ply2.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        try {
                            Intent in = new Intent();
                            in.setClass(Home_page.this,Default_m3u8.class);
                            in.putExtra("id",id);
                            in.putExtra("name",name);
                            in.putExtra("image",image);
                            in.putExtra("description",description);
                            in.putExtra("url",url);
                            startActivity(in);
/*
                            Intent intent = new Intent(Intent.ACTION_VIEW);

                            Uri videoUri = Uri.parse(url);

                            intent.setDataAndType( videoUri, "application/x-mpegURL" );

                            intent.setPackage( "com.mxtech.videoplayer.ad");

                            startActivity( intent );*/
                            dialog.dismiss();
                            return;
                        } catch (Exception exception1) {
                            Toast.makeText(getApplicationContext(), "Device not supported", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                /*Intent intent = new Intent(getApplicationContext(), com.kavita.rkslivetv.myvlc.class);
                intent.putExtra("videoUrl", url);
                startActivity(intent);*/
            }
        });

        listview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View arg0) {
                // TODO Auto-generated method stub
                /* Toast.makeText(getApplicationContext(), "Position",Toast.LENGTH_LONG).show(); */
                return false;
            }
        });

        new get_onlinedata().execute();

        LinearLayout livebtn = (LinearLayout) findViewById(R.id.livebtn);
        livebtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                page_url = page_url1;
                new get_onlinedata().execute();
            }
        });

        LinearLayout moviesbtn = (LinearLayout) findViewById(R.id.moviesbtn);
        moviesbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                page_url = page_url2;
                new get_onlinedata().execute();
            }
        });

        LinearLayout webseriesbtn = (LinearLayout) findViewById(R.id.webseriesbtn);
        webseriesbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                page_url = page_url3;
                new get_onlinedata().execute();
            }
        });

        if(id.equals(""))
        {
            livebtn.setVisibility(View.GONE);
            moviesbtn.setVisibility(View.GONE);
            webseriesbtn.setVisibility(View.GONE);
        }

        livebtn.setVisibility(View.GONE);
        moviesbtn.setVisibility(View.GONE);
        webseriesbtn.setVisibility(View.GONE);
    }
    /*@Override
    public void onBackPressed() {
        finish();
    }*/

    private class get_onlinedata extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            menu_loading1.setVisibility(View.VISIBLE);
        }
        @SuppressWarnings("WrongThread")
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                result = "";
                InputStream isr = null;
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(page_url);

                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("api_id", "apiidkapil707sharma-11-22"));
                    nameValuePairs.add(new BasicNameValuePair("submit", "98c08565401579448aad7c64033dcb4081906dcb"));
                    nameValuePairs.add(new BasicNameValuePair("android_id", android_id));

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    isr = entity.getContent();
                } catch (Exception e) {
                    //Log.e("log_tag","Error in connection"+e.toString());
                    //tv.setText("couldn't connect to the database");
                    //mProgressDialog.dismiss();
                    //user_alert = "Check your internet";
                }
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"), 8);
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }
                    isr.close();
                    result = stringBuilder.toString();
                } catch (Exception e) {
                    // TODO: handle exception
                    //mProgressDialog.dismiss();
                }
            } catch (Exception e) {
                // TODO: handle exception
                //mProgressDialog.dismiss();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void args) {
            menu_loading1.setVisibility(View.GONE);
            try {
                livetvItems.clear();
                result = result.replace("teribhanki", "");
                byte[] data = Base64.decode(result, Base64.DEFAULT);
                try {
                    result = new String(data, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                result = result.replace("terimaki", "");
                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();

                JSONArray jArray = new JSONArray(result);
                for (int i = 0; i < jArray.length(); i++) {

                    JSONObject jsonObject = jArray.getJSONObject(i);
                    String id = jsonObject.getString("id");
                    String name = jsonObject.getString("name");
                    String image = jsonObject.getString("image");
                    String url = jsonObject.getString("url");
                    String description = jsonObject.getString("description");
                    String mytype = jsonObject.getString("mytype");

                    url = url.replace("tukutahakuta", "");
                    byte[] data1 = Base64.decode(url, Base64.DEFAULT);
                    url = new String(data1, "UTF-8");
                    url = url.replace("tukaminahakamin", "");

                    Data_get_or_set livetv = new Data_get_or_set();
                    livetv.id(id);
                    livetv.name(name);
                    livetv.image(image);
                    livetv.url(url);
                    livetv.description(description);
                    livetv.mytype(mytype);
                    livetvItems.add(livetv);
                }
            } catch (Exception e) {
                //Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_LONG).show();
                /*ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo ni = cm.getActiveNetworkInfo();
                if (ni != null) {
                    //Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    page_url = page_url4;
                    new get_onlinedata().execute();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Check Your Internet Connection", Toast.LENGTH_LONG).show();
                }*/
            }
            adapter.notifyDataSetChanged();
        }
    }
}
