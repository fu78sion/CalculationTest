package com.example.calculationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

/**
 * 命名比较奇怪的三个地方
 * 1. data.highScore xml中绑定数据 这里的highScore其实是get方法的后缀 语法糖
 * 2. FragmentQuestionBinding 声明binding 前缀其实是对应xml的名字 这个类是自动生成的
 * 3. setData binding双向绑定 set后缀其实是xml声明的变量
 */