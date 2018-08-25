package com.goodbaby.printermodule.utils;

import android.hardware.usb.UsbDevice;

import com.goodbaby.printermodule.entity.CashEntity;
import com.goodbaby.printermodule.entity.GoodEntity;
import com.goodbaby.printermodule.entity.OrderEntity;
import com.printer.sdk.PrinterConstants;
import com.printsdk.cmd.PrintCmd;
import com.printer.sdk.Table;
import com.printsdk.usbsdk.UsbDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by limingguang on 2018/8/23.
 */

public class PrintUtil {
    /**
     * 测试打印
     *
     * @param usbDev
     * @param usbDriver
     */
    public static void printMS(UsbDevice usbDev, UsbDriver usbDriver, String msg) {
        int iStatus = getPrinterStatus(usbDev, usbDriver);
        if (checkStatus(iStatus) != 0)
            return;
        try {
            // 打印测试
            usbDriver.write(PrintCmd.PrintString(msg, 0), usbDev); // 0换行 1不换行

            // 走纸换行、切纸、清理缓存
            setFeedCut(1, usbDev, usbDriver); // 0全切 1半切
        } catch (Exception e) {
//            LogFileUtil.writeLog(e.getMessage());
        }
    }

    /**
     * 1.销售小票
     * @param usbDev
     * @param usbDriver
     * @param orderEntity
     */
    public static void printTicket(UsbDevice usbDev, UsbDriver usbDriver, OrderEntity orderEntity){
        String noticeStr = "***特卖商品一经售出，恕不退换***";
        String welcomeStr = "***谢谢惠顾***";
        String rePrintStr = "***重印***";
        int iStatus = getPrinterStatus(usbDev, usbDriver);
        if (checkStatus(iStatus) != 0)
            return;
        try {
            // Logo
            usbDriver.write(PrintCmd.SetBold(0),usbDev); // 0 不加粗、 1 加粗
            usbDriver.write(PrintCmd.SetAlignment(1),usbDev); // 0 左、1 居中、2 右
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
            usbDriver.write(PrintCmd.PrintString("GB好孩子", 0),usbDev);
            usbDriver.write(PrintCmd.SetAlignment(0),usbDev);
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
            // 走纸
            usbDriver.write(PrintCmd.PrintFeedline(1));
            // 打印条形码
            usbDriver.write(PrintCmd.SetAlignment(0),usbDev);
            usbDriver.write(PrintCmd.Print1Dbar(3, 50, 0, 0, 10, orderEntity.getNumId()),usbDev);
            usbDriver.write(PrintCmd.SetAlignment(0),usbDev);
            // 走纸
            usbDriver.write(PrintCmd.PrintFeedline(1));
            if(orderEntity.isRePrint()){
                // 重印标记
                usbDriver.write(PrintCmd.SetBold(1),usbDev); // 0 不加粗、 1 加粗
                usbDriver.write(PrintCmd.SetAlignment(1),usbDev); // 0 左、1 居中、2 右
                usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
                usbDriver.write(PrintCmd.PrintString(rePrintStr, 0),usbDev);
                usbDriver.write(PrintCmd.SetAlignment(0),usbDev);
                usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
            }
            // 标题
            usbDriver.write(PrintCmd.SetBold(0),usbDev); // 0 不加粗、 1 加粗
            usbDriver.write(PrintCmd.SetAlignment(1),usbDev); // 0 左、1 居中、2 右
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
            usbDriver.write(PrintCmd.PrintString(orderEntity.getUnitName(), 0),usbDev);
            usbDriver.write(PrintCmd.SetAlignment(0),usbDev);
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
            // 副标题
            usbDriver.write(PrintCmd.SetBold(0),usbDev); // 0 不加粗、 1 加粗
            usbDriver.write(PrintCmd.SetAlignment(1),usbDev); // 0 左、1 居中、2 右
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
            usbDriver.write(PrintCmd.PrintString(orderEntity.getOrderType(), 0),usbDev);
            usbDriver.write(PrintCmd.SetAlignment(0),usbDev);
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
            // 走纸
            usbDriver.write(PrintCmd.PrintFeedline(1));
            // 交易时间
            usbDriver.write(PrintCmd.SetBold(0),usbDev); // 0 不加粗、 1 加粗
            usbDriver.write(PrintCmd.SetAlignment(0),usbDev); // 0 左、1 居中、2 右
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
            usbDriver.write(PrintCmd.PrintString("交易时间："+orderEntity.getPayDateTime(), 0),usbDev);
            // POS机号
            usbDriver.write(PrintCmd.PrintString("POS机号："+orderEntity.getClientId()+"("+orderEntity.getClientName()+")", 0),usbDev);
            // 小票号码
            usbDriver.write(PrintCmd.PrintString("单号："+orderEntity.getNumId(), 0),usbDev);
            // 收银员
            usbDriver.write(PrintCmd.PrintString("收银员："+orderEntity.getEmpeName(), 0),usbDev);
            usbDriver.write(PrintCmd.SetBold(0),usbDev);
            usbDriver.write(PrintCmd.SetAlignment(0),usbDev);
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
            // 走纸
//            usbDriver.write(PrintCmd.PrintFeedline(1));
            // 打印购物清单
            printSaleTable(usbDev, usbDriver, orderEntity.getItemList());
            // 打印横线
            printLine(usbDev, usbDriver);
            // 数量合计
            usbDriver.write(PrintCmd.SetBold(0),usbDev); // 0 不加粗、 1 加粗
            usbDriver.write(PrintCmd.SetAlignment(0),usbDev); // 0 左、1 居中、2 右
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
            usbDriver.write(PrintCmd.PrintString("数量合计："+orderEntity.getItemList().size(), 0),usbDev);
            usbDriver.write(PrintCmd.PrintString("金额合计："+orderEntity.getFamount(), 0),usbDev);
            usbDriver.write(PrintCmd.PrintString("付款金额："+orderEntity.getPamount(), 0),usbDev);
            usbDriver.write(PrintCmd.PrintString("找零："+orderEntity.getRamount(), 0),usbDev);
            usbDriver.write(PrintCmd.PrintString("免收："+orderEntity.getMalingAmount(), 0),usbDev);
            // 打印横线
            printLine(usbDev, usbDriver);
            // 结算方式和金额
            for(int i =0; i < orderEntity.getCashList().size(); i++){
                CashEntity cashEntity = orderEntity.getCashList().get(i);
                if(cashEntity.isReturnCashSign()){
                    continue;
                }
                usbDriver.write(PrintCmd.PrintString(cashEntity.getPayType()+":"+cashEntity.getPayAmount(), 0),usbDev);
            }
            usbDriver.write(PrintCmd.SetAlignment(0),usbDev);
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
            // 打印横线
            printLine(usbDev, usbDriver);
            // 提醒
            usbDriver.write(PrintCmd.SetBold(0),usbDev); // 0 不加粗、 1 加粗
            usbDriver.write(PrintCmd.SetAlignment(1),usbDev); // 0 左、1 居中、2 右
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
            usbDriver.write(PrintCmd.PrintString(welcomeStr, 0),usbDev);
            usbDriver.write(PrintCmd.PrintString(noticeStr, 0),usbDev);
            usbDriver.write(PrintCmd.SetAlignment(0),usbDev);
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);

            // 走纸换行
            usbDriver.write(PrintCmd.PrintFeedline(3), usbDev);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 2.打印优惠信息
     * @param usbDev
     * @param usbDriver
     * @param orderEntity
     */
    public static void printDeduct(UsbDevice usbDev, UsbDriver usbDriver, OrderEntity orderEntity){
        int iStatus = getPrinterStatus(usbDev, usbDriver);
        if (checkStatus(iStatus) != 0)
            return;
        try {
            if(orderEntity.getDeductList().size() == 0){
                return;
            }
            // 小票标题
            usbDriver.write(PrintCmd.SetBold(0),usbDev); // 0 不加粗、 1 加粗
            usbDriver.write(PrintCmd.SetAlignment(1),usbDev); // 0 左、1 居中、2 右
            usbDriver.write(PrintCmd.SetSizetext(1, 1),usbDev);
            usbDriver.write(PrintCmd.PrintString("促销优惠信息", 0),usbDev);
            usbDriver.write(PrintCmd.SetAlignment(0),usbDev);
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
            // 走纸
            usbDriver.write(PrintCmd.PrintFeedline(1));
            // 打印购物清单
            printDeductList(usbDev, usbDriver, orderEntity.getDeductList());

            // 走纸换行
            usbDriver.write(PrintCmd.PrintFeedline(3), usbDev);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 3.打印提货存根联
     */
    public static void printPickUpStub(UsbDevice usbDev, UsbDriver usbDriver, OrderEntity orderEntity) {
        int iStatus = getPrinterStatus(usbDev, usbDriver);
        if (checkStatus(iStatus) != 0)
            return;
        try {
            if(orderEntity.getDeliveryItemList().size() == 0){
                return;
            }
            // 小票标题
            usbDriver.write(PrintCmd.SetBold(1),usbDev); // 0 不加粗、 1 加粗
            usbDriver.write(PrintCmd.SetAlignment(1),usbDev); // 0 左、1 居中、2 右
            usbDriver.write(PrintCmd.SetSizetext(1, 1),usbDev);
            usbDriver.write(PrintCmd.PrintString("提货存根联", 0),usbDev);
            usbDriver.write(PrintCmd.SetAlignment(0),usbDev);
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
            // 走纸
            usbDriver.write(PrintCmd.PrintFeedline(2));
            // POS机号
            usbDriver.write(PrintCmd.SetBold(0),usbDev); // 0 不加粗、 1 加粗
            usbDriver.write(PrintCmd.SetAlignment(0),usbDev); // 0 左、1 居中、2 右
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
            usbDriver.write(PrintCmd.PrintString("POS机号：" + orderEntity.getClientId(), 0),usbDev);
            // 生成时间
            usbDriver.write(PrintCmd.PrintString("生成时间：" + orderEntity.getOrderCreateDate(), 0),usbDev);
            // 小票号码
            usbDriver.write(PrintCmd.PrintString("小票号：" + orderEntity.getNumId(), 0),usbDev);
            usbDriver.write(PrintCmd.SetBold(0),usbDev);
            usbDriver.write(PrintCmd.SetAlignment(0),usbDev);
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
            // 走纸
            usbDriver.write(PrintCmd.PrintFeedline(1));
            // 打印购物清单
            printMSTable(usbDev, usbDriver, orderEntity.getDeliveryItemList());
            // 打印横线
            printLine(usbDev, usbDriver);

            // 走纸换行
            usbDriver.write(PrintCmd.PrintFeedline(3), usbDev);
        } catch (Exception e) {
        }
    }

    /**
     * 4.打印提货单
     * @param usbDev
     * @param usbDriver
     * @param orderEntity
     */
    public static void printPickUpBill(UsbDevice usbDev, UsbDriver usbDriver, OrderEntity orderEntity, GoodEntity goodEntity){
        String noticeStr = "***凭此单提货，请妥善保管***";
        int iStatus = getPrinterStatus(usbDev, usbDriver);
        if (checkStatus(iStatus) != 0)
            return;
        try {
            // 提醒
            usbDriver.write(PrintCmd.SetBold(0),usbDev); // 0 不加粗、 1 加粗
            usbDriver.write(PrintCmd.SetAlignment(1),usbDev); // 0 左、1 居中、2 右
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
            usbDriver.write(PrintCmd.PrintString(noticeStr, 0),usbDev);
            usbDriver.write(PrintCmd.SetAlignment(0),usbDev);
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
            // 小票标题
            usbDriver.write(PrintCmd.SetBold(0),usbDev); // 0 不加粗、 1 加粗
            usbDriver.write(PrintCmd.SetAlignment(1),usbDev); // 0 左、1 居中、2 右
            usbDriver.write(PrintCmd.SetSizetext(1, 1),usbDev);
            usbDriver.write(PrintCmd.PrintString("提货单", 0),usbDev);
            usbDriver.write(PrintCmd.SetAlignment(0),usbDev);
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
            // 走纸
            usbDriver.write(PrintCmd.PrintFeedline(1));
            // 打印条形码
            usbDriver.write(PrintCmd.SetAlignment(0),usbDev);
            usbDriver.write(PrintCmd.Print1Dbar(3, 50, 0, 0, 10, goodEntity.getSerialNo()+""),usbDev);
            usbDriver.write(PrintCmd.SetAlignment(0),usbDev);
            // 走纸
            usbDriver.write(PrintCmd.PrintFeedDot(2));
            // 提货号
            usbDriver.write(PrintCmd.SetBold(0),usbDev); // 0 不加粗、 1 加粗
            usbDriver.write(PrintCmd.SetAlignment(0),usbDev); // 0 左、1 居中、2 右
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
            usbDriver.write(PrintCmd.PrintString(goodEntity.getSerialNo()+"", 0),usbDev);
            usbDriver.write(PrintCmd.SetAlignment(0),usbDev);
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
            // 走纸
            usbDriver.write(PrintCmd.PrintFeedline(1));
            // POS机号
            usbDriver.write(PrintCmd.SetBold(0),usbDev); // 0 不加粗、 1 加粗
            usbDriver.write(PrintCmd.SetAlignment(0),usbDev); // 0 左、1 居中、2 右
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
            usbDriver.write(PrintCmd.PrintString("POS机号："+orderEntity.getClientId(), 0),usbDev);
            // 生成时间
            usbDriver.write(PrintCmd.PrintString("生成时间："+orderEntity.getOrderCreateDate(), 0),usbDev);
            // 小票号码
            usbDriver.write(PrintCmd.PrintString("小票号："+orderEntity.getNumId(), 0),usbDev);
            // 库位
            usbDriver.write(PrintCmd.PrintString("库位："+goodEntity.getLocName(), 0),usbDev);
            usbDriver.write(PrintCmd.SetBold(0),usbDev);
            usbDriver.write(PrintCmd.SetAlignment(0),usbDev);
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
            // 走纸
            usbDriver.write(PrintCmd.PrintFeedline(1));
            // 打印购物清单
            printPickupTable(usbDev, usbDriver, goodEntity);
            // 提醒
            usbDriver.write(PrintCmd.SetBold(0),usbDev); // 0 不加粗、 1 加粗
            usbDriver.write(PrintCmd.SetAlignment(1),usbDev); // 0 左、1 居中、2 右
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);
            usbDriver.write(PrintCmd.PrintString(noticeStr, 0),usbDev);
            usbDriver.write(PrintCmd.SetAlignment(0),usbDev);
            usbDriver.write(PrintCmd.SetSizetext(0, 0),usbDev);

            // 走纸换行
            usbDriver.write(PrintCmd.PrintFeedline(3), usbDev);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 提货单存根明细
     * @param usbDev
     * @param usbDriver
     * @param goods
     */
    private static void printMSTable(UsbDevice usbDev, UsbDriver usbDriver, List<GoodEntity> goods) {
        String column = "货号;数量;品名;条码";
        Table table = null;
        table = new Table(column, ";", new int[]{15, 8, 12, 15});
        for (int i = 0; i < goods.size(); i++) {
            GoodEntity goodBean = goods.get(i);
            table.addRow(goodBean.getItemId() + ";" + goodBean.getQty() + ";" + goodBean.getItemName() + ";" + goodBean.getBarcode());
        }
        usbDriver.write(PrintCmd.PrintString(table.getTableText(), 0), usbDev); // 0换行 1不换行
    }

    /**
     * 提货单明细
     * @param usbDev
     * @param usbDriver
     * @param good
     */
    private static void printPickupTable(UsbDevice usbDev, UsbDriver usbDriver, GoodEntity good) {
        String column = "货号;数量;品名;条码";
        Table table = null;
        table = new Table(column, ";", new int[]{15, 8, 12, 15});
        table.addRow(good.getItemId() + ";" + good.getQty() + ";" + good.getItemName() + ";" + good.getBarcode());
        usbDriver.write(PrintCmd.PrintString(table.getTableText(), 0), usbDev); // 0换行 1不换行
    }

    /**
     * 销售小票详情
     * @param usbDev
     * @param usbDriver
     * @param goods
     */
    private static void printSaleTable(UsbDevice usbDev, UsbDriver usbDriver, List<GoodEntity> goods){
        String column = "货号;数量;单价;品名;金额";
        Table table = null;
        table = new Table(column, ";", new int[]{15, 8, 7, 12, 8});

        for (int i = 0; i < goods.size(); i++) {
            GoodEntity goodBean = goods.get(i);
            table.addRow(goodBean.getItemId() + ";" + goodBean.getQty() + ";" + goodBean.getTradePrice()
                    + ";" + goodBean.getItemName() + ";" + goodBean.getTradeAmount());
        }
        usbDriver.write(PrintCmd.PrintString(table.getTableText(), 0), usbDev); // 0换行 1不换行
    }

    /**
     * 优惠信息详情
     * @param usbDev
     * @param usbDriver
     * @param goods
     */
    private static void printDeductList(UsbDevice usbDev, UsbDriver usbDriver, List<GoodEntity> goods){
        String column = "货号;数量;优惠金额";
        Table table = null;
        int totalQty = 0;
        double totalAmount = 0d;
        table = new Table(column, ";", new int[]{20, 10, 20});

        for (int i = 0; i < goods.size(); i++) {
            GoodEntity goodBean = goods.get(i);
            totalQty += goodBean.getQty();
            totalAmount += goodBean.getDeductAmount();
            table.addRow(goodBean.getItemId() + ";" + goodBean.getQty() + ";" + goodBean.getDeductAmount());
        }
        table.addRow("合计：;" + totalQty + ";" + totalAmount);
        usbDriver.write(PrintCmd.PrintString(table.getTableText(), 0), usbDev); // 0换行 1不换行
    }

    private static void printLine(UsbDevice usbDev, UsbDriver usbDriver){
        StringBuffer sb = new StringBuffer();
        usbDriver.write(PrintCmd.SetBold(0), usbDev); // 0不加粗 1加粗
        usbDriver.write(PrintCmd.SetAlignment(0), usbDev); // 字符对其：0左 1中 2右
        usbDriver.write(PrintCmd.SetSizetext(0, 0), usbDev); // 放大高度 放大宽度（0-8）

        if (PrinterConstants.paperWidth == 384) {
            sb.append("------------------------------");
        } else if (PrinterConstants.paperWidth == 576) {
            sb.append("----------------------------------------------");

        } else if (PrinterConstants.paperWidth == 724) {
            sb.append("----------------------------------------------------------------");
        }
        System.out.println(PrinterConstants.paperWidth);

        usbDriver.write(PrintCmd.PrintString(sb.toString(), 0), usbDev); // 0换行 1不换行
        usbDriver.write(PrintCmd.SetAlignment(0), usbDev);
        usbDriver.write(PrintCmd.SetSizetext(0, 0), usbDev);
    }

    // 检测打印机状态
    private static int getPrinterStatus(UsbDevice usbDev, UsbDriver usbDriver) {
        int iRet = -1;

        byte[] bRead1 = new byte[1];
        byte[] bWrite1 = PrintCmd.GetStatus1();
        if (usbDriver.read(bRead1, bWrite1, usbDev) > 0) {
            iRet = PrintCmd.CheckStatus1(bRead1[0]);
        }

        if (iRet != 0)
            return iRet;

        byte[] bRead2 = new byte[1];
        byte[] bWrite2 = PrintCmd.GetStatus2();
        if (usbDriver.read(bRead2, bWrite2, usbDev) > 0) {
            iRet = PrintCmd.CheckStatus2(bRead2[0]);
        }

        if (iRet != 0)
            return iRet;

        byte[] bRead3 = new byte[1];
        byte[] bWrite3 = PrintCmd.GetStatus3();
        if (usbDriver.read(bRead3, bWrite3, usbDev) > 0) {
            iRet = PrintCmd.CheckStatus3(bRead3[0]);
        }

        if (iRet != 0)
            return iRet;

        byte[] bRead4 = new byte[1];
        byte[] bWrite4 = PrintCmd.GetStatus4();
        if (usbDriver.read(bRead4, bWrite4, usbDev) > 0) {
            iRet = PrintCmd.CheckStatus4(bRead4[0]);
        }


        return iRet;
    }

    private static int checkStatus(int iStatus) {
        int iRet = -1;

        StringBuilder sMsg = new StringBuilder();


        //0 打印机正常 、1 打印机未连接或未上电、2 打印机和调用库不匹配
        //3 打印头打开 、4 切刀未复位 、5 打印头过热 、6 黑标错误 、7 纸尽 、8 纸将尽
        switch (iStatus) {
            case 0:
                sMsg.append("正常");       // 正常
                iRet = 0;
                break;
            case 8:
                sMsg.append("纸将尽"); // 纸将尽
                iRet = 0;
                break;
            case 3:
                sMsg.append("打印头打开"); //打印头打开
                break;
            case 4:
                sMsg.append("切刀未复位");
                break;
            case 5:
                sMsg.append("打印头过热");
                break;
            case 6:
                sMsg.append("黑标错误");
                break;
            case 7:
                sMsg.append("缺纸");     // 纸尽==缺纸
                break;
            case 1:
                sMsg.append("打印机未连接");
                break;
            default:
                sMsg.append("异常");     // 异常
                break;
        }
        System.out.println(sMsg.toString());
        return iRet;
    }

    // 走纸换行、切纸、清理缓存
    public static void setFeedCut(int iMode, UsbDevice usbDev, UsbDriver usbDriver) {
        usbDriver.write(PrintCmd.PrintFeedline(3), usbDev);      // 走纸换行
        usbDriver.write(PrintCmd.PrintCutpaper(iMode), usbDev);  // 切纸类型
    }
    // --------------------- MS lmg end ---------------------

    /**
     * 检测MS打印机是否连接
     * @param device
     * @return
     */
    public static boolean isUsbPrinter(UsbDevice device) {
        if(device == null){
            return false;
        }
        int vendorId = device.getVendorId();
        int productId = device.getProductId();
        if ((1305 == vendorId && 8211 == productId)) {
            return true;
        }
        return false;
    }
}
