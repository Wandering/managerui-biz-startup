package cn.thinkjoy.dto;

//import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;


public class ResourceDTO extends BaseDTO {
	private long resourceId;  //本身id
	private String resourceName;
	private long parentResourceId;
	private List<ResourceDTO> resourceInfos  = new ArrayList<ResourceDTO>();
	private long roleId;
	
	
	
	
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	public long getResourceId() {
		return resourceId;
	}
	public void setResourceId(long resourceId) {
		this.resourceId = resourceId;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public long getParentResourceId() {
		return parentResourceId;
	}
	public void setParentResourceId(long parentResourceId) {
		this.parentResourceId = parentResourceId;
	}
	public List<ResourceDTO> getResourceInfos() {
		return resourceInfos;
	}
	public void setResourceInfos(List<ResourceDTO> resourceInfos) {
		this.resourceInfos = resourceInfos;
	}
	
	
	


	
	 
	 
	 
	

}
