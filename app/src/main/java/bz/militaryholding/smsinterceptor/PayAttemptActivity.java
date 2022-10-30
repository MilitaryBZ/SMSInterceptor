package bz.militaryholding.smsinterceptor;

import android.os.Vibrator;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class PayAttemptActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_attempt);

        long[] pattern = {150};
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, 2);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        System.exit(0);
    }
}