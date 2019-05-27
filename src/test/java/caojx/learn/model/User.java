package caojx.learn.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户表实体
 * 
 * @author caojx
 * @version \$Id: User.java,v 1.0 2019/05/27 11:14 caojx
 * @date 2019/05/27 11:14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements java.io.Serializable {
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 邮箱
     */
    private String email;
}