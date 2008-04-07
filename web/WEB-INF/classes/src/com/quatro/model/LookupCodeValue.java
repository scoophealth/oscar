package com.quatro.model;
import java.util.List;
public class LookupCodeValue {
	private int lineId;
	private String prefix;
	private String code;
	private String description;
	private String shortDesc;
	private String note;
	private boolean active;
    private boolean selectable;
	private String parentCode;
	private String buf1;
	private String codeTree;
	private String buf2;
	
    private int orderByIndex;
    private List associates;
    
    public List getAssociates() {
		return associates;
	}
	public void setAssociates(List associates) {
		this.associates = associates;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCodeTree() {
		return codeTree;
	}
	public void setCodeTree(String codeTree) {
		this.codeTree = codeTree;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public int getOrderByIndex() {
		return orderByIndex;
	}
	public void setOrderByIndex(int orderByIndex) {
		this.orderByIndex = orderByIndex;
	}
	public String getParentCode() {
		return parentCode;
	}
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
	public boolean isSelectable() {
		return selectable;
	}
	public void setSelectable(Boolean selectable) {
		this.selectable = selectable;
	}
	public String getShortDesc() {
		return shortDesc;
	}
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public int getLineId() {
		return lineId;
	}
	public void setLineId(int lineId) {
		this.lineId = lineId;
	}
    
	public String getCodeId()
	{
		return this.prefix + ":" + this.code;
	}
	public String getBuf1() {
		return buf1;
	}
	public void setBuf1(String buf1) {
		this.buf1 = buf1;
	}
	public String getBuf2() {
		return buf2;
	}
	public void setBuf2(String buf2) {
		this.buf2 = buf2;
	}
}
