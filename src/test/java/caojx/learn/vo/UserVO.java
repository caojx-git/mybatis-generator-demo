package caojx.learn.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户表VO
 * 
 * @author caojx
 * @version \$Id: UserVO.java,v 1.0 2019/06/03 14:27 caojx
 * @date 2019/06/03 14:27
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="UserVO对象", description="用户表VO对象")
public class UserVO implements java.io.Serializable {
    @ApiModelProperty(value = "主键ID", name = "id")
    private Long id;

    @ApiModelProperty(value = "姓名", name = "name")
    private String name;

    @ApiModelProperty(value = "年龄", name = "age")
    private Integer age;

    @ApiModelProperty(value = "邮箱", name = "email")
    private String email;

    @ApiModelProperty(value = "生日", name = "birthday")
    private String birthday;
}