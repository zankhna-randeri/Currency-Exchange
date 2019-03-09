package sjsu.zankhna.currency.exchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import sjsu.zankhna.currency.exchange.model.Exchange;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private final String REQ_TAG = "exchange";
    private Button btnClose;
    private Button btnApply;
    private TextView txtAmount;
    private TextView txtCurrency;
    private Exchange data;

    private RequestQueue requestQueue;
    private JsonObjectRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        data = fetchExtras(getIntent());
        initView();
    }

    private Exchange fetchExtras(Intent intent) {
        Exchange data = new Exchange();
        data.setAmount(intent.getFloatExtra("amount", 0f));
        data.setCurrency(intent.getStringExtra("currency"));
        data.setSymbol(intent.getStringExtra("symbol"));
        return data;
    }

    private void initView() {
        btnApply = findViewById(R.id.btnApply);
        btnClose = findViewById(R.id.btnClose);
        txtAmount = findViewById(R.id.txtAmount);
        txtCurrency = findViewById(R.id.txtCurrency);
        btnClose.setOnClickListener(this);
        btnApply.setOnClickListener(this);
        txtAmount.setText(String.valueOf(data.getAmount()));
        txtCurrency.setText(data.getCurrency());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnApply:
                convertCurrency();
                break;
            case R.id.btnClose:
                this.finish();
                break;
        }
    }

    private void convertCurrency() {
        String url = "https://api.exchangeratesapi.io/latest?base=USD";
        requestQueue = Volley.newRequestQueue(MainActivity.this);
        request = new JsonObjectRequest(Request.Method.GET, url, null,
                new ResponseListener(data, MainActivity.this), new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Could not fetch currency rate",
                        Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag(REQ_TAG);
        requestQueue.add(request);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(REQ_TAG);
        }
    }

}
