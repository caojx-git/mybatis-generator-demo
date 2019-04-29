package caojx.learn.plugin;

import caojx.learn.util.ReflectConvertHelper;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.JavaFormatter;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.internal.util.StringUtility;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 扩展FalseMethodPlugin 定制化Maapper的生成
 *
 * @author caojx
 * @version $Id: MapperPlugin.java,v 1.0 2019-04-17 18:41 caojx
 * @date 2019-04-17 18:41
 */
public class MapperPlugin extends FalseMethodPlugin {

    /**
     * dao(Mapper接口)所在文件夹targetProject
     */
    private String daoTargetDir;

    /**
     * dao(Mapper接口)所在包targetPackage
     */
    private String daoTargetPackage;

    /**
     * dao(Mapper继承的父接口)daoSuperClass
     */
    private String daoSuperClass;

    /**
     * Dao（Mapper）扩展名
     */
    private String daoSuffix;

    /**
     * 作者
     */
    private String author;

    /**
     * 获取配置参数并验证参数是否有效
     *
     * @param warnings
     * @return
     */
    @Override
    public boolean validate(List<String> warnings) {
        //目标路径
        daoTargetDir = properties.getProperty("targetProject");
        boolean valid = StringUtility.stringHasValue(daoTargetDir);

        //目标包
        daoTargetPackage = properties.getProperty("targetPackage");
        boolean valid2 = StringUtility.stringHasValue(daoTargetPackage);

        //父类
        daoSuperClass = StringUtils.isEmpty(properties.getProperty("daoSuperClass")) ? "" : properties.getProperty("daoSuperClass");

        //扩展名
        daoSuffix = StringUtils.isEmpty(properties.getProperty("daoSuffix")) ? "" : properties.getProperty("daoSuffix");

        //作者
        author = properties.getProperty("author");
        if (!StringUtility.stringHasValue(author)) {
            author = System.getProperties().getProperty("user.name");
        }
        return valid && valid2;
    }

    /**
     * 给Mapper（Dao）添加扩展名和类的文档注释
     *
     * @param introspectedTable
     * @return
     */
    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        JavaFormatter javaFormatter = introspectedTable.getContext().getJavaFormatter();
        List<GeneratedJavaFile> mapperJavaFiles = new ArrayList<GeneratedJavaFile>();

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        //Mapper注释
        String remark = StringUtils.isEmpty(introspectedTable.getRemarks()) ? "类注释，描述" : introspectedTable.getRemarks() + daoSuffix;

        for (GeneratedJavaFile javaFile : introspectedTable.getGeneratedJavaFiles()) {
            CompilationUnit unit = javaFile.getCompilationUnit();
            FullyQualifiedJavaType baseModelJavaType = unit.getType();

            String shortName = baseModelJavaType.getShortName();

            GeneratedJavaFile mapperJavafile = null;

            if (shortName.endsWith("Mapper")) {
                Interface mapperInterface = new Interface(daoTargetPackage + "." + shortName);
                shortName = mapperInterface.getType().getShortName();

//                /**
//                 * $Id: MovOrderCouponService.java,v 1.0 2018/8/13 下午3:22 caojx
//                 *
//                 * @Copyright (c) 2018/8/13, XXX Group All Rights Reserved.
//                 */
//                mapperInterface.addFileCommentLine("/**");
//                mapperInterface.addFileCommentLine(" * $Id: " + shortName + ".java, v 1.0 " + sdf1.format(new Date()) + " " + author);
//                mapperInterface.addFileCommentLine(" * ");
//                mapperInterface.addFileCommentLine(" * @Copyright (c) " + sdf1.format(new Date()) + ", XXX Group All Rights Reserved.");
//                mapperInterface.addFileCommentLine(" */");

                //给Mapper添加文档注释
                mapperInterface.setVisibility(JavaVisibility.PUBLIC);
                mapperInterface.addJavaDocLine("/**");
                mapperInterface.addJavaDocLine(" * " + remark);
                mapperInterface.addJavaDocLine(" * ");
                mapperInterface.addJavaDocLine(" * @author " + author);
                mapperInterface.addJavaDocLine(" * @version \\$Id: " + shortName + ".java,v 1.0 " + sdf1.format(new Date()) + " " + author);
                mapperInterface.addJavaDocLine(" * @date " + sdf1.format(new Date()));
                mapperInterface.addJavaDocLine(" */");

                //是否配置父类和泛型支持
                if (!StringUtils.isEmpty(daoSuperClass)) {
                    FullyQualifiedJavaType daoSuperType = new FullyQualifiedJavaType(daoSuperClass);
                    daoSuperType.addTypeArgument(baseModelJavaType);
                    mapperInterface.addImportedType(baseModelJavaType);
                    mapperInterface.addImportedType(daoSuperType);
                    mapperInterface.addSuperInterface(daoSuperType);
                }
                ReflectConvertHelper.fieldCopyIgnoreHasValueTargeProperties(javaFile.getCompilationUnit(), mapperInterface);
                mapperJavafile = new GeneratedJavaFile(mapperInterface, daoTargetDir,javaFormatter);
                mapperJavaFiles.add(mapperJavafile);
            }
        }
//        return introspectedTable.getGeneratedJavaFiles();
        return mapperJavaFiles;
    }

}
