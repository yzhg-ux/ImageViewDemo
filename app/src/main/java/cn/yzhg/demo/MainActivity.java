package cn.yzhg.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import cn.yzhg.demo.bean.UserInfo;
import cn.yzhg.demo.http.DataType;
import cn.yzhg.demo.http.OnResultListener;
import cn.yzhg.demo.http.YHttp;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView iv_image_icon = (ImageView) findViewById(R.id.iv_image_icon);

        iv_image_icon.setImageResource(R.mipmap.aaaa);

        YHttp.initYHttp(this);
        YHttp.Builder builder = new YHttp.Builder();
        builder
                .setParams("", "")
                .setUrl("")
                .setClass(UserInfo.class)
                .setTag(MainActivity.class.toString())
                .build()
                .postRx(new OnResultListener<UserInfo>() {
                    @Override
                    public void onSuccess(UserInfo result) {

                    }

                    @Override
                    public void onError(int code, String message) {

                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });

        builder
                .setParams("", "")
                .setUrl("")
                .setClass(UserInfo.class)
                .setTag(MainActivity.class.toString())
                .build()
                .post(new OnResultListener<UserInfo>() {
                    @Override
                    public void onSuccess(UserInfo result) {
                    }

                    @Override
                    public void onError(int code, String message) {
                    }

                    @Override
                    public void onFailure(String message) {
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
