package com.dneero.util.jcaptcha;

import com.octo.captcha.service.image.ImageCaptchaService;
import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.engine.image.gimpy.DefaultGimpyEngine;

/**
 * jcaptcha service singleton
 */

public class CaptchaServiceSingleton {

    //private static ImageCaptchaService instance = new BufferedEhcacheManageableImageCaptchaService(new DefaultGimpyEngine(), false, 10000, 300, 180, 1200, 100000);
    private static DefaultManageableImageCaptchaService instance = new DefaultManageableImageCaptchaService();
    private static boolean configured = false;

    public static ImageCaptchaService getInstance(){
        if (!configured){
            configure();
        }
        return instance;
    }

    private static void configure(){
        instance.setMinGuarantedStorageDelayInSeconds(1000);
    }
}
