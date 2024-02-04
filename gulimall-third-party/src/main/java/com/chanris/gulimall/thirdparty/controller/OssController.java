package com.chanris.gulimall.thirdparty.controller;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.chanris.gulimall.common.utils.Result;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author chenyue7@foxmail.com
 * @date 3/2/2024
 * @description
 */
@RestController
public class OssController {

    @Resource
    private OSSClient ossClient;

    // 只要在nacos-config 配置了就能找到，不会报错
    @Value("${alibaba.cloud.oss.bucketName}")
    private String bucketName;


    // https://help.aliyun.com/zh/oss/use-cases/obtain-signature-information-from-the-server-and-upload-data-to-oss?spm=a2c4g.11186623.0.0.454639dbfaK1Bn
    @GetMapping("oss/policy")
    public Result policy(HttpServletResponse response, HttpServletRequest request) {
        String host = "https://" + bucketName + "." + ossClient.getEndpoint().getHost();
        // 用户上传文件时指定的前缀
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dir = sdf.format(new Date()) + "/";
        try {
            long expireTime = 30;
            // 过期时间为30秒
            long expireEndTime = System.currentTimeMillis() + expireTime * 1000;
            Date expiration = new Date(expireEndTime);
            // PostObject请求最大可支持的文件大小为5 GB，即CONTENT_LENGTH_RANGE为5*1024*1024*1024。
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, 1048576000);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes(StandardCharsets.UTF_8);
            String accessId = ossClient.getCredentialsProvider().getCredentials().getAccessKeyId();

            String encodedPolicy = BinaryUtil.toBase64String(binaryData);
            String postSignature = ossClient.calculatePostSignature(postPolicy);

            Map<String, String> respMap = new LinkedHashMap<String, String>();
            respMap.put("accessid", accessId);
            respMap.put("policy", encodedPolicy);
            respMap.put("signature", postSignature);
            respMap.put("dir", dir);
            respMap.put("host", host);
            respMap.put("expire", String.valueOf(expireEndTime / 1000));
//             respMap.put("expire", formatISO8601Date(expiration));

            //JSONObject jasonCallback = new JSONObject();
            //jasonCallback.put("callbackUrl", callbackUrl);
            // jasonCallback.put("callbackBody", "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
            //jasonCallback.put("callbackBodyType", "application/x-www-form-urlencoded");
            //String base64CallbackBody = BinaryUtil.toBase64String(jasonCallback.toString().getBytes());
            //respMap.put("callback", base64CallbackBody);

            //JSONObject ja1 = JSONObject.fromObject(respMap);
            // System.out.println(ja1.toString());
            //response.setHeader("Access-Control-Allow-Origin", "*");
            //response.setHeader("Access-Control-Allow-Methods", "GET, POST");
            //response(request, response, ja1.toString());
            return new Result().ok(respMap);
        } catch (Exception e) {
            // Assert.fail(e.getMessage());
            System.out.println(e.getMessage());
        }
        return new Result();
    }

    @GetMapping("/bucketName")
    public Result getBucketName() {
        URI endpoint = ossClient.getEndpoint();
        HashMap<String, String> m = new HashMap<>();
        m.put("endpoint", endpoint.toString());

//        return new Result().ok(ossClient.getBucketLocation(bucketName));
        return new Result().ok(m);
    }

}
