package com.shiro.parm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/7 10:41
 */
@Getter
@Setter
@ToString
public class SysAclModuleParam {

    private Integer id;

    @NotBlank(message = "权限操作模块名称不能为空!!!")
    @Length(min = 2, max = 20, message = "权限操作模块名称长度应在2-20个字之间")
    private String name;

    private Integer parentId = 0;

    @NotNull(message = "权限操作模块状态值不能为空")
    private Integer status;

    @NotNull(message = "展示顺序不能为空")
    private Integer seq;

    @Length(max = 150, message = "备注长度需在150字之内")
    private String remark;
}
