package com.ulearning.ulms.hk.runner;

import com.ulearning.ulms.hk.service.MemberFlowUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author chenyu
 * @desc 摄像头实时获取数据
 * @datetime 2021年11月09日 11:42
 * Copyright (c) 2006-2021.Beijing WenHua Online Sci-Tech Development Co. Ltd
 * All rights reserved.
 */
@Component
@Slf4j
public class DataReadRunner implements ApplicationRunner {

    @Value("${camera.ip}")
    private String ip;
    @Value("${camera.username}")
    private String username;
    @Value("${camera.password}")
    private String password;
    @Value("${camera.port}")
    private int port;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Data Read Runner Start... ");
        new MemberFlowUploadService().initMemberFlowUpload(ip, username, password, port);
        log.info("Data Read Runner Start Success... ");
        //等待过程中，如果设备上传报警信息，在报警回调函数里面接收和处理报警信息
        Timer timer = new Timer();// 实例化Timer类
        timer.schedule(new TimerTask() {
            public void run() {
                try {
                    log.info("Data Scheduled Read Runner Start... ");
                    new MemberFlowUploadService().initMemberFlowUpload(ip, username, password, port);
                    log.info("Data Scheduled Read Runner Start Success... ");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }, 5 * 60 * 1000, 5 * 60 * 1000 );// 这里毫秒
    }
}
