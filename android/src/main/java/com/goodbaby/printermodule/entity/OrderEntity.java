package com.goodbaby.printermodule.entity;

import java.util.List;

/**
 * Created by limingguang on 2018/8/24.
 */

public class OrderEntity {
    /**
     * 门店名称
     */
    private String unitName;
    /**
     * POS机号
     */
    private Integer clientId;
    /**
     * POS机名
     */
    private String clientName;
    /**
     * 订单日期
     */
    private String orderDate;
    /**
     * 订单创建日期
     */
    private String orderCreateDate;
    /**
     * 门店地址
     */
    private String unitAddr;
    /**
     * 门店电话
     */
    private String unitPhone;
    /**
     * 订单类型
     */
    private String orderType;
    /**
     * 小票号码
     */
    private String numId;
    /**
     * 收银员
     */
    private String empeName;
    /**
     * 总金额
     */
    private double famount;
    /**
     * 付款金额
     */
    private double pamount;
    /**
     * 退回金额
     */
    private double ramount;
    /**
     * 免收金额
     */
    private double malingAmount;
    /**
     * 付款完成时间，对于打印的交易日期
     */
    private String payDateTime;
    /**
     * 是否重印
     */
    private boolean rePrint;
    /**
     * 付款方式组
     */
    private List<CashEntity> cashList;
    /**
     * 商品清单
     */
    private List<GoodEntity> itemList;
    /**
     * 提货存根
     */
    private List<GoodEntity> deliveryItemList;
    /**
     * 提货单，每一个item打印一张提货单
     */
    private List<GoodEntity> containerItemList;
    /**
     * 优惠信息
     */
    private List<GoodEntity> deductList;

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderCreateDate() {
        return orderCreateDate;
    }

    public void setOrderCreateDate(String orderCreateDate) {
        this.orderCreateDate = orderCreateDate;
    }

    public String getUnitAddr() {
        return unitAddr;
    }

    public void setUnitAddr(String unitAddr) {
        this.unitAddr = unitAddr;
    }

    public String getUnitPhone() {
        return unitPhone;
    }

    public void setUnitPhone(String unitPhone) {
        this.unitPhone = unitPhone;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getNumId() {
        return numId;
    }

    public void setNumId(String numId) {
        this.numId = numId;
    }

    public String getEmpeName() {
        return empeName;
    }

    public void setEmpeName(String empeName) {
        this.empeName = empeName;
    }

    public double getFamount() {
        return famount;
    }

    public void setFamount(double famount) {
        this.famount = famount;
    }

    public double getPamount() {
        return pamount;
    }

    public void setPamount(double pamount) {
        this.pamount = pamount;
    }

    public double getRamount() {
        return ramount;
    }

    public void setRamount(double ramount) {
        this.ramount = ramount;
    }

    public double getMalingAmount() {
        return malingAmount;
    }

    public void setMalingAmount(double malingAmount) {
        this.malingAmount = malingAmount;
    }

    public String getPayDateTime() {
        return payDateTime;
    }

    public void setPayDateTime(String payDateTime) {
        this.payDateTime = payDateTime;
    }

    public boolean isRePrint() {
        return rePrint;
    }

    public void setRePrint(boolean rePrint) {
        this.rePrint = rePrint;
    }

    public List<CashEntity> getCashList() {
        return cashList;
    }

    public void setCashList(List<CashEntity> cashList) {
        this.cashList = cashList;
    }

    public List<GoodEntity> getItemList() {
        return itemList;
    }

    public void setItemList(List<GoodEntity> itemList) {
        this.itemList = itemList;
    }

    public List<GoodEntity> getDeliveryItemList() {
        return deliveryItemList;
    }

    public void setDeliveryItemList(List<GoodEntity> deliveryItemList) {
        this.deliveryItemList = deliveryItemList;
    }

    public List<GoodEntity> getContainerItemList() {
        return containerItemList;
    }

    public void setContainerItemList(List<GoodEntity> containerItemList) {
        this.containerItemList = containerItemList;
    }

    public List<GoodEntity> getDeductList() {
        return deductList;
    }

    public void setDeductList(List<GoodEntity> deductList) {
        this.deductList = deductList;
    }
}
