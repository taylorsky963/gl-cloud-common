package com.gl.myparadise.common.bean.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: UserLevelModel
 * @ProjectName: gl-common
 * @Date 2023-07-22 - 2:25 p.m.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserLevelModel extends BaseEntity {

    private static final long serialVersionUID = 1438724499928393484L;

    @JsonIgnore
    private String userLevel1;

    @JsonIgnore
    private String userLevel2;

    @JsonIgnore
    private String userLevel3;

    @JsonIgnore
    private String userLevel4;

    @JsonIgnore
    private String userLevel5;

    @JsonIgnore
    private String userLevel6;

    @JsonIgnore
    private transient String[] userLevelArray = new String[6];

    @JsonIgnore
    private transient int userLevel = 0;

    @JsonIgnore
    public int getUserLevel() {

        if(!StringUtils.isEmpty(userLevel6)){

            userLevel = 6;

        }else{

            if(!StringUtils.isEmpty(userLevel5)){

                userLevel = 5;

            }else{

                if(!StringUtils.isEmpty(userLevel4)){

                    userLevel = 4;

                }else{

                    if(!StringUtils.isEmpty(userLevel3)){

                        userLevel = 3;

                    }else{

                        if(!StringUtils.isEmpty(userLevel2)){

                            userLevel = 2;

                        }else{

                            if(!StringUtils.isEmpty(userLevel1)){

                                userLevel = 1;

                            }
                        }

                    }

                }

            }

        }

        return userLevel;

    }

    @JsonIgnore
    public String[] getUserLevelArray() {

        if(!StringUtils.isEmpty(userLevel1)){

            userLevelArray[0] = userLevel1;

        }

        if(!StringUtils.isEmpty(userLevel2)){

            userLevelArray[1] = userLevel2;

        }

        if(!StringUtils.isEmpty(userLevel3)){

            userLevelArray[2] = userLevel3;

        }

        if(!StringUtils.isEmpty(userLevel4)){

            userLevelArray[3] = userLevel4;

        }

        if(!StringUtils.isEmpty(userLevel5)){

            userLevelArray[4] = userLevel5;

        }

        if(!StringUtils.isEmpty(userLevel6)){

            userLevelArray[5] = userLevel6;

        }

        return userLevelArray;

    }


    public void setUserLevelArray(String[] userLevelArray) {

        if(userLevelArray != null && userLevelArray.length == 6){

            if(!StringUtils.isEmpty(userLevelArray[0])){

                setUserLevel1(userLevelArray[0]);

            }

            if(!StringUtils.isEmpty(userLevelArray[1])){

                setUserLevel2(userLevelArray[1]);

            }

            if(!StringUtils.isEmpty(userLevelArray[2])){

                setUserLevel3(userLevelArray[2]);

            }

            if(!StringUtils.isEmpty(userLevelArray[3])){

                setUserLevel4(userLevelArray[3]);

            }

            if(!StringUtils.isEmpty(userLevelArray[4])){

                setUserLevel5(userLevelArray[4]);

            }

            if(!StringUtils.isEmpty(userLevelArray[5])){

                setUserLevel6(userLevelArray[5]);

            }

        }

    }

}