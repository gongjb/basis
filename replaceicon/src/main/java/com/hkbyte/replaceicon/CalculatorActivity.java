package com.hkbyte.replaceicon;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.chenenyu.router.annotation.Route;
import com.hkbyte.replaceicon.databinding.ActivityCalculatorBinding;
import com.hkbyte.replaceicon.router.ReplaceRouter;
import com.yft.zbase.base.BaseActivity;
import com.yft.zbase.base.BaseViewModel;
import com.yft.zbase.router.RouterFactory;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

@Route(ReplaceRouter.CALCULATOR_ACTIVITY)
public class CalculatorActivity extends BaseActivity<ActivityCalculatorBinding, BaseViewModel> {
    private TextView textViewResult;
    private StringBuilder inputBuilder;
    @Override
    public void initView() {
        textViewResult = mDataBing.textViewResult;
        inputBuilder = new StringBuilder();
        // 设置按钮点击监听
        setupButtonListeners();
    }

    private void setupButtonListeners() {
        int[] buttonIds = {
                R.id.button0, R.id.button1, R.id.button2, R.id.button3,
                R.id.button4, R.id.button5, R.id.button6, R.id.button7,
                R.id.button8, R.id.button9, R.id.buttonAdd, R.id.buttonSubtract,
                R.id.buttonMultiply, R.id.buttonDivide, R.id.buttonClear, R.id.buttonEqual,
                R.id.buttonPercent, R.id.buttonBackspace, R.id.buttonDot, R.id.button00
        };

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(this::onButtonClick);
        }
    }


    @Override
    protected void statusBarModel(boolean isDarkMode) {
        super.statusBarModel(false);
    }

    private void onButtonClick(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();

        switch (buttonText) {
            case "=":
                calculateResult();
                break;
            case "C":
                clearInput();
                break;
            case "←":
                backspace();
                break;
            case "00":
                if (inputBuilder.length() > 0) {
                    appendInput("00");
                }
                break;
            case "%":
               // appendInput("/100");
                if (inputBuilder.length() > 0) {
                    inputBuilder.append("/100");
                    calculateResult(inputBuilder.toString());
                }
                break;
            default:
                handleInput(buttonText);
                break;
        }

        String pwd = inputBuilder.toString();
        if (!TextUtils.isEmpty(pwd)) {
            if ("0000".equals(pwd)) {
                String activityName = RouterFactory.getInstance().getPage("MainActivity");
                RouterFactory.getInstance().startRouterActivity(this, activityName);
            }
        }
    }

    private void handleInput(String input) {
        appendInput(input);
    }

    private void appendInput(String input) {
        inputBuilder.append(input);
        textViewResult.setText(inputBuilder.toString());
    }

    private void backspace() {
        if (inputBuilder.length() > 0) {
            inputBuilder.setLength(inputBuilder.length() - 1);
            textViewResult.setText(inputBuilder.length() > 0 ? inputBuilder.toString() : "0");
        }
    }

    private void calculateResult(String formula) {
        if (TextUtils.isEmpty(formula)) {
            return;
        }
        try {
            Expression exp = new ExpressionBuilder(formula).build();
            double result = exp.evaluate();
            textViewResult.setText(String.valueOf(result));
            inputBuilder.setLength(0);  // 清空输入
        } catch (ArithmeticException | IllegalArgumentException e) {
            textViewResult.setText("错误");
            inputBuilder.setLength(0);  // 清空输入
        }
    }

    private void calculateResult() {
        String expression = inputBuilder.toString();
        try {
            Expression exp = new ExpressionBuilder(expression).build();
            double result = exp.evaluate();
            if (result == (int) result) {
                textViewResult.setText(String.valueOf((int) result));  // 显示整数
            } else {
                textViewResult.setText(String.valueOf(result));  // 显示小数
            }
            inputBuilder.setLength(0);  // 清空输入
        } catch (ArithmeticException | IllegalArgumentException e) {
            textViewResult.setText("错误");
            inputBuilder.setLength(0);  // 清空输入
        }
    }

    private void clearInput() {
        inputBuilder.setLength(0);
        textViewResult.setText("0");
    }

    @Override
    public void initListener() {
        // 这里可以添加其他监听器
    }

    @Override
    public void initData() {
        // 初始化数据
    }

    @Override
    public int getLayout() {
        return R.layout.activity_calculator;
    }
}
