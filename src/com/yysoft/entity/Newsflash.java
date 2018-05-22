package com.yysoft.entity;
/*
 * 功能描述：股市快讯
 * @author Peng.Wu
 * @date:2018年02月07日
 * @version:1.0
 */
public class Newsflash {
	  private String name;//上市公司简称 
	  private String code;//上市公司代码 
	  private String title;//标题 
	  private String contents;//内容
	  private String publishdate;//内容
	  private int id;
	  private int publish;//0未发布,1已发布 
	  
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		
		public int getPublish() {
			return publish;
		}
		public void setPublish(int publish) {
			this.publish = publish;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}

		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}

		public String getContents() {
			return contents;
		}
		public void setContents(String contents) {
			this.contents = contents;
		}

		public String getPublishdate() {
			return publishdate;
		}
		public void setPublishdate(String publishdate) {
			this.publishdate = publishdate;
		}

		
}
