package com.shiro.parm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/4 15:08
 */
@Setter
@Getter
@ToString
public class DeptParam {

    private Integer id;

    @NotBlank(message = "部门名称不能为空")
    @Length(min = 2, max = 15, message = "部门名称长度在2-15之间")
    private String name;

    private Integer parentId = 0;

    @NotNull(message = "展示顺序不能为空")
    private Integer seq;

    @Length(max = 150, message = "备注长度需在150字之内")
    private String remark;

}
