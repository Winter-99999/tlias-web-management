package com.winter.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
public class OssUtil {

    // 1.通过@Value注解读取application.yml中的配置
//    @Value("${aliyun.oss.endpoint}")
//    private String endpoint;
//
//    @Value("${aliyun.oss.bucket-name}")
//    private String bucketName;
    /*
    @Value("${aliyun.oss.domain}")
    private String domain;
    */

    //2.把配置信息封装到类中，随后注入OssProperties对象
    // 【修复】这里必须用【构造器注入】
    // 之前写的是 @Autowired 字段 + "String endpoint = ossProperties.getEndpoint();"
    // 这种字段初始化语句是在 new OssUtil() 的时候执行的，而那一刻 Spring 还没来得及
    // 完成 @Autowired 注入，ossProperties 仍然是 null，所以启动就抛 NullPointerException。
    private final OssProperties ossProperties;

    public OssUtil(OssProperties ossProperties) {
        this.ossProperties = ossProperties;
    }


    // 读取cmd设置的系统环境变量 OSS_ACCESS_KEY_ID / OSS_ACCESS_KEY_SECRET
    private String getAk() {
        return System.getenv("OSS_ACCESS_KEY_ID");
    }

    private String getSk() {
        return System.getenv("OSS_ACCESS_KEY_SECRET");
    }

    /**
     * 上传输入流到OSS
     * @param inputStream 文件输入流
     * @param ossKey      OSS内文件路径key，如 upload/xxxx.jpg
     * @return 完整文件访问URL
     */
    public String upload(InputStream inputStream, String ossKey) {
        // 【修复】改成调用时实时取值，不再依赖字段初始化时机
        String endpoint = ossProperties.getEndpoint();
        String bucketName = ossProperties.getBucketName();
        OSS ossClient = new OSSClientBuilder().build(endpoint, getAk(), getSk());
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, ossKey, inputStream);
            ossClient.putObject(putObjectRequest);
            // 拼接完整访问url返回
            // 不再读取domain配置，代码自动拼接访问域名
            String realDomain = "https://" + bucketName + "." + endpoint;
            return realDomain + "/" + ossKey;
        } finally {
            // 关闭客户端释放资源
            ossClient.shutdown();
        }
    }

    /**
     * 对外直接调用：MultipartFile 文件上传
     * @param file 前端传过来的文件
     * @return 完整访问URL
     * @throws IOException IO异常
     */
    public String uploadFile(MultipartFile file) throws IOException {
        // 生成唯一文件名，防止重名覆盖
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFileName = UUID.randomUUID().toString().replace("-", "") + suffix;
        // OSS存储路径，统一放在upload文件夹下
        String ossKey = "upload/" + uniqueFileName;
        // 调用上传，直接返回url
        return upload(file.getInputStream(), ossKey);
    }
}
