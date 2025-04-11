package com.myskybeyond.dingtalk.controller;
import cn.dev33.satoken.annotation.SaIgnore;
import com.alibaba.fastjson.JSON;
import com.aliyun.dingtalkcontact_1_0.models.GetUserHeaders;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenRequest;
import com.aliyun.dingtalkoauth2_1_0.models.GetUserTokenResponse;
import com.aliyun.teautil.models.RuntimeOptions;
import com.myskybeyond.dingtalk.sevice.DingTalkServiceApi;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.springframework.web.bind.annotation.*;

/**
 * @author Lijt
 * 钉钉开发平台能力封装
 */
@RestController
@RequestMapping("/dingtalk")
@Slf4j
public class DingTalkController extends BaseController {

    private final DingTalkServiceApi dingTalkServiceApi;

    public DingTalkController(DingTalkServiceApi dingTalkServiceApi) {
        this.dingTalkServiceApi = dingTalkServiceApi;
    }

    @SaIgnore
    @GetMapping("/message/send")
    public R<Boolean> sendMessage() {
        return R.ok(dingTalkServiceApi.sendMessage("126911464726196399", "这是一条测试的钉钉通知消息", "DINGTALK-HANGTAI"));
    }

    @SaIgnore
    @GetMapping("/user/getByUnionId")
    public String getUseridGetByUnionId(@RequestParam String unionId) {
        return dingTalkServiceApi.getUseridGetByUnionId(unionId, "DINGTALK-*");
    }


//    @RequestMapping(value = "/auth", method = RequestMethod.GET)
//    public String getAccessToken(@RequestParam(value = "authCode")String authCode) throws Exception {
//        com.aliyun.dingtalkoauth2_1_0.Client client = authClient();
//        GetUserTokenRequest getUserTokenRequest = new GetUserTokenRequest()
//
//            //应用基础信息-应用信息的AppKey,请务必替换为开发的应用AppKey
//            .setClientId("dingizwlsbiuevqlp6rc")
//
//            //应用基础信息-应用信息的AppSecret，,请务必替换为开发的应用AppSecret
//            .setClientSecret("ke1BnCFAiZ4DFHhjZokA8vaNYDsr0Xl5ZWarOObEAW82YOmMLxisrg7KX3FzBJMZ")
//            .setCode(authCode)
//            .setGrantType("authorization_code");
//        GetUserTokenResponse getUserTokenResponse = client.getUserToken(getUserTokenRequest);
//        //获取用户个人token
//        String accessToken = getUserTokenResponse.getBody().getAccessToken();
//        return  getUserinfo(accessToken);
//
//    }

//    public com.aliyun.dingtalkoauth2_1_0.Client authClient() throws Exception {
//        Config config = new Config();
//        config.protocol = "https";
//        config.regionId = "central";
//        return new com.aliyun.dingtalkoauth2_1_0.Client(config);
//    }

//    public String getUserinfo(String accessToken) throws Exception {
//        com.aliyun.dingtalkcontact_1_0.Client client = contactClient();
//        GetUserHeaders getUserHeaders = new GetUserHeaders();
//        getUserHeaders.xAcsDingtalkAccessToken = accessToken;
//        //获取用户个人信息，如需获取当前授权人的信息，unionId参数必须传me
//        String me = JSON.toJSONString(client.getUserWithOptions("me", getUserHeaders, new RuntimeOptions()).getBody());
//        System.out.println(me);
//        return me;
//    }

//    public com.aliyun.dingtalkcontact_1_0.Client contactClient() throws Exception {
//        Config config = new Config();
//        config.protocol = "https";
//        config.regionId = "central";
//        return new com.aliyun.dingtalkcontact_1_0.Client(config);
//    }
}
