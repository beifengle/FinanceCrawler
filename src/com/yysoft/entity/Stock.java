package com.yysoft.entity;
/**
 * 功能描述：上市公司实体
 * @author Huanyan.Lu
 * @date:2015年9月17日
 * @time:下午2:59:56
 * @version:1.0
 */
public class Stock {
  private String name;//上市公司简称 
  private String code;//上市公司代码 
  private String iname;//行业简称
  private String acronym;//首字母缩写
  private String concepts;//所属概念 
  private String city;//所属地域 
  private String announcedDate;//上市日期
  private int id;
  private int icode;//行业代码
  private long generalCapital;//总股本 
  private int boardType;//0为主板,1为创业板 
  private int componentType;//1-个股，2-上证指数，3-深证成指，4-深市主板，5-中小板指，6-创业板指
  private int dealType;//1-停牌，0-已复牌，正常交易
  private int liveType;//1-上市，0-退市,2-未上市
  private int bourseType;//沪深标记,0-深市,1-沪市
  
  
  private String mainBusiness; //主营业务
  private String businessScope;//业务范围
  private String chairman;//董事长
  private String secretaries;//董秘
  private String registeredAddress;//注册地址
  private String officeAddress;//办公地址
  private String postCode;//邮编
  private String officezipcode;//上港集团用的邮编代码
  private String phone;//电话
  private String email;//邮箱
  private String website;//网址 数据库中用url。
  private String companyName;//公司全称。
  
	  
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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

	public String getConcepts() {
		return concepts;
	}
	public void setConcepts(String concepts) {
		this.concepts = concepts;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public long getGeneralCapital() {
		return generalCapital;
	}
	public void setGeneralCapital(long generalCapital) {
		this.generalCapital = generalCapital;
	}
	public int getBoardType() {
		return boardType;
	}
	public void setBoardType(int boardType) {
		this.boardType = boardType;
	}
	public int getComponentType() {
		return componentType;
	}
	public void setComponentType(int componentType) {
		this.componentType = componentType;
	}
	public int getDealType() {
		return dealType;
	}
	public void setDealType(int dealType) {
		this.dealType = dealType;
	}
	public int getLiveType() {
		return liveType;
	}
	public void setLiveType(int liveType) {
		this.liveType = liveType;
	}
	public int getBourseType() {
		return bourseType;
	}
	public void setBourseType(int bourseType) {
		this.bourseType = bourseType;
	}
	public int getIcode() {
		return icode;
	}
	public void setIcode(int icode) {
		this.icode = icode;
	}
	public String getIname() {
		return iname;
	}
	public void setIname(String iname) {
		this.iname = iname;
	}
	public String getAcronym() {
		return acronym;
	}
	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}
	public String getAnnouncedDate() {
		return announcedDate;
	}
	public void setAnnouncedDate(String announcedDate) {
		this.announcedDate = announcedDate;
	}
	
	
	
	public String getMainBusiness() {
		return mainBusiness;
	}
	public void setMainBusiness(String mainBusiness) {
		this.mainBusiness = mainBusiness;
	}
	public String getBusinessScope() {
		return businessScope;
	}
	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}
	public String getChairman() {
		return chairman;
	}
	public void setChairman(String chairman) {
		this.chairman = chairman;
	}
	public String getSecretaries() {
		return secretaries;
	}
	public void setSecretaries(String secretaries) {
		this.secretaries = secretaries;
	}
	public String getRegisteredAddress() {
		return registeredAddress;
	}
	public void setRegisteredAddress(String registeredAddress) {
		this.registeredAddress = registeredAddress;
	}
	public String getOfficeAddress() {
		return officeAddress;
	}
	public void setOfficeAddress(String officeAddress) {
		this.officeAddress = officeAddress;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getOfficezipcode() {
		return officezipcode;
	}
	public void setOfficezipcode(String officezipcode) {
		this.officezipcode = officezipcode;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	@Override
	public String toString() {
		return "Stock [name=" + name + ", code=" + code + ", iname=" + iname + ", acronym=" + acronym + ", concepts="
				+ concepts + ", city=" + city + ", announcedDate=" + announcedDate + ", id=" + id + ", icode=" + icode
				+ ", generalCapital=" + generalCapital + ", boardType=" + boardType + ", componentType=" + componentType
				+ ", dealType=" + dealType + ", liveType=" + liveType + ", bourseType=" + bourseType + ", mainBusiness="
				+ mainBusiness + ", businessScope=" + businessScope + ", chairman=" + chairman + ", secretaries="
				+ secretaries + ", registeredAddress=" + registeredAddress + ", officeAddress=" + officeAddress
				+ ", postCode=" + postCode + ", officezipcode=" + officezipcode + ", phone=" + phone + ", email="
				+ email + ", website=" + website + ", companyName=" + companyName + "]";
	}
	
	
}
