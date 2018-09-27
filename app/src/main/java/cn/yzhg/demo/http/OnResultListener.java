package cn.yzhg.demo.http;

/**
 * 类 名: OnResultListener
 * 作 者: yzhg
 * 创 建: 2018/9/25 0025
 * 版 本: 1.0
 * 历 史: (版本) 作者 时间 注释
 * 描 述: 接口,指定数据返回成功失败
 */
public class OnResultListener<T> {

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 请求成功的数据
     */
    public void onSuccess(T result) {
    }

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 响应成功,但是失败
     */
    public void onError(int code, String message) {
    }

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 请求失败
     */
    public void onFailure(String message) {
    }

}
