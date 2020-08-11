package com.wata.payslip.model.dto;

import java.util.List;

import com.wata.payslip.model.entity.ProjectEntity;
import com.wata.payslip.model.entity.WorkTimeEntity;

public class WorkDTO {

	private List<WorkTimeEntity> idWorktime = null;

	private ProjectEntity projectEntity = null;

	private String summary;

	private String explain;

	public WorkDTO() {
	}

	public WorkDTO(/*
					 * Integer idWork, List<WorkTimeEntity> idWorktime, ProjectEntity projectEntity,
					 * Integer idProject,
					 */String summary, String explain) {

		// this.idWorktime = idWorktime;
		// this.projectEntity = projectEntity;
		this.summary = summary;
		this.explain = explain;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	/*
	 * public List<WorkTimeEntity> getIdWorktime() { return null; }
	 * 
	 * public void setIdWorktime(List<WorkTimeEntity> idWorktime) { this.idWorktime
	 * = idWorktime; }
	 * 
	 * public ProjectEntity getProjectEntity() { return null; }
	 * 
	 * public void setProjectEntity(ProjectEntity projectEntity) {
	 * this.projectEntity = projectEntity; }
	 */
	/*
	 * public List<WorkTimeEntity> getIdWorktime() { return idWorktime; }
	 * 
	 * public void setIdWorktime(List<WorkTimeEntity> idWorktime) { this.idWorktime
	 * = idWorktime; }
	 * 
	 * public ProjectEntity getProjectEntity() { return projectEntity; }
	 * 
	 * public void setProjectEntity(ProjectEntity projectEntity) {
	 * this.projectEntity = projectEntity; }
	 */

}
