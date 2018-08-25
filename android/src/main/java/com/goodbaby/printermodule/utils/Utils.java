package com.goodbaby.printermodule.utils;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.goodbaby.printermodule.entity.CashEntity;
import com.goodbaby.printermodule.entity.GoodEntity;
import com.goodbaby.printermodule.entity.OrderEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by limingguang on 2018/8/24.
 */

public class Utils {
    /**
     * 序列化订单信息
     * @param json
     * @return
     */
    public static OrderEntity serializeOrder(ReadableMap json) {
        if(json == null){
            return null;
        }
        OrderEntity orderEntity = new OrderEntity();
        // pos机号
        orderEntity.setClientId(json.getInt("tml_client_id"));
        // pos机名称
        orderEntity.setClientName(json.getString("tml_client_name"));
        orderEntity.setEmpeName(json.getString("empe_name"));
        orderEntity.setFamount(json.getMap("tml_pay_hdr").getDouble("f_amount"));
        orderEntity.setMalingAmount(json.getMap("tml_pay_hdr").getDouble("maling_amount"));
        orderEntity.setNumId(json.getString("tml_num_id"));
        orderEntity.setOrderCreateDate(json.getString("create_date"));
        orderEntity.setOrderDate(json.getString("order_date"));
        orderEntity.setOrderType(json.getString("order_type"));
        orderEntity.setPamount(json.getMap("tml_pay_hdr").getDouble("all_pamount"));
        orderEntity.setPayDateTime(json.getMap("tml_pay_hdr").getString("f_payment_dtme"));
        orderEntity.setRamount(json.getMap("tml_pay_hdr").getDouble("r_amount"));
        orderEntity.setUnitAddr(json.getString("sub_unit_adr"));
        orderEntity.setUnitName(json.getString("sub_unit_telephone"));
        orderEntity.setUnitPhone(json.getString("sub_unit_name"));
        orderEntity.setRePrint(json.getInt("repeat_print_sign")==1);
        // 小票明细
        orderEntity.setItemList(serializeGood(json.getArray("info_list")));
        // 提货存根
        orderEntity.setDeliveryItemList(serializeGood(json.getArray("delivery_item_list")));
        // 提货单
        orderEntity.setContainerItemList(serializeGood(json.getArray("item_container_info_list")));
        // 优惠信息
        orderEntity.setDeductList(serializeGood(json.getArray("deduct_list")));
        // 付款信息
        orderEntity.setCashList(serializeCash(json.getArray("cash_dtls")));
        return orderEntity;
    }

    /**
     * 序列化商品信息
     * @return
     */
    private static List<GoodEntity> serializeGood(ReadableArray goods){
        List<GoodEntity> itemList = new ArrayList<>();
        for (int i = 0; i< goods.size(); i++) {
            GoodEntity goodEntity = new GoodEntity();
            ReadableMap good = goods.getMap(i);
            // 品名
            goodEntity.setItemName(good.getString("item_name"));
            // 货号
            goodEntity.setItemId(good.getString("itemid"));
            // 条码
            goodEntity.setBarcode(good.getString("barcode"));
            // 数量
            goodEntity.setQty(good.getInt("qty"));
            // 行号
            if (good.hasKey("seq_line")){
                goodEntity.setSeqLine(good.getInt("seq_line"));
            }
            // 库存简称
            if (good.hasKey("pty_sim_name")){
                goodEntity.setSimpleName(good.getString("pty_sim_name"));
            }
            // 金额
            if (good.hasKey("trade_amount")){
                goodEntity.setTradeAmount(good.getDouble("trade_amount"));
            }
            // 单价
            if (good.hasKey("trade_price")){
                goodEntity.setTradePrice(good.getString("trade_price"));
            }
            // 优惠金额
            if (good.hasKey("deduct_amount")){
                goodEntity.setDeductAmount(good.getDouble("deduct_amount"));
            }
            // 提货单号
            if(good.hasKey("container_serlno")){
                goodEntity.setSerialNo(good.getInt("container_serlno"));
            }
            // 库位
            if(good.hasKey("loc_name")){
                goodEntity.setLocName(good.getString("loc_name"));
            }
            if(goodEntity.getSimpleName() != null && !goodEntity.getSimpleName().trim().isEmpty()){
                goodEntity.setItemName("("+goodEntity.getSimpleName()+")" + goodEntity.getItemName());
            }
            itemList.add(goodEntity);
        }
        return itemList;
    }

    /**
     * 序列化付款信息
     * @param cashs
     * @return
     */
    private static List<CashEntity> serializeCash(ReadableArray cashs){
        List<CashEntity> cashList = new ArrayList<>();
        for (int i = 0; i< cashs.size(); i++) {
            CashEntity cashEntity = new CashEntity();
            ReadableMap cash = cashs.getMap(i);
            cashEntity.setPayType(cash.getString("pay_type_name"));
            cashEntity.setPayAmount(cash.getDouble("pay_amount"));
            if(cash.hasKey("r_cash_sign")){
                cashEntity.setReturnCashSign(cash.getInt("r_cash_sign") == 1);
            }
            cashList.add(cashEntity);
        }
        return cashList;
    }
}
