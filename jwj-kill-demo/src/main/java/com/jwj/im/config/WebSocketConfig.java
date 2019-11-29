package com.jwj.im.config;//package com.virtual.wallet.config.socket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yeauty.standard.ServerEndpointExporter;

/**
 * @ClassName : WebSocketConfig  //类名
 * @Description : websocket配置类  //描述
 * @Author : JiangWenJie  //作者
 * @Date: 2019-11-08 16:36  //时间
 */
@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
