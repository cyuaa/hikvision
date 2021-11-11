package com.ulearning.ulms.hk.service;

import com.alibaba.fastjson.JSONObject;
import com.sun.jna.Pointer;
import com.ulearning.ulms.hk.HCNetSDK;
import com.ulearning.ulms.util.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author chenyu
 * @desc 客流量上传回调
 * @datetime 2021年11月03日 13:45
 * Copyright (c) 2006-2021.Beijing WenHua Online Sci-Tech Development Co. Ltd
 * All rights reserved.
 */
@Slf4j
public class MemberFlowUploadCallBackImpl implements HCNetSDK.FMSGCallBack_V31 {

    private final String TOKEN = "6D348AB5B68A89D527AFE04DB53C7121";


    @Override
    public boolean invoke(int lCommand, HCNetSDK.NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {
        log.info("进入回调了");
        try {
            String sAlarmType = new String();
            //报警时间
            Date today = new Date();
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String[] sIP = new String[2];

            sAlarmType = new String("lCommand=") + lCommand;
            //lCommand是传的报警类型
            HCNetSDK.NET_DVR_PDC_ALRAM_INFO strPDCResult = new HCNetSDK.NET_DVR_PDC_ALRAM_INFO();
            strPDCResult.write();
            Pointer pPDCInfo = strPDCResult.getPointer();
            pPDCInfo.write(0, pAlarmInfo.getByteArray(0, strPDCResult.size()), 0, strPDCResult.size());
            strPDCResult.read();

            /*if (strPDCResult.byMode == 0) {
                strPDCResult.uStatModeParam.setType(HCNetSDK.NET_DVR_STATFRAME.class);
                sAlarmType = sAlarmType + "：客流量统计，进入人数：" + strPDCResult.dwEnterNum + "，离开人数：" + strPDCResult.dwLeaveNum +
                        ", byMode:" + strPDCResult.byMode + ", dwRelativeTime:" + strPDCResult.uStatModeParam.struStatFrame.dwRelativeTime +
                        ", dwAbsTime:" + strPDCResult.uStatModeParam.struStatFrame.dwAbsTime;
            }*/
            if (strPDCResult.byMode == 1) {
                strPDCResult.uStatModeParam.setType(HCNetSDK.NET_DVR_STATTIME.class);
                //在这里实现数据的保存等业务逻辑，下面注释的代码是SDK提供的参考示例

                    String strtmStart = "" + String.format("%04d", strPDCResult.uStatModeParam.struStatTime.tmStart.dwYear) +
                            String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmStart.dwMonth) +
                            String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmStart.dwDay) +
                            String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmStart.dwHour) +
                            String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmStart.dwMinute) +
                            String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmStart.dwSecond);
                    String strtmEnd = "" + String.format("%04d", strPDCResult.uStatModeParam.struStatTime.tmEnd.dwYear) +
                            String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmEnd.dwMonth) +
                            String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmEnd.dwDay) +
                            String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmEnd.dwHour) +
                            String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmEnd.dwMinute) +
                            String.format("%02d", strPDCResult.uStatModeParam.struStatTime.tmEnd.dwSecond);
                    sAlarmType = sAlarmType + "：客流量统计，进入人数：" + strPDCResult.dwEnterNum + "，离开人数：" + strPDCResult.dwLeaveNum +
                            ", byMode:" + strPDCResult.byMode + ", tmStart:" + dateFormat2.format(dateFormat.parse(strtmStart)) + ",tmEnd :" + dateFormat2.format(dateFormat.parse(strtmEnd));
                JSONObject header = new JSONObject();
                header.put("Authorization", TOKEN);
                JSONObject body = new JSONObject();
                body.put("startTime", dateFormat.parse(strtmStart).getTime());
                body.put("endTime", dateFormat.parse(strtmEnd).getTime());
                body.put("joinPeople", strPDCResult.dwEnterNum);
                body.put("leavePeople", strPDCResult.dwLeaveNum);
                body.put("orgId", 337);

                String result = HttpClientUtil.doPost("https://sjjxapi.tongshike.cn/camera", body, header);
                log.info("Camera Data Send Result: {}", result);

            }
            log.info("sAlarmData: [{}]", sAlarmType);
            //报警类型
            //报警设备IP地址
            sIP = new String(strPDCResult.struDevInfo.struDevIP.sIpV4).split("\0", 2);
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

}
