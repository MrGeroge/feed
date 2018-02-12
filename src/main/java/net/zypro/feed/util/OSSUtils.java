package net.zypro.feed.util;

import java.io.IOException;
import java.io.InputStream;

import com.aliyun.openservices.oss.OSSClient;
import com.aliyun.openservices.oss.model.ObjectMetadata;

public class OSSUtils {

	private static final String accessKeyId = "YXQgY83rQThKc7jb";
	private static final String accessKeySecret = "nzSfm46kUVqLnCNPt9L0w8xzWB2eyG";
	private static final String bucketName = "wbsn";

	public static String putObject(String key, InputStream content) {

		OSSClient client = new OSSClient(accessKeyId, accessKeySecret);
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentType("image/jpeg");
		try {
			meta.setContentLength(content.available());
			client.putObject(bucketName, key, content, meta);
			return "http://" + bucketName + ".oss-cn-hangzhou.aliyuncs.com/"
					+ key;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
