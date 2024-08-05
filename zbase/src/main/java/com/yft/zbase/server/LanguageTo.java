package com.yft.zbase.server;

public enum LanguageTo {
    HOME("Home", "首页"),
    SKETCH("Sketch", "短剧"),
    DATA("Data", "数据"),
    ME("Me", "我的"),
    //下载中...
    DOWNLOAD("Downloading...", "下载中..."),
    HOME_SEARCH("Search for hot dramas", "搜索你想看的热剧"),
    SEARCH("Search", "搜索"),
    LATEST_RELEASE("Latest release", "最新发布"),
    BESTSELLER_LIST("Bestseller list", "畅销榜单"),
    THERE_NO_DATA("There is no more data", "我也是有底线的！"),
    EPISODES_IN_TOTAL("%s episodes in total", "共%s集"), // 共10集
    //Loading, please wait...
    LOADING_WAIT("Loading, please wait...", "正在加载，请稍后..."),
    //Episode 10 has a total of 100 episodes
    EPISODES_IN_TOTAL_IN_ALL("Episode %s has a total of %s episodes", "第%s集/共%s集"),
    STREAMING("Streaming", "投流"),
    ANALECTA("Analecta", "选集"),
    SHARE("Share", "分享"),
    TO_STREAM("I'm going to invest", "我要投流"),
    GO_TO_STREAM("Go to the invest", "去投流"),
    EXPERIENCE_STREAM("Experience investing", "体验投流"),
    OFFICIALLY_STREAM("Formal investment", "正式投流"),
    WAYS_INVEST("Ways to invest", "请选择您的投流方式"),
    SHARE_NOW("Share now", "立即分享"),
    GLOBAL_DATA("Global data", "全球数据"),
    CHINA_DATA("China data", "中国地区数据"),
    EUROPE("Europe", "欧洲"),
    AMERICAS("Europe", "美洲"),
    ASIA("Asia", "亚洲"),
    TOTAL_GLOBAL_REVENUE("Total revenue from global flows", "全球投流总收益"),
    TOTAL_CHINA_REVENUE("Total revenue from China flows", "中国投流总收益"),
    AVERAGE_AMOUNT("The average amount paid", "平均付费金额"),
    TRANSACTIONS_NUBMER("Number of transactions", "成交人数"),
    WATCH_SELL_RATE("Watch-Sell Rate", "观看-成交率"),
    BROWSE_IMPRESSIONS("Browse impressions", "浏览曝光量"),
    CLICKS("Clicks", "点击次数"),
    TIKTOK_STREAMING("TikTok streaming", "TikTok投流"),
    RECHARGE("Recharge", "充值"),
    PAYOUTS("Payouts", "提现"),
    //互转
    TRANSFERS("Transfers", "互转"),
    INVITE("Invite", "邀请"),
    ONLINE_SERVICE("Online customer service", "在线客服"),
    MATCHED_SERVICE("Not matched to customer service!", "未匹配到客服!"),
    MORE_SETTINGS("More settings", "更多设置"),
    INVESTMENT_DETAILS("Investment details", "投流明细"),
    MY_TEAM("My team", "我的团队"),
    MY_FAVORITES("My favorites", "我的收藏"),
    BALANCE("Balance", "余额"),
    //待分润
    TO_BE_DIVIDED("To be divided", "待分润"),
    MOBILE_PHONE_NUMBER("Your mobile phone number is", "您的手机号为"),
    PROMPT("Prompt", "提示"),
    SURE("Ok", "确定"),
    CANCEL("Cancel", "取消"),
    //拍摄、存储权限说明
    PERMISSION_DESCRIPTION("Description of shooting and storage permissions", "拍摄、存储权限说明"),
    PERMISSION_DESCRIPTION_DES("If you use this function to capture, store, or read photos from an album to set user profile picture and other information, please confirm your authorization, otherwise you will not be able to use this function.",
            "使用该功能拍摄或存储、读取相册的照片，以便于设置用户头像等信息，请您确认授权，否则无法使用该功能。"),
    UPGRADING("Upgrading···", "上传中···"),
    //"头像更新成功"
    AVATAR_SUCCESS("The avatar was updated successfully", "头像更新成功"),
    //等级
    LEVEL("level", "等级"),
    //手机号码
    PHONE_NUMBER("Mobile phone number", "手机号码"),
    //昵称
    NICKNAME("nickname", "昵称"),
    // 头像
    AVATAR("avatar", "头像"),
    PERSONAL_INFORMATION("Personal Information", "个人信息"),
    //拍照
    PHOTOGRAPH("photograph", "拍照"),
    //相册
    PHOTO_ALBUM("photo album", "相册"),
    //选择网络
    SELECT_NETWORK("Select Network", "选择网络"),
    //余额明细
    BALANCE_DETAILS("Balance details", "余额明细"),
    //全部
    ALL("All", "全部"),
    //交易时间
    TRADING_HOURS("Trading hours", "交易时间"),
    TRANSACTION_DESCRIPTION("Transaction description", "交易说明"),
    TRANSACTION_AMOUNT("Transaction amount", "交易金额"),
    TRANSACTION_TYPE("Transaction type", "交易类型"),
    //提现手续费
    WITHDRAWAL_FEES("Withdrawal fees", "提现手续费"),
    STREAMING_INCOME("Streaming income", "投流收益"),
    //直推收益
    DIRECT_PUSH_EARNINGS("Direct Push Earnings", "直推收益"),
    //团队收益
    TEAM_EARNINGS("Team earnings", "团队收益"),
    //转出
    ROLL_OUT("Roll out", "转出"),
    //转入
    ROLL_IN("Turn in", "转入"),
    //回滚
    ROLLBACK("rollback", "提现失败"),
    //  PLATFORM_RECHARGE("adminRecharge", "平台充值"),
    //    PLATFORM_DEDUCTIONS("adminReduce", "平台扣减"),'
    //LCStr(@"平台充值", @"Platform recharge");
    //LCStr(@"平台扣减", @"Platform deductions");
    PLATFORM_RECHARGE("Platform recharge", "平台充值"),
    //回滚
    PLATFORM_DEDUCTIONS("Platform deductions", "平台扣减"),
    //明细筛选
    DETAIL_FILTERING("Detail filtering", "明细筛选"),
    // 投资剧集
    INVEST_EPISODES("Invest in episodes", "投资剧集"),
    //交易订单
    TRADING_ORDERS("Trading Orders", "交易订单"),
    // 交易金额
    TRADING_AMOUNT("Trading amount", "交易金额"),
    TRADING_DESCRIPTION("Trading Description", "交易说明"),
    INVESTMENT_TIME("Investment time", "投流时间"),
    EFFECTIVE("Y", "有效"),
    VOID("N", "无效"),
    //直推人数
    DIRECT_PUSH_QUANTITY("Introduce", "直推人数"),
    TOTAL_TEAM_PERFORMANCE("Team performance", "团队总业绩"),
    TEAM_QUANTITY("Team", "团队人数"),
    //团队有效人数
    TEAM_EFFECTIVE_NUMBER("Team effective", "团队有效人数"),
    //直推有效人数
    RECOMMEND_EFFECTIVE_NUMBER("Introduce effective", "直推有效人数"),
    //注册时间
    CREATION_TIME("Creation time", "注册时间"),
    //设置
    SET_UP("Set up", "设置"),
    //当前已是最新版本!
    LATEST_VERSION_TIPS("It's the latest version!", "当前已是最新版本!"),
    //您当前的账号已被注销!
    BEEN_CANCELLED_TIPS("Your current account has been cancelled!", "您当前的账号已被注销!"),
    // 您已经成功退出当前账号!
    CURRENT_ACCOUNT_TIPS("You have successfully logged out of your current account!", "您已经成功退出当前账号!"),
    //不支持该语言!
    NOT_SUPPORTED_TIPS("The language is not supported!", "不支持该语言!"),
    //清理完成
    NCLEANUP_COMPLETE("Cleanup is complete", "清理完成"),
    //请稍后...
    PLEASE_WAIT("Please wait...", "请稍后..."),
    //版本更新
    VERSION_UPDATES("Version updates", "版本更新"),
    //缓存清理
    CACHE_CLEANUP("Cache cleanup", "缓存清理"),
    //语言
    LANGUAGE("language", "语言切换"),
    //关于我们
    ABOUT_US("About us", "关于我们"),
    //注销账户
    CANCEL_ACCOUNT("Cancel account", "注销账户"),
    // 退出登录
    SIGN_OUT("Sign out", "退出登录"),
    //中文
    CHINESE("Chinese", "中文"),
    ENGLISH("English", "英文"),
    //检测到最新版本Apk，是否更新？
    IS_IT_UPDATED("he latest version of Apk has been detected, is it updated?", "检测到最新版本Apk，是否更新？"),
    //稍后更新
    UPDATE_LATER("Update later", "稍后更新"),
    //马上更新
    UPDATE_NOW("Update now", "马上更新"),
    //邀请好友
    INVITE_FRIENDS("Invite friends", "邀请好友"),
    //将图片存储到系统相册方便您邀请好友加入ReelShort，将授权存储权限，否则无法使用该功能。
    SAVING_PICTURES_PERMISSION("Saving pictures to the system album allows you to invite friends to join Reel Short, and the storage permission will be granted, otherwise you will not be able to use this feature.",
            "将图片存储到系统相册方便您邀请好友加入ReelShort，将授权存储权限，否则无法使用该功能。"),
    //存储权限说明
    DESCRIPTION_STORAGE_PERMISSIONS("Description of storage permissions",
            "存储权限说明"),
    //拍摄、存储权限说明
    DESCRIPTION_SHOOTING_STORAGE_PERMISSIONS("Description of shooting and storage permissions",
            "拍摄、存储权限说明"),
    //使用该功能拍摄或存储、读取相册的照片，以便于您完成上传图片、发表购物评价的功能，请您确认授权，否则无法使用该功能。
    DESCRIPTION_SHOOTING_STORAGE_PERMISSIONS_DES("If you use this function to take or store or read photos from the album so that you can complete the function of uploading pictures and posting shopping reviews, please confirm your authorization, otherwise you cannot use this function.",
            "使用该功能拍摄或存储、读取相册的照片，以便于您完成上传图片、发表购物评价的功能，请您确认授权，否则无法使用该功能。"),

