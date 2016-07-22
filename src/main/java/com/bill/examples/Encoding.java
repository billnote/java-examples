/**
 * 
 */
package com.bill.examples;

/**
 * 字符集枚举
 * 
 * @author Alex.Zhou
 * 
 */
public enum Encoding
{
	/**
	 * UTF8字符集
	 */
	UTF8("utf-8");

	private String strName;

	Encoding(String strName)
	{
		this.strName = strName;
	}

	/**
	 * 获取字符集名称
	 * 
	 * @return
	 */
	public String getStrName()
	{
		return this.strName;
	}

}
