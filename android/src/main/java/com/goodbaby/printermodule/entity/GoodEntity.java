package com.goodbaby.printermodule.entity;

/**
 * Created by limingguang on 2018/8/24.
 */

public class GoodEntity {
    /**
     * 商品条码
     */
    private String barcode;
    /**
     * 货号
     */
    private String itemId;
    /**
     * 品名
     */
    private String itemName;
    /**
     * 单价
     */
    private String tradePrice;
    /**
     * 数量
     */
    private int qty;
    /**
     * 金额
     */
    private double tradeAmount;
    /**
     * 优惠金额
     */
    private double deductAmount;
    /**
     * 库存属性简称，如果不为空，需要在品名前加前缀（）
     */
    private String simpleName;
    /**
     * 小票明细行号
     */
    private int seqLine;
    /**
     * 提货单号，打印成条码
     */
    private int serialNo;
    /**
     * 库位
     */
    private String locName;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(String tradePrice) {
        this.tradePrice = tradePrice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(double tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public double getDeductAmount() {
        return deductAmount;
    }

    public void setDeductAmount(double deductAmount) {
        this.deductAmount = deductAmount;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public int getSeqLine() {
        return seqLine;
    }

    public void setSeqLine(int seqLine) {
        this.seqLine = seqLine;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
    }

    public String getLocName() {
        return locName;
    }

    public void setLocName(String locName) {
        this.locName = locName;
    }
}
