package com.example.yuxiapeng.callfilter;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    Context context = this;
    final int REQUESTCODE = 1;
    private TelephonyManager telephonyManager;
    private PhoneCallListener myPhoneStateListener;
    private ListView listView;
    private RecoderAdpter recoderAdapter;
    private Recoders recoders;
    private BlackList blackList;
    private Handler handler = new Handler();
    private boolean started = true;
    private Menu menu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Recoders");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUESTCODE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUESTCODE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUESTCODE);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUESTCODE);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, REQUESTCODE);
        }

        this.permission();
        this.getResource();
        this.listView.setAdapter(this.recoderAdapter);
        this.OncallListenr();
        this.handler.postDelayed(runnable, 500);
    }

    private void permission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 1);
            } else {
                //TODO do something you need
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.checkBlackList://监听菜单按钮
                Intent myIntent2 = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(myIntent2);
                break;
            case R.id.turnOn:
                this.started = true;
                this.myPhoneStateListener.setStarted(this.started);
                this.menu.findItem(R.id.turnOff).setVisible(true);
                this.menu.findItem(R.id.turnOn).setVisible(false);
                break;
            case R.id.turnOff:
                this.started = false;
                this.myPhoneStateListener.setStarted(this.started);
                this.menu.findItem(R.id.turnOff).setVisible(false);
                this.menu.findItem(R.id.turnOn).setVisible(true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void getResource() {
        this.listView = (ListView) this.findViewById(R.id.listview);
        this.recoders = new Recoders();
        this.blackList = new BlackList();
        this.listView.setOnItemClickListener(new RecoderListOnItemClickListener());
        this.recoderAdapter = new RecoderAdpter(context, this.recoders);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUESTCODE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_DENIED)) {
                    Toast.makeText(context, "PERMISSION DENIED!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "PERMISSION GRANTED!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void OncallListenr() {
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        myPhoneStateListener = new PhoneCallListener(context, recoders, blackList, started);
        telephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 500);
            recoderAdapter.notifyDataSetChanged();
        }
    };

    class RecoderListOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, final long selectId) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Confirm");
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    deleteFunction(selectId);

                }
            });
            builder.setMessage("Do you want to delete this record?");
            builder.show();

        }

        private void deleteFunction(long selectId) {
            int selectItemId = (int) selectId;
            recoders.deleteRecoder(selectItemId);
            Toast.makeText(context, "DELETE SUCCESS!" + selectItemId, Toast.LENGTH_SHORT).show();
        }
    }

}

