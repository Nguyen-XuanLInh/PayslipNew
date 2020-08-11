package com.wata.payslip.model.dto;

import java.util.Date;

public class ProjectDTO {
	private Integer idProject;
	private String nameProject;
	private Date startDate;
	private Date endDate;
	private String description;
	private Integer idTypeProject;

	public ProjectDTO() {

	}

	public ProjectDTO(Integer idProject, String nameProject, Date startDate, Date endDate, String description,
			Integer idTypeProject) {
		this.idProject = idProject;
		this.nameProject = nameProject;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
		this.idTypeProject = idTypeProject;
	}

	public Integer getIdTypeProject() {
		return idTypeProject;
	}

	public void setIdTypeProject(Integer idTypeProject) {
		this.idTypeProject = idTypeProject;
	}

	public Integer getIdProject() {
		return idProject;
	}

	public void setIdProject(Integer idProject) {
		this.idProject = idProject;
	}

	public String getNameProject() {
		return nameProject;
	}

	public void setNameProject(String nameProject) {
		this.nameProject = nameProject;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