    //保存成功
    SAVE_SUCCESSFUL("Save Successful","保存成功"),
    //邀请您一起加入%s You are invited to join in %s
    JOIN_IN_INVITED("You are invited to join in %s","邀请您一起加入%s"),
    //关闭
    CLOSE("Close","关闭"),
    //网络协议
    NETWORK_PROTOCOLS("Network","网络协议"),
    //您未选择网络
    DIDNT_NETWORK("You didn't select a network","您未选择网络"),
    // 余额充值
    BALANCE_TOP_UP("Balance top-up","余额充值"),
    //金额
    AMOUNT("Amount","金额"),
    //复制成功
    THE_REPLICATION_SUCCESSFUL("The replication is successful","复制成功"),
    NOT_COPY_ADDRESS("The current address cannot be copied","无法复制当前地址"),
    //无法复制当前金额
    NOT_COPY_AMOUNT("The current amount cannot be copied","无法复制当前金额"),
    //请在倒计时结束之前完成支付
    REMAINING_PAYMENT_TIME("Remaining payment time","请在倒计时结束之前完成支付"),
    //提现功能
    WITHDRAWAL_FEATURES("Withdrawal features","提现功能"),
    //我的余额
    MY_BALANCE("My balance","我的余额"),
    //请填写提现金额！
    FILL_WITHDRAWAL_AMOUNT("Please fill in the withdrawal amount!","请填写提现金额！"),
    //请填写提现地址!
    FILL_WITHDRAWAL_ADDRESS("Please fill in the withdrawal address!","请填写提现地址！"),
    //最小提现金额为10u
    AMOUNT_10_U("The minimum withdrawal amount is 10 u","最小提现金额为10u"),
    //您的余额不足！
    BALANCE_INSUFFICIENT("The balance is insufficient","您的余额不足"),
    //请填写正确的数值！
    FILL_CORRECT_VALUES("Please fill in the correct values!","请填写正确的数值！"),
    //已经成功提交提现申请！
    SUBMISSION_SUCCESSFUL("The submission was successful","已经成功提交提现申请！"),
    //注意: 提现到账时间为2个小时之内，如遇提现问题请联系客服。
    WITHDRAWAL_CUSTOMER_SERVICE("The withdrawal time is within 2 hours, if you encounter withdrawal problems, please contact customer service.",
            "注意: 提现到账时间为2个小时之内，如遇提现问题请联系客服。"),
    //请输入提现地址
    WITHDRAWAL_ADDRESS("Withdrawal address","请输入提现地址"),
    //请输入提现金额
    ENTER_AMOUNT("Enter the amount","请输入提现金额"),
    //手续费
    PREMIUM("Premium","手续费"),
    //全部提现
    WITHDRAW_FULL("All","全部提现"),
    //注意: 本平台只收取usdt, 如果您还未下载钱包，请联系您的上级或者客服为您提供帮助。
    PLATFORM_ONLY_CHARGES_USDT("Note: This platform only charges USDT, if you have not downloaded the wallet, please contact your superior or customer service to help you.",
            "注意: 本平台只收取usdt, 如果您还未下载钱包，请联系您的上级或者客服为您提供帮助。"),

