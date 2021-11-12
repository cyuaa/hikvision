package com.ulearning.ulms.hk.config;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.ulearning.ulms.hk.HCNetSDK;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.InputStream;

/**
 * @author chenyu
 * @desc Sdk初始化
 * @datetime 2021年11月12日 9:39
 * Copyright (c) 2006-2021.Beijing WenHua Online Sci-Tech Development Co. Ltd
 * All rights reserved.
 */
@Slf4j
public class SdkInit {

    @SneakyThrows
    public static HCNetSDK init(HCNetSDK hcNetSDK) {
        if (Platform.isWindows()) {
            InputStream initialStream = new ClassPathResource("hkwinlib/HCNetSDK.dll").getInputStream();
            File targetFile = new File("hkwinlib/HCNetSDK.dll");
            if (targetFile.exists()){
                targetFile.delete();
            }
            FileUtils.copyInputStreamToFile(initialStream, targetFile);
            String path = targetFile.getAbsolutePath();
            log.info("Windows HCNetSDK Path Load Start...");
            hcNetSDK = (HCNetSDK) Native.loadLibrary(path, HCNetSDK.class);
            log.info("Windows HCNetSDK Path Load Success...");
        }
        if (Platform.isLinux()) {
            // 要把 hklinuxlib 这个文件夹复制下 jar 的同目录下
            InputStream initialStream = new ClassPathResource("hklinuxlib/libhcnetsdk.so").getInputStream();
            File targetFile = new File("hklinuxlib/libhcnetsdk.so");
            if (targetFile.exists()){
                targetFile.delete();
            }
            FileUtils.copyInputStreamToFile(initialStream, targetFile);
            String path = targetFile.getAbsolutePath();
            log.info("Linux HCNetSDK Path Load Start...");
            hcNetSDK = (HCNetSDK) Native.loadLibrary(path, HCNetSDK.class);

            //设置HCNetSDKCom组件库所在路径
            String strPathCom = ResourceUtils.getFile("hklinuxlib").getPath();
            HCNetSDK.NET_DVR_LOCAL_SDK_PATH struComPath = new HCNetSDK.NET_DVR_LOCAL_SDK_PATH();
            System.arraycopy(strPathCom.getBytes(), 0, struComPath.sPath, 0, strPathCom.length());
            struComPath.write();
            hcNetSDK.NET_DVR_SetSDKInitCfg(2, struComPath.getPointer());

            //设置libcrypto.so所在路径
            HCNetSDK.BYTE_ARRAY ptrByteArrayCrypto = new HCNetSDK.BYTE_ARRAY(256);
            String strPathCrypto = ResourceUtils.getFile("hklinuxlib/libcrypto.so").getPath();
            System.arraycopy(strPathCrypto.getBytes(), 0, ptrByteArrayCrypto.byValue, 0, strPathCrypto.length());
            ptrByteArrayCrypto.write();
            hcNetSDK.NET_DVR_SetSDKInitCfg(3, ptrByteArrayCrypto.getPointer());

            //设置libssl.so所在路径
            HCNetSDK.BYTE_ARRAY ptrByteArraySsl = new HCNetSDK.BYTE_ARRAY(256);
            String strPathSsl = ResourceUtils.getFile("hklinuxlib/libssl.so").getPath();
            System.arraycopy(strPathSsl.getBytes(), 0, ptrByteArraySsl.byValue, 0, strPathSsl.length());
            ptrByteArraySsl.write();
            hcNetSDK.NET_DVR_SetSDKInitCfg(4, ptrByteArraySsl.getPointer());
            log.info("Linux HCNetSDK Path Load Success...");
        }

        return hcNetSDK;
    }
    
}
