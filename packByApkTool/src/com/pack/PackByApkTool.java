package com.pack;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

public class PackByApkTool {

	private static final String VERSION = "1.1.1_Beta";
	private static final String DATE = "06261500";
	private static String apkName = "aa.apk";

	private static HashMap<String, String> ChannelMap = new HashMap<String, String>() {
		{
			// put("-1",
			// "Debug");
			put("0", "Normal");
			put("1", "Google");
			put("2", "Qq");
			put("3", "360");
			put("4", "91");
			put("5", "Baidu");
			put("6", "Mi");
			put("7", "Huawei");
		}
	};

	// 基础目录
	private static String basePaht = "D:\\java\\workspace\\packByApkTool";
	private static String keystore = basePaht + "\\aa.keystore";

	// 需要打包的 原始apk路径
	private static String apkPath = basePaht + File.separator + apkName;
	// 生成的apk包名称
	private static String generateApkName;
	// 反编译后源码存放路径
	private static String codePaht = basePaht + File.separator + "code";
	// 反编译后AndroidManifest存放路径
	private static String manifestPaht = codePaht + File.separator + "AndroidManifest.xml";
	// 生成未签名包存放路径
	private static String unsignPaht = basePaht + File.separator + "unSign";
	// 未签名包签名之后生成的签名包存放路径
	private static String signPaht = basePaht + File.separator + "sign";
	// 最终将签名包对齐之后apk存放路径
	private static String zipalignPaht = basePaht + File.separator + "sign_zipalign";

	public static void main(String[] args) {
		long time = System.currentTimeMillis();
		try {
			new File(zipalignPaht).delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!new File(signPaht).exists()) {
			new File(signPaht).mkdirs();
		}
		// 反编译apk
		CMDUtil.unPackApk(apkPath, codePaht);
		for (Entry<String, String> entry : ChannelMap.entrySet()) {
			String channelCode = entry.getKey();
			String channelName = entry.getValue();

			// "yourappname_Normal_1.1.0_Beta_06252343.apk";
			generateApkName = "yourappname" + "_" + channelName + "_" + VERSION + "_" + DATE;

			AndroidManifestUtil.change(manifestPaht, manifestPaht, "app_channel_name", channelCode);
			// 生成未签名apk的全路径
			String unSignApkPath = unsignPaht + File.separator + generateApkName + "_unsign.apk";
			// 生成签名apk的全路径
			String signApkPath = signPaht + File.separator + generateApkName + ".apk";
			// 重新回编译生成未签名apk
			CMDUtil.PackApk(codePaht, unSignApkPath);
			// 对apk进行签名
			CMDUtil.signApk(keystore, "alias", "password", unSignApkPath, signApkPath);
			if (!new File(zipalignPaht).exists()) {
				new File(zipalignPaht).mkdirs();
			}
			// 对签过名的apk进行对齐操作
			CMDUtil.zipalignApk(signApkPath, zipalignPaht + File.separator + generateApkName + ".apk");
			// 删除未签名apk包
			new File(unSignApkPath).delete();
			// 删除已经签名apk包
			new File(signApkPath).delete();
		}
		new File(unsignPaht).delete();
		new File(signPaht).delete();
		new File(zipalignPaht).delete();
		System.out.println("总耗时:" + (System.currentTimeMillis() - time) / 1000 + "秒");
	}
}
