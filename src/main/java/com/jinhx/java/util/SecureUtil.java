package com.jinhx.java.util;

import org.mindrot.jbcrypt.BCrypt;

import java.security.MessageDigest;
import java.util.Base64;

/**
 * 安全相关工具类，加密分为三种
 * 1、对称加密（symmetric），例如：AES、DES等
 * 2、非对称加密（asymmetric），例如：RSA、DSA
 * 3、摘要加密（digest），例如：MD5、SHA-1、SHA-256、HMAC等
 *
 * @author jinhaoxun
 * @since 2022-05-09
 */
public class SecureUtil {

    private static final String KEY_ALGORITHM = "AES";

    /**
     * 默认的加密算法
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * 通过Bcrypt加密
     *
     * @param str 需加密字段
     * @return String
     */
    public static String encoderByBcrypt(String str){
        return BCrypt.hashpw(str, BCrypt.gensalt());
    }

    /**
     * 通过Bcrypt校验字段是否正确
     *
     * @param str1 需校验字段
     * @param str2 正确字段
     * @return boolean
     */
    public static boolean checkByBcrypt(String str1, String str2){
        return BCrypt.checkpw(str1, str2);
    }

    /**
     * 通过MD5获取摘要
     *
     * @param str 需加密字段
     * @return String
     */
    public static String encoderByMD5(String str) throws Exception {
        MessageDigest md = MessageDigest.getInstance("md5");
        byte[] bytes = md.digest(str.getBytes());
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 通过MD5校验字段是否正确
     *
     * @param str1 需校验字段
     * @param str2 正确字段
     * @return boolean
     */
    public static boolean checkByMD5(String str1, String str2) throws Exception {
        return str2.equals(encoderByMD5(str1));
    }

//    /**
//     * @author jinhaoxun
//     * @description 进行加密方法
//     * @param str 传入未加密字段
//     * @return String
//     */
//    public static String encoderBySHA256(String str) throws Exception {
//        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
//        byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
//        return Hex.encodeHexString(hash);
//    }
//
//    /**
//     * @author jinhaoxun
//     * @description 判断用户密码是否正确方法
//     * @param password 用户输入的密码
//     * @param oldpassword 正确密码
//     * @return Boolean
//     */
//    public static boolean checkBySHA256(String password, String oldpassword) throws Exception {
//        return oldpassword.equals(encoderBySHA256(password));
//    }
//
//    /**
//     * @Author: jinhaoxun
//     * @Description: AES 加密操作
//     * @param content 待加密内容
//     * @param key 加密密钥
//     * @Date: 2020/4/2 上午12:46
//     * @Return: javax.crypto.spec.SecretKeySpec 返回Base64转码后的加密数据
//     * @Throws:
//     */
//    public static String encryptByAES(String content, String key) {
//        try {
//            // 创建密码器
//            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
//            byte[] byteContent = content.getBytes("utf-8");
//            // 初始化为加密模式的密码器
//            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(key));
//            // 加密
//            byte[] result = cipher.doFinal(byteContent);
//            //通过Base64转码返回
//            return new String(Base64Utils.encode(result));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * @Author: jinhaoxun
//     * @Description: AES 解密操作
//     * @param content
//     * @param key
//     * @Date: 2020/4/2 上午12:46
//     * @Return: javax.crypto.spec.SecretKeySpec
//     * @Throws:
//     */
//    public static String decryptByAES(String content, String key) {
//        try {
//            //实例化
//            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
//            //使用密钥初始化，设置为解密模式
//            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(key));
//            //执行操作
//            byte[] result = cipher.doFinal(Base64Utils.decode(content.getBytes()));
//            return new String(result, "utf-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * @Author: jinhaoxun
//     * @Description: 生成加密秘钥
//     * @param key
//     * @Date: 2020/4/2 上午12:46
//     * @Return: javax.crypto.spec.SecretKeySpec
//     * @Throws:
//     */
//    private static SecretKeySpec getSecretKey(final String key) {
//        //返回生成指定算法密钥生成器的 KeyGenerator 对象
//        KeyGenerator kg = null;
//        try {
//            kg = KeyGenerator.getInstance(KEY_ALGORITHM);
//            // 解决操作系统内部状态不一致问题（部分liunx不指定类型，无法解密）
//            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
//            secureRandom.setSeed(key.getBytes());
//            kg.init(128, secureRandom);
//
////            kg.init(128, new SecureRandom(key.getBytes()));
//            //生成一个密钥
//            SecretKey secretKey = kg.generateKey();
//            return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
