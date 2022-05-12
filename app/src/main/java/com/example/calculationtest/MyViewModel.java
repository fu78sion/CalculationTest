package com.example.calculationtest;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import java.util.Random;

public class MyViewModel extends AndroidViewModel {

    //1. 添加完构造器后，还得多写一个SavedStateHandle handle 作用：即使进程被杀死，handle数据也能存活
    SavedStateHandle handle;

    //2. 为了使用shp，需要声明一些常量
    private final static String KEY_HIGH_SCORE = "key_high_score"; //最高分
    private final static String KEY_LEFT_NUMBER = "key_left_number"; //左值
    private final static String KEY_RIGHT_NUMBER = "key_right_number"; //右值
    private final static String KEY_OPERATOR = "key_operator"; // 操作符
    private final static String KEY_Answer = "key_answer"; // 答案
    private final static String KEY_CURRENT_SCORE = "key_current_score"; // 当前得分
    private final static String SAVE_SHP_DATA_NAME = "save_shp_data_name";
    public boolean winFlag = false;

    //3. 开始写
    public MyViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);
        if (!handle.contains(KEY_HIGH_SCORE)) {

            //如果为空 就读取一个
            SharedPreferences shp = getApplication().getSharedPreferences(SAVE_SHP_DATA_NAME, Context.MODE_PRIVATE);

            //读完之后把数据设置进去，键值对，缺省值是0
            handle.set(KEY_HIGH_SCORE, shp.getInt(KEY_HIGH_SCORE, 0));

            //同理，意味着其他数据也没有，所以接下来进行初始化
            handle.set(KEY_LEFT_NUMBER, 0);
            handle.set(KEY_RIGHT_NUMBER, 0);
            handle.set(KEY_OPERATOR, "+");
            handle.set(KEY_Answer, 0);
            handle.set(KEY_CURRENT_SCORE, 0);
        }
        this.handle = handle;
    }

    //写这些量的get方法 必须写成public
    //否则在xml中无法绑定，绑定时直接data点，点的不是成员变量而是get方法后边的一串东西
    public MutableLiveData<Integer> getLeftNumber() {
        return handle.getLiveData(KEY_LEFT_NUMBER);
    }

    public MutableLiveData<Integer> getRightNumber() {
        return handle.getLiveData(KEY_RIGHT_NUMBER);
    }

    public MutableLiveData<String> getOperator() {
        return handle.getLiveData(KEY_OPERATOR);
    }

    public MutableLiveData<Integer> getHighScore() {
        return handle.getLiveData(KEY_HIGH_SCORE);
    }

    public MutableLiveData<Integer> getCurrentScore() {
        return handle.getLiveData(KEY_CURRENT_SCORE);
    }

    public MutableLiveData<Integer> getAnswer() {
        return handle.getLiveData(KEY_Answer);
    }

    //最关键的部分，生成一个题目
    public void generator() {
        int level = 20;
        Random random = new Random();
        int x, y;
        x = random.nextInt(level) + 1;
        y = random.nextInt(level) + 1;
        if (x % 2 == 0) {
            getOperator().setValue("+");
            getLeftNumber().setValue(x);
            getRightNumber().setValue(y);
            getAnswer().setValue(x + y);
        } else {
            if (x < y) {
                getOperator().setValue("-");
                getLeftNumber().setValue(x+y);
                getRightNumber().setValue(y);
                getAnswer().setValue(x);
            } else {
                getOperator().setValue("-");
                getLeftNumber().setValue(x);
                getRightNumber().setValue(y);
                getAnswer().setValue(x - y);
            }
        }
    }

    //保存最高纪录
    public void save() {

        //涉及到读取文件，就用shp
        SharedPreferences shp = getApplication().getSharedPreferences(SAVE_SHP_DATA_NAME, Context.MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor edit = shp.edit();

        //存入数据，有不同的put方法，按照键值对的形式
        edit.putInt(KEY_HIGH_SCORE, getHighScore().getValue());
        edit.apply();
    }

    //开始答题环节
    //答对了
    public void answerRight() {

        //获取当前数据，其实获取了一个livedata对象 要获取真实值的话必须得getValue()
        getCurrentScore().setValue(getCurrentScore().getValue() + 1);
        if (getCurrentScore().getValue() > getHighScore().getValue()) {
            getHighScore().setValue(getCurrentScore().getValue());
            winFlag = true;
        }

        //再给他生成一道新题
        generator();
    }
}
