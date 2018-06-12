package com.shiro.dto;

import com.shiro.model.SysAclModule;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/7 13:41
 */
@Getter
@Setter
@ToString
public class AclModuleDTO extends SysAclModule {

    private List<AclModuleDTO> aclModuleList = new ArrayList<AclModuleDTO>();

    private List<AclDTO> aclList = new ArrayList<AclDTO>();


    public static AclModuleDTO adapt(SysAclModule sysAclModule) {
        AclModuleDTO aclModuleDTO = new AclModuleDTO();
        BeanUtils.copyProperties(sysAclModule, aclModuleDTO);
        return aclModuleDTO;
    }

}
