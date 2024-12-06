package com.gl.myparadise.common.tools.image;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: ImgUtils
 * @ProjectName: myparadise-common
 * @Date 2023-04-18 - 0:19
 */
public class ImgUtils {
	
	private static ImgUtils m_instance = null;

    private ImgUtils() {
    }

	public static boolean isPicture(String pInput, String pImgeFlag) {
		// 文件名称为空的场合
		if (CheckIsNull(pInput)) {
			// 返回不和合法
			return false;
		}
		// 获得文件后缀名
		String tmpName = pInput.substring(pInput.lastIndexOf(".") + 1, pInput.length());
		// 声明图片后缀名数组
		String imgeArray[][] = { { "bmp", "0" }, { "dib", "1" }, { "gif", "2" }, { "jfif", "3" }, { "jpe", "4" },
				{ "jpeg", "5" }, { "jpg", "6" }, { "png", "7" }, { "tif", "8" }, { "tiff", "9" }, { "ico", "10" } };
		// 遍历名称数组
		for (int i = 0; i < imgeArray.length; i++) {
			// 判断单个类型文件的场合
			if (!CheckIsNull(pImgeFlag) && imgeArray[i][0].equals(tmpName.toLowerCase())
					&& imgeArray[i][1].equals(pImgeFlag)) {
				return true;
			}
			// 判断符合全部类型的场合
			if (CheckIsNull(pImgeFlag) && imgeArray[i][0].equals(tmpName.toLowerCase())) {
				return true;
			}
			
		}
		
		return false;
	}
	
	private static boolean CheckIsNull(String input){
		
		if(input != null && !"".equals(input)){
			
			return false;
			
		}
		
		return true;
	}

}
