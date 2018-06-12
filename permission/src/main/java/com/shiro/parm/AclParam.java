package com.shiro.parm;

import com.shiro.model.SysAcl;
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
 * @Date: Created in  2018/6/7 15:33
 */
@Getter
@Setter
@ToString
public class AclParam extends SysAcl {
    private Integer id;

    @NotBlank(message = "权限点名称不能为空!!!")
    @Length(min = 2, max = 15, message = "权限点名称长度应在2-15字之内")
    private String name;

    @NotNull(message = "权限模块Id能为空")
    private Integer aclModuleId;

    @NotBlank(message = "权限url访问链接不能为空")
    private String url;

    @NotNull(message = "权限点类型不能为空")
    @Min(value = 1, message = "权限点类型值不正确")
    @Max(value = 3, message = "权限点类型值不正确")
    private Integer type;

    @NotNull(message = "权限点状态不能为空")
    private Integer status;

    @NotNull(message = "权限点展示顺序不能为空")
    private Integer seq;

    @Length(max = 150, message = "备注应在150字之内")
    private String remark;

}
