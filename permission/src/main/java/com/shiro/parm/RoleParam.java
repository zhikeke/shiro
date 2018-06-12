package com.shiro.parm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/8 9:17
 */
@Getter
@Setter
@ToString
public class RoleParam {

    private Integer id;

    @NotBlank(message = "角色名不能为空!!!")
    @Length(min = 2, max = 15, message = "角色名长度应在2-15字之间")
    private String name;

    @Min(value = 1, message = "角色类型不正确")
    @Max(value = 2, message = "角色类型不正确")
    private Integer type = 1;

    @NotNull(message = "角色状态不能为空")
    private Integer status;

    @Length(max = 150, message = "角色备注应在150个字以内")
    private String remark;

}
