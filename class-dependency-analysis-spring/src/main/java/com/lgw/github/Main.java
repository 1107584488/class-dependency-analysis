package com.lgw.github;

import com.lgw.github.util.BeanDependencyGraphGenerator;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

/**
 * @author sunhuadong
 * Created on 2024-09-18
 */
public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");

        ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        // 分析 bean 之间的依赖关系，指定起点 bean 和最大深度，深度优先遍历，将遍历到的 bean 和依赖关系输出到文件
        context.getBean(BeanDependencyGraphGenerator.class).generateGraph("com.xxx.ad.brand.platform",
                "settlementController",
                3);
    }
}