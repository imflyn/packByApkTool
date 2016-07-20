package com.pack;

import java.io.File;
import java.io.IOException;

public class CMDUtil {
	public static void unPackApk(String apkPath, String targetPaht) {
		String cmd[] = { System.getProperty("user.dir") + File.separator + "apktool" + File.separator + "apktool.bat",
				"d", "-f", "-s", apkPath, "-o", targetPaht };
		System.out.println("---开始反编译---");
		CMDUtil.runCmd(cmd);
	}

	public static void PackApk(String projectPath, String newApkPaht) {
		String cmd[] = { System.getProperty("user.dir") + File.separator + "apktool" + File.separator + "apktool.bat",
				"b", "-f", projectPath, "-o", newApkPaht };
		System.out.println("开始打包");
		CMDUtil.runCmd(cmd);
	}

	public static void signApk(String keystorePath, String alias, String pwd, String unSignApkPath,
			String signApkPaht) {
		String cmd[] = { "jarsigner", "-verbose", "-keystore", keystorePath, "-storepass", pwd, "-keypass", pwd,
				"-digestalg", "SHA1", "-sigalg", "MD5withRSA", "-signedjar", signApkPaht, unSignApkPath, alias };
		System.out.println("开始签名");
		CMDUtil.runCmd(cmd);
	}

	public static void zipalignApk(String unzipalignApkPath, String zipalignApkPaht) {
		String cmd[] = { System.getProperty("user.dir") + File.separator + "zipalign ", "-v", "4", unzipalignApkPath,
				zipalignApkPaht };
		System.out.println("开始对齐");
		CMDUtil.runCmd(cmd);
	}

	private static void runCmd(final String cmd[], final String environment) {
		try {
			Process p = Runtime.getRuntime().exec(cmd, null, environment == null ? null : new File(environment));
			StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
			errorGobbler.start();
			StreamGobbler outGobbler = new StreamGobbler(p.getInputStream(), "STDOUT");
			outGobbler.start();
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private static void runCmd(final String cmd[]) {
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream(), "ERROR");
			errorGobbler.start();
			StreamGobbler outGobbler = new StreamGobbler(p.getInputStream(), "STDOUT");
			outGobbler.start();
			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
