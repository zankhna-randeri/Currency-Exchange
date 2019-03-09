package sjsu.zankhna.currency.exchange.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import sjsu.zankhna.currency.exchange.MainActivity;

public class CurrencyExchangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Exchange Receiver : ", "Invoked");
        float amount = intent.getFloatExtra("amount", 0f);
        String currency = intent.getStringExtra("currency");
        String symbol = intent.getStringExtra("symbol");
        Intent activity = new Intent(context, MainActivity.class);
        activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.putExtra("amount", amount);
        activity.putExtra("currency", currency);
        activity.putExtra("symbol", symbol);
        context.startActivity(activity);
    }
}
