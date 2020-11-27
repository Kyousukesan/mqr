package com.molicloud.mqr.plugin.aireply;

import cn.hutool.core.util.StrUtil;
import com.molicloud.mqr.plugin.core.PluginExecutor;
import com.molicloud.mqr.plugin.core.PluginParam;
import com.molicloud.mqr.plugin.core.PluginResult;
import com.molicloud.mqr.plugin.core.annotation.PHook;
import com.molicloud.mqr.plugin.core.enums.RobotEventEnum;
import com.molicloud.mqr.plugin.core.message.Face;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 智能回复插件
 *
 * @author feitao yyimba@qq.com
 * @since 2020/11/6 3:45 下午
 */
@Component
public class AiReplyPluginExecutor implements PluginExecutor {

    @Autowired
    private RestTemplate restTemplate;

    // 茉莉机器人API，以下api仅供测试，如需自定义词库和机器人名字等，请前往官网获取，获取地址 http://www.itpk.cn
    private static final String apiKey = "2efdd0243d746921c565225ca4fdf07b";
    private static final String apiSecret = "itpk123456";

    @PHook(name = "AiReply",
            defaulted = true,
            robotEvents = { RobotEventEnum.FRIEND_MSG, RobotEventEnum.GROUP_MSG })
    public PluginResult messageHandler(PluginParam pluginParam) {
        String message = String.valueOf(pluginParam.getData());
        PluginResult pluginResult = new PluginResult();
        if (RobotEventEnum.GROUP_MSG.equals(pluginParam.getRobotEventEnum())
                && !StrUtil.startWith(message, "#")) {
            pluginResult.setProcessed(false);
        } else {
            String reply = aiReply(message);
            pluginResult.setProcessed(true);
            pluginResult.setMessage(reply);
        }
        return pluginResult;
    }

    private String aiReply(String message) {
        if (StrUtil.startWith(message, "#")) {
            message = message.substring(1);
        }
        String aiUrl = String.format("http://i.itpk.cn/api.php?question=%s&api_key=%s&api_secret=%s", message, apiKey, apiSecret);
        return restTemplate.getForObject(aiUrl, String.class);
    }
}