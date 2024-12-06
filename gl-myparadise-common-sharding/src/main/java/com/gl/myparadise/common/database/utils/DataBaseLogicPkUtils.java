package com.gl.myparadise.common.database.utils;

import com.gl.myparadise.common.database.constants.DataBaseConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: DataBaseLogicPkUtils
 * @ProjectName: gl-common
 * @Date 2023-07-21 - 4:02 p.m.
 */
public class DataBaseLogicPkUtils {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceUtls.class);

    private DataBaseLogicPkUtils(){};

    private static String generateInviteCode()
    {
        StringBuffer shortBuffer = new StringBuffer();

        String uuid = UUID.randomUUID().toString().replace("-", "");

        for (int i = 0; i < 16; i++)
        {
            String str = uuid.substring(i * 2, i * 2 + 2);

            int x = Integer.parseInt(str, 16);
            // 如果是 chars62,则是x%62
            shortBuffer.append(DataBaseConstants.chars62[x % 62]);

        }

        return shortBuffer.toString();
    }

    public static String generateInviteCode(String codeBase){

        logger.debug("generateInviteCode codeBase : " + codeBase);

        SimpleDateFormat sdf = new SimpleDateFormat("mmss");

        String dayTime = sdf.format(new Date());

        return codeBase + generateInviteCode() + dayTime;

    }
}