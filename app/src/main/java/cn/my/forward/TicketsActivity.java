package cn.my.forward;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.mob.mobapi.API;
import com.mob.mobapi.APICallback;
import com.mob.mobapi.MobAPI;
import com.mob.mobapi.apis.TrainTickets;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.Map;

public class TicketsActivity extends AppCompatActivity {
    private EditText mEdit_from;
    private EditText mEdit_to;
    private ListView mShow_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets);
        bindViews();
    }

    private void bindViews() {
        mEdit_from = (EditText) findViewById(R.id.edit_from);
        mEdit_to = (EditText) findViewById(R.id.edit_to);
        mShow_data = (ListView) findViewById(R.id.show_data);
    }

    public void query(View view) {
        ((TrainTickets) MobAPI.getAPI(TrainTickets.NAME)).queryByStationToStation(mEdit_from
                .getText().toString(), mEdit_to.getText().toString(), new APICallback() {

            @Override
            public void onSuccess(API api, int i, Map<String, Object> map) {
                ArrayList<Map<String, Object>> res = ResHelper.forceCast(map.get("result"));
                if (res != null && res.size() > 0) {
                    showStationResult(res);
                }
            }

            @Override
            public void onError(API api, int i, Throwable throwable) {
                Toast.makeText(TicketsActivity.this, "查询出错", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showStationResult(ArrayList<Map<String, Object>> res) {
        SimpleAdapter adapter = new SimpleAdapter(this, res, R.layout
                .view_train_tickets_station_list_item,
                new String[]{"startStationName", "endStationName", "startTime", "arriveTime",
                        "stationTrainCode", "trainClassName", "lishi",
                        "pricesw", "pricetd", "pricegrw", "pricerw", "priceyw", "priceyd",
                        "priceed", "pricerz", "priceyz", "pricewz"},
                new int[]{R.id.tvStartStationName, R.id.tvEndStationName, R.id.tvStartTime, R.id
                        .tvArriveTime, R.id.tvStationTrainCode,
                        R.id.tvTrainClassName, R.id.tvLishi, R.id.tvPricesw, R.id.tvPricetd, R.id
                        .tvPricegrw, R.id.tvPricerw, R.id.tvPriceyw,
                        R.id.tvPriceyd, R.id.tvPriceed, R.id.tvPricerz, R.id.tvPriceyz, R.id
                        .tvPricewz});
        mShow_data.setAdapter(adapter);
    }


}
