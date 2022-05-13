package com.example.calculationtest;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.DialogInterface;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    NavController controller;
    MyViewModel myViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1. 获取controller,这样会报错
        //controller = Navigation.findNavController(this, R.id.fragmentContainerView);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView);
        assert navHostFragment != null;
        controller = navHostFragment.getNavController();

        //2. 设置左上角导航栏
        NavigationUI.setupActionBarWithNavController(this, controller);
    }

    //3. 设置导航栏作用
    @Override
    public boolean onSupportNavigateUp() {
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        //正在答题时退出
        if (controller.getCurrentDestination().getId() == R.id.questionFragment) {

            //创建对话框
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.dialog)); //getString获取一个对象 转换成String类型
            builder.setPositiveButton(R.string.positive_dialog, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    //退出时当前分数清零
                    myViewModel.getCurrentScore().setValue(0);
                    controller.navigateUp();
                }
            });
            builder.setNegativeButton(R.string.nagative_dialog, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (controller.getCurrentDestination().getId() == R.id.titleFragment) {
            finish();
        } else {
            controller.navigate(R.id.titleFragment);
        }
        return super.onSupportNavigateUp();
    }

    //4. 重写back键

    @Override
    public void onBackPressed() {
        onSupportNavigateUp();
    }
}

/**
 * 命名比较奇怪的三个地方
 * 1. data.highScore xml中绑定数据 这里的highScore其实是get方法的后缀 语法糖
 * 2. FragmentQuestionBinding 声明binding 前缀其实是对应xml的名字 这个类是自动生成的
 * 3. setData binding双向绑定 set后缀其实是xml声明的变量
 * 4. 最后成功发布到GitHub
 */