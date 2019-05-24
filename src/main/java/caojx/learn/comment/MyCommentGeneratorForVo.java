package caojx.learn.comment;


import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

/**
 * 扩展CommentGenerator 定制化Vo的注释生成规则
 *
 * @author caojx
 * @version $Id: MapperPlugin.java,v 1.0 2019-04-17 18:41 caojx
 * @date 2019-04-17 18:41
 */
public class MyCommentGeneratorForVo implements CommentGenerator {

    /**
     * properties配置文件
     */
    private Properties properties;

    /**
     * 父类时间
     */
    private boolean suppressDate;

    /**
     * 父类所有注释
     */
    private boolean suppressAllComments;

    /**
     * 当前时间
     */
    private String currentDateStr;

    /**
     * 作者
     */
    private String author;

    public MyCommentGeneratorForVo() {
        super();
        properties = new Properties();
        suppressDate = false;
        suppressAllComments = false;
        currentDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
    }

    /**
     * 从该配置中的任何属性添加此实例的属性CommentGenerator配置。
     * 这个方法将在任何其他方法之前被调用。
     *
     * @param properties
     */
    @Override
    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);
        suppressDate = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE));
        suppressAllComments = isTrue(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS));
        author = this.properties.getProperty("author");
        if (!StringUtility.stringHasValue(author)) {
            author = System.getProperties().getProperty("user.name");
        }
    }

    /**
     * 自定义的javadoc标签
     *
     * @param javaElement
     * @param markAsDoNotDelete
     */
    protected void addJavadocTag(JavaElement javaElement, boolean markAsDoNotDelete) {
        javaElement.addJavaDocLine(" *");
        StringBuilder sb = new StringBuilder();
        sb.append(" * ");
        sb.append(MergeConstants.NEW_ELEMENT_TAG);
        if (markAsDoNotDelete) {
            sb.append(" do_not_delete_during_merge");
        }
        String s = getDateString();
        if (s != null) {
            sb.append(' ');
            sb.append(s);
        }
        javaElement.addJavaDocLine(sb.toString());
    }

    /**
     * 为枚举添加注释
     *
     * @param innerEnum
     * @param introspectedTable
     */
    @Override
    public void addEnumComment(InnerEnum innerEnum, IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        innerEnum.addJavaDocLine("/**");
        sb.append(" * ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        innerEnum.addJavaDocLine(sb.toString().replace("\n", " "));
        innerEnum.addJavaDocLine(" */");
    }

    /**
     * Java属性注释
     *
     * @param field
     * @param introspectedTable
     */
    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        field.addJavaDocLine("/**");
        sb.append(" * ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        field.addJavaDocLine(sb.toString().replace("\n", " "));
        field.addJavaDocLine(" */");
    }

    /**
     * 为Vo属性字段
     *
     * @param field
     * @param introspectedTable
     * @param introspectedColumn
     */
    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        String column = introspectedColumn.getActualColumnName();
        if (introspectedColumn.getRemarks() != "" && introspectedColumn.getRemarks() != null) {
            field.addAnnotation("@ApiModelProperty(value = \"" + introspectedColumn.getRemarks() + "\", name = \"" + field.getName() + "\", required = true)");
        } else {
            field.addAnnotation("@ApiModelProperty(value = \"" + field.getName() + "\", name = \"" + field.getName() + "\",required = true)");
        }
    }

    /**
     * 普通方法的注释，这里主要是XXXMapper.java里面的接口方法的注释
     *
     * @param method
     * @param introspectedTable
     */
    @Override
    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }
        method.addJavaDocLine("/**");
        addJavadocTag(method, false);
        method.addJavaDocLine(" */");
    }

    /**
     * 给Java文件加注释，这个注释是在文件的顶部，也就是package上面。
     */
    @Override
    public void addJavaFileComment(CompilationUnit compilationUnit) {
        /**
         * $Id: MovOrderCouponService.java,v 1.0 2018/8/13 下午3:22 caojx
         *
         * @Copyright (c) 2018/8/13, XXX Group All Rights Reserved.
         */

//        String shortName = compilationUnit.getType().getShortName();
//        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy/MM/dd HH:mm");
//        compilationUnit.addFileCommentLine("/**");
//        compilationUnit.addFileCommentLine(" * $Id: "+shortName+".java, v 1.0 "+sdf1.format(new Date())+" "+author);
//        compilationUnit.addFileCommentLine(" * ");
//        compilationUnit.addFileCommentLine(" * @Copyright (c) "+ sdf1.format(new Date())+", XXX Group All Rights Reserved.");
//        compilationUnit.addFileCommentLine(" */");
    }

    @Override
    public void addComment(XmlElement xmlElement) {

    }

    /**
     * 为模型类添加注释
     *
     * @param topLevelClass
     * @param introspectedTable
     */
    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String className = topLevelClass.getType().getShortName() + "Vo对象";
        String description = introspectedTable.getRemarks();

        boolean generatorGetterAndSetter = Boolean.FALSE;
        if( !StringUtils.isEmpty(properties.getProperty("generatorGetterAndSetter")) && Boolean.TRUE.equals(Boolean.valueOf(properties.getProperty("generatorGetterAndSetter")))){
            generatorGetterAndSetter = Boolean.TRUE;
        }
        if (!generatorGetterAndSetter){
            topLevelClass.addAnnotation("@Data");
            topLevelClass.addAnnotation("@Builder");
            topLevelClass.addAnnotation("@NoArgsConstructor");
            topLevelClass.addAnnotation("@AllArgsConstructor");
            FullyQualifiedJavaType fullyQualifiedJavaType1 = new FullyQualifiedJavaType("lombok.Data");
            FullyQualifiedJavaType fullyQualifiedJavaType2 = new FullyQualifiedJavaType("lombok.Builder");
            FullyQualifiedJavaType fullyQualifiedJavaType3 = new FullyQualifiedJavaType("lombok.NoArgsConstructor");
            FullyQualifiedJavaType fullyQualifiedJavaType4 = new FullyQualifiedJavaType("lombok.AllArgsConstructor");
            topLevelClass.addImportedType(fullyQualifiedJavaType1);
            topLevelClass.addImportedType(fullyQualifiedJavaType2);
            topLevelClass.addImportedType(fullyQualifiedJavaType3);
            topLevelClass.addImportedType(fullyQualifiedJavaType4);
        }
        topLevelClass.addAnnotation("@ApiModel(value=\"" + className + "\", description=\"" + description + "\")");
        FullyQualifiedJavaType fullyQualifiedJavaType2 = new FullyQualifiedJavaType("io.swagger.annotations.ApiModel");
        FullyQualifiedJavaType fullyQualifiedJavaType3 = new FullyQualifiedJavaType("io.swagger.annotations.ApiModelProperty");
        FullyQualifiedJavaType fullyQualifiedJavaType4 = new FullyQualifiedJavaType("java.io.Serializable");

        topLevelClass.addImportedType(fullyQualifiedJavaType2);
        topLevelClass.addImportedType(fullyQualifiedJavaType3);
        topLevelClass.addSuperInterface(fullyQualifiedJavaType4);

        /**
         * 类注释，描述
         *
         * @author caojx
         * @version \$Id: ${NAME}.java,v 1.1 ${DATE} ${TIME} caojx
         * @date ${DATE} 下午${TIME}
         */
        //文档注释
