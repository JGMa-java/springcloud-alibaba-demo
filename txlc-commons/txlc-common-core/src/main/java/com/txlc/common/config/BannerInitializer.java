package com.txlc.common.config;

import com.nepxion.banner.BannerConstant;
import com.nepxion.banner.Description;
import com.nepxion.banner.LogoBanner;
import com.taobao.text.Color;
import com.txlc.common.constant.CommonConstant;
import com.txlc.common.utils.CustomBanner;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Banner初始化
 *
 * @author JGMa
 */
public class BannerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        if (!(applicationContext instanceof AnnotationConfigApplicationContext)) {
            LogoBanner logoBanner = new LogoBanner(BannerInitializer.class, "/txlcloud/logo.txt", "Welcome to Txlc-cloud", 1, 6, new Color[5], true);
            CustomBanner.show(logoBanner,
                    new Description("天下良仓.com", "V"+CommonConstant.PROJECT_VERSION, 0, 7)
            );
        }
    }
}
