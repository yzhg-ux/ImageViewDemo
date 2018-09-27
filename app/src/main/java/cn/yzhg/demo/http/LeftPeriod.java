package cn.yzhg.demo.http;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.ListCompositeDisposable;

/**
 * description (网络请求的生命周期管理)
 * Created by yzhg on 2017/9/22.
 */

public class LeftPeriod {

    /**
     * Retrofit生命周期的管理方法,内部封装了list集合
     */

    private static Map<String, ListCompositeDisposable> disposableMap = new HashMap<>();

    private static ListCompositeDisposable listCompositeDisposable = new ListCompositeDisposable();

    /**
     * 网络请求生命周期管理
     */
    public static void addDisposable(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed())
            listCompositeDisposable.add(disposable);
    }

    /**
     * 移除
     */
    public static void reDisposable(Disposable disposable) {
        if (disposable != null)
            listCompositeDisposable.remove(disposable);
    }

    public static void clear() {
        if (!listCompositeDisposable.isDisposed())
            listCompositeDisposable.clear();
    }
}
