package cn.yzhg.demo.http;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * 类 名: ApiService
 * 作 者: yzhg
 * 创 建: 2018/9/26 0026
 * 版 本: 1.0
 * 历 史: (版本) 作者 时间 注释
 * 描 述:
 */
public interface ApiService {


    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: get请求方式访问连接
     */
    @GET
    Call<ResponseBody> executeGet(@Url String url);


    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: post方式访问请求
     */
    @FormUrlEncoded
    @POST
    Call<ResponseBody> executePost(@Url String url, @FieldMap Map<String, String> map);


    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: RxJava方式请求
     */
    @FormUrlEncoded
    @POST
    Observable<ResponseBody> executePostRx(@Url String url, @FieldMap Map<String, String> map);

    /**
     * 作 者: yzhg
     * 历 史: (版本) 1.0
     * 描 述: RxJava get请求方式
     */
    @GET
    Observable<ResponseBody> executeGetRx(@Url String url);

}











