package cn.yzhg.demo.http;

import android.util.Log;

/**
 * 类 名: NetLoginUtils
 * 作 者: yzhg
 * 创 建: 2018/9/27 0027
 * 版 本: 1.0
 * 历 史: (版本) 作者 时间 注释
 * 描 述: 网络请求时的日志打印
 */
public class NetLoginUtils {
    private static String TAG = "--------------------  YHTTP --------------------";

    public static void i(String msg) {
        if (HttpConstant.HTTP_IS_OPEN_LOG)
            Log.i(TAG, msg);
    }

    public static void d(String msg) {
        if (HttpConstant.HTTP_IS_OPEN_LOG)
            Log.d(TAG, msg);
    }

    public static void e(String msg) {
        if (HttpConstant.HTTP_IS_OPEN_LOG)
            Log.e(TAG, msg);
    }

}
