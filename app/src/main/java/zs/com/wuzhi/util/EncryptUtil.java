package zs.com.wuzhi.util;

import android.text.TextUtils;
import android.util.Base64;

import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * Created by zhangshuqing on 16/8/14.
 */
public class EncryptUtil {

    /** 安全密钥 */
    private static final String keyData = "ABCDEFGHIJKLMNOPQRSTWXYZabcdefghijklmnopqrstwxyz0123456789-_.";

    private final static String DES = "DES";

    public EncryptUtil() {
    }

    /**
     * Description 根据键值进行加密
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) {
        String strs = "";
        if(TextUtils.isEmpty(data)){
            return data;
        }
        try{
            byte[] bt = encrypt(data.getBytes(), keyData.getBytes());
            strs = Base64.encodeToString(bt,Base64.DEFAULT);
        }catch (Exception e){
            e.printStackTrace();
        }

        return strs;
    }

    /**
     * Description 根据键值进行解密
     * @param data
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data){
        String str = "";
        try{
            if (data == null || TextUtils.isEmpty(data))
                return data;

            byte[] buf = Base64.decode(data,Base64.DEFAULT);
            byte[] bt = decrypt(buf,keyData.getBytes());
            str = new String(bt);

        }catch (Exception e){
           e.printStackTrace();
        }
        return str;
    }

    /**
     * Description 根据键值进行加密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }


    /**
     * Description 根据键值进行解密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();

        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

        return cipher.doFinal(data);
    }

}
