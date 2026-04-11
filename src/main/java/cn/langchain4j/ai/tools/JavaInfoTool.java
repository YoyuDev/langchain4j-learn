package cn.langchain4j.ai.tools;

import dev.langchain4j.agent.tool.Tool;
import java.lang.management.ManagementFactory;

/**
 * Java 系统信息工具
 * 供 AI 大模型自动调用
 */
public class JavaInfoTool {

    /**
     * 获取当前 Java 运行环境信息（版本、路径、内存、系统等）
     */
    @Tool("获取当前 Java 虚拟机和系统的详细信息，包括版本、路径、内存、操作系统")
    public String getJavaSystemInfo() {
        StringBuilder sb = new StringBuilder();

        // Java 版本
        sb.append("【Java 版本】：").append(System.getProperty("java.version")).append("\n");
        // Java 安装目录
        sb.append("【Java 安装路径】：").append(System.getProperty("java.home")).append("\n");
        // 操作系统
        sb.append("【操作系统】：").append(System.getProperty("os.name")).append("\n");
        // 用户名
        sb.append("【当前用户】：").append(System.getProperty("user.name")).append("\n");
        // 时区
        sb.append("【时区】：").append(System.getProperty("user.timezone")).append("\n\n");

        // JVM 内存信息
        long maxMemory = Runtime.getRuntime().maxMemory() / 1024 / 1024;
        long totalMemory = Runtime.getRuntime().totalMemory() / 1024 / 1024;
        long freeMemory = Runtime.getRuntime().freeMemory() / 1024 / 1024;

        sb.append("【JVM 内存】\n");
        sb.append("最大可用内存：").append(maxMemory).append(" MB\n");
        sb.append("已分配内存：").append(totalMemory).append(" MB\n");
        sb.append("空闲内存：").append(freeMemory).append(" MB");

        return sb.toString();
    }
}
