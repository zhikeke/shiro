package com.shiro.dto;

import com.shiro.model.SysDept;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/4 15:45
 */
@Getter
@Setter
@ToString
public class DeptLevelDTO extends SysDept {
    private List<DeptLevelDTO> deptList = new ArrayList<DeptLevelDTO>();

    public static DeptLevelDTO adapt(SysDept dept) {
        DeptLevelDTO deptLevelDTO = new DeptLevelDTO();
        BeanUtils.copyProperties(dept, deptLevelDTO);
        return deptLevelDTO;
    }

}
