package com.wata.payslip.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.wata.payslip.model.dto.ProjectDTO;
import com.wata.payslip.model.entity.ProjectEntity;

@Component
public class ProjectConverter {
	public ProjectEntity toEntity(ProjectDTO dto) {
		ProjectEntity entity = new ProjectEntity();
		entity.setNameProject(dto.getNameProject());
		entity.setStartDate(dto.getStartDate());
		entity.setEndDate(dto.getEndDate());
		entity.setDescription(dto.getDescription());
		return entity;
	}

	public ProjectDTO toDTO(ProjectEntity entity) {
		ProjectDTO dto = new ProjectDTO();
		dto.setIdProject(entity.getIdProject());
		dto.setIdTypeProject(entity.getTypeProject().getId());
		dto.setNameProject(entity.getNameProject());
		dto.setStartDate(entity.getEndDate());
		dto.setEndDate(entity.getEndDate());
		dto.setDescription(entity.getDescription());
		return dto;
	}

	public List<ProjectDTO> toDTOs(List<ProjectEntity> entities) {
		List<ProjectDTO> dtoes = new ArrayList<ProjectDTO>();
		for (ProjectEntity projectEntity : entities) {
			ProjectDTO dto = this.toDTO(projectEntity);
			dtoes.add(dto);
		}
		return dtoes;
	}

}
