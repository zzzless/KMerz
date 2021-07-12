package com.kmerz.community.vo;

public class CategoryVo {
	private int category_id;
	private int community_id;
	private String category_name;
	private String category_description;
	private String category_status;
	
	public CategoryVo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CategoryVo(int category_id, int community_id, String category_name, String category_description,
			String category_status) {
		super();
		this.category_id = category_id;
		this.community_id = community_id;
		this.category_name = category_name;
		this.category_description = category_description;
		this.category_status = category_status;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public int getCommunity_id() {
		return community_id;
	}

	public void setCommunity_id(int community_id) {
		this.community_id = community_id;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getCategory_description() {
		return category_description;
	}

	public void setCategory_description(String category_description) {
		this.category_description = category_description;
	}

	public String getCategory_status() {
		return category_status;
	}

	public void setCategory_status(String category_status) {
		this.category_status = category_status;
	}

	@Override
	public String toString() {
		return "CategoryVo [category_id=" + category_id + ", community_id=" + community_id + ", category_name="
				+ category_name + ", category_description=" + category_description + ", category_status="
				+ category_status + "]";
	}
	
	
}
