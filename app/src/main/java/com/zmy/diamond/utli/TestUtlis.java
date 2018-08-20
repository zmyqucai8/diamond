package com.zmy.diamond.utli;

/**
 * Created by zhangmengyun on 2018/6/22.
 */

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.zmy.diamond.utli.test.BaiDu_DataJson_Bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import okhttp3.Response;

public class TestUtlis {

    //openNewWindow
    private static final String AK = "9iKQpRZ4ZuyH6QHavzrzAAjQX9jRGZbB";
    private static String kerWords = "美食";
    private static String latlng = "22.450,113.767,22.867,114.617";// 深圳
    private static int sumNmb = 20;// 深圳


    public static int timeOutCount = 0;

    public static int noDataCount = 0;


    public static String filePath;

    public static void test() {

        filePath = MyUtlis.getAppFileDirPath() + File.separator + "test_data.txt";
        if (FileUtils.isFileExists(filePath)) {
            FileUtils.deleteFile(filePath);
            FileUtils.createOrExistsFile(filePath);
        }

        LogUtils.e("test 开始");


        startCollect(latlng, kerWords, 1);
    }


    /**
     * 获取当前矩形下的所有关键字poi
     *
     * @param latlng   矩形坐标  a,b,c,d
     * @param kerWords 关键字
     * @param page_num 页数
     */
    private static void startCollect(final String latlng, final String kerWords, final int page_num) {


        ThreadUtils.executeByFixed(AppConstant.THREAD_SIZE, new ThreadUtils.SimpleTask<Boolean>() {
            @Override
            public Boolean doInBackground() {


                return getBaiDuMapData(latlng, kerWords, page_num);
            }

            @Override
            public void onSuccess(Boolean result) {


                LogUtils.e("一条线程跑完了, 结果=" + result);

            }
        });


    }

    /**
     * 获取百度地图数据
     *
     * @param latlng
     * @param key
     * @param page_num
     * @return
     */
    private static Boolean getBaiDuMapData(String latlng, String key, int page_num) {

        BaiDu_DataJson_Bean baiDuMapData = getPOI(latlng, key, page_num);
        if (null == baiDuMapData) {
            LogUtils.e("没有数据" + page_num);

            noDataCount++;
            LogUtils.e("noDataCount=" + noDataCount);
            return false;
        }

        if (baiDuMapData.getTotal() >= 400) {

            LogUtils.e("数据大于400");
            //大于或等于400数据 ,去切割加载

            List<String> latLanes = getLatLng(latlng);
            // 递归获取分割后的左上矩形POI
            final String latlng0 = latLanes.get(0);


            ThreadUtils.executeByCachedWithDelay(new ThreadUtils.SimpleTask<Boolean>() {
                @Override
                public Boolean doInBackground() {
                    startCollect(latlng0, kerWords, 1);
                    return null;
                }

                @Override
                public void onSuccess(Boolean result) {

                }
            }, 10, TimeUnit.SECONDS);


            // 递归获取分割后的右上矩形POI
            final String latlng1 = latLanes.get(1);
            ThreadUtils.executeByCachedWithDelay(new ThreadUtils.SimpleTask<Boolean>() {
                @Override
                public Boolean doInBackground() {
                    startCollect(latlng1, kerWords, 1);
                    return null;
                }

                @Override
                public void onSuccess(Boolean result) {

                }
            }, 20, TimeUnit.SECONDS);
//            startCollect(latlng1, kerWords, 1);

            // 递归获取分割后的左下矩形POI
            final String latlng2 = latLanes.get(2);
            ThreadUtils.executeByCachedWithDelay(new ThreadUtils.SimpleTask<Boolean>() {
                @Override
                public Boolean doInBackground() {
                    startCollect(latlng2, kerWords, 1);
                    return null;
                }

                @Override
                public void onSuccess(Boolean result) {

                }
            }, 30, TimeUnit.SECONDS);
//            startCollect(latlng2, kerWords, 1);
            // 递归获取分割后的右下矩形POI
            final String latlng3 = latLanes.get(3);
            ThreadUtils.executeByCachedWithDelay(new ThreadUtils.SimpleTask<Boolean>() {
                @Override
                public Boolean doInBackground() {
                    startCollect(latlng3, kerWords, 1);
                    return null;
                }

                @Override
                public void onSuccess(Boolean result) {

                }
            }, 40, TimeUnit.SECONDS);
//            startCollect(latlng3, kerWords, 1);

        } else if (baiDuMapData.getTotal() < 400 && baiDuMapData.getTotal() > 0) {
            //小于400个数据 ,存储数据
            List<BaiDu_DataJson_Bean.ResultsBean> results = baiDuMapData.getResults();
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < results.size(); i++) {
                BaiDu_DataJson_Bean.ResultsBean poiBean = results.get(i);
                sb.append("名称=");
                sb.append(poiBean.getName());
                sb.append("\n地址=");
                sb.append(poiBean.getAddress());
                sb.append("\n电话=");
                sb.append(poiBean.getTelephone());
                sb.append("\n**************************************\n");
            }
            FileIOUtils.writeFileFromString(filePath, sb.toString(), true);
            return getBaiDuMapData(latlng, kerWords, page_num + 1);


        } else {
            //0条数据
            LogUtils.e(page_num + "页没有数据\n" + new Gson().toJson(baiDuMapData));
            return true;
        }

