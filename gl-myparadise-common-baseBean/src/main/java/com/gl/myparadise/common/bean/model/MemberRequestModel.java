package com.gl.myparadise.common.bean.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * @author Vincent
 * @description:
 * @className: MemberRequestModel
 * @projectName: gl-background-member
 * @date 2025-02-03 - 22:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class MemberRequestModel extends BaseWebsiteRequestModel {

    private static final long serialVersionUID = 7060506800918962677L;

    /**
     * @ignore
     * 用户id系统自动设置
     */
    @NotNull
    @Min(1)
    private Long memberInfoId;

    /**
     * @ignore
     * 用户登录账户 系统自动设置
     */
    @NotBlank
    private String memberAccount;

}
