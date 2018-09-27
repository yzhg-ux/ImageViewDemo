package cn.yzhg.demo.http;

import io.reactivex.disposables.Disposable;

/**
 * 类 名: RequestPairs
 * 作 者: yzhg
 * 创 建: 2018/9/27 0027
 * 版 本: 1.0
 * 历 史: (版本) 作者 时间 注释
 * 描 述: 用于添加RxJava生命周期
 */
public class RequestPairs {
    public String key;
    public Disposable value;


    public RequestPairs(String key, Disposable value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "RequestPairs{" +
                "key='" + key + '\'' +
                ", value=" + value +
                '}';
    }
}
