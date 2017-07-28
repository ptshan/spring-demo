package pers.qianshifengyi.httpclient.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: ZHANGSHAN193
 * Date: 16-9-9
 * Time: 上午10:37
 * To change this template use File | Settings | File Templates.
 */
public class H5BaseHttpClientTest {

    private String web_security_sign_prefix = "xyb";
    private String web_security_sign_suffix= "5b1a2d40f9ca4d080e3b4a2b30bf30a5";
    public static final String Local_Url = "https://test-ng-qhcs-mws-stg.pingan.com.cn:32443/qhcsmws/cm/common/transfer/cap/sp.do?_path=";
 //   public static final String Local_Url = "http://10.28.136.68:7001/qhcsmws/cm/common/transfer/cap/sp.do?_path=";
    public static ContentType UTF8ContentType = ContentType.create("text/plain", Consts.UTF_8);
    private static final String downloadPath = "/Users/zhangshan193/Downloads/";

    BasicCookieStore basicCookieStore = new BasicCookieStore();
    List<Cookie> cookieList;

    public HttpEntity buildCommonQueryString(Map<String, Object> map) {


        StringBuilder queryString = new StringBuilder();
        Set<String> keySet = map.keySet();
        List<String> keyList = new ArrayList(keySet);

        Collections.sort(keyList,new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        // file类型不参与验签拼接
        for(String key:keyList){
            String value = map.get(key).toString();
            if(value.toUpperCase().trim().startsWith("FILE:")){
                String filePath = value.replaceFirst("[Ff][iI][lL][eE]:","");
                entityBuilder.addPart(key, new FileBody(new File(filePath)));
            }else{
                queryString.append(key).append("=").append(map.get(key)).append("&");
                entityBuilder.addPart(key, new StringBody(value, UTF8ContentType));
            }
        }
        if(queryString.length() > 0) {
            queryString.deleteCharAt(queryString.length() - 1);
        }
        queryString.insert(0, web_security_sign_prefix).append(web_security_sign_suffix);
        System.out.println(queryString);
        map.put("sign",Hashing.md5().hashString(queryString.toString(), Charsets.UTF_8).toString());

        entityBuilder.addPart("sign", new StringBody(map.get("sign").toString(), UTF8ContentType));

        return entityBuilder.setCharset(Charset.forName("UTF-8")).build();
    }

    public JSONObject getAndPrintResponse(String url,HttpEntity entity)  {
        CloseableHttpResponse response = null;
        JSONObject result = null;
        HttpPost httppost = new HttpPost(Local_Url+url);
        if(cookieList != null && !cookieList.isEmpty()){
            for(Cookie cookie:cookieList){
                basicCookieStore.addCookie(cookie);
            }
        }

        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(basicCookieStore).build();
        try {
            System.out.println("executing request " + httppost.getRequestLine());
            httppost.setEntity(entity);
            response = httpclient.execute(httppost);
            if(cookieList == null || cookieList.isEmpty()) {
                cookieList = basicCookieStore.getCookies();
            }
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            HttpEntity resEntity = response.getEntity();
            String returnFileName = "";
            if(isReturnFile(response,returnFileName) == true){
                result = JSON.parseObject(returnFileName);
            }else {
                if (resEntity != null) {
                    String resStr = EntityUtils.toString(resEntity);
                    result = JSON.parseObject(resStr);
                }

                EntityUtils.consume(resEntity);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            try{
                response.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                httpclient.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public boolean isReturnFile(HttpResponse response,String returnFileName) {
        Header contentHeader = response.getFirstHeader("Content-disposition");
        InputStream is = null;
        OutputStream os = null;
        if(contentHeader == null){
            return false;
        }
        String filename = null;
        if (contentHeader != null) {
            HeaderElement[] values = contentHeader.getElements();
            if (values.length == 1) {
                NameValuePair param = values[0].getParameterByName("filename");
                if (param != null) {
                    try {
                        //filename = new String(param.getValue().toString().getBytes(), "utf-8");
                        //filename=URLDecoder.decode(param.getValue(),"utf-8");
                        filename = param.getValue();
                        returnFileName = filename;
                        File file = new File(downloadPath+filename);
                        if(file.exists()){
                            file.delete();
                        }
                        if(!file.exists()){
                            file.createNewFile();
                        }
                        is = response.getEntity().getContent();
                        os = new FileOutputStream(file);
                        byte[] buf = new byte[10240];
                        int i = is.read(buf);
                        while(i != -1){
                            os.write(buf,0,i);
                            i = is.read(buf);
                        }
                        os.flush();
                        os.close();
                        is.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally{
                    	if(os != null){
                    		try{
                    			os.close();
                    		}catch(Exception e){
                    			e.printStackTrace();
                    		}
                    	}
                    	if(is != null){
                    		try{
                    			is.close();
                    		}catch(Exception e){
                    			e.printStackTrace();
                    		}
                    	}
                    }
                }else{
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 商户平台获取普通登陆用户token
     * @return
     */
    public void getCommonLoginToken(){

        Map<String,Object> params = new HashMap<String,Object>();
        params.put("loginName","zhangshan");
        params.put("userPwd","e19d5cd5af0378da05f63f891c7467af");

        JSONObject result = getAndPrintResponse("/nologin/login",buildCommonQueryString(params));
        //System.out.println(JSON.toJSONString(result, true));
    }

}
