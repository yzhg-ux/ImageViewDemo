package cn.yzhg.demo;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import cn.yzhg.demo.bean.UserInfo;
import cn.yzhg.demo.http.DataType;
import cn.yzhg.demo.http.OnResultListener;
import cn.yzhg.demo.http.YHttp;
import okhttp3.OkHttpClient;

//http://payjds.leyiou.com/api/appCommon/getuserlevel
public class MainActivity extends AppCompatActivity {

    private String TAG = "--------------------  MainActivity    --------------------";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button but_get_net = findViewById(R.id.but_get_net);
        but_get_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNetData();
            }
        });
    }

    private void getNetData() {
        YHttp.initYHttp(this);
        YHttp.Builder builder = new YHttp.Builder();
        builder
                .setUrl("api/appCommon/getuserlevel")
                .setClass(DataType.STRING, UserInfo.class)
                .setTag(MainActivity.class.toString())
                .build()
                .getRx(new OnResultListener<String>() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onSuccess(String result) {
                        Log.i(TAG, "onSuccess: " + result);
                    }

                    @Override
                    public void onError(int code, String message) {
                        Log.i(TAG, "网络错误" + message);
                    }

                    @Override
                    public void onFailure(String message) {
                        Log.i(TAG, "网络错误--" + message);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        YHttp.getInstance().cancelRequest(MainActivity.class.toString());
        YHttp.getInstance().cancelRequestTag(MainActivity.class.toString());
    }
}
