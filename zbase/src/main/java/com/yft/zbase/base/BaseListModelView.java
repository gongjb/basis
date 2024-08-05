package com.yft.zbase.base;

/**
 * 需要分页需继承此类
 */
public class BaseListModelView extends BaseViewModel {
    // 是否请求中
    private volatile boolean isRequestGoods;
    // 是否是最后一页
    private volatile boolean isLastPage;
    private int count;
    // 当前页
    private volatile int thisPage = 1;

    public BaseListModelView() {
        this(20);
    }

    public BaseListModelView(int count) {
        this.count = count;
    }

    public void resetPage() {
        thisPage = 1;
        isLastPage = false;
    }

    public int getThisPage() {
        return thisPage;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public boolean isRequestGoods() {
        return isRequestGoods;
    }


    /**
     * test
     */
    public void requestPayoutDes() {
//        isRequestGoods = true;
//        mListModel.postPayoutDes(thisPage, count, new ResponseDataListener<List<PayoutBean>>() {
//            @Override
//            public void success(List<PayoutBean> data) {
//                isRequestGoods = false;
//                if (Utils.isCollectionEmpty(data) || data.size() < count) {
//                    isLastPage = true;
//                }
//                getPayoutMutableLiveData().postValue(data);
//                if (data.size() == count) {
//                    // 满足条件才有下一页
//                    thisPage++;
//                }
//            }
//
//            @Override
//            public void fail(Throwable throwable) {
//                isRequestGoods = false;
//                if (getErrorMutableLiveData() != null) {
//                    // 瀑布流商品获取失败
//                    getErrorMutableLiveData().postValue("1");
//                }
//            }
//        });
    }
}
