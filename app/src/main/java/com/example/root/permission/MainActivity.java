package com.example.root.permission;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.security.Permission;
import java.util.List;
import java.util.jar.Manifest;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 问题列表:
 * 1.检查当前应用是否获取了某种权限
 * <p>
 * <p>
 * 2.系统设置界面跳转
 * <p>
 * 3.判断获取联系人权限是否存在
 * 通过进入联系人页面选中联系人之后的返回值进行判断
 */

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.textView)
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//      ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_CONTACTS"}, 1);
    }

    @OnClick({R.id.btnSystemPermissionActivity, R.id.btnCheckPermission, R.id.btnCheckContactPermission})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSystemPermissionActivity:
                gotoSystemPermissionActivity();
                break;
            case R.id.btnCheckPermission:
                gotoCheckPermission();
                break;
            case R.id.btnCheckContactPermission:
                gotoCheckContactPermission();
        }
    }

    //1.检查当前应用是否获取了某种权限
    private void gotoCheckPermission() {

        /*
           PERMISSION_GRANTED = 0;
           PERMISSION_DENIED = -1;
         */

        int i = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS);
        Log.i("xiong", "gotoCheckPermission: ReadContact is " + (i == 0 ? "PERMISSION_GRANTED" : "PERMISSION_DENIED"));
    }

    //2.系统设置界面跳转
    private void gotoSystemPermissionActivity() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
//        intent.setData(Uri.parse("package:" + getPackageName())); //打开当前应用的设置界面,没有显示列表(包含所有应用)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //3.判断获取联系人权限是否存在
    private void gotoCheckContactPermission() {
        /*
            Contacts.CONTENT_URI，显示联系人列表。
            CommonDataKinds.Phone.CONTENT_URI，显示原始联系人的电话号码列表。
            StructuredPostal.CONTENT_URI，显示原始联系人的邮政地址列表。
            Email.CONTENT_URI，显示原始联系人的电子邮件地址列表。
        */
        Intent intentContacts = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intentContacts, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        //3.判断获取联系人权限是否存在
        if (requestCode == 1) {
            //判断授权和未授权情况下data返回的值是否相同
            Log.i("xiong", "data: " + data);

            final Pair<String, List<String>> contact = ContactUtil.retrievePhones(this, data);
        }
    }

}
