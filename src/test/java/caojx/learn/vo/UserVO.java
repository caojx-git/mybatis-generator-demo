package caojx.learn.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类注释，描述
 * 
 * @author caojx
 * @version \$Id: UserVO.java,v 1.0 2019/05/27 11:23 caojx
 * @date 2019/05/27 11:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="UserVO对象", description="用户表VO对象")
public class UserVO implements java.io.Serializable {
    @ApiModelProperty(value = "主键ID", name = "id", required = true)
    private Long id;

    @ApiModelProperty(value = "姓名", name = "name", required = true)
    private String name;

    @ApiModelProperty(value = "年龄", name = "age", required = true)
    private Integer age;

    @ApiModelProperty(value = "邮箱", name = "email", required = true)
    private String email;
}