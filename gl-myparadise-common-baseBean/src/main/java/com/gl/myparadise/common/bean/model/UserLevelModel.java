package com.gl.myparadise.common.bean.model;

import org.springframework.util.StringUtils;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: UserLevelModel
 * @ProjectName: gl-common
 * @Date 2023-07-22 - 2:25 p.m.
 */
public class UserLevelModel extends BaseEntity {

    private static final long serialVersionUID = 1438724499928393484L;
    private String userLevel1;

    private String userLevel2;

    private String userLevel3;

    private String userLevel4;

    private String userLevel5;

    private String userLevel6;

    private transient String[] userLevelArray = new String[6];

    private transient int userLevel = 0;

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

    public String getUserLevel1() {
        return userLevel1;
    }

    public void setUserLevel1(String userLevel1) {
        this.userLevel1 = userLevel1;
    }

    public String getUserLevel2() {
        return userLevel2;
    }

    public void setUserLevel2(String userLevel2) {
        this.userLevel2 = userLevel2;
    }

    public String getUserLevel3() {
        return userLevel3;
    }

    public void setUserLevel3(String userLevel3) {
        this.userLevel3 = userLevel3;
    }

    public String getUserLevel4() {
        return userLevel4;
    }

    public void setUserLevel4(String userLevel4) {
        this.userLevel4 = userLevel4;
    }

    public String getUserLevel5() {
        return userLevel5;
    }

    public void setUserLevel5(String userLevel5) {
        this.userLevel5 = userLevel5;
    }

    public String getUserLevel6() {
        return userLevel6;
    }

    public void setUserLevel6(String userLevel6) {
        this.userLevel6 = userLevel6;
    }


}