package net.ipetty.android.sdk;

import java.io.File;
import java.util.List;

import net.ipetty.android.sdk.domain.IpetPhoto;

import org.springframework.core.io.FileSystemResource;

/**
 * 用户关系API
 * 
 * @author xiaojinghai
 */
public interface PhotoApi {

	/**
	 * 发布图片
	 */
	public IpetPhoto publish(String text, FileSystemResource file);

	public IpetPhoto publish(String text, File file);

	public IpetPhoto publish(String text, String file);

	/** 为了解决发布文字乱码问题而临时提供的一个接口 */
	@Deprecated
	public IpetPhoto publishText(String id, String text);

	/**
	 * 按时间线分页获取我关注的图片
	 */
	public List<IpetPhoto> listFollowd(String date, String pageNumber, String pageSize);

}
