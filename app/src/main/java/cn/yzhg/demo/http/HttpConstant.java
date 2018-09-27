package cn.yzhg.demo.http;

/**
 * 类 名: HttpConstant
 * 作 者: yzhg
 * 创 建: 2018/9/26 0026
 * 版 本: 1.0
 * 历 史: (版本) 作者 时间 注释
 * 描 述: 网络请求常量配置
 */
public class HttpConstant {

    //配置总连接
    public static final String HTTP_BASE_URL = "";
    //读取超时时间
    public static final long HTTP_READ_TIME_OUT = 3000;
    //连接超时时间
    public static final long HTTP_CONNECT_TIME_OUT = 3000;
    //缓存目录
    public static final String HTTP_CACHE_PATH = "YHttpCache";
    //设置缓存空间
    public static final long HTTP_CACHE_TIME = 1024 * 1024 * 100;
    //设置解析类型
    public static final int HTTP_BODY_TYPE = DataType.JSON_OBJECT;
    //是否开启日志
    public static final boolean HTTP_IS_OPEN_LOG = true;



}
