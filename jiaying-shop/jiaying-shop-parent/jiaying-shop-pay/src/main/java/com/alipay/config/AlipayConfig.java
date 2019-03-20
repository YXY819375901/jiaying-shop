package com.alipay.config;

import java.io.FileWriter;
import java.io.IOException;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {
	
//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id = "2016092600599281";	
	
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCTcuoQY9Hs4SiAMchadlwpTk+z12L3reiUja/mt+ZU47ML3WK3qS9Ices64bGHMFxU56ivlqGmjNc2rc/R5sKOdOYxqtOCU73s86E3AXs4sbEv5cEe2/Iz2ECJjd0Mt//2y+HnXyg0FCGPcXIoqYV7k1Y/VrRtW8J7A9HjkXz9zAu9u8jOwKV8FN5PYxhgdNPSK1NwcwD8+g37hzRGC5ci1cYAi/U855ogxGdQHFu1odDwwuWfMqyDl69mv8c9+TCHStn+niVVp1n1ze4/DaMRJAYL8jfc3oUdYdapUGcZmZ4hC7HO+n5JyYFZWuuFd7RIVDw51LuR/hrXs0FZkqPLAgMBAAECggEAOYNaTzhQWuBcZtyENNz0B9yW9CQwY0G/dapqkUjsG/KRFTCuX8srOUvOIDKGh5/Sc//EaXEM7qUDET3s0Qf8nYp1e6wRQkWd0H4bRRRb1vpgjblFgd7y80wHqF/geCSRP9e47wHL8bxkdsoeaEPvYAtRWfVc/UcMbeVIikZmhavo2u9sfRulCrFt9ZoR8Hw4HTivys9U3OagHviN4UtBdrJ227fLXQeOHgeiAaq8OmBxMkKZTFdN1qqHq7JQakCBEJwqf7nplxjyFX2JM55QGuqyhXbazRXC6Fbd51kR/jQdyCWVmYkJCFADsHTUoN17+Jzg64z/4q2+iCuwRcuuyQKBgQDDX0HAvIW3N6YJ5Xe0eMpFx7Lo7HtRwbpVJgkUVCXu3kUe5tpxiVi0uI7Qesfgx5WKJptXz03/bMmJd/zggcq6UpU59ZUaVkLCQhreIzv5m5oC3HrRHyxCP/LsUPUvmbeSgvyDjdHpWmQrx2JL12wL4YEJHPaXOafwZXMUh93l1QKBgQDBNIsU+XusPQ67/PYzUbU5agfNnR9lKX0FN+h9cQocxtWERzkCbSMg/1H62ElvbqCI+nJEL6+Jm6xyM1xSakD+z+vnCfuKT8D0SpnzIA9al8VDgbtG2wNZNM1CY+c8WpP2cbYnkEvqy7a7XNRFNk+1Mv3YkU6M0bxZ9K/fQJETHwKBgEX7Mf9KyA7X1RbIZjdz6OhT3ucGBipD3W2woSHx/mBwp4upH2f5zZd2YFmXEU+Xzh3dNFmgvQyYOByQqTj15NsWx7MqH41+ho00xpD5R/th3CnYNfoAU4crhuVVHm49fn7xgmfQ/fa7VRRFaBVIhcA/t0u/XyjOEzTN8gnRnQ89AoGAIiTlB3nnr1LDDdnwsLUZ7+GbK20/JSOl4ow03CZkAR3qkgv8OT+MW8KDfG9I2xaT7GFGXGKJBSqr2SrWgAIjJmUcbaJH//OESCf++QaLcBkuVXXR4P3Ho7Y/KCJ7tSDU0VLbPDa2E/KYgpgvM2QbxgwS7HkRhrVyMfuaMTw550UCgYAcMocQXWcIyqegKoBo+b8sphnLDHB3wevM+A52VtAnshTaYB1b0j+85ih5lXrpY/ElXGK81LRt/K6xrr5XapQ3PYLYIqe9hmk34hE9YPkQInK5eTUyl0ahF4KzmTO1VneHJbAZTq5VdkR4g1X8JiVGqfzlWqmCH2zM+quVEdZVwA==";
	
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtuX1gQNAhDWuiYkc+Idvs+h0arRcdtRXtqERbf88UaZtO7MKKR2o0d9DMAKDz5/qx5AJY5O6czoPdHa8ElRLADRXoZRk6l1XC73wHKXQmL1XLjaj8PPMG9Wo+b5fpOdDKtLkR5uE7PL0vygHkjCOQhPdOJACQSAkGQnS8CGviDU6yWEoTdLenKAdKhm6FcgrMh9BZ8G0uFytUHTV25brap+2/f3lHyYaMOByXqikPvcS/m0mdALduvbIiDGeMLjns8lo1OmPSNqBZTXCF+Yw41MH8Laxb4Fytq2roTZMC3wS5kqsCZzJ+Sops4+kSBpwUdN1xReEgHIrpDsZLyCfEQIDAQAB";

	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url = "http://b7a2b4.natappfree.cc/alibaba/callBack/notifyUrl";
 
	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url = "http://b7a2b4.natappfree.cc/alibaba/callBack/returnUrl";

	// 签名方式
	public static String sign_type = "RSA2";
	
	// 字符编码格式
	public static String charset = "utf-8";
	
	// 支付宝网关
	public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";
	
	// 支付宝网关
	public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /** 
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