        return false;

    }


    /**
     * 获取分割后的4个矩形左上矩形左下右上坐标
     *
     * @param latlng
     * @return
     */
    public static List<String> getLatLng(String latlng) {
        List<Double> list = new ArrayList<Double>();
        StringTokenizer fenxi = new StringTokenizer(latlng.trim(), " ,"); // 以空格和逗号分
        while (fenxi.hasMoreTokens()) {
            String s = fenxi.nextToken();
            StringBuffer temps = new StringBuffer();
            char sr[] = s.toCharArray();
            for (int i = 0; i < sr.length; i++) {
                if (Character.isDigit(sr[i]) || (sr[i] == '.'))
                    temps.append(sr[i]);
            }
            if (temps.length() != 0) {
                list.add(Double.valueOf(Double.parseDouble(temps.toString())));
            }
        }
        List<String> latLanges = new ArrayList<String>();
        // 分割后的上左矩形
        String latlng0 = list.get(0) + "," + ((list.get(1) + list.get(3)) / 2) + "," + (list.get(0) + list.get(2)) / 2 + "," + list.get(3);
        latLanges.add(latlng0);
        // 分割后的上右矩形
        String latlng1 = ((list.get(0) + list.get(2)) / 2) + "," + ((list.get(1) + list.get(3)) / 2) + "," + list.get(2) + "," + list.get(3);
        latLanges.add(latlng1);
        // 分割后的下左矩形
        String latlng2 = list.get(0) + "," + list.get(1) + "," + ((list.get(0) + list.get(2)) / 2) + "," + ((list.get(1) + list.get(3)) / 2);
        latLanges.add(latlng2);
        // 分割后的下右矩形
        String latlng3 = ((list.get(0) + list.get(2)) / 2) + "," + list.get(1) + "," + list.get(2) + "," + ((list.get(1) + list.get(3)) / 2);
        latLanges.add(latlng3);
        return latLanges;
    }

    public static BaiDu_DataJson_Bean getPOI(String latlng, String kerWords, int pageNum) {

        String result = "";
        String url = "http://api.map.baidu.com/place/v2/search?query=" + kerWords + "&bounds=" + latlng + "&output=json&scope=2&page_size=" + 20 + "&page_num=" + pageNum + "&ak=" + AK;

        Response response = null;
        BaiDu_DataJson_Bean baiDu_dataJson_bean = null;
        try {
            response = OkGo.get(url).execute();


            result = response.body().string();


//        BaiDu_DataJson_Bean
//        LogUtils.e(result);

            baiDu_dataJson_bean = new Gson().fromJson(result, BaiDu_DataJson_Bean.class);
            if (baiDu_dataJson_bean.getStatus() == 401) {
                timeOutCount++;
                LogUtils.e("timeOutCount=" + timeOutCount);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        // 0 ok 正常 服务请求正常召回
        // 2 Parameter Invalid 请求参数非法 必要参数拼写错误或漏传（如query和tag请求中均未传入）
        // 3 Verify Failure 权限校验失败
        // 4 Quota Failure 配额校验失败 服务当日调用次数已超限，请前往API控制台提升（请优先进行开发者认证）
        // 5 AK Failure ak不存在或者非法 未传入ak参数；ak已被删除（可前往回收站恢复）；

        return baiDu_dataJson_bean;
    }





//    public String testStr;
//    public String testStr2 = "XYB6h/bg2NQ0l4YDqodswIMySZxADmlE9z7+6xN+3gsGvjqMGppvUR0pD9vu6nhA6GLxQqEchqWsddLTtQe995Y0MMLM07SWs/qqvMGhiZQXkTBoxq+XB4O5vYqTIRqcT3nkOi2lACR/NJ0o+bzC/A19EHapKl6entHtXHMvLDnrZYlecolKjKShOi8L4V33cqybw0GLU4iACjSw5t+WInm3vZYQDyTbnDJwFMDFJnL5pwho4RF0dgonFEyB0KEp11YO/rwThwDqqF6mwoJqk5wycBTAxSZy0P13CC1w25YVwcIWwHS8oiAoXL73AQz+uJq6TP/XKGKWNDDCzNO0llAfewt2ADHsF5EwaMavlweDub2KkyEanE955DotpQAkfzSdKPm8wvwNfRB2qSpenp7R7VxzLyw562WJXnKJSoykoTovC+Fd93Ksm8NBi1OIgAo0sObfliJ5t72WEA8k25wycBTAxSZykwHLXGi3GkQKJxRMgdChKdurLNV0VEuRr2tzmqHI1+qcMnAUwMUmcs0pDs1Lh+YyFcHCFsB0vKJ/0ma/pTH9M3U13W5bIDTSYiDiJ412ZuH/IxwdsWXSvSOB5bL6ewaazsM90VCikrF0CsqUlwIET04PuMuD0GAuhWqzA3F7HvAn2WUXRRkS/VSuIQYG8SQFs0RwDEtLfw1LXPGw/IioxH12qmViE6LUj7zk4P3o+2bWn3RN3NJiSXJhTPdQbKFEZFMuHHURqA8t62m3+xLcTxQRj8NPL4jzOTPuLQ8dS8DJqSpINUODOXTEjSF2T3b0X9mcpXAyNmJ6N3E65Zb1RXPWIlCNF65f6QF72gBn+3cXkHyKvCPXzSl5Pean71ThWwkyeBIzXKmWzaW5G/phJV/ZnKVwMjZiGBBN5XOtE5BKbsKgGXd70tfmYpOhp/V2X6K21W1yeWNEl4mPIePdu6dWSQIkoRvzhIHxyR38eBDoggoWwVcA/agHETzoN9NvVeRSqnht1Mch957+mTczA3W6T0xC/VUYQCm7owLksP3sv3J0kmhdtLKkK2WFT657Vo3/e0IEWyB1LPwg2hNZ2gsW/YBwqoz8¬";

//        if (isShowPlatformMenu) {
//            Log.e("TAG", "开始加密");
//            try {
//                testStr = TestUtlis.encode2Str("大家好,我将要被加密");
//                Log.e("TAG", "加密后=" + testStr);
//            } catch (Exception e) {
//                e.printStackTrace();
//
//            }
//        } else {
//            Log.e("TAG", "开始解密");
//            if (!TextUtils.isEmpty(testStr)) {
//                Log.e("TAG", "刚才加密过的String不为空,对其进行解密");
//
//                try {
//                    String s = TestUtlis.decode2Str(testStr, "2016051616090012345678tm");
//                    Log.e("TAG", "解密后=" + s);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            } else {
//                Log.e("TAG", "刚才加密过的String为空,对已有字符串进行解密");
//
//                try {
//                    String s = TestUtlis.decode2Str(testStr2, "2016051616090012345678tm");
//                    Log.e("TAG", "已有数据解密后=" + s);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//
//        }


//    public static final String ALGORITHM_3DES = "DESede/ECB/PKCS5Padding";
//    public static final String ALGORITHM_AES = "AES";
//    public static final String ALGORITHM_DES = "DES";
//    public static final int KEY_LENGTH_128 = 128;
//    public static final int KEY_LENGTH_168 = 168;
//    public static final int KEY_LENGTH_24 = 24;
//    public static final int KEY_LENGTH_256 = 256;
//    public static final int KEY_LENGTH_56 = 56;
//
//
//    public static byte[] decode2Byte(byte[] paramArrayOfByte, String paramString)
//            throws Exception {
//        return Base64.decode(decrypt(new SecretKeySpec(paramString.getBytes(), "DESede/ECB/PKCS5Padding"), paramArrayOfByte, "DESede/ECB/PKCS5Padding"), 0);
//    }
//
//    public static String decode2Str(String paramString1, String paramString2)
//            throws Exception {
//        return new String(Base64.decode(decrypt(new SecretKeySpec(paramString2.getBytes(), "DESede/ECB/PKCS5Padding"), paramString1, "DESede/ECB/PKCS5Padding"), 0), "UTF-8");
//    }
//
//    public static String decrypt(SecretKey paramSecretKey, String paramString1, String paramString2)
//            throws Exception {
//        try {
//            Cipher instance = Cipher.getInstance(paramString2);
//            instance.init(2, paramSecretKey);
//            String s = new String(instance.doFinal(Base64.decode(stringToBytes(paramString1), 0)), "UTF-8");
//            return s;
//        } catch (NoSuchAlgorithmException e) {
//            throw new Exception(e);
//        } catch (NoSuchPaddingException e) {
//            throw new Exception(e);
//        } catch (InvalidKeyException e) {
//            throw new Exception(e);
//        } catch (IllegalBlockSizeException e) {
//            throw new Exception(e);
//        } catch (BadPaddingException e) {
//            throw new Exception(e);
//        }
//    }
//
//    public static byte[] decrypt(SecretKey paramSecretKey, byte[] paramArrayOfByte, String paramString)
//            throws Exception {
//        try {
//            Cipher instance = Cipher.getInstance(paramString);
//            instance.init(2, paramSecretKey);
//            byte[] bytes = instance.doFinal(paramArrayOfByte);
//            return bytes;
//        } catch (NoSuchAlgorithmException e) {
//            throw new Exception(e);
//        } catch (NoSuchPaddingException e) {
//            throw new Exception(e);
//        } catch (InvalidKeyException e) {
//            throw new Exception(e);
//        } catch (IllegalBlockSizeException e) {
//            throw new Exception(e);
//        } catch (BadPaddingException e) {
//            throw new Exception(e);
//        }
//    }
//
//    public static String bytesToSting(byte[] paramArrayOfByte) {
//        try {
//            String s = new String(paramArrayOfByte, "UTF-8");
//            return s;
//        } catch (UnsupportedEncodingException e) {
//        }
//        return "";
//    }
//
//    public static byte[] encode2Byte(String paramString1, String paramString2)
//            throws Exception {
//        return encrypt(new SecretKeySpec(paramString2.getBytes(), "DESede/ECB/PKCS5Padding"), Base64.encode(paramString1.getBytes("UTF-8"), 0), "DESede/ECB/PKCS5Padding");
//    }
//
//    public static String encode2Str(String paramString)
//            throws Exception {
//        return encrypt(new SecretKeySpec("2016051616090012345678tm".getBytes(), "DESede/ECB/PKCS5Padding"), Base64.encodeToString(paramString.getBytes("UTF-8"), 0), "DESede/ECB/PKCS5Padding");
//    }
//
//    public static String encrypt(SecretKey paramSecretKey, String paramString1, String paramString2)
//            throws Exception {
//        try {
//            Cipher instance = Cipher.getInstance(paramString2);
//            instance.init(1, paramSecretKey);
//
//
//            String s = bytesToSting(Base64.encode(instance.doFinal(stringToBytes(paramString1)), 0));
//            return s;
//        } catch (NoSuchAlgorithmException e) {
//            throw new Exception(e);
//        } catch (NoSuchPaddingException e) {
//            throw new Exception(e);
//        } catch (InvalidKeyException e) {
//            throw new Exception(e);
//        } catch (IllegalBlockSizeException e) {
//            throw new Exception(e);
//        } catch (BadPaddingException e) {
//            throw new Exception(e);
//        } catch (Exception e) {
//            throw new Exception(e);
//        }
//    }
//
//    public static byte[] encrypt(SecretKey paramSecretKey, byte[] paramArrayOfByte, String paramString)
//            throws Exception {
//        try {
//            Cipher instance = Cipher.getInstance(paramString);
//            instance.init(1, paramSecretKey);
//            byte[] bytes = instance.doFinal(paramArrayOfByte);
//            return bytes;
//        } catch (NoSuchAlgorithmException e) {
//            throw new Exception(e);
//        } catch (NoSuchPaddingException e) {
//            throw new Exception(e);
//        } catch (InvalidKeyException e) {
//            throw new Exception(e);
//        } catch (IllegalBlockSizeException e) {
//            throw new Exception(e);
//        } catch (BadPaddingException e) {
//            throw new Exception(e);
//        }
//    }
//
//
//    public static byte[] stringToBytes(String paramString) {
//        if (paramString != null) {
//        }
//        try {
//            if (paramString.length() > 0) {
//                return paramString.getBytes("UTF-8");
//            }
//            return new byte[0];
//        } catch (Exception e) {
//        }
//        return new byte[0];
//    }
//

}
