package com.shiro.parm;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.annotations.Param;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/6 10:30
 */
@Getter
@Setter
public class UserParam {
    private Integer id;

    @NotBlank(message = "用户名不能为空!!!")
    private String username;

    @NotBlank(message = "手机号不能为空!!!")
    @Pattern(regexp = "^1[34578]\\d{9}$", message = "请输入正确的手机号码!!!")
    private String telephone;

    @NotBlank(message = "邮箱不能为空!!!")
    @Email(message = "请输入正确的邮箱地址!!!")
    private String mail;

    @Length(max = 150, message = "备注长度需在150字之内")
    private String remark;

    @NotNull(message = "所属部门不能为空!!!")
    private Integer deptId;

    @NotNull(message = "用户状态不能为空")
    private Integer status;




}
