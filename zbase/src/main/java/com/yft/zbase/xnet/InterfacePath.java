package com.yft.zbase.xnet;

public interface InterfacePath {
    /**
     * 获取服务器地址
     */
    String getBasePath = "/sys/server/info";

    /**
     * 发送验证码
     */
    String SEND_CODE = "/sys/send/sms/code";

    /**
     * 上传文件
     */
    String UPDATE_FILE = "/user/upload/file";

    /**
     * 用户注册
     */
    String USER_REG = "/sys/signup";

    /**
     * 用户登录
     */
    String USER_LOGIN = "/sys/login";

    /**
     * 获取用户信息
     */
    String USER_MY_INFO = "/user/my/info";

    /**
     * 修改信息
     */
    String USER_UPDATE_INFO = "/user/update/personal";

    /**
     * 省-市-区列表
     */
    String USER_ADDRESS_CODE_LIST = "/user/area/list";

    /**
     * 用户新增\修改\删除地址
     */
    String USER_NEW_ADDRESS = "/user/address/modify";

    /**
     * 用户获取地址列表
     */
    String USER_ADDRESS_LIST = "/user/address/list";

    /*
     获取各个订单的提示数量
     */
    String USER_ORDER_TIPS = "/order/tips";

    /*
     退出登录
   */
    String USER_EXIT_LOGIN = "/user/log/out";

    /**
     * /user/change/password 修改密码（新增密码）
     */
    String CHANGE_PASSWORD="/user/change/password";

    /*
    退出登录
  */
    String USER_EXIT_LOG_Off = "/user/write/off";


    /**
     * 首页配置信息
     */
    String HOME_CONFIG = "/sys/home/info";

    /**
     * 获取首页分类视频
     */
    String BY_CATEGORY = "/shorttv/list/by/category";

    /*
     获取最新发布视频
     */
    String SHORT_TV_LIST_BY_DATE = "/shorttv/listby/date";

    /**
     * 随机短视频
     */
    String SHORT_RANDOM = "/shorttv/random";

    /**
     * 剧集列表
     */
    String SHORT_SHORT_TV_ITEM = "/shorttv/list/detail";

    /**
     * 添加收藏
     */
    String ADD_LIKE = "/user/add/like";

    /**
     * 是否收藏够
     */
    String ORNOT = "/user/like/ornot";

    /**
     * 删除收藏
     */
    String LIKE_DELETE = "/user/delete/like";


    /**
     * 品鉴区配置信息
     */
    String HOME_APPRAISE = "/sys/appraise/info";

    /**
     * 首页瀑布流
     */
    String HOME_COMMODITY_LIST = "/goods/waterfall";

    /**
     * 分类列表
     */
    String LISTBY_CATEGORY = "/goods/listby/category";

    /**
     * 活动商品列表
     */
    String LIST_ACTIVITY = "/goods/list/activity";

    /**
     * 商品搜索
     */
    String SHORT_SEARCH = "/shorttv/search";

    /**
     * 商品详情
     */
    String GOODS_DETAILS = "/goods/detail";

    /**
     * 收藏添加
     */
    String ADD_COLLECTION = "/user/add/collection";

    /**
     * 取消收藏
     */
    String DELETE_COLLECTION = "/user/delete/collection";

    /**
     * 收藏列表
     */
    String LIST_COLLECTION = "/user/my/collection";

    /**
     * 是否收藏过
     */
    String ORNOT_COLLECTION = "/user/collection/ornot";

    /**
     * 加入购物车
     */
    String ADD_CART = "/order/add/cart";

    /**
     * 我的购物车
     */
    String CART_LIST = "/order/my/cart";

    String CART_LIST_V2 = "/order/my/cart/v2";

    /**
     * 删除购物车
     */
    String CART_DEL = "/order/delete/cart";

    /**
     * 修改购物车数量
     */
    String CART_UPDATE_NUM = "/order/change/cart";

    /**
     * 订单预览
     */
    String ORDER_PREVIEW = "/order/create/preview";


    /**
     * 呼气支付
     */
    String ORDER_CALL_PAY = "/order/callpay";

    /**
     * 查询支付结果
     */
    String ORDER_CHECK_PAY = "/order/check/pay";

    /**
     * 获取公告消息
     */
    String NOTICE_MESSAGE = "/sys/notice/message";

    /**
     * 获取开屏广告
     */
    String START_PAGE = "/sys/start/page";

    /**
     * 获取最新版本
     */
    String NEWEST_VERSION = "/sys/newest/version";

    /**
     * 用户实名认证
     */
    String USER_REAL_NAME = "/user/realname";

    /**
     * 获取实名信息， 不同手机可同步实名信息
     */
    String USER_REAL_NAME_INFO = "/user/realname/info";

    /**
     * 品鉴类目
     */
    String APPRAISE_CATEGORY = "/sys/appraise/category";

    /**
     * 订单列表
     */
    String ORDER_MY_LIST = "/order/my/list";

    /**
     * 关闭订单
     */
    String ORDER_CLOSE = "/order/close";

    /**
     * 删除订单
     */
    String ORDER_DELETE = "/order/delete";

    ///order/confirm/receive
    String ORDER_CONFIRM = "/order/confirm/receive";

    /**
     * 获取数据
     */
    String SHARE_DATA = "/share/data";

    /**
     * 线性数据
     */
    String CONTINENT_DATA = "/share/continent/data";

    /**
     * 充值
     */
    String ORDER_RECHARGE_CREATE = "/order/recharge";

    /**
     * 穿件投流订单
     */
    String ORDER_INVEST_CREATE = "/order/invest";

    /**
     * 提现订单
     */
    String ORDER_FETCH = "/order/fetch";

    /**
     * 提现订单
     */
    String ORDER_TRANSFER = "/order/transfer";

    ///order/recharge/detail
    String RECHARGE_DETAIL = "/order/recharge/detail";

    /**
     * 获取支持的网络
     */
    String LIST_PROTOCOL= "/order/list/protocol";
    /**
     * 我的团队
     */
    String  MY_TEAM = "/user/my/team";
    // 余额明细
    String AMOUNT_LIST = "/user/amount/list";
    // 充值明细
    String RECHARGE_LIST = "/order/recharge/list";

    //投流列表
    String INVEST_LIST = "/order/invest/list";
    //收藏列表
    String MY_LIKE = "/user/my/like";
    //分润明细
    String INVEST_ITEM = "/user/invest/item";
    //提现明细
    String PAYOUT_DES = "/order/fetch/list";
}
