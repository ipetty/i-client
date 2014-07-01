package net.ipetty.android.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author yneos
 */
public class PrettyDateTest {

	public static void main(String[] args) throws Exception {
		long curTime = System.currentTimeMillis();

		format(curTime, "#a H点", "yy- MM-dd a H点");

		format(curTime, "##a H点", "yy- MM-dd a H点");

		format(curTime, "# HH:mm:dd", "yy-MM-dd HH:mm:dd");

		format(curTime, "# a HH:mm:dd", "yy-MM-dd HH:mm:dd");

		format(curTime, "## HH:mm", "yy-MM-dd a HH:mm");

		format(curTime, "## a HH:mm", "yy-MM-dd a HH:mm");

		format(curTime, "##", "yyyy-MM-dd");

		format(curTime, "@", "yyyy-MM-dd HH:mm:dd");
	}

	public static void format(long curTime, String format, String fullFormat) {
		System.out.println("    format: " + format);
		System.out.println("fullFormat: " + fullFormat);
		System.out.println();

		Date date2 = new Date(curTime - 30 * 1000L); // 30 秒前
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.print(sdf.format(date2) + " 格式化为 : ");
		System.out.println(new PrettyDateFormat(format, fullFormat).format(date2));

		date2 = new Date(curTime - 3600 * 1000L * 6); // 六小时前
		System.out.print(sdf.format(date2) + " 格式化为 : ");
		System.out.println(new PrettyDateFormat(format, fullFormat).format(date2));

		date2 = new Date(curTime - 3600 * 1000L * 20); // 20小时前
		System.out.print(sdf.format(date2) + " 格式化为 : ");
		System.out.println(new PrettyDateFormat(format, fullFormat).format(date2));

		date2 = new Date(curTime - 3600 * 1000L * 54); // 54小时前
		System.out.print(sdf.format(date2) + " 格式化为 : ");
		System.out.println(new PrettyDateFormat(format, fullFormat).format(date2));

		date2 = new Date(curTime - 3600 * 1000L * 78); // 78小时前
		System.out.print(sdf.format(date2) + " 格式化为 : ");
		System.out.println(new PrettyDateFormat(format, fullFormat).format(date2));
		System.out.println("========================================================");

	}
}
