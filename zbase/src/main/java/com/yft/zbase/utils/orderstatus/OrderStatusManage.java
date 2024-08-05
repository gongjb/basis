package com.yft.zbase.utils.orderstatus;

import android.text.TextUtils;

import com.yft.zbase.utils.Utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class OrderStatusManage {
    public static final String UNPAID_STATUS = "unpaid"; // 待支付
    public static final String UNSHIPPED_STATUS = "unshipped"; //待发货
    public static final String UNRECEIVED_GOODS_STATUS = "unreceived_goods"; //待收货
    public static final String CONFIRM_RECEIPT_STATUS = "confirm_receipt_status"; // 确认收货
    public static final String REFUND_STATUS = "refund"; // 退款状态
    public static final String EVALUATE_STATUS = "evaluate"; // 待评价
    public static final String END_STATUS = "end"; // 最终状态

    public static final String REFUND_ALLOWED = "refund_allowed"; // 可退款状态

    private ConcurrentHashMap<String, List<OrderStatus>> statusMap; // 状态合集
    private ConcurrentHashMap<String, String> statusDescriptions;

    private OrderStatusManage() {
        // 待支付状态合集
        List<OrderStatus> unpaidStatus = new ArrayList<>();
        unpaidStatus.add(OrderStatus.CREATE);
        unpaidStatus.add(OrderStatus.WAIT_PAY);
        unpaidStatus.add(OrderStatus.PAYING);

        // 待发货合集
        List<OrderStatus> unshippedStatus = new ArrayList<>();
        unshippedStatus.add(OrderStatus.PAYED);
        unshippedStatus.add(OrderStatus.WAIT_SENDOUT);

        // 待收货状态合集 （需要用户确认订单）
        List<OrderStatus> unreceivedGoodsStatus = new ArrayList<>();
        unreceivedGoodsStatus.add(OrderStatus.SENDOUTED);
        unreceivedGoodsStatus.add(OrderStatus.TRANSPORTING);
        unreceivedGoodsStatus.add(OrderStatus.WAIT_SIGN);
        unreceivedGoodsStatus.add(OrderStatus.WAIT_CONFIRM);

        // 确认收货--交易成功
        List<OrderStatus> confirmReceiptStatus = new ArrayList<>();
        confirmReceiptStatus.add(OrderStatus.CONFIRMED);

        // 退款中
        List<OrderStatus> refundStatus = new ArrayList<>();
        refundStatus.add(OrderStatus.REFUND_APPLY);
        refundStatus.add(OrderStatus.REFUND_REJECT);
        refundStatus.add(OrderStatus.REFUND_REJECT_FINISHED);
        refundStatus.add(OrderStatus.REFUNDING);
        refundStatus.add(OrderStatus.REFUND_GOODS);
        refundStatus.add(OrderStatus.REFUND_MONEY);
        refundStatus.add(OrderStatus.REFUND_FINISHED);

        // 最终态 包括（正常完成订单、未支付导致失败、退款流程完成）
        List<OrderStatus> endStatus = new ArrayList<>();
        endStatus.add(OrderStatus.SUCCESS);
        endStatus.add(OrderStatus.FAIL_UNPAY);
        endStatus.add(OrderStatus.REFUND_FINISHED);

        // 正常完成的订单才能评价
        List<OrderStatus> evaluateStatus = new ArrayList<>();
        evaluateStatus.add(OrderStatus.CONFIRMED);
        evaluateStatus.add(OrderStatus.SUCCESS);

        List<OrderStatus> refundAllowedStatus = new ArrayList<>();
        refundAllowedStatus.add(OrderStatus.PAYED);
        refundAllowedStatus.add(OrderStatus.WAIT_SENDOUT);
        refundAllowedStatus.add(OrderStatus.SENDOUTED);
        refundAllowedStatus.add(OrderStatus.TRANSPORTING);
        refundAllowedStatus.add(OrderStatus.WAIT_SIGN);
        refundAllowedStatus.add(OrderStatus.WAIT_CONFIRM);
        refundAllowedStatus.add(OrderStatus.CONFIRMED);
        refundAllowedStatus.add(OrderStatus.REFUNDING); // 退款中可完成退款

        statusMap = new ConcurrentHashMap<>();
        statusDescriptions = new ConcurrentHashMap<>();

        statusMap.put(UNPAID_STATUS, unpaidStatus);
        statusDescriptions.put(UNPAID_STATUS, "待支付");

        statusMap.put(UNSHIPPED_STATUS, unshippedStatus);
        statusDescriptions.put(UNSHIPPED_STATUS, "待发货");

        statusMap.put(UNRECEIVED_GOODS_STATUS, unreceivedGoodsStatus);
        statusDescriptions.put(UNRECEIVED_GOODS_STATUS, "待收货");

        statusMap.put(CONFIRM_RECEIPT_STATUS, confirmReceiptStatus);
        statusDescriptions.put(CONFIRM_RECEIPT_STATUS, "确认收货");

        statusMap.put(REFUND_STATUS, refundStatus); // 退款
        statusDescriptions.put(REFUND_STATUS, "退款状态");

        statusMap.put(EVALUATE_STATUS, evaluateStatus);//
        statusDescriptions.put(EVALUATE_STATUS, "待评价");

        statusMap.put(END_STATUS, endStatus); // 最终态
        statusDescriptions.put(END_STATUS, "已完成订单");

        statusMap.put(REFUND_ALLOWED, refundAllowedStatus); // 可申请退款
        statusDescriptions.put(REFUND_ALLOWED, "可退款");
    }

    private static OrderStatusManage instance;

    public static synchronized OrderStatusManage getInstance() {
        if (instance == null) {
            synchronized (OrderStatusManage.class) {
                if (instance == null) {
                    instance = new OrderStatusManage();
                }
            }
        }
        return instance;
    }

    public String[] findOrderStatusArray(String status) {

        String[] result = null;
        List<OrderStatus> orderStatusList = null;
        if (statusMap.containsKey(status)) {
            orderStatusList = statusMap.get(status);
        }

        if (orderStatusList == null) {
            return null;
        }

        result = new String[orderStatusList.size()];

        for (int i = 0; i < result.length; i++) {
            result[i] = orderStatusList.get(i).getStatusStr();
        }

        return result;
    }

    /**
     * 得到某一个状态的合集
     *
     * @param status 参考：{@link OrderStatusManage#UNPAID_STATUS}
     *               {@link OrderStatusManage#UNSHIPPED_STATUS}
     *               {@link OrderStatusManage#UNRECEIVED_GOODS_STATUS}
     *               {@link OrderStatusManage#CONFIRM_RECEIPT_STATUS}
     *               {@link OrderStatusManage#REFUND_STATUS}
     *               {@link OrderStatusManage#END_STATUS}
     * @return 状态合集
     */
    public List<OrderStatus> findOrderStatus(String status) {
        if (statusMap.containsKey(status)) {
            return statusMap.get(status);
        }
        return null;
    }

    /**
     * 当前状态是否是最终态
     *
     * @param status OrderStatus枚举中的订单状态 {@link OrderStatus#CREATE} 等等...
     * @return 当前订单是否已结束
     */
    public boolean isEndStatus(final String status) {
        if (TextUtils.isEmpty(status)) {
            return false;
        }
        // 得到最终状态合集
        List<OrderStatus> endStatuses = statusMap.get(OrderStatusManage.END_STATUS);
        // 是否处于最终态...
        return isToStatus(status, endStatuses);
    }

    /**
     * 当前当前订单状态是否处于订单的某一阶段比如（是否处于待支付阶段）
     *
     * @param status OrderStatus枚举中的订单状态 {@link OrderStatus#CREATE} 等等...
     * @param orderStatuses 订单状态哥合集 取自{@link  OrderStatusManage#statusMap}的值
     *
     * @return true ｜ false
     */
    public boolean isToStatus(final String status, final List<OrderStatus> orderStatuses) {
        if (TextUtils.isEmpty(status) || orderStatuses == null || orderStatuses.size() == 0) {
            return false;
        }

        long count = orderStatuses.stream().filter(orderStatus -> status.equals(orderStatus.getStatusStr())).count();
        if (count > 0) {
            return true;
        }
        return false;
    }

    public String getShowText(String orderStatus) {
        if (TextUtils.isEmpty(orderStatus)) {
            return "";
        }

        OrderStatus[] orderStatuses = OrderStatus.values();
        for (OrderStatus o : orderStatuses) {
            if (orderStatus.equals(o.getStatusStr())) {
                return o.getShowText();
            }
        }
        return "";
    }


    public String getStatusType(final String orderStatus) {
        if (TextUtils.isEmpty(orderStatus)) {
            return "";
        }

        Set<?> set = statusMap.entrySet();
        Iterator<?> iterator = set.iterator();
        String statusKey = "";
        while (iterator.hasNext()) {
            // 反推找出订单的分类状态
            Map.Entry mapentry = (Map.Entry) iterator.next();
            String status = (String) mapentry.getKey();
            List<OrderStatus> statusList = (List<OrderStatus>) mapentry.getValue();
            List<OrderStatus> orderStatusList = statusList.stream()
                    .filter(orderStatus1 -> orderStatus.equals(orderStatus1.getStatusStr()))
                    .collect(Collectors.toList());
            if (!Utils.isCollectionEmpty(orderStatusList)) {
                // 找到对应的分类状态
                statusKey = status;
                break;
            }
        }

        if (TextUtils.isEmpty(statusKey) || !statusDescriptions.containsKey(statusKey)) {
            return "";
        }

        return statusDescriptions.get(statusKey);
    }

    /**
     * 是否显示评价按钮
     * @param status
     * @return
     */
    public boolean isEvaluate(final String status) {
        return OrderStatus.SUCCESS.getStatusStr().equals(status);
    }
}
