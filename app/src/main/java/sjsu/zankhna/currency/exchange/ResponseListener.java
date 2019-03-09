package sjsu.zankhna.currency.exchange;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import sjsu.zankhna.currency.exchange.model.Exchange;

public class ResponseListener implements Response.Listener {

    private final String receiver = "sjsu.zankhna.currency.CONVERTER_RECEIVER";
    private Exchange data;
    private final String TAG = "ResponseListener";
    private Context context;

    public ResponseListener(Exchange data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public void onResponse(Object response) {
        Log.d("Exchange Response : ", response.toString());
        float answer = parseResponse(response);
        sendBroadCast(answer);
        ((MainActivity) context).finish();
    }

    private void sendBroadCast(float amount) {
        Log.d(TAG, "sendBroadcast invoked");
        Intent currencyConverterBroadcast = new Intent(receiver);
        currencyConverterBroadcast.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        currencyConverterBroadcast.putExtra("amount", amount);
        if (Build.VERSION.SDK_INT >= 8) {
            currencyConverterBroadcast.setComponent(null);
        }
        context.sendBroadcast(currencyConverterBroadcast);
    }

    private float parseResponse(Object response) {
        float answer = 0f;
        try {
            if (response instanceof JSONObject) {
                JSONObject jsonResponse = (JSONObject) response;
                JSONObject rates = (JSONObject) jsonResponse.get("rates");
                double baseRate = (double) rates.get(data.getSymbol());
                double result = data.getAmount() * baseRate;
                int r = (int) Math.round(result * 100);
                answer = r / 100f;
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return answer;
    }


}
