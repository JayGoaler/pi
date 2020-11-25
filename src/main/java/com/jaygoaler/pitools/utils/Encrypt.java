package com.jaygoaler.pitools.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.ClassPathResource;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * @author JayGoal
 * @date 2019/11/3
 */
public class Encrypt {
    private static final String KEY_MD5 = "MD5";
    /**
     * 全局数组
     */
    private static final String[] strDigits = {"0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * 返回形式为数字跟字符串
     *
     * @param bByte
     * @return
     */
    private static String byteToArrayString(byte bByte) {
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    /**
     * 转换字节数组为16进制字串
     *
     * @param bByte
     * @return
     */
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    /**
     * MD5加密
     *
     * @param strObj
     * @return
     * @throws Exception
     */
    public static String getMD5Code(String strObj) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(KEY_MD5);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
        // md.digest() 该函数返回值为存放哈希值结果的byte数组
        return byteToString(md.digest(strObj.getBytes())).toUpperCase();
    }


    /**
     * rsa
     *
     * @param certificatePath
     * @return
     * @throws Exception
     */
    private static Certificate getCertificate(String certificatePath) throws Exception {

        //返回指定证书类型的 CertificateFactory 对象。X.509是由国际电信联盟（ITU-T）制定的数字证书标准。
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

        ClassPathResource resource = new ClassPathResource(certificatePath);

        return certificateFactory.generateCertificate(resource.getInputStream());
    }

    private static final int MAX_ENCRYPT_BLOCK = 117;
    /**
     * java自带keytool生成的工具密钥长度为2048，所以解密块长度为2048/8=256 同理若密钥长度为1024，解密块长度为128
     */
    private static final int MAX_DECRYPT_BLOCK = 256;

    /**
     * 使用公钥或私钥加密数据
     *
     * @param data   需要加密的数据
     * @param cipher 密码器
     * @return 加密后的数据（异常则返回空字符串）
     */
    private static String encryptWithKey(String data, Cipher cipher) {
        try {
            int length = data.getBytes().length;
            int offset = 0;
            byte[] cache;
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            int i = 0;
            while (length - offset > 0) {
                if (length - offset > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data.getBytes(), offset, length - offset);
                }
                outStream.write(cache, 0, cache.length);
                i++;
                offset = i * MAX_ENCRYPT_BLOCK;
            }
            return Base64.encodeBase64String(outStream.toByteArray());
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * 使用公钥或者私钥解密
     *
     * @param data   需要解密的数据
     * @param cipher 密码器
     * @return 解密后的数据
     */
    private static String deEncryptWithKey(String data, Cipher cipher) {
        try {
            byte[] bEncrypt = Base64.decodeBase64(data);
            int inputLen = bEncrypt.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(bEncrypt, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(bEncrypt, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return new String(decryptedData);
        } catch (IOException | BadPaddingException | IllegalBlockSizeException e) {
            throw new IllegalArgumentException("无法解析的授权码，请联系管理员确认！异常信息:" + e.getMessage());
        }
    }

    /**
     * 公钥加密
     *
     * @param data 加密数据
     * @param path keystore路径
     * @return 加密后的数据
     * @throws Exception 加载异常
     */
    public static String EncryptWRSA_Pub(String data, String path) throws Exception {
        //        String encryptData ="";
        X509Certificate x509Certificate = (X509Certificate) getCertificate(path);
        // 获得公钥
        PublicKey publicKey = x509Certificate.getPublicKey();
        //公钥加密
        Cipher cipher = Cipher.getInstance("rsa");
        SecureRandom random = new SecureRandom();
        cipher.init(Cipher.ENCRYPT_MODE, publicKey, random);

        return encryptWithKey(data, cipher);
    }

    /**
     * 私钥加密
     *
     * @param data 加密数据
     * @param path keystore路径
     * @return 加密后的数据
     * @throws Exception 加载异常
     */
    public static String EncryptWRSA_Pri(String data, String path) {
        try (FileInputStream in = new FileInputStream(path)) {
            // JKS: Java KeyStoreJKS，可以有多种类型
            KeyStore ks = KeyStore.getInstance("JKS");
            //使用密码加载keystore文件
            ks.load(in, "tfswx2019".toCharArray());
            in.close();

            // 记录的别名
            String alias = "server";
            // 记录的访问密码
            String pswd = "tfswx2019";
            Certificate cert = ks.getCertificate(alias);
            //获取私钥
            PrivateKey privateKey = (PrivateKey) ks.getKey(alias, pswd.toCharArray());
            //私钥加密
            Cipher cipher = Cipher.getInstance("rsa");
            SecureRandom random = new SecureRandom();
            cipher.init(Cipher.ENCRYPT_MODE, privateKey, random);
            return encryptWithKey(data, cipher);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 公钥解密数据
     *
     * @param data 需要解密的数据
     * @param path 公钥路径
     * @return 解密后的数据
     * @throws Exception 加载异常
     */
    public static String DecryptWithRSA_Pub(String data, String path) {
        try {
            X509Certificate x509Certificate = (X509Certificate) getCertificate(path);
            // 获得公钥
            PublicKey publicKey = x509Certificate.getPublicKey();

            Cipher cipher = Cipher.getInstance("rsa");
            SecureRandom random = new SecureRandom();
            //私钥解密
            cipher.init(Cipher.DECRYPT_MODE, publicKey, random);

            return deEncryptWithKey(data, cipher);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 私钥解密数据
     *
     * @param data 需要解密的数据
     * @param path 私钥路径
     * @return 解密后的数据
     * @throws Exception 加载异常
     */
    public static String DecryptWithRSA_Pri(String data, String path) throws Exception {
        try (FileInputStream in = new FileInputStream(path)) {

            // JKS: Java KeyStoreJKS，可以有多种类型
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(in, "tfswx2019".toCharArray());
            in.close();

            // 记录的别名
            String alias = "server";
            // 记录的访问密码
            String pswd = "tfswx2019";
            //        Certificate cert = ks.getCertificate(alias);
            //获取私钥
            PrivateKey privateKey = (PrivateKey) ks.getKey(alias, pswd.toCharArray());

            Cipher cipher = Cipher.getInstance("rsa");
            SecureRandom random = new SecureRandom();
            //私钥解密
            cipher.init(Cipher.DECRYPT_MODE, privateKey, random);
            return deEncryptWithKey(data, cipher);
        }
    }

}