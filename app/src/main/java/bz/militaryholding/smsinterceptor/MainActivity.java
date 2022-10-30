package bz.militaryholding.smsinterceptor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import bz.militaryholding.smsinterceptor.tools.Prefs;

public class MainActivity extends AppCompatActivity {
    private Button btnPermissions, btnScriptUrl, btnBankListSettings;
    private EditText etScriptUrl;

    private boolean isGranted = false;

    private boolean isEditing = false;

    private Prefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isGranted = isSMSReceiveGranted();
        prefs = Prefs.getInstance(getApplicationContext());
        setView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isSMSReceiveGranted()) {
            if(btnPermissions!=null) {
                btnPermissions.setVisibility(View.GONE);
            }
        }
    }



    private boolean isSMSReceiveGranted() {
       if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
           return false;
       }
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.RECEIVE_SMS},
                1);
    }

    private void showPermissionSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if(btnPermissions!=null) {
                btnPermissions.setVisibility(View.GONE);
            }
        } else {
           Toast.makeText(this, "Вы запретили приложению принимать СМС.\n " +
                    "Измените разрешения в настройках приложения", Toast.LENGTH_LONG).show();
        }
    }

    private void setView() {
        setContentView(R.layout.activity_main);
        Button btnPermissions = (Button) findViewById(R.id.btnPermissions);
        btnPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPermissionSettings();
            }
        });
        if(isGranted) {
            btnPermissions.setVisibility(View.GONE);
        }

        etScriptUrl = (EditText) findViewById(R.id.etScriptUrl);
        etScriptUrl.setText(prefs.getScriptUrl());
        etScriptUrl.setEnabled(isEditing);

        btnScriptUrl = (Button) findViewById(R.id.btnScriptUrl);
        btnScriptUrl.setText("Редактировать");
        btnScriptUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEditing) {
                    isEditing = true;
                    etScriptUrl.setEnabled(true);
                    btnScriptUrl.setText("Сохранить");
                } else {
                    isEditing = false;
                    etScriptUrl.setEnabled(false);
                    btnScriptUrl.setText("Редактировать");
                    prefs.saveScriptUrl(etScriptUrl.getText().toString());
                }
            }
        });

        btnBankListSettings = (Button) findViewById(R.id.btnBankListSettings);
        btnBankListSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BankListActivity.class));
            }
        });

        if(!isGranted) {
            requestPermissions();
        }
    }
}