//        String shortName = topLevelClass.getType().getShortName();
//        SimpleDateFormat sdf1=new SimpleDateFormat("yyyy/MM/dd HH:mm");
//        topLevelClass.addJavaDocLine("/**");
//        topLevelClass.addJavaDocLine(" * 类注释，描述");
//        topLevelClass.addJavaDocLine(" * ");
//        topLevelClass.addJavaDocLine(" * @author "+author);
//        topLevelClass.addJavaDocLine(" * @version \\$Id: "+shortName+".java,v 1.0 "+sdf1.format(new Date())+" "+author);
//        topLevelClass.addJavaDocLine(" * @date "+sdf1.format(new Date()));
//        topLevelClass.addJavaDocLine(" */");
    }

    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {

    }

    @Override
    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean b) {

    }

    /**
     * 为调用此方法作为根元素的第一个子节点添加注释。
     */
    @Override
    public void addRootComment(XmlElement arg0) {

    }

    /**
     * 给getter方法加注释
     *
     * @param method
     * @param introspectedTable
     * @param introspectedColumn
     */
    @Override
    public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
//        if (suppressAllComments) {
//            return;
//        }
//        method.addJavaDocLine("/**");
//        StringBuilder sb = new StringBuilder();
//        sb.append(" * ");
//        sb.append(introspectedColumn.getRemarks());
//        method.addJavaDocLine(sb.toString().replace("\n", " "));
//        sb.setLength(0);
//        sb.append(" * @return ");
//        sb.append(introspectedColumn.getActualColumnName());
//        sb.append(" ");
//        sb.append(introspectedColumn.getRemarks());
//        method.addJavaDocLine(sb.toString().replace("\n", " "));
//        method.addJavaDocLine(" */");
    }

    /**
     * 给setter方法加注释
     *
     * @param method
     * @param introspectedTable
     * @param introspectedColumn
     */
    @Override
    public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
//        if (suppressAllComments) {
//            return;
//        }
//        method.addJavaDocLine("/**");
//        StringBuilder sb = new StringBuilder();
//        sb.append(" * ");
//        sb.append(introspectedColumn.getRemarks());
//        method.addJavaDocLine(sb.toString().replace("\n", " "));
//        Parameter parm = method.getParameters().get(0);
//        sb.setLength(0);
//        sb.append(" * @param ");
//        sb.append(parm.getName());
//        sb.append(" ");
//        sb.append(introspectedColumn.getRemarks());
//        method.addJavaDocLine(sb.toString().replace("\n", " "));
//        method.addJavaDocLine(" */");
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    protected String getDateString() {
        String result = null;
        if (!suppressDate) {
            result = currentDateStr;
        }
        return result;
    }
}
