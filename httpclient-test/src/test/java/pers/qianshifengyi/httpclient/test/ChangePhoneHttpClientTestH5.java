package pers.qianshifengyi.httpclient.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.hash.Hashing;
import org.junit.Test;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ZHANGSHAN193
 * Date: 16-9-9
 * Time: 下午3:28
 * To change this template use File | Settings | File Templates.
 */
public class ChangePhoneHttpClientTestH5 extends H5BaseHttpClientTest {

    String str = "{\"code\":\"0\",\"formulas\":[\"(list (name fb:loan.YFD))\",\"(list (name fb:property.context.MonthFeeRate))\"],\"msg\":\"成功\"}";

    /**
     * 新增语料
     */
    @Test
    public void testGetImageCode() {
        //   log.info("--->{}", TOKEN);
        Map<String, Object> params = Maps.newHashMap();
   ///     {"code":"0","msg":"Operation successfully.","data":{"vUid":"b14fae02711d41d693c162b219ddb8c7","captchaBase":"data:image/png;base64,
        params.put("phone", "18721686227"); // 98 99 100
     //   sign:0f55800f958b5468a83fddffa1d9a35e
        JSONObject result = getAndPrintResponse("/v1/zx_xyb_common.rymGetCaptcha", buildCommonQueryString(params));
        System.out.println(JSON.toJSONString(result, true));
        String vUid = result.getJSONObject("data").getString("vUid");
        String imageStr = result.getJSONObject("data").getString("captchaBase").replace("data:image/png;base64,","");
        Base64Utils.GenerateImage(imageStr);
        System.out.println("vUid:"+vUid);

    }

    /**
     * 新增语料
     */
    @Test
    public void testGetPhoneVCode() {
        //   log.info("--->{}", TOKEN);
        Map<String, Object> params = Maps.newHashMap();

        params.put("type", 3); // 98 99 100
        params.put("phone", "18721686227");  // 1=是 0=否
        params.put("vUid", "d29c4928696440a8aea67654bafc72b3");
        params.put("captchaCode", "4442");

        JSONObject result = getAndPrintResponse("/v1/zx_xyb_common.rymGetPhoneCode", buildCommonQueryString(params));
        System.out.println(JSON.toJSONString(result, true));

    }

    /**
     * 新增语料
     */
    @Test
    public void testRealAuth() {
        //   log.info("--->{}", TOKEN);
        Map<String, Object> params = Maps.newHashMap();
        params.put("bankEnc", "6222021001072755561"); // 98 99 100
        params.put("bankId", "3");  // 工商银行
        params.put("telephone", "18721686229");
        params.put("identityNumber", "412721198906025840");
        params.put("accountName", "邹永娜");
        params.put("bankType", "D");
        params.put("type", "3");
        params.put("vcode", "1234567");
        params.put("securityCode", "wdi7orwgs90hu5m7rmh592cgt9y2oqviickhptqe");
        // https://test-ng-qhcs-mws-stg.pingan.com.cn:32443/qhcsmws/cm/h5realNameAuth.do?_path=/v1/zx_xyb_onlyOtp.realNameRecognitionStatus
       // JSONObject result = getAndPrintResponse("/h5realNameAuth.do?_path=/web/zx_xyb_admin.realNameAuth", buildCommonQueryString(params));
        JSONObject result = getAndPrintResponse("/v1/h5/realNameAuth", buildCommonQueryString(params));
        System.out.println(JSON.toJSONString(result, true));
    }

    /**
     * 获取语料详情
     */
    @Test
    public void testChangePhone() {
        //   log.info("--->{}", TOKEN);
        Map<String, Object> params = Maps.newHashMap();

        params.put("accountKey", "account_key_6086bc77ff0e4a06b2eb5c9ef3095e56");
        params.put("telephone", "18721686227");
        params.put("type", "3");
        params.put("vcode", "1234567");
        params.put("securityCode", "si9lu0hohp7ohe42rgxckyh9n8tacx787saagtt8");


        //   sign(map);
        JSONObject result = getAndPrintResponse("/v1/h5/changePhone", buildCommonQueryString(params));
        System.out.println(JSON.toJSONString(result, true));

    }

    @Test
    public void md5(){
        String str = "PLATFORM_mchtUserAuthSelected=1,4,5,6,7&orgId=1&roleDesc=前海征信内部&roleName=QHZX1_45357a5c731751a44000d1ba2c0e25fb";
        String str2 = "PLATFORM_mchtUserAuthSelected=1,4,5,6,7&orgId=1&roleDesc=%E5%89%8D%E6%B5%B7%E5%BE%81%E4%BF%A1%E5%86%85%E9%83%A8&roleName=QHZX1_45357a5c731751a44000d1ba2c0e25fb";
        //System.out.println(Hashing.md5().hashBytes(str.getBytes("ISO-8859-1")).toString());
        String md5Str = Hashing.md5().hashString(str2, Charsets.UTF_8).toString();
        System.out.println("md5:"+md5Str);

    }



}
