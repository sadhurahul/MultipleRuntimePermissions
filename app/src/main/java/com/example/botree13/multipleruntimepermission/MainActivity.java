package com.example.botree13.multipleruntimepermission;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private int i=1;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 100;
    String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_CONTACTS,Manifest.permission.RECORD_AUDIO};
    CoordinatorLayout coordinatorLayout;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*i=1;*/
        Log.v("###","onStart Method");

        if(checkPermissions()){
            initElements();
        }else{
            requestPermissions();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("###","onStop Method");
        if(snackbar!=null && snackbar.isShown()){
            snackbar.dismiss();
            Log.v("###","SnackBar Dismiss");
        }
    }

    private void initElements() {
        Toast.makeText(this,"Your Code is Start",Toast.LENGTH_LONG).show();
    }


    //// CHECK PERMISSIONS
    private boolean checkPermissions(){

        return  ActivityCompat.checkSelfPermission(this, PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, PERMISSIONS[1]) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, PERMISSIONS[2]) == PackageManager.PERMISSION_GRANTED;

    }

   /// CKECK DO NOT ASK AGAIN
    private boolean isDoNotAskAgain(){

        if(i == 1){
            i++;
            return false;
        }

        for(String permission : PERMISSIONS){
           if(!ActivityCompat.shouldShowRequestPermissionRationale(this,permission)){

             if(ActivityCompat.checkSelfPermission(this,permission) == PackageManager.PERMISSION_DENIED){
                 Log.v("###",permission);
                 return true;
             }

           }
        }


        return false;
    }


    /// SHOW REQUESTED PRMISSIONS
    private void showRequestedPermission(){
        ActivityCompat.requestPermissions(this,PERMISSIONS,REQUEST_PERMISSIONS_REQUEST_CODE);
    }


    private void showAlertDailog(){
        snackbar=Snackbar.make(coordinatorLayout, getString(R.string.permissions_alert), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        startActivity(intent);
                    }
                })
                .show();
    }


    /// REQUEST PERMISSION
    private void requestPermissions(){
        showRequestedPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_PERMISSIONS_REQUEST_CODE:{
                boolean isGranted = true;
                if(grantResults.length>0){
                    for(int result:grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                          isGranted=false;
                        }
                    }

                    if(isGranted){
                        initElements();
                    }else{
                        if(isDoNotAskAgain()){
                            showAlertDailog();
                            Log.v("###","Show AlertDailog");
                        }else{
                            Log.v("###","Show RequestDailog");
                            showRequestedPermission();
                        }
                    }
                }
            }

            default:{
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }
}
