package com.fzy.http;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.fzy.http.bean.HttpResultComman;
import com.fzy.http.httpserver.HttpManager;
import com.fzy.http.httpserver.RequestService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void retrofitTest(View v){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpManager.BASE_URL)
                //增加返回值为Gson的支持(以实体类返回)  //可以接收自定义的Gson，当然也可以不传
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RequestService service = retrofit.create(RequestService.class);
        Call<HttpResultComman> call = service.listVideo();
        //如果要调用同步请求，只需调用execute；而发起一个异步请求则是调用enqueue。
        call.enqueue(new Callback<HttpResultComman>() {
            @Override
            public void onResponse(Call<HttpResultComman> call, Response<HttpResultComman> response) {
                //在Retrofit 1.9中，如果获取的 response 不能背解析成定义好的对象，则会调用failure。
                // 但是在Retrofit 2.0中，不管 response 是否能被解析。onResponse总是会被调用。但是在结果不能被解析的情况下，response.body()会返回null。别忘了处理这种情况。
                //如果response存在什么问题，比如404什么的，onResponse也会被调用。你可以从response.errorBody().string()中获取错误信息的主体。
                Log.i("openxu", response.body().toString());
            }
            @Override
            public void onFailure(Call<HttpResultComman> call, Throwable t) {
                //主线程中调用
            }
        });
        try {
            //会阻塞线程，因此你不能在安卓的主线程中调用
//            Response<HttpResultComman> res = call.execute();
        }catch (Exception e){

        }
    }

    public void retrofitRxjava(View v) {
        //手动创建一个OkHttpClient并设置超时时间
        okhttp3.OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(1, TimeUnit.SECONDS);

        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(HttpManager.BASE_URL)
                .build();
        //加载框
        final ProgressDialog pd = new ProgressDialog(this);

        RequestService apiService = retrofit.create(RequestService.class);
        Observable<HttpResultComman> observable = apiService.getAllVedio();
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResultComman>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        pd.show();
                        Log.i("openxu", "请求数据开始");
                    }
                    @Override
                    public void onCompleted() {
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        Log.i("openxu", "请求数据完成");
                    }
                    @Override
                    public void onError(Throwable e) {
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }
                        Log.e("openxu", "请求数据失败：\n" + e.getMessage());
                    }
                    @Override
                    public void onNext(HttpResultComman httpResultComman) {
                        Log.w("openxu", "无封装：\n" + httpResultComman.getData().toString());
                    }
                });
    }


}
