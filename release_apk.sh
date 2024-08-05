#!/usr/bin/env bash
#source .bash_profile
#adb shell am kill com.fuan.market
#adb shell am start -a android.intent.action.VIEW -d "kshop://jwruihe/address?json={"title":"2","url":"https://www.baidu.com"}"
#adb shell am start -a android.intent.action.VIEW -d "kshop://jwruihe/productDetails?json={"productId":"2","pmc":"123456"}"
# 清除之前的版本
./gradlew clean
# 打包正式版
./gradlew assembleReelhk