package caojx.learn.plugin;

import org.mybatis.generator.api.*;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.internal.util.StringUtility;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 扩展FalseMethodPlugin 定制化Vo的生成
 *
 * @author caojx
 * @version $Id: VoPlugin.java,v 1.0 2019-04-17 18:41 caojx
 * @date 2019-04-17 18:41
 */
public class VoPlugin extends FalseMethodPlugin {

    /**
     * vo扩展名
     */
    private String voSuffix;

    /**
     * 作者
     */
    private String author;

    /**
     * 验证参数是否有效
     *
     * @param warnings
     * @return
     */
    @Override
    public boolean validate(List<String> warnings) {
        voSuffix = properties.getProperty("voSuffix");

        author = properties.getProperty("author");
        if (!StringUtility.stringHasValue(author)) {
            author = System.getProperties().getProperty("user.name");
        }
        return true;
    }

    /**
     * 生成mapping 添加自定义sql
     *
     * @param document
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {

        //创建Select查询
    /*    XmlElement select = new XmlElement("select");
        select.addAttribute(new Attribute("id", "selectAll"));
        select.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        select.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
        select.addElement(new TextElement("select * from " + introspectedTable.getFullyQualifiedTableNameAtRuntime()));

        XmlElement queryPage = new XmlElement("select");
        queryPage.addAttribute(new Attribute("id", "queryPage"));
        queryPage.addAttribute(new Attribute("resultMap", "BaseResultMap"));
        queryPage.addAttribute(new Attribute("parameterType", "com.fendo.bean.Page"));
        queryPage.addElement(new TextElement("select * from " + introspectedTable.getFullyQualifiedTableNameAtRuntime()));

        XmlElement parentElement = document.getRootElement();
        parentElement.addElement(select);
        parentElement.addElement(queryPage);*/
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    /**
     * 给Model添加扩展名
     *
     * @param topLevelClass
     * @param introspectedTable
     * @return
     */
    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        try {
            FullyQualifiedJavaType fullyQualifiedJavaType = topLevelClass.getType();
            java.lang.reflect.Field baseQualifiedNameField = fullyQualifiedJavaType.getClass().getDeclaredField("baseQualifiedName");
            java.lang.reflect.Field baseShortNameField = fullyQualifiedJavaType.getClass().getDeclaredField("baseShortName");
            baseQualifiedNameField.setAccessible(true);
            baseShortNameField.setAccessible(true);

            String remark = StringUtils.isEmpty(introspectedTable.getRemarks()) ? "类注释，描述" : introspectedTable.getRemarks() + voSuffix;


            System.out.println(baseShortNameField.get(fullyQualifiedJavaType));
            System.out.println(baseQualifiedNameField.get(fullyQualifiedJavaType));

            String baseShortNameFieldValue = baseShortNameField.get(fullyQualifiedJavaType).toString();
            String baseQualifiedNameFieldValue = baseQualifiedNameField.get(fullyQualifiedJavaType).toString();

            baseShortNameField.set(topLevelClass.getType(), baseShortNameFieldValue + voSuffix);
            baseQualifiedNameField.set(topLevelClass.getType(), baseQualifiedNameFieldValue + voSuffix);

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm");
            String shortName = topLevelClass.getType().getShortName();

//            topLevelClass.addFileCommentLine("/**");
//            topLevelClass.addFileCommentLine(" * $Id: "+shortName+".java, v 1.0 "+sdf1.format(new Date())+" "+author);
//            topLevelClass.addFileCommentLine(" * ");
//            topLevelClass.addFileCommentLine(" * @Copyright (c) "+ sdf1.format(new Date())+", XXX Group All Rights Reserved.");
//            topLevelClass.addFileCommentLine(" */");

            //给Dao接口添加文档注释
            topLevelClass.setVisibility(JavaVisibility.PUBLIC);
            topLevelClass.addJavaDocLine("/**");
            topLevelClass.addJavaDocLine(" * " + remark);
            topLevelClass.addJavaDocLine(" * ");
            topLevelClass.addJavaDocLine(" * @author " + author);
            topLevelClass.addJavaDocLine(" * @version \\$Id: " + shortName + ".java,v 1.0 " + sdf1.format(new Date()) + " " + author);
            topLevelClass.addJavaDocLine(" * @date " + sdf1.format(new Date()));
            topLevelClass.addJavaDocLine(" */");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.modelBaseRecordClassGenerated(topLevelClass, introspectedTable);
    }
}
