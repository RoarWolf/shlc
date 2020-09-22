package com.hedong.hedongwx.entity;

public class UserEquipment {

	private Integer userId;

    private String equipmentCode;
    
    private Integer manid;
    
    private Double divideinto;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

	public String getEquipmentCode() {
		return equipmentCode;
	}

	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}

	public Integer getManid() {
		return manid;
	}

	public void setManid(Integer manid) {
		this.manid = manid;
	}

	public Double getDivideinto() {
		return divideinto;
	}

	public void setDivideinto(Double divideinto) {
		this.divideinto = divideinto;
	}

	@Override
	public String toString() {
		return "UserEquipment [userId=" + userId + ", equipmentCode=" + equipmentCode + ", manid=" + manid
				+ ", divideinto=" + divideinto + "]";
	}
	

    
}
