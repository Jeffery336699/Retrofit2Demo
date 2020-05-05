package com.example.okio;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.okio.retrofit.GitHubService;
import com.example.okio.retrofit.Repo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Jeffery";
    private Button btRetrofit;
    private Handler mainHandler=new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initData() {
        btRetrofit.setOnClickListener(v-> {
//            Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
            //TODO:添加主线程调度
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "-->"+Thread.currentThread().getName(), Toast.LENGTH_SHORT).show();
                }
            });

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            GitHubService github = retrofit.create(GitHubService.class);
            Call<List<Repo>> call = github.listRepos("square");
//                List<Repo> repos = call.execute().body();//同步

                call.enqueue(new Callback<List<Repo>>() {
                    @Override
                    public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                        System.out.println(Thread.currentThread().getName()+"------------onResponse------------size:"+response.body().size());
                    }

                    @Override
                    public void onFailure(Call<List<Repo>> call, Throwable t) {
                        System.out.println("------------onFailure------------");
                    }
                });

        });
    }

    private void initView() {
        btRetrofit = findViewById(R.id.btRetrofit);
    }
}