    //请填写投流金额！
    FILL_INVESTMENT_AMOUNT("Fill in the investment amount","请填写投流金额！"),
    //充值额度不能小于1u
    LESS_1_U("The recharge amount cannot be less than 1 U","充值额度不能小于1u"),
    //请填写充值金额！
    FILL_DEPOSIT_AMOUNT("Please fill in the deposit amount!","请填写充值金额！"),
    //请输入1u以上的金额
    AMOUNT_MORE_TIPS("Please enter an amount of 1 u or more","请输入1u以上的金额"),
    //您的余额不足，请前往充值！
    PLEASE_GO_UP("Your balance is insufficient, please go to top up!","您的余额不足，请前往充值！"),
    //请输入1-99u范围之内的金额
    AMOUNT1_99("The amount is 1-99 u","请输入1-99u范围之内的金额"),
    //注意：当前为体验投流，您最多能够投99u，请确保您的余额充足，当您的余额不足时可前往充值页面去充值。
    NOTE_BALANCE_INSUFFICIENT_DES("Note: You can invest up to 99 U for experience streaming, please make sure your balance is sufficient, and when your balance is insufficient, you can go to the recharge page to recharge.",
            "注意：当前为体验投流，您最多能够投99u，请确保您的余额充足，当您的余额不足时可前往充值页面去充值。"),
    //请输入100u以上的金额
    AMOUNT100UP("The amount is more than 100 U","请输入100u以上的金额"),
    //注意：当前为正式投流，投流金额最少投100u，请确保您的余额充足，当您的余额不足时可前往充值页面去充值。
    NOTE_BALANCE_INSUFFICIENT_RECHARGE_DES("Note: At present, the current investment amount is 100 U, please make sure that your balance is sufficient, and when your balance is insufficient, you can go to the recharge page to recharge.",
            "注意：当前为正式投流，投流金额最少投100u，请确保您的余额充足，当您的余额不足时可前往充值页面去充值。"),
    //去充值
    GO_TO_RECHARGE("recharge.","去充值"),
    //充值额度不能小于一百u
    LESS_THAN_100("The recharge amount cannot be less than 100 U","充值额度不能小于100u"),
    //体验投流, 充值额度只能填写1-99u，请修改充值额度！
    FILLED_IN_1_99U("Experience streaming, the recharge limit can only be filled in 1-99u, please modify the recharge amount!","体验投流, 充值额度只能填写1-99u，请修改充值额度！"),
    //投流成功！
    SUCCESSFUL_THROWING("Successful throwing!","投流成功！"),
    //余额不足
    THE_BALANCE_INSUFFICIENT("The balance is insufficient!","余额不足！"),
    //全部转出
    ALL_TRANSFERRED_OUT("All","全部转出"),
    //转账功能
    TRANSFER_TITLE("Transfer","转账功能"),
    //请填写互转金额！
    PLEASE_TRANSFER_AMOUNT("Please fill in the transfer amount!","请填写互转金额！"),
    //请填写接收人8位数字账号！
    ACCOUNT_8_NUMBER("Please fill in the recipient's 8-digit account number!","请填写接收人8位数字账号！"),
    //账号填写有误！
    FILLED_8_NUMBER_FILLED("The account number is filled in incorrectly!","账号填写有误！"),
    //最小互转金额为1u"
    MINIMUM_TRANSFER_1_U("The minimum transfer amount is 1 u","最小互转金额为1u"),
    //已成功转出！
    SUCCESSFULLY_TRANSFERRED_OUT("Successfully transferred out!","已成功转出！"),
    //请输入转出金额
    ENTER_THE_AMOUNT("Enter the amount","请输入转出金额"),
    //免手续费
    FREE_CHARGE("No handling fee","免手续费"),
    //请输入接收人8位账号
    OTHER_PARTY_NUMBER("Enter the account number of the other party","请输入接收人8位账号"),
    //"注意: 互转金额会实时到账，如遇互转问题请联系客服。"
    OTHER_PARTY_NUMBER_DES("Note: The transfer amount will be credited to the account in real time, please contact customer service if you encounter any transfer problems.","注意: 互转金额会实时到账，如遇互转问题请联系客服。"),
    //如您不同意《用户服务协议》与《隐私政策》，我们将无法为您提供 ReelShort App的完整功能，您可以选择使用仅游览模式或直接退出应用
    AGREE_PRIVACY_DES("If you do not agree to the User Service Agreement and Privacy Policy, we will not be able to provide you with the full functionality of the ReelShort App, and you can choose to use the tour-only mode or exit the App directly",
            "如您不同意《用户服务协议》与《隐私政策》，我们将无法为您提供 ReelShort App的完整功能，您可以选择使用仅游览模式或直接退出应用"),
    //获取验证码
    VERIFICATION_CODE("Verification code","获取验证码"),
    //登录成功
    LOGIN_SUCCESSFUL("Login successful","登录成功"),
    //隐私政策
    PRIVACY_POLICY("Privacy Policy","隐私政策"),
    //用户协议
    USER_AGREEMENT("User Agreement","用户协议"),
    //正在获取验证码，请稍等
    VERIFICATION_CODE_PLEASE_WAIT("Getting verification code, please wait","正在获取验证码，请稍等"),
    //请填写正确的电话号码!
    VCORRECT_PHONE_NUMBER("Please fill in the correct phone number!","请填写正确的电话号码！"),
    //请输入电话号码！
    PLEASE_ENTER_PHONE_NUMBER("Please enter a phone number!","请输入电话号码或者邮箱！"),
    //请填写正确的验证码！
    CORRECT_VERIFICATION_CODE_TIPS("Please fill in the correct verification code!","请填写正确的验证码！"),
    //请填写验证码！
    THE_VERIFICATION_CODE_TIPS("Please fill in the verification code!","请填写验证码！"),
    //立即注册
    SIGN_UP_NOW("Sign up now","立即注册"),
    //立即登录
    LON_IN_NOW("Log in now","立即登录"),
    //"请阅读用户服务协议与隐私协议"
    SERVICE_AGREEMENT_AND_PRIVACY_POLICY("Please read the User Service Agreement and Privacy Policy","请阅读用户服务协议与隐私协议"),
    //正在请求，请稍后···
    REQUESTING_PLEASE_WAIT("Requesting, please wait...","正在请求，请稍后···"),
    //请输入手机号或者邮箱
    YOUR_MOBILE_PHONE_NUMBER("Mobile phone number","请输入手机号或者邮箱"),
    //请输入验证码
    YOUR_VERIFICATION_CODE_NUMBER("Verification code","请输入验证码"),
    //请输入邀请码（选填）
    OPTIONAL_NUMBER("Please enter an invitation code (optional)","请输入邀请码（选填）"),
    //注册
    REGISTER("Register","注册"),
    //登录
    LOGIN("Login","登录"),
    //我已阅读并同意
    HAVE_READ_AGREED("Agree","我已阅读并同意" ),
    //《用户服务协议》
    USER_SERVICE_AGREEMENT("User Agreement","《用户服务协议》"),
    //《隐私协议》
    PRIVACY_POLICY_DES("Privacy Policy","《隐私协议》"),
    //这里什么都没有哦！
    NOTHING_DES("There's nothing here!","这里什么都没有哦！"),
    //抱歉，数据好像丢失了！
    SORRY_DES("Sorry, the data seems to be lost!","抱歉，数据好像丢失了！"),
    //好像没有网络哦！ I don't think there's an internet connection!
    NO_NET_DES("I don't think there's an internet connection!","好像没有网络哦！"),
    //您还不是会员，请点击去绑卡开通会员
    NO_VIP_DES("You are not a member yet, please click to bind a card to open a member!","您还不是会员，请点击去绑卡开通会员！"),
    //去绑卡
    NO_BIND_DES("Bind card","去绑卡"),
    //中国地区暂未开放，仅可投流！
    STREAMING_ONLY_DES("The China region is not open for the time being, and can only be streamed!","中国地区暂未开放，仅可投流！"),
    //加载中...
    LOADING_DES("Loading...","加载中..."),
    //全球
    GLOBE("Global","全球"),
    CHINA("China","中国区"),
    //华东
    EAST_CHINA("East China","华东"),
    SOUTH_CHINA("South China","华南"),
    CENTRAL_CHINA("Central China","华中"),
    //个人总业绩
    USER_TOTAL_PERFORMANCE("User performance","个人总业绩"),
    //待分润明细
    DETAILS_SHARED("Details to be shared","待分润明细"),
    // 前往
    GO("Go","前往"),
    //您还未设置安全密码，前往设置安全没密码！
    SET_PWD_TIPS_GO("You haven't set a security password yet, go to Set up a security password!","您还未设置安全密码，前往设置安全没密码！"),
    //请输入您旧的密码
    YOUR_OLD_PASSWORD_TIPS("Please enter your old password","请输入您旧的密码"),
    //修改密码
    CHANGE_PASSWORD("Change your password","修改密码"),
    //请输入您新的密码
    YOUR_NEW_PASSWORD_TIPS("Your new password","请输入您新的密码"),
    //请再次输入您新的密码
    YOUR_NEW_PASSWORD_AGAIN("Your new password again","请再次输入您新的密码"),
    //新增密码
    ADD_PASSWORD("Add your password","新增密码"),
    //请检查您输入的密码是否一致！
    CHECK_PWD_TWO_TIPS("Please check that the passwords you have entered are the same!","请检查您输入的密码是否一致！"),
    //密码长度为6位数字
    PWD_LEN_SIX_TIPS("The length of the password is 6 digits","密码长度为6位数字"),
    //安全密码
    SECURE_PASSWORDS("Secure passwords","安全密码"),
    //忘记密码
    FORGOT_PASSWORD("Forgot password","忘记密码"),
    //设置密码成功！
    SET_SUCCESS_TIPS("Password set successfully!","设置密码成功！"),
    //电子邮件
    Email("email","电子邮件"),
    //"充值订单"
    RECHARGE_ORDER("Details","充值订单"),
    //收款地址
    RECEIVING_ADDRESS("Receiving address","收款地址"),
    //版本
    VERSION("Version","版本"),
    //分享邀请好友
    SHARE_INVITE_FRIENDS("Invite friends","分享邀请好友"),
    //保存二维码
    SAVE_THE_QR_CODE("Save","保存二维码"),
    //邀请码
    INVITATION_CODE("Invitation code","邀请码"),
    //充值明细
    TOP_UP_DETAILS("Top-up details","充值明细"),
    //充值状态
    TOP_UP_STATUS("Top-up status","充值状态"),
    //创建时间
    CREATION_TIME_2("Creation time","创建时间"),
    YES("Yes","是"),
    NO("No","否"),
    //我已充值
    TOPPED_UP("I've topped up","我已充值"),
    //订单充值成功！
    DEPOSIT_SUCCESSFUL("Deposit successful!","订单充值成功！"),
    //未查询到订单状态，如已经支付请在充值订单中查看！
    THE_RECHARGE_ORDER("The order status has not been queried, if it has been paid, please check it in the recharge order!","未查询到订单状态，如已经支付请在充值订单中查看！"),
    //提现说明:
    WITHDRAWAL_INSTRUCTIONS("Withdrawal Instructions:","提现说明:"),
    //1、每天提现时间10:00-20:00；
    WITHDRAWAL_TIME("1.Daily withdrawal time from 10:00 to 20:00.","1、每天提现时间10:00-20:00；"),
    //2、每笔提现10USDT起提;
    WITHDRAWAL_MINIMUM("2.Each withdrawal is subject to a minimum withdrawal of 10 U.","2、每笔提现10USDT起提;"),
    //3、每笔提现收取2USDT手续;
    WITHDRAWAL_CHARGED("3.A fee of 2 U is charged for each withdrawal.","3、每笔提现收取2USDT手续;"),
    //4、提现到账时间为2小时内到账;
    WITHDRAWAL_HOURS("4.The withdrawal should arrive within 2 hours.","4、提现到账时间为2小时内到账。"),
    //投流渠道
    STREAMING_CHANNELS("Streaming channels","投流渠道"),
    //投流账号
    TIKTOK_ID("TikTok ID","TikTok投流账号"),
    //提现明细
    PAYOUT_DETAILS("Payout details","提现明细"),
    //处理中
    PROCESSING("processing","处理中"),
    //提现失败
    WITHDRAWAL_FAILED("processing","提现失败"),
    WITHDRAWAL_SUCCESS("withdrawal was successful", "提现成功"),
    //提现金额
    AMOUNT_WITHDRAWN("The amount to be withdrawn", "提现金额"),
    //提现地址
    WITHDRAWAL_ADDRESS_DES("Withdrawal address", "提现地址"),
    //提现时间
    WITHDRAWAL_TIME_DES("Withdrawal address", "提现时间"),
    //交易状态
    TRANSACTION_STATUS("Transaction status", "交易状态"),
    //"注:充值严格按照输入的数量充值，否则无法到账。"
    TIPS_TWO("Note: The recharge is strictly in accordance with the entered quantity, otherwise it will not be credited to the account.", "注:充值严格按照输入的数量充值，否则无法到账。"),
    //请输入6位数字的安全密码 请输入密码
    PLEASE_ENTER_PASSWORD("Please enter your password", "请输入6位数字的安全密码"),
    //您输入的密码不正确
    PASSWORD_INCORRECT("The password is incorrect", "您输入的密码不正确"),
    //确定要退出当前账号吗？
    LOG_OUT("Log out of your account?", "确定要退出当前账号吗？");
    private String en;
    private String cn;

    LanguageTo(String en, String cn) {
        this.en = en;
        this.cn = cn;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }
}
