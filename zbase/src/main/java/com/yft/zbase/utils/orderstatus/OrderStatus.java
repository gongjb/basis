package com.yft.zbase.utils.orderstatus;


public enum OrderStatus {
//    CREATE：订单创建（非终态）
//    WAIT_PAY：等待支付（非终态）
//    PAYING：支付中（非终态）
//    PAYED：完成支付（非终态)
//    WAIT_SENDOUT：等待发货（非终态）
//    SENDOUTED：已发货（非终态）
//    TRANSPORTING：运输中（非终态）
//    WAIT_SIGN：等待物流签收（非终态）
//    WAIT_CONFIRM：等待确认收货（非终态）
//    CONFIRMED：已确认收货（非终态）
//    SUCCESS：已正常完成（终态）
//    FAIL_UNPAY：未支付导致失败（终态）
//    REFUND_APPLY：申请退款（非终态）
//    REFUND_REJECT：拒绝退款（非终态）
//    REFUND_REJECT_FINISHED：拒绝退款完成（终态）
//    REFUNDING：退款中（非终态）
//    REFUND_GOODS：商家收到退款货物（非终态）
//    REFUND_MONEY：用户收到退款（非终态）
//    REFUND_FINISHED：退款流程完成（终态）
    /**
     * statusStrMap.set("CREATE", "等待买家付款")
     *     statusStrMap.set("WAIT_PAY", "等待买家付款")
     *     statusStrMap.set("PAYING", "等待买家付款")
     *
     *     statusStrMap.set("FAIL_UNPAY", "订单关闭")
     *
     *     statusStrMap.set("PAYED","等待发货")
     *     statusStrMap.set("WAIT_SENDOUT","等待发货")
     *
     *     statusStrMap.set("SENDOUTED", "已发货")
     *     statusStrMap.set("TRANSPORTING", "运输中")
     *     statusStrMap.set("WAIT_SIGN", "等待物流签收")
     *     statusStrMap.set("WAIT_CONFIRM", "等待确认收货")
     *
     *     statusStrMap.set("REFUND_APPLY", "申请退款中");
     *     statusStrMap.set("REFUND_REJECT", "您申请的退款已被驳回");
     *     statusStrMap.set("REFUND_REJECT_FINISHED", "您申请的退款已被驳回");
     *     statusStrMap.set("REFUNDING", "退款中");
     *     statusStrMap.set("REFUND_GOODS", "商家收到退款货物");
     *     statusStrMap.set("REFUND_MONEY", "已退款");
     *     statusStrMap.set("REFUND_FINISHED", "已退款");
     *
     *     statusStrMap.set("CONFIRMED","已确认收货");
     *     statusStrMap.set("SUCCESS","订单已完成");
     */
    CREATE(0,"订单创建", false, "CREATE", "等待买家付款"),
    WAIT_PAY(1, "等待支付", false, "WAIT_PAY", "等待买家付款"),
    PAYING(2, "支付中", false, "PAYING", "等待买家付款"),
    PAYED(3, "完成支付", false, "PAYED", "等待发货"),
    WAIT_SENDOUT(4, "等待发货", false, "WAIT_SENDOUT", "等待发货"),
    SENDOUTED(5, "已发货", false, "SENDOUTED", "已发货"),
    TRANSPORTING(6, "运输中", false, "TRANSPORTING", "运输中"),
    WAIT_SIGN(7, "等待物流签收",false, "WAIT_SIGN", "等待物流签收"),
    WAIT_CONFIRM(8, "等待确认收货",false, "WAIT_CONFIRM","等待确认收货"),
    CONFIRMED(9, "已确认收货",false, "CONFIRMED", "已确认收货"),
    SUCCESS(10, "已正常完成", true, "SUCCESS", "订单已完成"),
    FAIL_UNPAY(11, "未支付导致失败",true, "FAIL_UNPAY", "订单关闭"),
    REFUND_APPLY(12, "申请退款",false, "REFUND_APPLY", "申请退款中"),
    REFUND_REJECT(13, "拒绝退款",false, "REFUND_REJECT", "您申请的退款已被驳回"),
    REFUND_REJECT_FINISHED(14, "拒绝退款完成",true, "REFUND_REJECT_FINISHED", "您申请的退款已被驳回"),
    REFUNDING(15, "退款中",false, "REFUNDING", "退款中"),
    REFUND_GOODS(16, "商家收到退款货物",false, "REFUND_GOODS", "商家收到退款货物"),
    REFUND_MONEY(17, "用户收到退款", false, "REFUND_MONEY", "已退款"),
    REFUND_FINISHED(18, "退款流程完成", true, "REFUND_FINISHED", "已退款");

    private int status; // 当前状态
    private String des; // 描述
    private boolean isEnd; // 是否是最终状态
    private String statusStr; // 状态的string
    private String showText;

    OrderStatus(int status, String des, boolean isEnd, String statusStr, String showText) {
        this.status = status;
        this.des = des;
        this.isEnd = isEnd;
        this.statusStr = statusStr;
        this.showText = showText;
    }

    public int getStatus() {
        return status;
    }

    public String getShowText() {
        return showText;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    /**
     * 根据状态找到枚举
     * @param status
     * @return
     */
    public OrderStatus findStatus(int status) {
        OrderStatus[] orderStatuses =  OrderStatus.values();
        for (OrderStatus o : orderStatuses) {
            if (o.status == status) {
                return o;
            }
        }
        return null;
    }

    /**
     * 根据描述找到枚举
     * @param des
     * @return
     */
    public OrderStatus findDescribe(String des) {
        OrderStatus[] orderStatuses =  OrderStatus.values();
        for (OrderStatus o : orderStatuses) {
            if (o.des.equals(des)) {
                return o;
            }
        }
        return null;
    }


    /**
     * 根据状态找到枚举
     * @param statusStr
     * @return
     */
    public OrderStatus findStatusStr(String statusStr) {
        OrderStatus[] orderStatuses = OrderStatus.values();
        for (OrderStatus o : orderStatuses) {
            if (o.statusStr.equals(statusStr)) {
                return o;
            }
        }
        return null;
    }

}
