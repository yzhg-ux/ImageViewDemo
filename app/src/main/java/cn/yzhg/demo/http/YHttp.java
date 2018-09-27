package cn.yzhg.demo.http;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.ListCompositeDisposable;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 类 名: YHttp
 * 作 者: yzhg
 * 创 建: 2018/9/21 0021
 * 版 本: 1.0
 * 历 史: (版本) 作者 时间 注释
 * 描 述: Retrofit + OkHttp 封装
 */
public class YHttp {

    private static String TAG = "--------------------  YHTTP --------------------";

    private static Context context;

    /*
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 总连接
     */
    private String BASE_URL = HttpConstant.HTTP_BASE_URL;

    /*
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 创建OkHttpClient实例
     */
    private OkHttpClient okHttpClient;

    /*
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 创建 读取时间和连接时间  默认30秒
     */
    private long readTimeOut = HttpConstant.HTTP_READ_TIME_OUT;
    private long connectTimeOut = HttpConstant.HTTP_CONNECT_TIME_OUT;

    /*
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 设置缓存目录,以及缓存时间(默认24个小时)
     */
    private String cachePath = HttpConstant.HTTP_CACHE_PATH;
    // private long cacheTime = 1024 * 1024 * 100;
    private long cacheTime = HttpConstant.HTTP_CACHE_TIME;

    /*
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: url连接, 拼接在BaseUrl后面
     */
    private String url;
    /*
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 网络连接标识,可用于储存网络请求,取消网络请求
     */
    private Object tag;
    /*
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 添加请求参数
     */
    private Map<String, String> params = new HashMap<>();
    /*
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 数据类型,默认json类型
     */
    @DataType.Type
    private int bodyType = HttpConstant.HTTP_BODY_TYPE;
    /*
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 解析类
     */
    private Class clazz = null;

    /*
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: Retrofit实例
     */
    private Retrofit retrofit;

    /*
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 保存请求,请求时需要将次call添加进来,结束时将清除
     */
    private static final Map<String, Call> CALL_MAP = new HashMap<>();

    /*
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 储存网络请求 用于RxJava方式请求网络
     */
    private static final Map<String, Observable> OBSERVABLE_MAP = new HashMap<>();

    /*
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 管理RxJava生命周期
     */
    private static final List<RequestPairs> mRequestPool = new ArrayList<>();
    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 构建单利设计模式
     */
    @SuppressLint("StaticFieldLeak")
    private static YHttp instance = null;

    public static void initYHttp(Context context) {
        YHttp.context = context.getApplicationContext();
    }

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述:  获取单一实例
     */
    public static YHttp getInstance() {
        synchronized (YHttp.class) {
            if (instance == null) {
                instance = new YHttp();
            }
        }
        return instance;
    }

