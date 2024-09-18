package com.lgw.github.util;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizCmdLineEngine;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

/**
 * @author sunhuadong
 * Created on 2024-09-18
 */
@Component
public class BeanDependencyGraphGenerator {

    private final ApplicationContext applicationContext;
    private DefaultListableBeanFactory beanFactory;
    private Set<String> processedBeans;
    private Set<String> addedDependencies;
    private MutableGraph graph;

    public static final Integer TIME_OUT_SECONDS = 120;

    public BeanDependencyGraphGenerator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        ConfigurableListableBeanFactory configurableBeanFactory =
                ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        if (configurableBeanFactory instanceof DefaultListableBeanFactory) {
            this.beanFactory = (DefaultListableBeanFactory) configurableBeanFactory;
        } else {
            throw new IllegalStateException("Unable to get DefaultListableBeanFactory");
        }
    }

    public void generateGraph(String packagePrefix, String startBean, int maxDepth) throws IOException {
        processedBeans = new HashSet<>();
        addedDependencies = new HashSet<>();
        graph = mutGraph("bean_dependencies").setDirected(true);

        if (startBean != null && !startBean.isEmpty()) {
            processBeanDependencies(startBean, packagePrefix, 0, maxDepth);
        } else {
            for (String beanName : beanFactory.getBeanDefinitionNames()) {
                if (beanName.startsWith(packagePrefix)) {
                    processBeanDependencies(beanName, packagePrefix, 0, maxDepth);
                }
            }
        }

        try (GraphvizCmdLineEngine engine = new GraphvizCmdLineEngine()) {
            engine.timeout(TIME_OUT_SECONDS, TimeUnit.SECONDS);
            Graphviz.useEngine(engine);
            Graphviz.fromGraph(graph).render(Format.DOT).toFile(new File("bean_dependencies.dot"));
            Graphviz.fromGraph(graph).render(Format.SVG).toFile(new File("bean_dependencies.svg"));
        }


    }

    private void processBeanDependencies(String beanName, String packagePrefix, int currentDepth, int maxDepth) {
        if (currentDepth > maxDepth || processedBeans.contains(beanName)) {
            return;
        }

        processedBeans.add(beanName);
        MutableNode currentNode = mutNode(beanName);

        String[] dependencyNames = beanFactory.getDependenciesForBean(beanName);

        for (String dependencyName : dependencyNames) {
            if (beanFactory.containsBean(dependencyName)) {
                Class<?> dependencyClass = beanFactory.getType(dependencyName);
                if (dependencyClass != null && dependencyClass.getName().startsWith(packagePrefix)) {
                    String forwardDependency = beanName + "->" + dependencyName;
                    String backwardDependency = dependencyName + "->" + beanName;

                    if (
                            !addedDependencies.contains(forwardDependency)
                        // 如果需要展示单向依赖，可以取消下面两行注释
                        //                                    && !addedDependencies.contains(backwardDependency)
                        //                                    && !addedDependencies.contains(dependencyName)
                    ) {
                        MutableNode dependencyNode = mutNode(dependencyName);
                        MutableNode link = currentNode.addLink(dependencyNode);
                        if (processedBeans.contains(dependencyName)) {
                            link.add(Color.RED, Style.BOLD);
                        } else {
                            link.add(Color.BLUE);
                        }
                        addedDependencies.add(forwardDependency);
                        addedDependencies.add(backwardDependency);
                        addedDependencies.add(dependencyName);
                        graph.add(currentNode);

                        processBeanDependencies(dependencyName, packagePrefix, currentDepth + 1, maxDepth);
                    }
                }
            }
        }
    }
}