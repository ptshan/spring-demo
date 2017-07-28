package pers.qianshifengyi.httpclient.test;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientWithAllMethodTest {

    private CloseableHttpClient httpclient = null;
    private List<NameValuePair> formparams = null;
    private Map<String,Object> jsonMap = null;
    private UrlEncodedFormEntity uefEntity;
    private HttpPost httppost;
    private CloseableHttpResponse response;
    private HttpEntity entity;
    public static final String LOCAL_ADDR = "http://localhost:8080" ;
    public static final String REMOTE_ADDR = "http://jkkit-is-info-stg1.paic.com.cn" ;
    public static ContentType UTF8ContentType = ContentType.create("text/plain", Consts.UTF_8);
    // 添加参数,防止中文编码
    // MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
    // entityBuilder.addPart(key, new StringBody(value, UTF8ContentType));

    @Before
    public void init(){
        httpclient = HttpClients.createDefault();
        formparams = new ArrayList<NameValuePair>();
        jsonMap = new HashMap<String,Object>();

        formparams.add(new BasicNameValuePair("appId", "3322"));
        formparams.add(new BasicNameValuePair("token", "5566"));

    }


    /**
     * 打印返回请求
     */
    private void printResponse()throws Exception {
        formparams.add(new BasicNameValuePair("data", JSONObject.toJSON(jsonMap).toString()));
        uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(uefEntity);


        System.out.println("executing request " + httppost.getURI()+"?"+formparams.get(0).toString()
                +"&"+formparams.get(1).toString()+"&"+formparams.get(2).toString());
        response = httpclient.execute(httppost);
        System.out.println("response:"+response);
        entity = response.getEntity();
        if (entity != null) {
            System.out.println("------------------Response content--------------------");
            System.out.println(EntityUtils.toString(entity, "UTF-8"));
            System.out.println("--------------------------------------");
        }else{
            System.out.println(" entity is null");
        }
    }

    @After
    public void close(){
        if(response != null){
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        if(httpclient != null){
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }


    /**
     * HttpClient连接SSL
     */
    public void ssl() {
        CloseableHttpClient httpclient = null;
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            FileInputStream instream = new FileInputStream(new File("d:\\tomcat.keystore"));
            try {
                // 加载keyStore d:\\tomcat.keystore
                trustStore.load(instream, "123456".toCharArray());
            } catch (CertificateException e) {
                e.printStackTrace();
            } finally {
                try {
                    instream.close();
                } catch (Exception ignore) {
                }
            }
            // 相信自己的CA和所有自签名的证书
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
            // 只允许使用TLSv1协议
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1" }, null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            // 创建http请求(get方式)
            HttpGet httpget = new HttpGet("https://localhost:8443/myDemo/Ajax/serivceJ.action");
            System.out.println("executing request" + httpget.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    System.out.println("Response content length: " + entity.getContentLength());
                    System.out.println(EntityUtils.toString(entity));
                    EntityUtils.consume(entity);
                }
            } finally {
                response.close();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } finally {
            if (httpclient != null) {
                try {
                    httpclient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * post方式提交表单（模拟用户登录请求）
     */
    @Test
    public void postForm() {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost("http://localhost:8090/anshao_intent/sparqlExecutor/selectFormulaInfoBeanList");
        // 创建参数队列
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("utterance", "月费率低于1%"));
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    System.out.println("--------------------------------------");
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
                    System.out.println("--------------------------------------");
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送 post方式提交xml文档
     */
    @Test
    public void testPostXml() {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost("http://localhost:9090/user/setUser");
        // 创建参数队列

        StringEntity myEntity = new StringEntity("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><user><age>17</age><birth>2016-03-22T11:27:23.755+08:00</birth><id>778899</id><name>lucy</name></user>", "UTF-8");
        myEntity.setContentType(ContentType.APPLICATION_XML.getMimeType());
        httppost.setEntity(myEntity);
        try {
            httppost.setEntity(myEntity);
            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            System.out.println("response:"+response);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    System.out.println("--------------------------------------");
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
                    System.out.println("--------------------------------------");
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送 post方式提交json
     */
    @Test
    public void testPostJson() {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost

        HttpPost httppost = new HttpPost("http://localhost:8080/jkkit-is-info/collectionFacade/collectNews");
        // 创建参数队列

        StringEntity myEntity = new StringEntity("{\"clientNo\":\"222012138faa495f81d0e9f57621000\",\"originalUrl\":\"http://cooperations.wallstreetcn.com/yizhangtong/node/000101\",\"title\":\"test标题101\",\"appId\":\"334455\"}", "UTF-8");
        myEntity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
        httppost.setEntity(myEntity);
        try {
            httppost.setEntity(myEntity);
            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            System.out.println("response:"+response);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    System.out.println("--------------------------------------");
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
                    System.out.println("--------------------------------------");
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送 post提交表单
     */
    @Test
    public void testPostFormData() {

        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost("http://localhost:9090/user/setUser");
        // 创建参数队列
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("age", "28"));
        formparams.add(new BasicNameValuePair("id", "bbcc22"));
        formparams.add(new BasicNameValuePair("name", "lucy666"));

        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    System.out.println("--------------------------------------");
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
                    System.out.println("--------------------------------------");
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送 put请求
     */
    @Test
    public void testPut() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            // 创建httpget.
            HttpPut httpPut = new HttpPut("http://localhost:9090/user/updateUser?id=5566&name=李坏水");
            System.out.println("executing request " + httpPut.getURI());
            // 执行get请求.

            CloseableHttpResponse response = null;
            UrlEncodedFormEntity uefEntity;
            try {
                response = httpclient.execute(httpPut);
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                System.out.println("--------------------------------------");
                // 打印响应状态
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    // 打印响应内容长度
                    System.out.println("Response content length: " + entity.getContentLength());
                    // 打印响应内容
                    System.out.println("Response content: " + EntityUtils.toString(entity));
                }
                System.out.println("------------------------------------");
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送 delete请求
     */
    @Test
    public void testDelete() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            // 创建httpget.
            HttpDelete httpDelete = new HttpDelete("http://localhost:9090/user/deleteUser?id=7788");
            System.out.println("executing request " + httpDelete.getURI());
            // 执行get请求.

            CloseableHttpResponse response = null;
            UrlEncodedFormEntity uefEntity;
            try {
                response = httpclient.execute(httpDelete);
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                System.out.println("--------------------------------------");
                // 打印响应状态
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    // 打印响应内容长度
                    System.out.println("Response content length: " + entity.getContentLength());
                    // 打印响应内容
                    System.out.println("Response content: " + EntityUtils.toString(entity));
                }
                System.out.println("------------------------------------");
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送 get请求
     */
    @Test
    public void testGet() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            // 创建httpget.
            String url = "http://10.59.97.75:3002/sparql?query=PREFIX+fb%3A+%3Chttp%3A%2F%2Frdf.freebase.com%2Fns%2F%3E+"
                    + "SELECT+DISTINCT+%3Fx1+WHERE+%7B+fb%3Aintent.loanAmount+fb%3Aproperty.context.MonthFeeRate"
                    + "+%3Fx1+%7D+LIMIT+10";
            HttpGet httpget = new HttpGet(url);
            System.out.println("executing request " + httpget.getURI());
            // 执行get请求.
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                System.out.println("--------------------------------------");
                // 打印响应状态
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    // 打印响应内容长度
                    System.out.println("Response content length: " + entity.getContentLength());
                    // 打印响应内容
                    System.out.println("Response content: " + EntityUtils.toString(entity));
                }
                System.out.println("------------------------------------");
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 上传文件
     */
    public void upload() {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost("http://localhost:8080/myDemo/Ajax/serivceFile.action");

            FileBody bin = new FileBody(new File("F:\\image\\sendpix0.jpg"));
            StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);

            HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("bin", bin).addPart("comment", comment).build();

            httppost.setEntity(reqEntity);

            System.out.println("executing request " + httppost.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    System.out.println("Response content length: " + resEntity.getContentLength());
                }
                EntityUtils.consume(resEntity);
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testNewsCollection() {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        String params = "";
        HttpPost httppost = new HttpPost("http://localhost:8080/jkkit-is-info/collectionFacade/collectNews");
        //  HttpPost httppost = new HttpPost("http://jkkit-is-info-stg1.paic.com.cn/jkkit-is-info/collectionFacade/collectNews");
        // 创建参数队列
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        //     formparams.add(new BasicNameValuePair("data", "{\"clientNo\":\"228931429954457082\",\"originalUrl\":\"http://cooperations.wallstreetcn.com/yizhangtong/node/000101\",\"title\":\"test标题101\",\"appId\":\"334455\"}"));
        //     formparams.add(new BasicNameValuePair("data", "{\"clientNo\":\"222012138faa495f81d0e9f57621002\",\"originalUrl\":\"http://cooperations.wallstreetcn.com/yizhangtong/node/000203\",\"title\":\"test标题203\",\"appId\":\"334455\"}"));
        formparams.add(new BasicNameValuePair("data", "{\"clientNo\":\"222012138faa495f81d0e9f57621012\"," +
                "\"originalUrl\":\"http://www.icdc.com/error/3543\"," +
                "\"title\":\"test标题_3678\",\"appId\":\"334455\"}"));

        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            System.out.println("response:"+response);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    System.out.println("--------------------------------------");
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
                    System.out.println("--------------------------------------");
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testRemoveCollection(){
        // http://jkkit-is-info-stg1.paic.com.cn/jkkit-is-info/collectionFacade/removeCollect?data={"userCollectionId":"2091f1f8e1484f17b7ac6277818f9afe","appId":"123414567"}
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        String params = "";
        HttpPost httppost = new HttpPost("http://localhost:8080/jkkit-is-info/collectionFacade/removeCollect");
        //  HttpPost httppost = new HttpPost("http://jkkit-is-info-stg1.paic.com.cn/jkkit-is-info/collectionFacade/collectNews");
        // 创建参数队列
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        //     formparams.add(new BasicNameValuePair("data", "{\"clientNo\":\"228931429954457082\",\"originalUrl\":\"http://cooperations.wallstreetcn.com/yizhangtong/node/000101\",\"title\":\"test标题101\",\"appId\":\"334455\"}"));
        //     formparams.add(new BasicNameValuePair("data", "{\"clientNo\":\"222012138faa495f81d0e9f57621002\",\"originalUrl\":\"http://cooperations.wallstreetcn.com/yizhangtong/node/000203\",\"title\":\"test标题203\",\"appId\":\"334455\"}"));
        formparams.add(new BasicNameValuePair("data", "{\"userCollectionId\":\"136273b0356240cab888469d3e28a46d\",\"appId\":\"334455\"}"));

        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            System.out.println("response:"+response);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    System.out.println("--------------------------------------");
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
                    System.out.println("--------------------------------------");
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void testGetJxlNewsContent(){
        // http://jkkit-is-info-stg1.paic.com.cn/jkkit-is-info/collectionFacade/removeCollect?data={"userCollectionId":"2091f1f8e1484f17b7ac6277818f9afe","appId":"123414567"}
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        String params = "";
        HttpPost httppost = new HttpPost("http://localhost:8080/jkkit-is-info/newsFacade/getJxlNewsContent");
        //  HttpPost httppost = new HttpPost("http://jkkit-is-info-stg1.paic.com.cn/jkkit-is-info/collectionFacade/collectNews");
        // 创建参数队列


        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("appId", "3322"));
        formparams.add(new BasicNameValuePair("token", "5566"));
        formparams.add(new BasicNameValuePair("data", "{\"id\":4753}"));

        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);


            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            System.out.println("response:"+response);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    System.out.println("--------------------------------------");
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
                    System.out.println("--------------------------------------");
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Test
    public void testGetJxlNewsListByForm(){
        // http://jkkit-is-info-stg1.paic.com.cn/jkkit-is-info/collectionFacade/removeCollect?data={"userCollectionId":"2091f1f8e1484f17b7ac6277818f9afe","appId":"123414567"}
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        String params = "";
        HttpPost httppost = new HttpPost("http://localhost:8080/jkkit-is-info/newsFacade/getJxlNewsList");
        //  HttpPost httppost = new HttpPost("http://jkkit-is-info-stg1.paic.com.cn/jkkit-is-info/collectionFacade/collectNews");
        // 创建参数队列
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("appId", "3322"));
        formparams.add(new BasicNameValuePair("token", "5566"));
        formparams.add(new BasicNameValuePair("data", "{\"city\":\"sh\",\"pageNo\":2}"));

        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            System.out.println("response:"+response);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    System.out.println("--------------------------------------");
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
                    System.out.println("--------------------------------------");
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testGetJxlNewsListByForm2(){
        // http://jkkit-is-info-stg1.paic.com.cn/jkkit-is-info/collectionFacade/removeCollect?data={"userCollectionId":"2091f1f8e1484f17b7ac6277818f9afe","appId":"123414567"}
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        String params = "";
        HttpPost httppost = new HttpPost("http://localhost:8080/jkkit-is-info/newsFacade/getJxlNewsList");
        //  HttpPost httppost = new HttpPost("http://jkkit-is-info-stg1.paic.com.cn/jkkit-is-info/collectionFacade/collectNews");
        // 创建参数队列
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("appId", "3322"));
        formparams.add(new BasicNameValuePair("token", "5566"));
        formparams.add(new BasicNameValuePair("data", "{\"city\":\"sh\",\"pageNo\":3,\"pageSize\":15}"));

        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);
            System.out.println("executing request " + httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            System.out.println("response:"+response);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    System.out.println("--------------------------------------");
                    System.out.println("Response content: " + EntityUtils.toString(entity, "UTF-8"));
                    System.out.println("--------------------------------------");
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void testGetNewsList()throws Exception {
        // newsFacade/getNewsList?appId=10000&data={"limit":3,"lastRecordId":-1}

        httppost = new HttpPost(REMOTE_ADDR+"/jkkit-is-info/newsFacade/getNewsList");

        jsonMap.put("limit",3);
        jsonMap.put("lastRecordId",-1);

        printResponse();
    }

    @Test
    public void testGetStockPickingList()throws Exception {
        httppost = new HttpPost(REMOTE_ADDR+"/jkkit-is-info/aggregationFacade/getStockPickingList");

        jsonMap.put("clientNo","186002");
        jsonMap.put("type",0);
        jsonMap.put("limit",5);

        printResponse();
    }

    @Test
    public void testGetJxlNewsList()throws Exception {
        httppost = new HttpPost(REMOTE_ADDR+"/jkkit-is-info/newsFacade/getJxlNewsList");
        //  HttpPost httppost = new HttpPost("http://jkkit-is-info-stg1.paic.com.cn/jkkit-is-info/collectionFacade/collectNews");
        // 创建参数队列
        // data	clientNo	用户id	是	String
        //data	newsId	新闻id	是	int
        // data	type	操作类型	是	String	1：点赞
        // data	status	操作状态	是	String	1表示赞，0表示取消赞
        //jsonMap.put("title","什么");
        jsonMap.put("city","nj");
        jsonMap.put("pageNo",1);
        jsonMap.put("pageSize",15);

        printResponse();
    }

    @Test
    public void testGetJxlNewsTitleList()throws Exception {
        httppost = new HttpPost(LOCAL_ADDR+"/jkkit-is-info/newsFacade/getJxlNewsList");
        //  HttpPost httppost = new HttpPost("http://jkkit-is-info-stg1.paic.com.cn/jkkit-is-info/collectionFacade/collectNews");
        // 创建参数队列
        // data	clientNo	用户id	是	String
        //data	newsId	新闻id	是	int
        // data	type	操作类型	是	String	1：点赞
        // data	status	操作状态	是	String	1表示赞，0表示取消赞

        jsonMap.put("city","sh");
        jsonMap.put("pageNo",1);
        jsonMap.put("pageSize",15);

        printResponse();
    }






    @Test
    public void testGetJXLNewsContent()throws Exception {
        httppost = new HttpPost(REMOTE_ADDR+"/jkkit-is-info/newsFacade/getJxlNewsContent");

        jsonMap.put("id",20600);
        jsonMap.put("clientNo","187001");

        printResponse();
    }

    // 测试新闻列表点赞
    @Test
    public void testUserOptNewsNewList()throws Exception {
        httppost = new HttpPost(REMOTE_ADDR+"/jkkit-is-info/userBehaviorFacade/userOperateNews");
        jsonMap.put("clientNo","187003");
        jsonMap.put("newsId",2140);
        jsonMap.put("type",1); // 1 赞 2踩
        jsonMap.put("status",1); // 1有效 0取消
        jsonMap.put("channel",1); // 1新闻列表  2聚信立

        printResponse();
    }
    // 测试聚信立点赞
    @Test
    public void testUserOptNewsJXL()throws Exception {
        httppost = new HttpPost(REMOTE_ADDR+"/jkkit-is-info/userBehaviorFacade/userOperateNews");
        jsonMap.put("clientNo","187001");
        jsonMap.put("newsId",20600);
        jsonMap.put("type",1);
        jsonMap.put("status",0);
        jsonMap.put("channel",2); // 1新闻列表  2聚信立
        printResponse();
    }
	

}