    private YHttp() {
        okHttpClient = getOkHttpClient();
    }


    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 创建OkHttpClient
     */
    private OkHttpClient getOkHttpClient() {
        File cacheFile = new File(context.getCacheDir(), cachePath);
        Cache cache = new Cache(cacheFile, cacheTime);
        OkHttpClient.Builder build = new OkHttpClient.Builder();
        build.readTimeout(readTimeOut, TimeUnit.MILLISECONDS);
        build.connectTimeout(connectTimeOut, TimeUnit.MILLISECONDS);
        if (cacheTime > 0) {//如果缓存时间大于0则 需要设置缓存
            if ("".equals(cachePath)) {
                throw new IllegalArgumentException(TAG + "请检查您是否设置了缓存时间,但是设置了缓存路径为空");
            } else {
                build.cache(cache);
            }
        }
        return build.build();
    }

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 获取Retrofit实例
     */
    private void getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .build();
        }
    }

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: post网络请求
     */
    public void post(final OnResultListener onResultListener) {
        Call<ResponseBody> mCall = retrofit.create(ApiService.class).executePost(this.url, this.params);
        putRequest(mCall);  // 添加请求
        request(mCall, onResultListener);
    }

    public void postRx(final OnResultListener onResultListener) {
        Observable<ResponseBody> mObservable = retrofit.create(ApiService.class).executePostRx(this.url, this.params);
        request(mObservable, onResultListener);
    }

    //https://www.jianshu.com/p/29c2a9ac5abf
    //https://www.jianshu.com/p/bd758f51742e
    //https://blog.csdn.net/j550341130/article/details/80540759


    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 实现网络请求
     */
    private void request(Observable<ResponseBody> mObservable, final OnResultListener onResultListener) {
        mObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        putRequest(d);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String result = responseBody.string();
                            parseData(result, clazz, bodyType, onResultListener);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (tag != null) {
                            removeRequestTag(url);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        onResultListener.onFailure(e.getMessage());
                        if (tag != null) {
                            removeRequestTag(url);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (tag != null) {
                            removeRequestTag(url);
                        }
                    }
                });
    }

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 网络请求
     */
    private void request(Call<ResponseBody> mCall, final OnResultListener onResultListener) {
        mCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (200 == response.code()) {
                    String result = null;
                    try {
                        result = response.body().string();
                        parseData(result, clazz, bodyType, onResultListener);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (!response.isSuccessful() || 200 != response.code()) {
                    onResultListener.onError(response.code(), response.message());
                }
                if (null != tag) {
                    removeCall(url);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                onResultListener.onFailure(t.getMessage());
                if (null != tag) {
                    removeCall(url);
                }
            }
        });
    }

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 添加一个请求,可以用于取消请求,避免造成内存泄漏
     */
    private synchronized void putRequest(Call call) {
        if (this.tag == null) return;
        synchronized (CALL_MAP) {
            CALL_MAP.put(this.tag.toString() + this.url, call);
        }
    }

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 根据标签 取消某个网络请求
     */
    public synchronized void cancelRequest(Object tag) {
        if (tag == null) return;
        List<String> list = new ArrayList<>();
        synchronized (CALL_MAP) {
            for (String key : CALL_MAP.keySet()) {
                if (key.startsWith(tag.toString())) {
                    CALL_MAP.get(key).cancel();
                    list.add(key);
                }
            }
        }
        for (String callKey : list) {
            removeCall(callKey);
        }
    }


    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 移除某个网络请求
     */
    private synchronized void removeCall(String callKey) {
        synchronized (CALL_MAP) {
            for (String key : CALL_MAP.keySet()) {
                if (key.contains(callKey)) {
                    callKey = key;
                    break;
                }
            }
            CALL_MAP.remove(callKey);
        }
    }


    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 添加网络请求
     */
    private synchronized void putRequest(Disposable d) {
        if (mRequestPool != null) {
            mRequestPool.add(new RequestPairs(this.tag.toString() + this.url, d));
        }
    }

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 取消单个网络请求
     */
    public void cancelRequestTag(Object tag) {
        if (tag == null) return;
        RequestPairs requestPairs = null;
        try {
            if (mRequestPool != null) {
                //倒叙解除某个请求
                for (int i = mRequestPool.size() - 1; i >= 0; i--) {
                    requestPairs = mRequestPool.get(i);
                    if (requestPairs.key.startsWith(tag.toString())) {
                        Disposable disposable = requestPairs.value;   //找到此网络请求
                        if (disposable != null && !disposable.isDisposed()) {
                            disposable.dispose();
                        }
                        NetLoginUtils.i(tag.toString() + "---网络请求已经取消");
                    } else {
                        NetLoginUtils.i(tag.toString() + "---没有在网络池中找到该请求,或许已经结束");
                    }
                }
                removeRequestTag(tag.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 取消多个网络请求
     */
    public void cancelRequestMoreTag(final String... mRequestTags) {
        if (mRequestTags != null && mRequestTags.length > 0) {
            for (String mRequestTag : mRequestTags) {
                cancelRequestTag(mRequestTag);
            }
        }
    }

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 取消全部网络请求
     */
    public void cancelRequestAllTag() {
        if (mRequestPool != null && mRequestPool.size() > 0) {
            for (RequestPairs requestPairs : mRequestPool) {
                cancelRequestTag(requestPairs);
            }
        }
    }

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 移除单个网络请求
     */
    private synchronized void removeRequestTag(String callKey) {
        RequestPairs removeRequest = null;
        synchronized (mRequestPool) {
            for (RequestPairs requestPairs : mRequestPool) {
                if (requestPairs.key.contains(callKey)) {
                    removeRequest = requestPairs;
                }
            }
            mRequestPool.remove(removeRequest);
        }
    }

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: Builder 设计模式打造网络请求框架
     */
    public static final class Builder {
        //总连接
        private String baseUrl = "";
        //url连接
        private String url = "";
        //网络连接标识,可用于储存网络请求,取消网络请求
        private Object tag = null;
        //请求参数
        private Map<String, String> params = new HashMap<>();
        //数据类型,默认json类型
        @DataType.Type
        private int bodyType = DataType.JSON_OBJECT;
        //解析类
        private Class clazz;

        /*
         * 作 者: yzhg
         * 历 史: (版本) 1.0
         * 描 述: 设置超时时间  默认不设置为0
         */
        private long readTimeOut = 0;
        private long connectTimeOut = 0;

        /*
         * 作 者: yzhg
         * 历 史: (版本) 1.0
         * 描 述: 设置缓存目录,以及缓存时间(默认24个小时)
         */
        private String cachePath = "";
        private long cacheTime = 0;

        public Builder() {
        }

        /**
         * 作 者: yzhg
         * 历 史: (版本) 1.0
         * 描 述: 设置  总连接地址
         */
        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        /**
         * 作 者: yzhg
         * 历 史: (版本) 1.0
         * 描 述: 设置访问连接
         */
        public Builder setUrl(String url) {
            this.url = url;
            return this;
        }

        /**
         * 作 者: yzhg
         * 历 史: (版本) 1.0
         * 描 述: 设置标签,用于标记网络和取消网络
         */
        public Builder setTag(Object tag) {
            this.tag = tag;
            return this;
        }

        /**
         * 作 者: yzhg
         * 历 史: (版本) 1.0
         * 描 述: 设置网络请求参数
         */
        public Builder setParams(String key, String value) {
            this.params.put(key, value);
            return this;
        }

        /**
         * 作 者: yzhg
         * 历 史: (版本) 1.0
         * 描 述: 直接设置Map集合参数
         */
        public Builder setMapParams(Map<String, String> params) {
            this.params.putAll(params);
            return this;
        }

        /**
         * 作 者: yzhg
         * 历 史: (版本) 1.0
         * 描 述: 设置解析响应体类型,
         * 默认类型为json字符串, 可以设置为
         * DataType.STRING  字符串类型
         * DataType.XML     XML类型
         * DataType.JSON_OBJECT     json对象类型
         * DataType.JSON_ARRAY      json数组类型
         */
        public <T> Builder setClass(@DataType.Type int bodyType, @NonNull Class<T> clazz) {
            this.bodyType = bodyType;
            this.clazz = clazz;
            return this;
        }

        public Builder setClass(@DataType.Type int bodyType) {
            this.bodyType = bodyType;
            return this;
        }

        /**
         * 作 者: yzhg
         * 历 史: (版本) 1.0
         * 描 述: 设置解析类
         */
        public <T> Builder setClass(@NonNull Class<T> clazz) {
            this.clazz = clazz;
            return this;
        }

        /**
         * 作 者: yzhg
         * 历 史: (版本) 1.0
         * 描 述: 设置读取时间
         */
        public Builder setReadTimeOut(long readTimeOut) {
            this.readTimeOut = readTimeOut;
            return this;
        }

        /**
         * 作 者: yzhg
         * 历 史: (版本) 1.0
         * 描 述: 设置连接超时时间
         */
        public Builder setConnectTimeOut(long connectTimeOut) {
            this.connectTimeOut = connectTimeOut;
            return this;
        }

        /**
         * 作 者: yzhg
         * 历 史: (版本) 1.0
         * 描 述: 设置缓存路径
         */
        public Builder setCachePath(String cachePath) {
            this.cachePath = cachePath;
            return this;
        }

        /**
         * 作 者: yzhg
         * 历 史: (版本) 1.0
         * 描 述: 设置缓存时间
         */
        public Builder setCacheTime(long cacheTime) {
            this.cacheTime = cacheTime;
            return this;
        }

        /**
         * 作 者: yzhg
         * 历 史: (版本) 1.0
         * 描 述: 给应用赋值
         */
        void applyConfig(YHttp yHttp) {
            if (!"".equals(baseUrl)) yHttp.BASE_URL = this.baseUrl;
            yHttp.url = this.url;  //必须要设置url
            if (null != tag) yHttp.tag = this.tag;  //设置tag
            yHttp.params = this.params;  //设置参数
            yHttp.bodyType = this.bodyType;//设置解析类型
            if (this.bodyType == DataType.JSON_OBJECT || this.bodyType == DataType.JSON_ARRAY) {
                if (this.clazz == null)
                    throw new IllegalArgumentException(TAG + "解析类型为json对象和json数组,请确认是否设置了解析类,否则解析不出来数据");
            }
            yHttp.clazz = this.clazz;
            if (this.readTimeOut != 0) yHttp.readTimeOut = this.readTimeOut; //设置读取时间
            if (this.connectTimeOut != 0) yHttp.connectTimeOut = this.connectTimeOut;  //设置网络连接时间
            if (!"".equals(this.cachePath)) yHttp.cachePath = this.cachePath;  //设置缓存路径
            if (0 != this.cacheTime) yHttp.cacheTime = this.cacheTime;  //设置缓存时间
            if ("".equals(url)) throw new IllegalArgumentException(TAG + "请确认您是否设置url");
        }

        public YHttp build() {
           /* if (!TextUtils.isEmpty(baseUrl)) {
                BASE_URL = baseUrl;
            }*/
            YHttp yHttp = YHttp.getInstance();
            yHttp.getRetrofit();
            applyConfig(yHttp);
            return yHttp;
        }
    }

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: 解析数据类型
     *
     * @param data             请求回来的json数据类型
     * @param clazz            解析类
     * @param bodyType         解析数据类型
     * @param onResultListener 回调方数据接口
     */
    @SuppressWarnings("unchecked")
    private void parseData(String data, Class clazz, @DataType.Type int bodyType, OnResultListener onResultListener) {
        switch (bodyType) {
            case DataType.STRING:  //如果是字符串类型,则不需要解析直接返回
                onResultListener.onSuccess(data);
                break;
            case DataType.JSON_OBJECT:  //如果是json对象类型,则需要解析
                onResultListener.onSuccess(JsonUtil.parseJsonToBean(data, clazz));
                break;
            case DataType.JSON_ARRAY:
                onResultListener.onSuccess(JsonUtil.parseToArrayList(data, clazz));
                break;
            case DataType.XML:
                onResultListener.onSuccess("暂时不支持XML解析");
                break;
            default:
                throw new IllegalArgumentException(TAG + "请注意n您想要的数据格式,目前仅支持字符串,json对象,json数组格式.默认为json对象格式,请注意设置");
        }
    }
}







