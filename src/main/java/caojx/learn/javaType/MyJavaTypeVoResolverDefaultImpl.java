package caojx.learn.javaType;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

import java.sql.Types;

/**
 * 扩展JavaTypeResolverDefaultImpl 定制化Vo类型映射
 *
 * @author caojx
 * @version $Id: MyJavaTypeVoResolverDefaultImpl.java,v 1.0 2019-04-17 18:41 caojx
 * @date 2019-04-17 18:41
 */
public class MyJavaTypeVoResolverDefaultImpl extends JavaTypeResolverDefaultImpl {

    public MyJavaTypeVoResolverDefaultImpl() {
        super();
        super.typeMap.put(Types.TINYINT, new JdbcTypeInformation("TINYINT", new FullyQualifiedJavaType(Integer.class.getName())));
        super.typeMap.put(Types.SMALLINT, new JdbcTypeInformation("TINYINT", new FullyQualifiedJavaType(Integer.class.getName())));
        super.typeMap.put(Types.TIMESTAMP, new JdbcTypeInformation("TIMESTAMP", new FullyQualifiedJavaType(String.class.getName())));
        super.typeMap.put(Types.DATE, new JdbcTypeInformation("DATE", new FullyQualifiedJavaType(String.class.getName())));
    }
}
